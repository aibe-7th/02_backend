/**
 * 실습 221-1. 1단계: JVM 메모리 영역 & 변수 스코프
 * 
 * [학습 포인트]
 * 1. 멤버 변수(인스턴스 변수)는 힙(Heap) 영역에 생성되며, 기본값으로 자동 초기화됩니다.
 * 2. 지역 변수(Local Variable)는 스택(Stack) 영역에 생성되며, 반드시 사용 전에 초기화해야 합니다.
 * 3. 메서드 호출 시 해당 메서드를 위한 스택 프레임(Stack Frame)이 생기고, 종료 시 완전히 소멸합니다.
 */
public class Ex01_MemoryAndScope {

    // 1. 멤버 변수 (인스턴스 변수)
    // 힙 영역에 생성되는 객체 내부에 존재하며, 값을 할당하지 않아도 기본값(0)으로 자동 초기화됩니다.
    int instanceCount; 
    boolean isActivated;
    String statusName; // 참조 타입은 null로 초기화됨

    public void testMethod(int parameterValue) {
        // parameterValue는 매개변수로, 지역 변수와 동일하게 스택 프레임에 저장됩니다.
        System.out.println("매개변수 parameterValue (스택 영역): " + parameterValue);

        // 2. 지역 변수 (Local Variable)
        // 스택 영역에 생성되며 자동 초기화되지 않습니다.
        int localSum = 0; // 명시적 초기화 완료
        
        // [미션 1] 아래 주석을 해제하여 컴파일 에러를 관찰해 보세요.
        // 자바는 초기화되지 않은 지역 변수의 사용을 엄격히 금지합니다.
        // int uninitializedLocal;
        // System.out.println(uninitializedLocal); // 컴파일 에러 발생!

        localSum += 10;
        System.out.println("지역 변수 localSum (스택 영역): " + localSum);
    }

    public static void main(String[] args) {
        System.out.println("=== 1단계: JVM 메모리 영역 및 변수 수명 실습 ===");

        // 3. 객체 생성과 JVM 메모리 배치 매핑
        // - 'memoryTest' 참조 변수는 스택(Stack) 영역에 할당됩니다.
        // - 'new Ex01_MemoryAndScope()'로 생성된 실제 객체는 힙(Heap) 영역에 할당됩니다.
        // - memoryTest 변수에는 힙 영역에 있는 객체의 메모리 주소(참조값)가 복사되어 저장됩니다.
        Ex01_MemoryAndScope memoryTest = new Ex01_MemoryAndScope();

        // 4. 멤버 변수의 자동 초기화 상태 출력 확인
        System.out.println("--- 멤버 변수 자동 초기화 값 (힙 영역) ---");
        System.out.println("instanceCount: " + memoryTest.instanceCount); // 0 출력 기대
        System.out.println("isActivated: " + memoryTest.isActivated);     // false 출력 기대
        System.out.println("statusName: " + memoryTest.statusName);       // null 출력 기대

        // 5. 멤버 변수 값 할당 후 변경 확인
        memoryTest.instanceCount = 99;
        memoryTest.statusName = "Running";
        System.out.println("변경 후 instanceCount: " + memoryTest.instanceCount);
        System.out.println("변경 후 statusName: " + memoryTest.statusName);

        // 6. 스택 프레임의 생성과 소멸 관찰
        System.out.println("\n--- 메서드 호출 시 스택 프레임 동작 ---");
        memoryTest.testMethod(500);
        // testMethod() 호출 시: 스택에 testMethod 프레임 생성 (parameterValue=500, localSum=10 적재)
        // testMethod() 종료 시: 스택에서 프레임이 제거(Pop)되며 내부의 매개변수와 지역 변수 소멸

        // [참고] static 변수와 상수는 JVM이 구동될 때 클래스 로더에 의해 메서드 영역(Metaspace)에 올라갑니다.
    }
}
