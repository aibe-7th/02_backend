import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 실습 03: 원자적 변수(Atomic)와 ConcurrentMap
 * 
 * [목표]
 * 1. 락 없이 스레드 안전한 연산을 지원하는 AtomicInteger의 기본 사용법을 익힙니다.
 * 2. 멀티스레드 환경에서 일반 HashMap을 사용할 때 발생하는 동시성 문제(데이터 유실)를 목격합니다.
 * 3. 실무 웹 개발 및 캐시 설계에서 필수적인 ConcurrentHashMap의 안전성과 사용법을 이해합니다.
 */
public class Ex03_AtomicAndConcurrentMap {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 실습 3-1: AtomicInteger 기초 ===");
        runAtomicDemo();

        System.out.println("\n=== 실습 3-2: 일반 HashMap의 동시성 위험성 ===");
        runNormalHashMapDemo();

        System.out.println("\n=== 실습 3-3: ConcurrentHashMap을 활용한 안전한 공유 데이터 제어 ===");
        runConcurrentHashMapDemo();
    }

    /**
     * AtomicInteger를 활용한 스레드 안전한 카운팅
     */
    private static void runAtomicDemo() throws InterruptedException {
        AtomicInteger atomicCount = new AtomicInteger(0);
        int numberOfIncrements = 50_000;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < numberOfIncrements; i++) {
                // TODO: atomicCount를 원자적으로 1 증가시키는 메서드를 작성하세요.
                atomicCount.incrementAndGet();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < numberOfIncrements; i++) {
                atomicCount.incrementAndGet();
            }
        });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("AtomicInteger 최종 결과: " + atomicCount.get() + " (기대값: 100000)");
        System.out.println("-> 설명: 락 블로킹 없이 CPU 레벨의 CAS 연산을 이용해 오버헤드 없이 안전하게 증가시켰습니다.");
    }

    /**
     * 동시성 제어가 없는 일반 HashMap에 멀티스레드가 동시에 쓰기(put) 작업을 수행하는 데모
     */
    private static void runNormalHashMapDemo() throws InterruptedException {
        // 공유 자원으로 사용될 일반 HashMap
        Map<String, Integer> map = new HashMap<>();
        int taskCount = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < taskCount; i++) {
            final int keyNumber = i;
            executor.execute(() -> {
                // 각 스레드가 고유한 키와 값을 맵에 삽입합니다.
                map.put("key-" + keyNumber, keyNumber);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("일반 HashMap에 등록을 기대한 키 개수: " + taskCount);
        System.out.println("일반 HashMap에 실제 저장된 키 개수: " + map.size());
        System.out.println("데이터 유실 발생 여부: " + (map.size() != taskCount));
        System.out.println("-> 설명: 일반 HashMap은 내부 배열 재조정(Resize) 등 작업이 스레드 안전하지 않아 데이터가 덮어써지거나 유실됩니다.");
    }

    /**
     * ConcurrentHashMap을 사용하여 스레드 안전하게 쓰기 작업을 수행하고 원자적 연산을 처리하는 데모
     */
    private static void runConcurrentHashMapDemo() throws InterruptedException {
        // TODO: ConcurrentHashMap을 생성하여 스레드 안전하게 동작하도록 만들어 보세요.
        // 힌트: Map 인터페이스 계열로 ConcurrentHashMap 객체를 생성합니다.
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        int taskCount = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < taskCount; i++) {
            final int keyNumber = i;
            executor.execute(() -> {
                concurrentMap.put("key-" + keyNumber, keyNumber);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("ConcurrentHashMap에 등록을 기대한 키 개수: " + taskCount);
        System.out.println("ConcurrentHashMap에 실제 저장된 키 개수: " + concurrentMap.size());
        System.out.println("데이터 유실 발생 여부: " + (concurrentMap.size() != taskCount));

        System.out.println("\n--- 실무 활용: putIfAbsent를 이용한 중복 방지 캐싱 실습 ---");
        // putIfAbsent는 기존에 해당 키가 없으면 추가하고 null을 반환하며, 
        // 이미 키가 존재하면 추가하지 않고 기존 값을 반환하는 원자적(Atomic) 메서드입니다.
        
        // 1. 초기 등록
        Integer result1 = concurrentMap.putIfAbsent("cached-token", 9999);
        System.out.println("처음 등록 시 반환값 (기존 값 없음): " + result1 + " (저장된 값: " + concurrentMap.get("cached-token") + ")");

        // 2. 동일한 키로 중복 등록 시도
        Integer result2 = concurrentMap.putIfAbsent("cached-token", 1111);
        System.out.println("중복 등록 시 반환값 (기존 값 존재): " + result2 + " (저장된 값: " + concurrentMap.get("cached-token") + ")");
        System.out.println("-> 설명: putIfAbsent를 통해 멀티스레드 환경에서 중복 요청이나 동일 키의 캐시 덮어쓰기 오작동을 안전하게 방지할 수 있습니다.");
    }
}
