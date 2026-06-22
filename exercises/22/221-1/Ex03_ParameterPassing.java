/**
 * 실습 221-1. 3단계: Java의 Parameter Passing (Call by Value)
 * 
 * [학습 포인트]
 * 1. Java는 100% Call by Value(값에 의한 전달) 방식으로 작동합니다.
 * 2. 기본 타입(Primitive Type)을 인자로 전달하면 변수의 실제 '리터럴 값'이 그대로 복사되어 전달됩니다.
 * 3. 참조 타입(Reference Type)을 인자로 전달하면 변수가 갖고 있는 '참조값(힙 메모리 주소 복사본)'이 전달됩니다.
 * 4. 메서드 내부에서 전달된 참조 매개변수 자체에 새 객체를 대입해도, 호출 측의 원본 객체 변수에는 영향이 없습니다.
 */

class ScoreData {
    int score;

    public ScoreData(int score) {
        this.score = score;
    }
}

public class Ex03_ParameterPassing {

    // 기본 타입 값을 매개변수로 받는 메서드
    public static void changePrimitive(int value) {
        System.out.println("[changePrimitive] 호출 직후 value: " + value);
        value = 999; // 스택 영역에 복사된 local 매개변수 값을 변경
        System.out.println("[changePrimitive] 값 변경 후 value: " + value);
    }

    // 참조 타입 객체의 멤버 변수를 바꾸는 메서드
    public static void changeReferenceState(ScoreData data) {
        System.out.println("[changeReferenceState] 호출 직후 data.score: " + data.score);
        data.score = 999; // 복사된 참조 주소를 타고 들어가 힙 영역 객체의 인스턴스 변수를 변경
        System.out.println("[changeReferenceState] 값 변경 후 data.score: " + data.score);
    }

    // 참조 타입 매개변수 자체에 새 객체를 대입하는 메서드
    public static void changeReferenceBinding(ScoreData data) {
        System.out.println("[changeReferenceBinding] 호출 직후 data.score: " + data.score);
        
        // 매개변수 변수에 새로운 객체의 힙 주소를 대입 (기존 복사된 주소와의 연결이 끊김)
        data = new ScoreData(777); 
        
        System.out.println("[changeReferenceBinding] 새 객체 대입 후 data.score: " + data.score);
    }

    public static void main(String[] args) {
        System.out.println("=== 3단계: Call by Value (Parameter Passing) 검증 실습 ===");

        // 1. 기본 타입 테스트
        System.out.println("\n--- 1. 기본 타입 (Primitive Type) 전달 ---");
        int originalValue = 100;
        System.out.println("메서드 호출 전 originalValue: " + originalValue);
        
        changePrimitive(originalValue);
        
        // [퀴즈 1] 호출 후 originalValue의 값은 어떻게 될까요?
        // TODO: 예상값을 작성해 보세요. (예: 100 or 999)
        System.out.println("메서드 호출 후 originalValue: " + originalValue);


        // 2. 참조 타입 테스트 - 객체 상태 변경
        System.out.println("\n--- 2. 참조 타입 (Reference Type) - 내부 상태 변경 ---");
        ScoreData myData1 = new ScoreData(100);
        System.out.println("메서드 호출 전 myData1.score: " + myData1.score);
        
        changeReferenceState(myData1);
        
        // [퀴즈 2] 호출 후 myData1.score의 값은 어떻게 될까요?
        // TODO: 예상값을 작성해 보세요. (예: 100 or 999)
        System.out.println("메서드 호출 후 myData1.score: " + myData1.score);


        // 3. 참조 타입 테스트 - 매개변수 바인딩 재설정
        System.out.println("\n--- 3. 참조 타입 (Reference Type) - 매개변수에 새 객체 대입 ---");
        ScoreData myData2 = new ScoreData(100);
        System.out.println("메서드 호출 전 myData2.score: " + myData2.score);
        
        changeReferenceBinding(myData2);
        
        // [퀴즈 3] 호출 후 myData2.score의 값은 어떻게 될까요?
        // TODO: 예상값을 작성해 보세요. (예: 100, 999 or 777)
        System.out.println("메서드 호출 후 myData2.score: " + myData2.score);
    }
}
