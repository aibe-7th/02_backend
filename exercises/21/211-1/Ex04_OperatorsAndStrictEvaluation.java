/**
 * 자바의 핵심 연산자(단축 평가, 문자열 동등성 비교, Strict Boolean, instanceof, 비트마스크)를 다룹니다.
 */
public class Ex04_OperatorsAndStrictEvaluation {
    public static void main(String[] args) {
        // 1. 문자열 연결 연산자
        System.out.println("--- 1. 문자열 연결 ---");
        System.out.println("Result A: " + 10 + 20);      // "Result A: 10" + 20 -> "Result A: 1020"
        System.out.println("Result B: " + (10 + 20));    // 괄호 우선연산 -> "Result B: 30"

        // 2. 단축 평가 (Short-circuit Evaluation)
        System.out.println("\n--- 2. 단축 평가 ---");
        int count = 0;
        boolean result = (false && (++count > 0)); // false이므로 후행 연산 (++count > 0) 실행 안 됨
        System.out.println("단축 평가 결과: " + result);
        System.out.println("증가 연산 수행 여부 (count): " + count); // 0 유지

        // 3. 참조 비교 (==) vs 동등 비교 (equals())
        System.out.println("\n--- 3. 문자열 비교 (== vs equals()) ---");
        String str1 = "Java"; // String Constant Pool(상수 풀)에 생성되어 공유됨
        String str2 = "Java"; 
        String str3 = new String("Java"); // Heap 영역에 새 객체로 생성됨

        System.out.println("str1 == str2 (상수 풀 주소 비교): " + (str1 == str2)); // true
        System.out.println("str1 == str3 (힙 영역 주소 비교): " + (str1 == str3)); // false (주소 다름)
        System.out.println("str1.equals(str3) (문자열 값 비교): " + str1.equals(str3)); // true (내용 동일)

        // 4. Strict Boolean (Truthy/Falsy 없음)
        System.out.println("\n--- 4. Strict Boolean ---");
        // Java에서는 JS처럼 if(1) 또는 if(null) 등의 연산이 불가능함.
        int number = 1;
        // if (number) { } // 컴파일 에러 발생!
        if (number != 0) { // 명확한 boolean 식만 조건에 사용 가능
            System.out.println("조건식이 true로 판별됨 (number != 0)");
        }

        // 5. instanceof 연산자
        System.out.println("\n--- 5. instanceof ---");
        Object obj = "Hello Java";
        if (obj instanceof String) {
            String text = (String) obj;
            System.out.println("obj는 String 타입입니다. 문자열 길이: " + text.length());
        }

        // 6. 비트 연산 및 비트마스크 활용
        System.out.println("\n--- 6. 비트 연산 및 비트마스크 ---");
        // 비트 플래그 정의 (2진수: 0001, 0010, 0100, 1000)
        int FLAG_READ   = 1 << 0; // 1 (0001)
        int FLAG_WRITE  = 1 << 1; // 2 (0010)
        int FLAG_DELETE = 1 << 2; // 4 (0100)
        
        // 현재 권한 설정 (읽기 및 쓰기 권한 부여: 0001 | 0010 = 0011 -> 3)
        int userPermission = FLAG_READ | FLAG_WRITE;
        System.out.println("사용자 권한 비트: " + Integer.toBinaryString(userPermission));

        // 권한 확인 (비트 AND 연산)
        boolean canDelete = (userPermission & FLAG_DELETE) != 0;
        boolean canWrite = (userPermission & FLAG_WRITE) != 0;

        System.out.println("삭제 권한 여부: " + canDelete); // false
        System.out.println("쓰기 권한 여부: " + canWrite);  // true
    }
}
