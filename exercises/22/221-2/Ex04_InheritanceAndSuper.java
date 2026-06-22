/**
 * 4단계: 상속 객체의 생성 순서, super, super()
 *
 * 학습 포인트
 * - Child 인스턴스 하나에는 부모로부터 물려받은 상태와 자식 상태가 함께 존재합니다.
 * - 자식 생성자는 자신의 필드를 초기화하기 전에 부모 생성자를 먼저 호출해야 합니다.
 * - 생성자의 첫 문장에 부모 생성자 호출이 없으면 컴파일러가 super()를 삽입합니다.
 * - 부모에게 매개변수 없는 생성자가 없다면 자식이 super(args)를 직접 호출해야 합니다.
 * - super는 이름이 겹친 부모 멤버나 오버라이딩 전 부모 메서드를 가리킵니다.
 */
class Person2212 {
    protected final String name;

    Person2212(String name) {
        this.name = name;
        System.out.println("1. Person 생성자: " + name);
    }

    String introduce() {
        return "사람 " + name;
    }
}

class Student2212 extends Person2212 {
    private final String course;

    Student2212(String name, String course) {
        // Person2212에는 기본 생성자가 없으므로 알맞은 부모 생성자를 첫 줄에서 호출합니다.
        super(name);

        // 부모 영역 초기화가 끝난 다음 자식 고유 상태를 초기화합니다.
        this.course = course;
        System.out.println("2. Student 생성자: " + course);
    }

    @Override
    String introduce() {
        // super.introduce()는 오버라이딩 전 부모 구현을 명시적으로 호출합니다.
        return super.introduce() + ", 수강 과정 " + course;
    }
}

public class Ex04_InheritanceAndSuper {
    public static void main(String[] args) {
        System.out.println("=== 상속 객체의 생성 순서 ===");

        Student2212 student = new Student2212("김민수", "Java Backend");
        System.out.println(student.introduce());

        /*
         * 확인 1: super(name)을 제거하면 컴파일러가 자동으로 어떤 생성자를 호출하는지 확인하세요.
         * 확인 2: Person2212에 기본 생성자를 추가한 뒤 super(name)을 제거하고 name 값을 관찰하세요.
         * 확인 3: 상속이 단순 코드 재사용에 사용될 때 부모 변경이 자식에 미치는 영향을 적으세요.
         */
    }
}
