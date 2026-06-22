/**
 * 5단계: 메서드 오버라이딩 규칙
 *
 * 학습 포인트
 * - 오버라이딩은 부모의 인스턴스 메서드를 자식 클래스에서 같은 시그니처로 재정의합니다.
 * - 메서드명과 매개변수의 타입, 순서, 개수가 부모 메서드와 같아야 합니다.
 * - 자식 메서드의 접근 범위는 부모 메서드보다 좁아질 수 없습니다.
 * - 반환 타입은 같거나 부모 반환 타입의 하위 타입인 공변 반환 타입을 사용할 수 있습니다.
 * - @Override를 붙이면 시그니처 오타가 새 오버로딩으로 처리되는 실수를 막을 수 있습니다.
 */
class DeliveryResult {
    final String message;

    DeliveryResult(String message) {
        this.message = message;
    }
}

class ExpressDeliveryResult extends DeliveryResult {
    ExpressDeliveryResult(String message) {
        super(message);
    }
}

class DeliveryService {
    protected DeliveryResult deliver(String destination) {
        return new DeliveryResult(destination + " 일반 배송");
    }
}

class ExpressDeliveryService extends DeliveryService {
    @Override
    // protected보다 넓은 public 접근 범위와 하위 반환 타입을 사용합니다.
    public ExpressDeliveryResult deliver(String destination) {
        return new ExpressDeliveryResult(destination + " 당일 배송");
    }
}

public class Ex05_Overriding {
    public static void main(String[] args) {
        System.out.println("=== 오버라이딩 규칙 ===");

        // 참조 변수 타입은 부모이지만 실제 객체는 ExpressDeliveryService입니다.
        DeliveryService service = new ExpressDeliveryService();
        System.out.println(service.deliver("서울").message);

        /*
         * 확인 1: 자식 메서드의 public을 private으로 바꾸고 접근 범위 오류를 확인하세요.
         * 확인 2: @Override를 유지한 채 매개변수 타입을 Object로 바꾸고 오류를 확인하세요.
         * 확인 3: @Override를 제거한 상태에서 매개변수를 Object로 바꾸면 왜 컴파일되지만
         *         의도한 재정의가 되지 않는지 설명하세요.
         */
    }
}
