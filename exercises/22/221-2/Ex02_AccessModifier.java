/**
 * 2단계: 접근 제어자와 최소 노출 원칙
 *
 * 학습 포인트
 * - private 멤버는 선언한 클래스 내부에서만 접근할 수 있습니다.
 * - 접근 제어자를 생략한 default 멤버는 같은 패키지에서만 접근할 수 있습니다.
 * - protected 멤버는 같은 패키지 또는 상속 관계의 하위 클래스에서 접근할 수 있습니다.
 * - public 멤버는 모든 패키지에서 접근할 수 있습니다.
 * - 필드는 private부터 시작하고 필요한 행위만 메서드로 공개하는 것이 기본 원칙입니다.
 */
class AccessParent {
    // AccessParent 내부 전용: 자식 클래스에서도 직접 접근할 수 없습니다.
    private String secret = "private";

    // 접근 제어자를 생략했으므로 package-private(default)입니다.
    String packageValue = "default";

    // 다른 패키지에서도 상속 관계를 통해 접근할 수 있습니다.
    protected String inheritedValue = "protected";

    // 모든 영역에 공개됩니다.
    public String openValue = "public";

    // private 필드를 외부에 그대로 노출하지 않고 필요한 결과만 반환합니다.
    String readSecret() {
        return secret;
    }
}

class AccessChild extends AccessParent {
    void printAccessibleValues() {
        // System.out.println(secret); // private이므로 컴파일 오류
        System.out.println(packageValue);
        System.out.println(inheritedValue);
        System.out.println(openValue);
    }
}

public class Ex02_AccessModifier {
    public static void main(String[] args) {
        System.out.println("=== 접근 제어자 4단계 ===");

        AccessParent parent = new AccessParent();
        System.out.println("간접 조회: " + parent.readSecret());

        // 하위 클래스 메서드 안에서 접근 가능한 범위를 확인합니다.
        new AccessChild().printAccessibleValues();

        /*
         * 확인 1: parent.secret의 주석을 해제해 private 접근 오류를 확인하세요.
         * 확인 2: AccessChild를 다른 패키지로 옮기면 packageValue에는 접근할 수 없지만
         *         inheritedValue에는 접근할 수 있는 이유를 표로 정리하세요.
         * 확인 3: openValue를 실제 설계에서 public 필드로 두지 않는 이유를 설명하세요.
         */
    }
}
