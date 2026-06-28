import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BinaryOperator;

/**
 * 실습 01: 람다식(Lambda Expression)과 표준 함수형 인터페이스
 * 
 * [목표]
 * 1. 익명 클래스를 람다식으로 전환하는 방법을 배웁니다.
 * 2. 람다 내부에서의 변수 캡처링(Variable Capturing) 제약을 이해합니다.
 * 3. 자바의 핵심 표준 함수형 인터페이스(Consumer, Supplier, Function, Predicate, BinaryOperator)의 역할을 실습합니다.
 * 4. 람다식을 메서드 참조(Method Reference)로 축약하는 방법을 익힙니다.
 */
public class Ex01_LambdaAndFunctionalInterfaces {

    @FunctionalInterface
    interface CustomPrinter {
        void printMessage(String message);
    }

    public static void main(String[] args) {
        System.out.println("=== 실습 1-1: 익명 클래스 vs 람다식 ===");
        runLambdaSyntaxDemo();

        System.out.println("\n=== 실습 1-2: 변수 캡처링 제약 ===");
        runVariableCapturingDemo();

        System.out.println("\n=== 실습 1-3: 표준 함수형 인터페이스 활용 ===");
        runStandardInterfacesDemo();

        System.out.println("\n=== 실습 1-4: 메서드 참조 (Method Reference) ===");
        runMethodReferenceDemo();
    }

    /**
     * 익명 내부 클래스를 간결한 람다식으로 변환하는 실습
     */
    private static void runLambdaSyntaxDemo() {
        // 1. 기존의 익명 클래스 방식
        CustomPrinter oldPrinter = new CustomPrinter() {
            @Override
            public void printMessage(String message) {
                System.out.println("[익명 클래스] " + message);
            }
        };
        oldPrinter.printMessage("Hello Java!");

        // TODO: 위의 oldPrinter를 람다식을 이용해 작성해 보세요. (단 한 줄로 축약 가능)
        CustomPrinter lambdaPrinter = null; 
        
        if (lambdaPrinter != null) {
            lambdaPrinter.printMessage("Hello Lambda!");
        }
    }

    /**
     * 람다 내부에서 외부 지역 변수를 읽을 때의 제약 (Variable Capturing)
     */
    private static void runVariableCapturingDemo() {
        int limitValue = 10; // 지역 변수

        Runnable r = () -> {
            // 외부 지역 변수를 읽는 것은 가능합니다.
            System.out.println("람다 내부에서 외부 변수 읽기: " + limitValue);
            
            // TODO: 아래 주석을 해제하면 컴파일 에러가 발생합니다. 그 이유는 무엇일까요?
            // limitValue = 20; 
        };

        r.run();
        
        // TODO: 만약 람다 선언 뒤에 아래처럼 값을 변경하려고 하면 어떻게 될까요?
        // limitValue = 30;
        // r.run();
        
        System.out.println("-> 요약: 람다식 내부에서 참조하는 지역 변수는 final이거나 effectively final(사실상 상수)이어야 합니다.");
    }

    /**
     * java.util.function 패키지의 표준 함수형 인터페이스 활용
     */
    private static void runStandardInterfacesDemo() {
        // TODO: 아래 각각의 표준 함수형 인터페이스 명세에 맞게 람다식을 구현해 보세요.

        // 1. Consumer<T>: 인자를 받아서 소비하고 반환값이 없음
        // 예: 입력받은 문자열을 "[Consumer] " 머리글과 함께 System.out.println으로 출력
        Consumer<String> consumer = s -> {}; // TODO: 람다식 구현하기
        consumer.accept("홍길동");

        // 2. Supplier<T>: 인자를 받지 않고 결과값을 제공
        // 예: 호출될 때마다 "Hello World!" 문자열을 반환
        Supplier<String> supplier = () -> ""; // TODO: 람다식 구현하기
        System.out.println("[Supplier] 결과: " + supplier.get());

        // 3. Function<T, R>: T 타입 입력을 R 타입 결과로 변환(매핑)
        // 예: 입력받은 문자열을 정수(Integer)로 파싱하여 반환 (Integer.parseInt 사용)
        Function<String, Integer> function = s -> 0; // TODO: 람다식 구현하기
        System.out.println("[Function] 결과: " + (function.apply("123") + 10)); // 133 출력 기대

        // 4. Predicate<T>: 입력값을 평가해 boolean 값을 반환
        // 예: 입력받은 문자열이 비어있지 않은지(not empty) 여부 반환 (s.isEmpty() 활용)
        Predicate<String> predicate = s -> false; // TODO: 람다식 구현하기
        System.out.println("[Predicate] 'test'는 비어있지 않은가? " + predicate.test("test"));
        System.out.println("[Predicate] ''는 비어있지 않은가? " + predicate.test(""));

        // 5. BinaryOperator<T>: 동일한 타입 두 개를 연산하여 동일한 타입 결과 반환
        // 예: 두 개의 정수(Integer)를 더한 결과 반환
        BinaryOperator<Integer> adder = (a, b) -> 0; // TODO: 람다식 구현하기
        System.out.println("[BinaryOperator] 더하기 결과: " + adder.apply(40, 2)); // 42 출력 기대
    }

    /**
     * 람다식을 메서드 참조로 축약하여 코드 가독성을 극대화하는 실습
     */
    private static void runMethodReferenceDemo() {
        // 1. 정적 메서드 참조 (Static Method Reference)
        // 기존 람다: s -> Integer.parseInt(s)
        // TODO: 아래 람다식을 'Integer::parseInt' 형태로 리팩토링하세요.
        Function<String, Integer> staticRef = s -> Integer.parseInt(s);
        System.out.println("정적 메서드 참조 결과: " + staticRef.apply("999"));

        // 2. 인스턴스 메서드 참조 (Instance Method Reference)
        // 기존 람다: s -> System.out.println(s)
        // TODO: 아래 람다식을 'System.out::println' 형태로 리팩토링하세요.
        Consumer<String> instanceRef = s -> System.out.println(s);
        instanceRef.accept("인스턴스 메서드 참조를 통해 출력되었습니다.");
    }
}
