/**
 * 7단계: 상속과 합성의 선택
 *
 * 학습 포인트
 * - 상속은 "자식은 부모이다"라는 Is-A 관계가 자연스러울 때 사용합니다.
 * - 합성은 "객체가 다른 객체를 가진다"라는 Has-A 관계를 필드로 표현합니다.
 * - 단순 코드 재사용을 위한 상속은 부모 구현과 자식의 결합도를 높입니다.
 * - 합성은 기능을 담당하는 객체를 외부에서 전달하므로 부품 교체 지점이 명확합니다.
 * - 다형적 타입 확장이 목적이 아니라면 상속보다 합성을 먼저 검토합니다.
 */
class Engine2212 {
    private final String type;

    Engine2212(String type) {
        this.type = type;
    }

    String start() {
        return type + " 엔진 시동";
    }
}

class Car2212 {
    // Car2212 is an Engine이 아니라 Car2212 has an Engine 관계입니다.
    private final Engine2212 engine;

    Car2212(Engine2212 engine) {
        // 생성자를 통해 필요한 부품을 전달받습니다.
        this.engine = engine;
    }

    void drive() {
        System.out.println(engine.start());
        System.out.println("자동차 출발");
    }
}

public class Ex07_Composition {
    public static void main(String[] args) {
        System.out.println("=== Has-A 관계를 합성으로 표현 ===");

        Car2212 electricCar = new Car2212(new Engine2212("전기"));
        electricCar.drive();

        /*
         * 확인 1: Car2212가 Engine2212를 상속하면 "자동차는 엔진이다"가 되어
         *         모델링이 왜 부자연스러운지 설명하세요.
         * 확인 2: 현재 코드는 합성을 사용하지만 Engine2212 구체 타입에 의존합니다.
         *         구체 타입 의존성이 부품 교체에 주는 영향을 설명하세요.
         */
    }
}
