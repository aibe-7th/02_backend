import java.util.List;
import java.util.Optional;

/**
 * 실습 04: Java Stream vs JavaScript Array 고차함수
 * 
 * [목표]
 * 1. JavaScript의 즉시 연산(Eager Evaluation)과 Java의 지연 연산(Lazy Evaluation) 메모리 거동 차이를 이해합니다.
 * 2. JavaScript의 배열 가공 코드(filter, map, reduce)를 Java Stream 파이프라인으로 1:1 매핑 구현해 봅니다.
 */
public class Ex04_StreamVsJavaScript {

    public static void main(String[] args) {
        /*
         * [JavaScript 예시]
         * const numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
         * 
         * const sumOfEvenMultipliedByTen = numbers
         *   .filter(n => n % 2 === 0)       // 즉각 필터링된 임시 배열 생성 (6개 요소)
         *   .map(n => n * 10)              // 즉각 매핑된 임시 배열 생성 (6개 요소)
         *   .reduce((acc, val) => acc + val, 0); // 최종 단일 값 연산 (2 + 4 + 6 + 8 + 10 = 30 * 10 = 300)
         * 
         * console.log(sumOfEvenMultipliedByTen); // 300
         */

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // TODO: 위의 JavaScript 코드를 아래 Java Stream 파이프라인으로 번역해 보세요.
        // 1. numbers 리스트에서 stream()을 추출합니다.
        // 2. filter()를 사용해 짝수(n % 2 == 0)만 거릅니다.
        // 3. map()을 사용해 각 요소를 10배 증가시킵니다.
        // 4. reduce()를 사용해 모든 요소의 합을 구합니다.
        
        int result = 0;
        
        // result = numbers.stream()...
        
        System.out.println("Java Stream 연산 결과: " + result);
        // 기대값: 300
        
        // TODO-질문: JS와 Java Stream의 연산 진행 시 메모리 사용 및 평가 방식의 가장 핵심적인 차이점을 주석으로 정리해 봅시다.
        // (힌트: 즉시 연산 vs 지연 연산, 중간 배열 생성 여부)
    }
}
