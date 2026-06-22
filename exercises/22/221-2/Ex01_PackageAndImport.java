import static java.lang.Math.max;

import java.sql.Date;

/**
 * 1단계: 패키지, import, static import
 *
 * 학습 포인트
 * - package는 클래스의 소속을 나타내며 같은 이름의 클래스를 구분하는 네임스페이스입니다.
 * - 다른 패키지의 클래스는 전체 이름을 사용하거나 import로 경로를 선언해야 합니다.
 * - 서로 다른 패키지의 같은 이름을 동시에 사용할 때는 하나를 전체 이름으로 작성합니다.
 * - static import를 사용하면 클래스명 없이 정적 멤버를 호출할 수 있습니다.
 * - 와일드카드 import는 하위 패키지까지 가져오지 않으며 이름 충돌 가능성을 숨길 수 있습니다.
 */
public class Ex01_PackageAndImport {
    public static void main(String[] args) {
        System.out.println("=== 패키지와 임포트 ===");

        // java.sql.Date는 위에서 명시적으로 import했으므로 짧은 이름으로 사용합니다.
        Date releaseDate = Date.valueOf("2026-06-22");

        // java.sql.Date와 이름이 같은 java.util.Date는 전체 패키지 경로로 구분합니다.
        java.util.Date currentTime = new java.util.Date();

        System.out.println("출시일: " + releaseDate);
        System.out.println("현재 시각 타입: " + currentTime.getClass().getName());

        // import static java.lang.Math.max 덕분에 Math.max 대신 max로 호출합니다.
        System.out.println("더 큰 값: " + max(10, 20));

        /*
         * 확인 1: java.sql.Date import를 제거하고 타입을 java.sql.Date로 직접 작성하세요.
         * 확인 2: java.util.*와 java.sql.*를 함께 import한 뒤 Date를 사용하면
         *         컴파일러가 어느 Date인지 결정하지 못하는 이유를 설명하세요.
         * 확인 3: max 호출을 Math.max 호출로 되돌릴 때 제거해야 할 import를 찾으세요.
         */
    }
}
