import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 실습 222-1. 1단계: Checked Exception vs Unchecked Exception
 * 
 * [학습 포인트]
 * 1. Checked Exception (검사 예외)
 *    - Exception 클래스를 상속받되 RuntimeException을 상속받지 않는 예외들입니다.
 *    - 컴파일 타임에 예외 처리 여부를 확인하며, try-catch나 throws로 처리하지 않으면 컴파일 에러가 발생합니다.
 *    - 외부 시스템과의 연동(I/O, DB, Network 등) 과정에서 발생하기 쉬운 예외들에 사용됩니다.
 * 2. Unchecked Exception (비검사 예외)
 *    - RuntimeException 클래스를 상속받는 예외들입니다.
 *    - 컴파일 타임에 예외 처리를 강제하지 않고, 런타임에 예외 상황이 발생할 경우에 감지됩니다.
 *    - 주로 개발자의 논리적 오류(NullPointerException, ArithmeticException 등)로 인해 발생합니다.
 * 3. 자바 vs 자바스크립트
 *    - 자바스크립트는 언어 설계 상 모든 예외가 런타임에만 확인되는 Unchecked Exception 구조를 갖습니다.
 *    - 반면 자바는 컴파일 시점의 정적 타입 안정성을 강화하기 위해 Checked Exception을 도입했습니다.
 */
public class Ex01_CheckedVsUnchecked {

    public static void main(String[] args) {
        System.out.println("=== 1. Unchecked Exception (RuntimeException) 테스트 ===");
        runUncheckedExceptionSample();

        System.out.println("\n=== 2. Checked Exception (Exception) 테스트 ===");
        runCheckedExceptionSample();
    }

    /**
     * Unchecked 예외 예시: ArithmeticException, NullPointerException
     * 예외 처리를 하지 않아도 컴파일은 성공하지만, 실행 중 예외가 발생하면 프로그램이 즉시 강제 종료됩니다.
     */
    private static void runUncheckedExceptionSample() {
        int a = 10;
        int b = 0;

        try {
            // 0으로 나누기 시도 (ArithmeticException 발생)
            int result = a / b;
            System.out.println("나눗셈 결과: " + result);
        } catch (ArithmeticException e) {
            System.out.println("[예외 포착] 0으로 나눌 수 없습니다. (예외 메시지: " + e.getMessage() + ")");
        }

        // 아래 코드는 NullPointerException(Unchecked)을 발생시키는 예시입니다.
        // 예외 처리를 하지 않아도 자바 컴파일러는 태클을 걸지 않습니다.
        String str = null;
        try {
            System.out.println("문자열 길이: " + str.length());
        } catch (NullPointerException e) {
            System.out.println("[예외 포착] null 참조 변수에서 메서드를 호출했습니다.");
        }
    }

    /**
     * Checked 예외 예시: FileNotFoundException (IOException 계열)
     * 자바 컴파일러는 이 메서드가 던질 수도 있는 예외에 대한 처리를 반드시 의무화(강제)합니다.
     */
    private static void runCheckedExceptionSample() {
        // [컴파일 에러 재현용 설명]
        // 만약 아래의 try-catch 블록 없이 단독으로 new Scanner(new File("nonexistent.txt"))를 실행하려고 하면
        // 컴파일러가 "Unhandled exception type FileNotFoundException" 에러를 발생시킵니다.
        // 
        // ----------------------------------------------------
        // Scanner scanner = new Scanner(new File("nonexistent.txt")); // 컴파일 에러 발생!
        // ----------------------------------------------------

        try {
            File file = new File("nonexistent_file.txt");
            System.out.println("존재하지 않는 파일을 읽으려 시도합니다...");
            Scanner scanner = new Scanner(file);
            System.out.println("파일 첫 번째 줄: " + scanner.nextLine());
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("[예외 포착] 파일을 찾을 수 없습니다. (예외 메시지: " + e.getMessage() + ")");
            System.out.println("-> Checked 예외는 컴파일러가 이와 같이 예외 처리를 강제하여 예기치 못한 종료를 막아줍니다.");
        }
    }
}
