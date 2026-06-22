/**
 * 실습 221-1. 5단계: 내부 클래스(Inner Class)와 메모리 누수
 * 
 * [학습 포인트]
 * 1. 비정적 내부 클래스(Non-static Inner Class)는 바깥 클래스 인스턴스에 대한 '숨은 외부 참조(Outer.this)'를 가집니다.
 * 2. 이로 인해 내부 클래스 객체가 살아있는 동안 바깥 클래스 객체는 사용되지 않더라도 GC(가비지 컬렉터)에 의해 수거되지 못하고 메모리 누수가 발생할 수 있습니다.
 * 3. 해결책: 바깥 인스턴스를 직접 참조할 필요가 없다면, 내부 클래스를 반드시 'static' 내부 클래스로 정의해야 합니다.
 */

// 1. 메모리 누수 위험이 있는 구조 (비정적 내부 클래스)
class OuterLeak {
    private String outerData = "Outer Data";
    
    // 비정적 내부 클래스 (Non-static Inner Class)
    public class DangerousInner {
        public void printOuter() {
            // 바깥 클래스의 private 멤버변수에 숨은 참조(OuterLeak.this)를 통해 바로 접근 가능합니다.
            System.out.println("바깥 데이터 접근: " + outerData);
        }
    }
}

// 2. 메모리 안전 구조 (정적 내부 클래스)
class OuterSafe {
    private static String outerStaticData = "Outer Static Data";
    private String outerInstanceData = "Outer Instance Data";

    // 정적 내부 클래스 (Static Inner Class)
    // 이 클래스는 바깥 인스턴스에 대한 숨은 참조가 생성되지 않아 메모리 누수가 발생하지 않습니다.
    public static class SafeStaticInner {
        
        public void printOuter() {
            // [정적 멤버 접근]
            // 바깥 클래스의 static 멤버에는 자유롭게 접근 가능
            System.out.println("바깥 static 데이터 접근: " + outerStaticData);

            // [미션 1] 아래 주석을 해제하여 컴파일 에러를 관찰해 보세요.
            // static 내부 클래스는 바깥 인스턴스가 존재하지 않을 수 있으므로, 바깥 인스턴스 멤버에 직접 접근할 수 없습니다.
            // System.out.println(outerInstanceData); // 컴파일 에러!
        }

        // 정적 내부 클래스에서 바깥 클래스의 인스턴스 멤버에 접근하고 싶을 때의 올바른 방법
        public void printOuterInstance(OuterSafe outer) {
            // 명시적으로 바깥 클래스의 참조를 전달받아 사용하므로 숨은 참조를 유발하지 않아 안전합니다.
            System.out.println("전달받은 바깥 인스턴스 데이터: " + outer.outerInstanceData);
        }
    }
}

public class Ex05_InnerClassMemoryLeak {
    public static void main(String[] args) {
        System.out.println("=== 5단계: 내부 클래스의 아웃터 참조에 의한 메모리 누수 예방 실습 ===");

        // 1. 비정적 내부 클래스의 인스턴스 생성 흐름
        // 반드시 바깥 객체를 먼저 생성한 후에만 내부 클래스 객체를 생성할 수 있습니다.
        OuterLeak outerLeak = new OuterLeak();
        
        // 문법: outerInstance.new InnerClass()
        // 이 inner 객체는 메모리상에서 outerLeak 객체에 대한 숨겨진 참조를 갖고 있게 됩니다.
        OuterLeak.DangerousInner inner = outerLeak.new DangerousInner();
        inner.printOuter();

        // 2. 정적 내부 클래스의 인스턴스 생성 흐름
        // 바깥 객체 생성 여부와 관계없이 클래스 레벨에서 바로 단독으로 생성할 수 있습니다.
        // 문법: new OuterClass.StaticInnerClass()
        OuterSafe.SafeStaticInner safeInner = new OuterSafe.SafeStaticInner();
        safeInner.printOuter();

        // 3. 정적 내부 클래스에서 바깥 인스턴스 제어 테스트
        OuterSafe outerSafe = new OuterSafe();
        safeInner.printOuterInstance(outerSafe);

        System.out.println("\n--- 메모리 참조 관계 요약 ---");
        System.out.println("- 비정적 내부 클래스 객체 -> 바깥 클래스 객체 참조 유지 (GC 불가 리스크)");
        System.out.println("- 정적 내부 클래스 객체 -> 바깥 클래스 객체 참조 없음 (메모리 안전, 실무 권장)");
    }
}
