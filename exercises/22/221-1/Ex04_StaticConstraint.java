/**
 * 실습 221-1. 4단계: Static 제약 조건
 * 
 * [학습 포인트]
 * 1. 'static' 변수/메서드는 인스턴스가 아닌 클래스에 종속되며, JVM 메서드 영역(Method Area)에 로드됩니다.
 * 2. 모든 인스턴스가 하나의 static 변수를 공유하여 공동 자원으로 활용할 수 있습니다.
 * 3. static 메서드는 인스턴스 생성 없이 클래스명으로 상시 호출할 수 있습니다.
 * 4. static 메서드 내부에서는 인스턴스 변수/메서드에 직접 접근이 불가능하며, 'this' 키워드도 사용할 수 없습니다.
 */

class UserAccount {
    String username;          // 인스턴스 변수 (힙 영역)
    
    // static 변수 (메서드 영역) - 모든 UserAccount 객체가 이 변수를 공유합니다.
    static int activeUsers = 0; 

    public UserAccount(String username) {
        this.username = username;
        // 객체가 생성될 때마다 공유 변수인 activeUsers를 1씩 증가
        activeUsers++; 
    }

    // 인스턴스 메서드: static 멤버와 인스턴스 멤버에 모두 자유롭게 접근 가능
    public void printUserSession() {
        System.out.println("[인스턴스 메서드] 유저: " + this.username + " | 현재 접속자 수: " + activeUsers);
    }

    // 정적(static) 메서드
    public static int getActiveUsers() {
        // [미션 1] 아래 주석을 풀어 컴파일 에러를 관찰하고, 왜 에러가 나는지 분석해 보세요.
        // static 메서드는 인스턴스가 메모리에 없어도 호출될 수 있어야 하므로, 인스턴스 변수에 직접 접근이 불가능합니다.
        // System.out.println("접속 유저명: " + username); // 컴파일 에러!
        // System.out.println("this 정보: " + this);      // 컴파일 에러! (this 사용 불가)

        // TODO: 아래 반환값을 static 변수인 activeUsers로 변경하여 컴파일 에러를 해결하고 개수를 반환하세요.
        return 0; 
    }

    // 정적(static) 메서드 내부에서 인스턴스 멤버를 굳이 사용하고 싶을 때 해결법
    public static void displayUserStatic(UserAccount user) {
        // 매개변수나 로컬 영역에서 인스턴스 주소를 명시적으로 받으면(또는 내부에서 new 생성 시) 접근 가능
        System.out.println("[정적 메서드 우회 접근] 유저명: " + user.username);
    }
}

public class Ex04_StaticConstraint {
    public static void main(String[] args) {
        System.out.println("=== 4단계: static 키워드 및 정적 메서드 제약 조건 실습 ===");

        // 1. 객체를 생성하지 않은 시점에서 static 메서드 호출 테스트
        // 클래스명으로 즉시 접근 가능
        System.out.println("초기 생성자 호출 전 activeUsers (static 호출): " + UserAccount.getActiveUsers());

        // 2. 객체 생성 및 인스턴스 메서드 호출
        UserAccount user1 = new UserAccount("김민수");
        user1.printUserSession();

        UserAccount user2 = new UserAccount("이지원");
        user2.printUserSession();

        UserAccount user3 = new UserAccount("박준형");
        user3.printUserSession();

        // 3. 누적된 static 변수 확인
        System.out.println("\n--- static 변수 공유 결과 ---");
        System.out.println("최종 활성 사용자 수 (클래스 레벨 호출): " + UserAccount.getActiveUsers());
        
        // 권장되지는 않지만 인스턴스를 통해 static 변수에 접근할 수도 있습니다.
        // 그러나 이는 static의 본질적 의미를 왜곡하므로 UserAccount.activeUsers 방식을 쓰는 것이 표준입니다.
        System.out.println("user1을 통해 호출한 활성 사용자 수: " + user1.activeUsers); 

        // 4. 정적 메서드 우회 접근 실행
        System.out.println("\n--- 정적 메서드 내에서 인스턴스 제어 우회 ---");
        UserAccount.displayUserStatic(user1);
    }
}
