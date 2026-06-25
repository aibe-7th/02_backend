/**
 * 실습 222-1. 3단계: 자동 자원 해제 (try-with-resources) 와 Effectively Final
 * 
 * [학습 포인트]
 * 1. AutoCloseable 인터페이스
 *    - 자바에서 닫아야 하는 자원(파일, 소켓, DB 커넥션 등)은 AutoCloseable을 구현하여 close() 메서드를 가집니다.
 * 2. try-with-resources의 필요성
 *    - 기존 try-finally 방식은 close() 호출 시 추가 예외 처리가 발생해 보일러플레이트 코드가 기하급수적으로 복잡해집니다.
 *    - try-with-resources를 사용하면 블록을 벗어날 때 JVM이 close()를 자동으로 안전하게 호출해 줍니다.
 * 3. 자원 스코프(Scope) 제한
 *    - try(자원선언)에서 선언된 변수는 오직 try 블록 내부에서만 사용할 수 있습니다. catch/finally나 외부에서는 접근할 수 없습니다.
 * 4. Effectively Final 자원 위임 (Java 9+)
 *    - 외부에서 이미 생성된 자원 변수라도 값이 변경되지 않는 사실상 final(effectively final) 상태라면,
 *      try 선언부에 변수명만 넣어 관리를 위임할 수 있습니다.
 *    - 단, 관리가 위임된 자원은 블록 종료 시 close()되어 소멸하므로, 그 이후 재사용하면 런타임 에러가 발생합니다.
 */
public class Ex03_TryWithResources {

    public static void main(String[] args) {
        System.out.println("=== 1. 전통적인 try-finally 방식 (비추천) ===");
        runTraditionalClose();

        System.out.println("\n=== 2. try-with-resources를 활용한 자동 해제 ===");
        runTryWithResources();

        System.out.println("\n=== 3. 자원 위임(Effectively Final) 및 소멸 후 재사용 금지 ===");
        runEffectivelyFinalResource();
    }

    /**
     * 전통적인 try-finally 방식: 자원 해제 코드가 복잡하고 지저분해짐
     */
    private static void runTraditionalClose() {
        CustomResource resource = null;
        try {
            resource = new CustomResource("전통 자원");
            resource.work();
        } finally {
            if (resource != null) {
                try {
                    resource.close(); // close() 메서드 자체도 Exception을 던질 수 있어 다시 try-catch가 필요할 수 있음
                } catch (Exception e) {
                    System.out.println("자원 닫기 실패: " + e.getMessage());
                }
            }
        }
    }

    /**
     * try-with-resources 방식: 코드가 직관적이고 자원이 안전하게 자동 반환됨
     */
    private static void runTryWithResources() {
        try (CustomResource resource = new CustomResource("자동 자원")) {
            resource.work();
            // resource 변수는 이 블록 안에서만 유효함
        } catch (Exception e) {
            System.out.println("예외 처리: " + e.getMessage());
        }
        
        // [컴파일 에러 재현용 설명]
        // resource 변수는 try 블록 밖에서는 소멸하므로 아래 코드는 컴파일 에러가 발생합니다.
        // ----------------------------------------------------
        // resource.work(); // 컴파일 에러: resource cannot be resolved to a variable
        // ----------------------------------------------------
    }

    /**
     * 사실상 final(effectively final)인 자원을 try-with-resources에 위임하고, 블록 밖에서 재사용을 금지하는 규칙 실습
     */
    private static void runEffectivelyFinalResource() {
        // 1. 외부에서 자원을 생성 (Java 9 이상 지원)
        CustomResource outerResource = new CustomResource("위임 자원");

        // 2. try 괄호 안에 변수명만 선언하여 JVM에 자원 닫기 위임
        try (outerResource) {
            outerResource.work();
        } catch (Exception e) {
            System.out.println("예외 처리: " + e.getMessage());
        }

        // 3. 소멸 후 재사용 금지 규칙 확인
        // outerResource는 try 블록이 끝나면서 자동으로 close() 되었습니다.
        // 따라서 close된 자원의 메서드를 재호출 시 런타임 예외가 발생하도록 설계해야 합니다.
        try {
            System.out.println("자원 반환 후 재사용 시도...");
            outerResource.work(); // 런타임 예외 유도
        } catch (IllegalStateException e) {
            System.out.println("[예외 포착] " + e.getMessage());
        }
    }

    /**
     * AutoCloseable을 구현하여 자동 자원 해제를 증명하기 위한 테스트 자원 클래스
     */
    static class CustomResource implements AutoCloseable {
        private final String name;
        private boolean isClosed = false;

        public CustomResource(String name) {
            this.name = name;
            System.out.println("[" + name + "] 자원이 생성되었습니다.");
        }

        public void work() {
            if (isClosed) {
                throw new IllegalStateException("[" + name + "] 이미 닫힌 자원에는 접근할 수 없습니다!");
            }
            System.out.println("[" + name + "] 자원을 사용하여 비즈니스 로직을 수행 중입니다.");
        }

        @Override
        public void close() throws Exception {
            this.isClosed = true;
            System.out.println("[" + name + "] 자원이 안전하게 해제(close)되었습니다.");
        }
    }
}
