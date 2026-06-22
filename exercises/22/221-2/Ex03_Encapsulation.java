/**
 * 3단계: Getter/Setter와 행위 중심 캡슐화
 *
 * 학습 포인트
 * - private 필드는 외부에서 직접 변경할 수 없어 객체의 상태 규칙을 보호합니다.
 * - Getter는 조회가 필요한 값만 읽기 전용으로 공개하는 접근 통로입니다.
 * - Setter를 모든 필드에 만들면 외부 코드가 객체 상태를 임의로 조합할 수 있습니다.
 * - accumulatePoints처럼 의도가 드러나는 행위 메서드 안에서 값 검증과 상태 변경을 함께 처리합니다.
 * - 객체가 항상 지켜야 하는 조건을 불변식이라고 하며 생성자와 행위 메서드가 이를 보장합니다.
 */
class LoyaltyMember {
    private final String name;
    private String grade = "BASIC";
    private int points;

    LoyaltyMember(String name) {
        // 잘못된 객체가 생성되지 않도록 생성 단계에서 이름을 검증합니다.
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        this.name = name;
    }

    void accumulatePoints(int amount) {
        // setPoints(int)로 값을 덮어쓰는 대신 적립이라는 비즈니스 행위를 표현합니다.
        if (amount <= 0) {
            throw new IllegalArgumentException("적립 포인트는 양수여야 합니다.");
        }

        points += amount;

        // 상태 변경 규칙을 객체 내부 한 곳에서 관리합니다.
        if (points >= 10_000) {
            grade = "VIP";
        }
    }

    // 가변 필드 자체를 공개하지 않고 화면에 필요한 요약 결과만 제공합니다.
    String summary() {
        return "%s / %s / %,d점".formatted(name, grade, points);
    }
}

public class Ex03_Encapsulation {
    public static void main(String[] args) {
        System.out.println("=== Setter 없는 상태 변경 ===");

        LoyaltyMember member = new LoyaltyMember("이지원");
        member.accumulatePoints(4_000);
        System.out.println("1차 적립: " + member.summary());

        member.accumulatePoints(6_000);
        System.out.println("2차 적립: " + member.summary());

        /*
         * 확인 1: 음수 포인트 적립을 호출해 불변식이 보호되는지 확인하세요.
         * 확인 2: setGrade와 setPoints를 추가했을 때 만들 수 있는 잘못된 상태를 적으세요.
         * 확인 3: VIP 승급을 promoteToVip() 행위로 분리한다면 호출 조건을 어디서
         *         검증해야 객체의 자율성을 유지할 수 있는지 설명하세요.
         */
    }
}
