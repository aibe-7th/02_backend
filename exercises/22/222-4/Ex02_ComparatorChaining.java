import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 실습 02: Comparator 체이닝을 통한 다차원 정렬
 * 
 * [목표]
 * 1. Comparator의 comparing 및 thenComparingInt 디폴트 메서드를 학습합니다.
 * 2. 람다식 및 메서드 참조를 사용하여 복잡한 정렬 조건을 안전하게 작성하는 법을 익힙니다.
 */
public class Ex02_ComparatorChaining {

    // 도메인 클래스 설계 (슬라이드 기준)
    static class User {
        private final String name;
        private final int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>(List.of(
            new User("Kim", 25),
            new User("Lee", 30),
            new User("Kim", 20),
            new User("Park", 22),
            new User("Lee", 25)
        ));

        System.out.println("정렬 전: " + users);

        // TODO: 아래 조건에 맞게 users 리스트를 정렬해 보세요.
        // 조건 1: 이름(name) 알파벳 순으로 오름차순 1차 정렬 (User::getName 사용)
        // 조건 2: 이름이 같을 경우 나이(age) 오름차순으로 2차 정렬 (User::getAge 또는 User::getAge를 이용한 thenComparingInt 사용)
        
        // users.sort(...);

        System.out.println("정렬 후: " + users);
        // 기대 출력 결과: [Kim(20), Kim(25), Lee(25), Lee(30), Park(22)]
    }
}
