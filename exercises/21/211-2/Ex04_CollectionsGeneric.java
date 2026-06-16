import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 자바 컬렉션 프레임워크(ArrayList, HashMap, HashSet)와 제네릭(Generic) 타입 설정을 다룹니다.
 */
public class Ex04_CollectionsGeneric {
    public static void main(String[] args) {
        // 1. List 계열 - ArrayList (동적 배열)
        System.out.println("--- 1. ArrayList (동적 배열) ---");
        // 제네릭 <String>을 지정하여 문자열 객체만 담을 수 있도록 강제합니다.
        ArrayList<String> list = new ArrayList<>();
        
        list.add("Java");
        list.add("Spring");
        list.add("Java"); // 중복 데이터 저장 허용, 삽입 순서 유지됨
        
        System.out.println("현재 리스트 크기(size): " + list.size());
        System.out.println("0번 인덱스 조회: " + list.get(0));
        
        list.remove(2); // 2번 인덱스의 중복 "Java" 삭제
        System.out.println("삭제 후 전체 리스트: " + list);

        // 2. Map 계열 - HashMap (Key-Value 구조)
        System.out.println("\n--- 2. HashMap (Key-Value) ---");
        // 제네릭으로 Key는 String, Value는 Integer로 고정
        HashMap<String, Integer> studentScores = new HashMap<>();
        
        studentScores.put("장원영", 90);
        studentScores.put("안유진", 100);
        studentScores.put("장원영", 95); // 동일한 Key에 값을 다시 넣으면 덮어쓰기(Update) 동작
        
        System.out.println("장원영의 점수: " + studentScores.get("장원영")); // 95
        System.out.println("카리나라는 키가 존재하는지 여부: " + studentScores.containsKey("카리나")); // false
        
        studentScores.remove("안유진");
        System.out.println("안유진 삭제 후 전체 Map 크기: " + studentScores.size());

        // 3. Set 계열 - HashSet (중복 제거 집합)
        System.out.println("\n--- 3. HashSet (중복 제거 집합) ---");
        HashSet<String> uniqueTags = new HashSet<>();
        
        uniqueTags.add("Java");
        uniqueTags.add("자바");
        boolean isAddedAgain = uniqueTags.add("Java"); // 이미 존재하므로 추가되지 않고 false 반환
        
        System.out.println("동일 데이터 중복 추가 시도 결과: " + isAddedAgain);
        System.out.println("Java 태그 포함 여부: " + uniqueTags.contains("Java")); // true
        System.out.println("전체 Set 크기: " + uniqueTags.size()); // 2 ("Java", "자바")
        System.out.println("Set 요소 전체 출력 (순서 보장 없음): " + uniqueTags);
    }
}
