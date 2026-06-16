/**
 * 자바의 변수 타입(원시 타입과 참조 타입), 메모리 구조, 그리고 형변환(Casting)을 다룹니다.
 */
public class Ex02_VariablesAndTypes {
    public static void main(String[] args) {
        // 1. 원시 타입 (Primitive Type) - 스택 영역에 직접 값 저장
        System.out.println("--- 1. 원시 타입과 접미사 ---");
        
        byte b = 127;
        short s = 32767;
        int i = 2147483647; // 정수 연산의 기본 단위
        
        // long 타입은 값 뒤에 'L' 또는 'l' 접미사가 필수 (대문자 L 권장)
        long l = 10000000000L; 
        
        // float 타입은 값 뒤에 'F' 또는 'f' 접미사가 필수
        float f = 3.14f; 
        double d = 3.1415926535; // 실수 연산의 기본 단위, 더 높은 정밀도

        char c = '가'; // 내부적으로 유니코드 정수값(44032)으로 저장됨
        boolean isJavaFun = true; // true, false만 허용 (정수 0 이나 null 등 호환 불가)

        System.out.println("long 값: " + l);
        System.out.println("float 값: " + f);
        System.out.println("char '가'의 정수값 표현: " + (int) c);
        System.out.println("boolean 값: " + isJavaFun);

        // 2. 형변환 (Type Casting)
        System.out.println("\n--- 2. 형변환 (Casting) ---");
        
        // 묵시적 형변환 (작은 타입 -> 큰 타입: 데이터 유실 없음)
        int smallInt = 100;
        double bigDouble = smallInt; 
        System.out.println("자동 형변환 (int -> double): " + bigDouble);

        // 명시적 형변환 (큰 타입 -> 작은 타입: 데이터 유실 위험 감수)
        double pi = 3.141592;
        int truncatedPi = (int) pi; // 소수점 아래 버림
        System.out.println("강제 형변환 (double -> int): " + truncatedPi);

        // 3. 문자열 파싱 (String -> 숫자)
        System.out.println("\n--- 3. 문자열 -> 숫자 파싱 ---");
        String numberStr = "12345";
        int parsedInt = Integer.parseInt(numberStr);
        
        String doubleStr = "99.9";
        double parsedDouble = Double.parseDouble(doubleStr);

        System.out.println("파싱된 int 값 + 5 = " + (parsedInt + 5));
        System.out.println("파싱된 double 값 + 0.1 = " + (parsedDouble + 0.1));
    }
}
