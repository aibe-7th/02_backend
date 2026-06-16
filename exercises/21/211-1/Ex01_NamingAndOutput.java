/**
 * Javadoc 문서화 주석 예시입니다.
 * 이 클래스는 자바의 표기법(Naming Convention)과 표준 출력, 문자와 문자열의 구분을 다룹니다.
 *
 * @author AIBE 7th Backend Agent
 * @version 1.0
 */
public class Ex01_NamingAndOutput {

    // 상수 표기법: 대문자 스네이크 케이스 (UPPER_SNAKE_CASE)
    public static final int MAX_USERS = 100;

    public static void main(String[] args) {
        // 변수 표기법: 카멜 케이스 (camelCase)
        int userAge = 25;
        String userName = "민지";

        // 1. 표준 출력
        System.out.println("--- 1. 표준 출력 ---");
        System.out.print("안녕하세요. "); // 줄 바꿈 없음
        System.out.println("반갑습니다."); // 출력 후 줄 바꿈
        System.out.println("이름: " + userName + ", 나이: " + userAge);

        // 2. 문자와 문자열의 엄격한 구분
        System.out.println("\n--- 2. 문자와 문자열 구분 ---");
        char singleChar = 'A'; // 홑따옴표 사용, 2바이트 단일 문자
        String multiChar = "A"; // 쌍따옴표 사용, 참조 타입 문자열 객체

        System.out.println("char 값: " + singleChar);
        System.out.println("String 값: " + multiChar);

        // 컴파일 에러 유발 케이스 (주석 해제 시 에러 발생)
        // char errorChar = "A"; // 에러: String을 char에 대입할 수 없음
        // String errorString = 'A'; // 에러: char를 String에 대입할 수 없음
    }
}
