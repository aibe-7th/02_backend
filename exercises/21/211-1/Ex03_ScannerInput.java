import java.util.Scanner;

/**
 * Scanner를 이용한 표준 입력과 next() vs nextLine()의 버퍼 오류 현상을 실습합니다.
 */
public class Ex03_ScannerInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Scanner 입력 실습 ===");
        
        // 1. 단어 단위 입력 (next)
        System.out.print("공백을 포함해 두 단어를 입력하세요 (예: Hello World): ");
        String firstWord = scanner.next();
        String secondWord = scanner.next();
        System.out.println("첫 번째 단어: " + firstWord);
        System.out.println("두 번째 단어: " + secondWord);

        // 버퍼에 남아있는 줄바꿈 문자(\n) 소비하기
        scanner.nextLine();

        // 2. 버퍼 오류 재현 및 해결
        System.out.println("\n--- nextInt() 이후 nextLine() 버퍼 오류 상황 ---");
        System.out.print("나이를 입력하세요 (nextInt): ");
        int age = scanner.nextInt(); // 정수만 읽어가고 줄바꿈(\n)은 버퍼에 그대로 남겨둠
        
        // 오류 해결을 위해 버퍼를 비워주는 과정이 없으면, 
        // 바로 아래의 nextLine()은 버퍼에 남아있는 '\n'을 빈 줄로 인식하여 입력을 건너뜁니다.
        System.out.println("[해결책] scanner.nextLine()을 호출해 버퍼의 개행문자를 제거합니다.");
        scanner.nextLine(); // 버퍼 비우기!!

        System.out.print("이름을 입력하세요 (nextLine): ");
        String name = scanner.nextLine(); // 버퍼가 깨끗하므로 사용자의 실제 입력을 대기함

        System.out.println("입력된 나이: " + age);
        System.out.println("입력된 이름: " + name);

        scanner.close();
    }
}
