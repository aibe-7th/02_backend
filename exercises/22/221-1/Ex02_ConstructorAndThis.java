/**
 * 실습 221-1. 2단계: 생성자 오버로딩 & this 체이닝
 * 
 * [학습 포인트]
 * 1. 생성자(Constructor)는 반환 타입이 없으며, 이름은 클래스명과 동일해야 합니다.
 * 2. 커스텀 생성자를 하나라도 직접 정의하면 컴파일러가 제공하는 기본 생성자(Default Constructor)는 자동 생성되지 않습니다.
 * 3. 'this' 키워드는 힙 메모리 상의 자기 자신 인스턴스를 가리킵니다.
 * 4. 'this()'는 같은 클래스 내의 다른 생성자를 호출할 때 사용하며, 반드시 생성자의 '첫 번째 줄'에 작성되어야 합니다.
 */

class SmartPhone {
    String brand;
    String model;
    int price;

    // [미션 1] 아래 주석 처리된 기본 생성자가 없는 상태에서
    // main 메서드 내의 'new SmartPhone()' 호출 시 발생하는 컴파일 에러를 관찰하고,
    // 에러를 해결하기 위해 아래 기본 생성자의 주석을 해제해 보세요.
    /*
    public SmartPhone() {
        System.out.println("기본 생성자가 호출되었습니다.");
    }
    */

    // 생성자 오버로딩 (1): 브랜드와 모델을 초기화하는 생성자
    public SmartPhone(String brand, String model) {
        // [미션 2] this() 체이닝을 사용하여 생성자 오버로딩(2)을 호출하도록 구현해 보세요.
        // 브랜드와 모델을 전달받으면 가격은 기본적으로 1,000,000원으로 초기화되게 만듭니다.
        // 주의: this()는 반드시 생성자의 '첫 번째 라인'에 있어야 합니다. 아래 TODO를 수정하세요.
        // TODO:여기에 적절한 this(...) 코드를 작성하세요.
        this.brand = brand;
        this.model = model;
        this.price = 1000000;
        System.out.println("생성자(brand, model)가 호출되었습니다.");
    }

    // 생성자 오버로딩 (2): 모든 멤버 변수를 명시적으로 지정하여 초기화하는 생성자
    public SmartPhone(String brand, String model, int price) {
        // 'this.brand = brand;' 형식은 매개변수 brand와 인스턴스 멤버 변수 brand를 구분하기 위해 사용됩니다.
        this.brand = brand;
        this.model = model;
        this.price = price;
        System.out.println("생성자(brand, model, price)가 호출되었습니다. (가장 구체적인 생성자)");
    }

    public void showInfo() {
        System.out.println("브랜드: " + brand + ", 모델: " + model + ", 가격: " + price + "원");
    }
}

public class Ex02_ConstructorAndThis {
    public static void main(String[] args) {
        System.out.println("=== 2단계: 생성자 오버로딩과 this() 실습 ===");

        // 1. 커스텀 생성자를 통해 객체 생성
        System.out.println("--- 1. 커스텀 생성자로 폰 생성 ---");
        SmartPhone phone1 = new SmartPhone("Apple", "iPhone 15 Pro", 1550000);
        phone1.showInfo();

        // 2. this() 체이닝 생성자를 통해 객체 생성
        System.out.println("\n--- 2. this() 체이닝으로 기본가 폰 생성 ---");
        SmartPhone phone2 = new SmartPhone("Samsung", "Galaxy S24");
        phone2.showInfo();

        // 3. 기본 생성자를 통한 객체 생성
        System.out.println("\n--- 3. 기본 생성자로 빈 폰 생성 ---");
        // [주의] 아래 코드는 SmartPhone 클래스 내부에 '기본 생성자(매개변수 없는)'가 
        // 직접 작성되어 있어야 컴파일 에러가 나지 않습니다.
        // (SmartPhone 클래스에 인자가 있는 생성자가 이미 존재하므로, 컴파일러가 자동으로 주입해주지 않기 때문)
        
        // SmartPhone phone3 = new SmartPhone(); // 미션 1 해결 후 주석 해제하여 테스트
        // phone3.showInfo();
        System.out.println("(기본 생성자 미션 해결 후 주석을 해제하여 작동을 검증해 보세요.)");
    }
}
