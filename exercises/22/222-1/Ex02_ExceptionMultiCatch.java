/**
 * 실습 222-1. 2단계: 다형성 기반 다중 catch 설계 및 멀티 캐치
 * 
 * [학습 포인트]
 * 1. 예외 업캐스팅(Upcasting)과 다중 catch
 *    - catch 블록은 선언된 예외 타입의 하위 예외 객체까지 모두 포착(Upcasting)할 수 있습니다.
 *    - 다중 catch 구성 시 반드시 구체적인 하위 예외를 상위에 배치하고, 일반적인 상위 예외를 하위에 배치해야 합니다.
 * 2. Unreachable catch block 컴파일 에러
 *    - 만약 Exception 이나 RuntimeException 같은 상위 클래스를 첫 catch 블록에 선언하면,
 *      뒤에 선언된 하위 예외 블록은 실행될 기회가 없어 컴파일 에러가 발생합니다.
 * 3. 멀티 캐치(Multi-catch) 구문
 *    - Java 7부터 도입된 `|` 연산자를 사용하여 여러 예외를 한 번에 처리할 수 있습니다.
 *    - 예: `catch (ArithmeticException | NullPointerException e)`
 *    - 멀티 catch 블록 내에서 예외 매개변수 e는 암묵적으로 final 속성을 가집니다. (값 재할당 불가)
 */
public class Ex02_ExceptionMultiCatch {

    public static void main(String[] args) {
        System.out.println("=== 1. 올바른 다중 catch 선언 예시 ===");
        testCorrectCatchOrder(1); // ArithmeticException 유도
        testCorrectCatchOrder(2); // NullPointerException 유도
        testCorrectCatchOrder(3); // RuntimeException 유도
        testCorrectCatchOrder(4); // 일반 Exception 유도

        System.out.println("\n=== 2. 멀티 캐치(|) 구문 예시 ===");
        testMultiCatch(1);
        testMultiCatch(2);
    }

    /**
     * 올바른 다중 catch 순서: 구체적 예외 -> 일반적 예외
     */
    private static void testCorrectCatchOrder(int type) {
        try {
            if (type == 1) {
                int divideByZero = 10 / 0; // ArithmeticException (RuntimeException의 하위)
            } else if (type == 2) {
                String str = null;
                str.length(); // NullPointerException (RuntimeException의 하위)
            } else if (type == 3) {
                // ArrayIndexOutOfBoundsException 발생 (RuntimeException의 하위)
                int[] arr = new int[2];
                int invalid = arr[5];
            } else {
                // ClassNotFoundException 발생 (Checked Exception, 일반 Exception 하위)
                // 이를 시뮬레이션하기 위해 Class.forName() 호출
                Class.forName("non.existent.Class");
            }
        } catch (ArithmeticException e) {
            System.out.println("[ArithmeticException 포착] 0 나누기 오류 발생: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("[NullPointerException 포착] Null 참조 오류 발생: " + e.getMessage());
        } catch (RuntimeException e) {
            // ArithmeticException과 NullPointerException은 앞의 catch에서 필터링되므로,
            // 그 외의 런타임 예외(예: ArrayIndexOutOfBoundsException)가 여기서 포착됩니다.
            System.out.println("[RuntimeException 포착] 기타 런타임 예외 발생: " + e.getClass().getSimpleName());
        } catch (Exception e) {
            // Checked Exception을 포함한 모든 예외의 최후의 보루
            System.out.println("[Exception 포착] 일반 예외 발생: " + e.getClass().getSimpleName());
        }

        // [컴파일 에러 재현용 설명]
        // 만약 아래와 같이 최상위 Exception을 맨 앞에 선언하면 컴파일 에러가 발생합니다.
        // ----------------------------------------------------
        // try {
        //     int divideByZero = 10 / 0;
        // } catch (Exception e) {
        //     System.out.println("모든 예외 처리");
        // } catch (ArithmeticException e) { // 컴파일 에러: Unreachable catch block for ArithmeticException.
        //     System.out.println("0 나누기 오류");
        // }
        // ----------------------------------------------------
    }

    /**
     * 멀티 캐치 구문(|)을 활용한 예외 통합 처리
     */
    private static void testMultiCatch(int type) {
        try {
            if (type == 1) {
                int divideByZero = 5 / 0;
            } else {
                String str = null;
                str.trim();
            }
        } catch (ArithmeticException | NullPointerException e) {
            // 여러 예외를 한 블록에서 처리
            System.out.println("[멀티 캐치 포착] 산술 혹은 Null 참조 오류 발생: " + e.getClass().getSimpleName());
            
            // [주의] 멀티 캐치에서 매개변수 e는 암묵적으로 final입니다.
            // e = new NullPointerException(); // 컴파일 에러: Multi-catch parameter e may not be assigned
        }
    }
}
