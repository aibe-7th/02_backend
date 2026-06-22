/**
 * 6단계: 다형성, 정적 바인딩, 동적 바인딩
 *
 * 학습 포인트
 * - 부모 타입 참조 변수는 부모 객체뿐 아니라 모든 자식 객체를 참조할 수 있습니다.
 * - 필드는 오버라이딩되지 않고 참조 변수의 선언 타입을 기준으로 정적 바인딩됩니다.
 * - 오버라이딩 가능한 인스턴스 메서드는 실제 객체 타입을 기준으로 동적 바인딩됩니다.
 * - private, final, static 메서드는 오버라이딩 대상이 아니므로 정적으로 결정됩니다.
 * - 부모 타입 배열을 사용하면 서로 다른 자식 객체를 동일한 호출 코드로 처리할 수 있습니다.
 */
class Notification {
    String channel = "기본 채널";

    String send(String message) {
        return channel + ": " + message;
    }
}

class EmailNotification extends Notification {
    // 같은 이름의 부모 필드를 재정의하는 것이 아니라 별개의 필드를 숨깁니다.
    String channel = "이메일";

    @Override
    String send(String message) {
        return channel + " 발송: " + message;
    }
}

class SmsNotification extends Notification {
    @Override
    String send(String message) {
        return "SMS 발송: " + message;
    }
}

public class Ex06_PolymorphismAndBinding {
    public static void main(String[] args) {
        System.out.println("=== 필드와 메서드의 바인딩 차이 ===");

        // 업캐스팅: 부모 타입 참조가 자식 인스턴스를 가리킵니다.
        Notification notification = new EmailNotification();

        // 필드는 선언 타입 Notification 기준, 메서드는 실제 타입 EmailNotification 기준입니다.
        System.out.println("필드: " + notification.channel);
        System.out.println("메서드: " + notification.send("주문 완료"));

        System.out.println("\n=== 부모 타입 배열의 다형성 ===");
        Notification[] notifications = {new EmailNotification(), new SmsNotification()};
        for (Notification item : notifications) {
            // 호출문은 하나지만 런타임의 실제 객체에 따라 다른 구현이 실행됩니다.
            System.out.println(item.send("배송 시작"));
        }

        /*
         * 확인 1: 참조 변수 타입을 EmailNotification으로 바꾸고 필드 출력 결과를 비교하세요.
         * 확인 2: EmailNotification의 channel 필드를 제거하고 결과 변화를 설명하세요.
         * 확인 3: PushNotification을 추가하되 반복문의 호출 코드는 수정하지 마세요.
         */
    }
}
