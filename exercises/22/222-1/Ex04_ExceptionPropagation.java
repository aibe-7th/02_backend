import java.io.IOException;

/**
 * 실습 222-1. 4단계: 예외 전파(Propagation)와 위임 메커니즘
 * 
 * [학습 포인트]
 * 1. 예외 전파 (Exception Propagation)
 *    - 메서드 내에서 예외가 발생하고 이를 직접 처리(try-catch)하지 않으면,
 *      해당 메서드는 호출을 중단하고 예외 객체를 자신을 호출한 상위 메서드(Caller)로 던집니다.
 * 2. throws 키워드
 *    - 메서드 선언부에 throws를 사용하여 이 메서드가 발생시킬 수 있는 예외를 호출자에게 사전에 알립니다.
 *    - Checked 예외의 경우 throws 선언이 없으면 컴파일 에러가 나므로 필수입니다.
 * 3. Unchecked 예외의 자동 전파
 *    - RuntimeException 계열의 언체크 예외는 throws를 기술하지 않아도, 호출 스택을 통해 자동으로 상위로 전파됩니다.
 * 4. 전역 관심사 분리 (AOP 기반 일괄 포착)
 *    - 개별 비즈니스 메서드 내에서 지저분하게 try-catch를 하지 않고, 예외를 최상위 호출처로 던진 후,
 *      진입점(Global Exception Handler)에서 일괄 처리함으로써 깔끔한 가독성을 얻을 수 있습니다.
 */
public class Ex04_ExceptionPropagation {

    public static void main(String[] args) {
        System.out.println("=== 1. Checked 예외 전파 및 최상위 포착 ===");
        try {
            controllerMethodForChecked();
        } catch (IOException e) {
            System.out.println("[전역 핸들러] Checked 예외 최종 포착 및 정형 응답 가공: " + e.getMessage());
        }

        System.out.println("\n=== 2. Unchecked 예외 자동 전파 및 최상위 포착 ===");
        try {
            controllerMethodForUnchecked();
        } catch (ArithmeticException e) {
            System.out.println("[전역 핸들러] Unchecked 예외 최종 포착 및 에러 로그 기록: " + e.getMessage());
        }
    }

    // =========================================================================
    // Case A: Checked Exception의 전파 (throws 명시 필수)
    // =========================================================================
    
    private static void controllerMethodForChecked() throws IOException {
        System.out.println("Controller: Service 호출");
        serviceMethodForChecked();
    }

    private static void serviceMethodForChecked() throws IOException {
        System.out.println("Service: Repository 호출");
        repositoryMethodForChecked();
    }

    /**
     * Checked 예외를 발생시키는 메서드. throws IOException을 선언하여 호출자에게 위임.
     */
    private static void repositoryMethodForChecked() throws IOException {
        System.out.println("Repository: 디스크 파일 읽기 시도 중 에러 발생...");
        // Checked Exception 발생
        throw new IOException("디스크 I/O 오류가 발생했습니다.");
    }

    // =========================================================================
    // Case B: Unchecked Exception의 전파 (throws 명시 생략 가능)
    // =========================================================================

    private static void controllerMethodForUnchecked() {
        System.out.println("Controller: Service 호출");
        serviceMethodForUnchecked();
    }

    private static void serviceMethodForUnchecked() {
        System.out.println("Service: Repository 호출");
        repositoryMethodForUnchecked();
    }

    /**
     * Unchecked 예외를 발생시키는 메서드.
     * RuntimeException 계열이므로 throws 선언을 생략해도 호출 스택을 타고 자동 전파됩니다.
     */
    private static void repositoryMethodForUnchecked() {
        System.out.println("Repository: 0으로 나누는 연산 오류 발생...");
        // Unchecked Exception 발생 (throws 구문 생략 가능)
        int err = 10 / 0;
    }
}
