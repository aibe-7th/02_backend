import java.io.IOException;

/**
 * 실습 222-1. 5단계: Checked 예외의 Unchecked 포장(Exception Wrapping) 전환
 * 
 * [학습 포인트]
 * 1. Checked 예외 전파의 한계와 OCP 위반
 *    - 리포지토리나 로우 레벨 기술에서 Checked Exception(예: SQLException, IOException)을 던지면,
 *      이를 호출하는 모든 상위 메서드 시그니처(서비스, 컨트롤러)에 throws 선언을 강제하게 됩니다.
 *    - 이는 하위 레이어의 구체적인 구현 기술이 상위 레이어에 유출되는 결과를 낳고(OCP 위반), 코드 변경에 취약해집니다.
 * 2. 예외 포장 (Exception Wrapping)
 *    - Checked 예외를 잡아서(try-catch) 애플리케이션의 비즈니스 예외(Custom Unchecked Exception)로 포장하여 다시 던집니다.
 *    - 이를 통해 상위 메서드들이 더 이상 로우 레벨의 Checked 예외를 throws할 필요가 없어집니다.
 * 3. 근본 원인 예외(Cause) 보존
 *    - 예외를 포장할 때는 반드시 원본 예외를 생성자로 전달하여 원인 예외를 누락시키지 않아야 합니다. (디버깅 단절 방지)
 *    - 상위 호출부에서는 `getCause()`를 사용해 최초에 발생한 에러의 실체를 규명할 수 있습니다.
 */
public class Ex05_ExceptionWrapping {

    public static void main(String[] args) {
        System.out.println("=== 1. 예외 포장 전파 및 처리 테스트 ===");
        
        try {
            // 상위 컨트롤러 레이어에서 서비스 호출
            runControllerService();
        } catch (BusinessException e) {
            System.out.println("[컨트롤러 포착] 비즈니스 예외 포착!");
            System.out.println("- 포장된 메시지: " + e.getMessage());
            
            // 근본 원인 예외 추적
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("- 근본 원인 예외 종류: " + cause.getClass().getName());
                System.out.println("- 근본 원인 메시지: " + cause.getMessage());
            } else {
                System.out.println("- 근본 원인이 없습니다.");
            }
        }
    }

    /**
     * 컨트롤러 역할을 하는 메서드. 
     * 서비스가 Unchecked 예외(BusinessException)를 던지므로 throws 구문이 필요 없습니다.
     */
    private static void runControllerService() {
        System.out.println("Controller -> Service 호출 시도");
        executeBusinessService();
    }

    /**
     * 비즈니스 서비스 로직을 수행하는 메서드.
     * 마찬가지로 throws 구문 없이 리포지토리를 안전하게 호출하고 있습니다.
     */
    private static void executeBusinessService() {
        System.out.println("Service -> Repository 호출 시도");
        // 리포지토리의 로우 레벨 예외(IOException)를 직접 throws 받지 않고 
        // 런타임 예외로 포장된 BusinessException을 받기 때문에 시그니처가 깔끔합니다.
        loadUserDataFromDatabase();
    }

    /**
     * DB에서 유저 데이터를 가져오는 데이터베이스 레이어 메서드 (시뮬레이션).
     * 로우 레벨에서 발생한 Checked 예외를 포착하여 Unchecked 예외로 포장해 던집니다.
     */
    private static void loadUserDataFromDatabase() {
        try {
            System.out.println("Repository: 데이터베이스 물리 커넥션 획득 시도 중...");
            // SQLException이나 IOException 같은 Checked Exception 발생 상황 가정
            throw new IOException("데이터베이스 물리적 커넥션 끊김 (I/O Timeout)");
        } catch (IOException e) {
            System.out.println("Repository: Checked 예외 포착 및 BusinessException(Unchecked)으로 포장 전환");
            // 원본 예외 e를 생성자에 넘겨주어 root cause를 보존합니다!
            throw new BusinessException("사용자 데이터를 불러오는 데 실패했습니다.", e);
        }
    }

    /**
     * 사용자 정의 런타임 예외 클래스
     * 비즈니스 영역에서 공통으로 처리하기 위해 RuntimeException을 상속받습니다.
     */
    static class BusinessException extends RuntimeException {
        
        // 메시지만 받는 생성자
        public BusinessException(String message) {
            super(message);
        }

        /**
         * 중요: 근본 원인 예외(cause)를 상위 RuntimeException에 보관하는 생성자
         * 이를 통해 에러의 근본 추적이 가능해집니다.
         */
        public BusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
