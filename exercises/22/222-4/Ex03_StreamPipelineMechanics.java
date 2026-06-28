import java.util.List;
import java.util.stream.Collectors;

/**
 * 실습 03: 스트림 파이프라인 동작 메커니즘 (지연 연산, 루프 퓨전, 쇼트 서킷)
 * 
 * [목표]
 * 1. 중간 연산(Intermediate)의 지연 연산(Lazy Evaluation) 특성을 확인합니다.
 * 2. 요소가 파이프라인을 수직적으로 통과하는 루프 퓨전(Loop Fusion) 현상을 이해합니다.
 * 3. limit()와 같은 쇼트 서킷(Short-circuit) 연산이 불필요한 연산을 어떻게 차단하는지 관찰합니다.
 */
public class Ex03_StreamPipelineMechanics {

    public static void main(String[] args) {
        List<String> names = List.of("Kim", "Park", "Lee", "Choi");

        System.out.println("=== 실습 3-1: 최종 연산이 없는 지연 연산 ===");
        runLazyEvaluationDemo(names);

        System.out.println("\n=== 실습 3-2: 최종 연산 추가 및 루프 퓨전 관찰 ===");
        runLoopFusionDemo(names);

        System.out.println("\n=== 실습 3-3: 쇼트 서킷 (limit) 적용 ===");
        runShortCircuitDemo(names);
    }

    /**
     * 최종 연산이 없는 스트림은 아예 작동하지 않음을 관찰하는 실습
     */
    private static void runLazyEvaluationDemo(List<String> names) {
        System.out.println("[시작] 스트림 정의");

        // 중간 연산만 등록하고 최종 연산은 부착하지 않음
        names.stream()
            .filter(name -> {
                System.out.println("  Lazy Filter: " + name);
                return name.length() >= 4;
            })
            .map(name -> {
                System.out.println("  Lazy Map: " + name);
                return name.toUpperCase();
            }); // 최종 연산 없음!

        System.out.println("[종료] 스트림 정의 종료 (아무런 필터링/매핑 로그가 안 찍혀야 정상입니다)");
    }

    /**
     * 최종 연산을 부착하여 모든 중간 연산이 순차적(수직적)으로 작동함을 관찰하는 실습 (Loop Fusion)
     */
    private static void runLoopFusionDemo(List<String> names) {
        System.out.println("[시작] 스트림 실행");

        // TODO: 아래 스트림 파이프라인 끝에 최종 연산인 .collect(Collectors.toList()) 또는 .forEach(System.out::println) 등을 붙여서 실행해 보세요.
        // 각 원소(Kim, Park, Lee, Choi)가 어떻게 필터와 맵을 번갈아 타며 지나가는지(수직적 평가) 로그 출력을 확인하세요.
        names.stream()
            .filter(name -> {
                System.out.println("  Filter: " + name);
                return name.length() >= 4;
            })
            .map(name -> {
                System.out.println("  Map: " + name);
                return name.toUpperCase();
            }); // TODO: 여기에 최종 연산을 부착하세요.

        System.out.println("[종료] 스트림 실행 완료");
    }

    /**
     * 쇼트 서킷(limit)이 실행 과정을 어떻게 단락시키는지 관찰하는 실습
     */
    private static void runShortCircuitDemo(List<String> names) {
        System.out.println("[시작] 쇼트 서킷 스트림 실행");

        // TODO: 아래 스트림 파이프라인 중간에 .limit(1) 중간 연산을 삽입해 보세요.
        // 어떤 요소까지만 처리되고 중단되는지 로그를 관찰하세요.
        List<String> result = names.stream()
            .filter(name -> {
                System.out.println("  Filter: " + name);
                return name.length() >= 4;
            })
            .map(name -> {
                System.out.println("  Map: " + name);
                return name.toUpperCase();
            })
            // 여기에 limit(1)을 넣으면 어떻게 실행 과정이 변하나요?
            .collect(Collectors.toList());

        System.out.println("결과 리스트: " + result);
        System.out.println("[종료] 쇼트 서킷 스트림 실행 완료");
    }
}
