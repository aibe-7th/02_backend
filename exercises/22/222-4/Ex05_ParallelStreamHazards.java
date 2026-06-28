import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 실습 05: 병렬 스트림(Parallel Stream) 사용 시 주의점과 2대 위협
 * 
 * [목표]
 * 1. 공유 가변 상태(Shared Mutable State)를 조작할 때 발생하는 경쟁 상태(Race Condition)를 관찰하고 올바른 대안을 찾습니다.
 * 2. 공통 스레드 풀(ForkJoinPool.commonPool)의 특징을 이해하고, 블로킹 I/O 작업이 병렬 스트림에 미치는 마비 현상을 체험합니다.
 */
public class Ex05_ParallelStreamHazards {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 실습 5-1: 공유 가변 상태 오염 (ArrayList 경쟁 상태) ===");
        runSharedStateContaminationDemo();

        System.out.println("\n=== 실습 5-2: 공통 스레드 풀 마비 (CommonPool Block) ===");
        runCommonPoolBlockageDemo();
    }

    /**
     * 비안전 공유 컬렉션에 병렬 스트림으로 동시 쓰기를 진행하여 데이터가 유실되거나 에러가 나는 현상 관찰
     */
    private static void runSharedStateContaminationDemo() {
        List<Long> unsafeList = new ArrayList<>();
        int count = 100_000;

        System.out.println("일반 ArrayList에 병렬 스트림으로 원소 추가 중...");
        try {
            LongStream.rangeClosed(1, count)
                .parallel()
                .forEach(unsafeList::add); // ArrayList는 멀티스레드 환경에서 안전하지 않음!
        } catch (Exception e) {
            System.out.println("예외 발생! (예: ArrayIndexOutOfBoundsException 등): " + e.getMessage());
        }

        System.out.println("예상 저장 개수: " + count);
        System.out.println("실제 저장 개수: " + unsafeList.size());
        System.out.println("정합성 충족 여부: " + (unsafeList.size() == count));

        // TODO-질문: 데이터 누락이나 예외를 해결하기 위해 아래 2가지 해결 방법 중 어떤 것을 사용해야 할까요?
        // 방법 A: Collections.synchronizedList(new ArrayList<>())로 감싸기
        // 방법 B: forEach를 쓰지 않고 .boxed().collect(Collectors.toList())로 스트림 최종 연산으로 수집하기
        // 권장되는 함수형 프로그래밍 방식(부작용이 없고 병렬 수집기가 안전하게 조율하는 방식)은 무엇인가요?
    }

    /**
     * 병렬 스트림이 전역 ForkJoinPool.commonPool()을 공유하기 때문에 발생하는 블로킹 전염 현상
     */
    private static void runCommonPoolBlockageDemo() throws InterruptedException {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        System.out.println("시스템 가용 CPU 코어 수: " + cpuCores);
        System.out.println("전체 공통 스레드 풀 크기: " + ForkJoinPool.commonPool().getParallelism());

        // 1. 공통 스레드 풀을 점유하여 대기하는 블로킹 작업 스레드 구동 (예: 네트워크 요청 대기 가정)
        Thread blockingTaskThread = new Thread(() -> {
            System.out.println("[블로킹 작업 스레드] 공통 스레드 풀에서 Sleep 작업 10개 구동 시작...");
            LongStream.rangeClosed(1, 10)
                .parallel()
                .forEach(i -> {
                    try {
                        // 스레드를 2초간 멈춤 (블로킹 I/O 모사)
                        System.out.println("  [commonPool] 블로킹 작업 수행 중: " + Thread.currentThread().getName() + " - 작업 " + i);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            System.out.println("[블로킹 작업 스레드] 작업 완료.");
        });

        // 2. 다른 독립된 스레드에서 즉시 연산이 수행되는 병렬 스트림 구동
        Thread computationThread = new Thread(() -> {
            try {
                // 블로킹 작업이 스레드 풀을 점유할 수 있도록 미세하게 늦춰서 시작
                Thread.sleep(200); 
            } catch (InterruptedException e) {}

            long startTime = System.currentTimeMillis();
            System.out.println("[연산 스레드] 독립적인 병렬 연산(각 500ms 소요) 시작...");
            
            long sum = LongStream.rangeClosed(1, 5)
                .parallel()
                .map(i -> {
                    try {
                        Thread.sleep(500); // 500ms 대기
                    } catch (InterruptedException e) {}
                    return i * 2;
                })
                .sum();
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("[연산 스레드] 연산 결과: " + sum + " (소요시간: " + duration + "ms)");
            System.out.println("  -> 공통 풀이 비어있다면 대략 500ms가 걸려야 하지만,");
            System.out.println("  -> 풀이 마비되어 순차 처리되면 약 2500ms가 소요됩니다.");
        });

        blockingTaskThread.start();
        computationThread.start();

        blockingTaskThread.join();
        computationThread.join();
        
        System.out.println("\n-> 요약: 병렬 스트림은 공통 풀을 사용하므로, Blocking I/O(네트워크/DB 대기)와 함께 사용하면 애플리케이션 전체가 마비될 수 있습니다.");
    }
}
