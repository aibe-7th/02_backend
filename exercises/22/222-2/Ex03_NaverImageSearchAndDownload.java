import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 실습 222-2. 3단계: OpenAPI 연동 및 바이너리 HTTP 파일 다운로드
 * 
 * [학습 포인트]
 * 1. HttpClient (Java 11+)
 *    - 외부 라이브러리(OkHttp, Apache Client) 없이 JDK 표준 라이브러리만으로 HTTP 통신을 수행할 수 있습니다.
 * 2. Header 기반 인증
 *    - 네이버 OpenAPI 등 보안이 걸린 API는 요청 헤더(Header)에 API Key 정보를 세팅해야 합니다.
 * 3. BodyHandlers.ofFile
 *    - HTTP 응답 바이트를 자바 메모리로 전부 로딩하지 않고, 지정한 로컬 파일 경로(Path)로 
 *      스트림 형태로 디렉트 출력(저장)하여 대용량 이미지 다운로드 시 메모리 낭비를 원천 차단합니다.
 * 4. 문자열 조작 기반 파싱 (정규식 배제)
 *    - 학습 목적으로 JSON 파싱 라이브러리(Jackson, Gson)나 복잡한 정규식(Pattern/Matcher)을 사용하지 않고,
 *      String의 indexOf()와 substring() 같은 기본적인 문자열 메서드만을 활용해 이미지 URL을 추출합니다.
 */
public class Ex03_NaverImageSearchAndDownload {

    // 시스템 환경변수(System.getenv)를 통해 네이버 OpenAPI Client ID와 Secret 키를 안전하게 주입받습니다.
    // 터미널 혹은 런타임 환경설정에서 NAVER_CLIENT_ID와 NAVER_CLIENT_SECRET 환경변수를 지정해주어야 합니다.
    private static final String CLIENT_ID = System.getenv("NAVER_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("NAVER_CLIENT_SECRET");

    // 기본 이미지 검색 키워드
    private static final String DEFAULT_SEARCH_KEYWORD = "귀여운 강아지";
    // 저장할 파일 경로
    private static final String DOWNLOAD_PATH = "downloaded_dog.jpg";

    public static void main(String[] args) {
        System.out.println("=== 네이버 이미지 OpenAPI 검색 & 바이너리 다운로드 실습 ===");

        // 1. API 키 설정 여부 검사 및 분기 (환경변수가 주입되었는지 확인)
        if (CLIENT_ID == null || CLIENT_SECRET == null || CLIENT_ID.isEmpty() || CLIENT_SECRET.isEmpty()) {
            System.out.println("[안내] 환경변수 NAVER_CLIENT_ID 또는 NAVER_CLIENT_SECRET 설정이 완료되지 않았습니다.");
            System.out.println("-> 가이드: 로컬 터미널 혹은 IDE 실행 환경(Run Configuration)에서 아래 환경변수를 등록해주세요.");
            System.out.println("   export NAVER_CLIENT_ID=\"발급받은ID\"");
            System.out.println("   export NAVER_CLIENT_SECRET=\"발급받은Secret\"");
            System.out.println("-> 대체 모드: 테스트용 Mock 이미지 주소로 파일 다운로드 실습을 대체 진행합니다.\n");
            
            // Mock 이미지 다운로드 직접 실행
            String mockImageUrl = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?q=80&w=640&auto=format&fit=crop";
            downloadBinaryFile(mockImageUrl, DOWNLOAD_PATH);
            return;
        }

        // 2. Scanner를 이용해 검색 키워드 콘솔 입력 받기 (try-with-resources 적용)
        String keyword = DEFAULT_SEARCH_KEYWORD;
        System.out.println("검색할 이미지 키워드를 입력하세요 (기본값: " + DEFAULT_SEARCH_KEYWORD + "): ");
        
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("키워드 입력: ");
            // 자동 빌드 및 검증 시 입력 만료 대기 방지
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    keyword = input;
                }
            } else {
                System.out.println("\n[안내] 입력 스트림이 만료되어 기본 키워드로 검색을 수행합니다.");
            }
        }
        
        System.out.println("-> '" + keyword + "' 키워드로 이미지 검색을 진행합니다.");

        try {
            // 3. 네이버 이미지 검색 API 호출
            System.out.println("\n1. 네이버 이미지 검색 OpenAPI 호출 시도...");
            String jsonResponse = searchImageFromNaver(keyword);
            System.out.println("-> 검색 API 응답 획득 성공!");

            // 4. 응답 JSON에서 이미지 URL("link") 추출
            System.out.println("\n2. JSON 결과에서 첫 번째 이미지의 원본 URL 파싱...");
            String imageUrl = extractFirstImageUrl(jsonResponse);
            if (imageUrl == null) {
                System.out.println("-> 에러: JSON 응답에서 이미지 원본 URL(link)을 파싱하지 못했습니다.");
                System.out.println("-> 응답 데이터 원본: " + jsonResponse);
                return;
            }
            System.out.println("-> 파싱된 이미지 URL: " + imageUrl);

            // 5. 추출된 URL에서 바이너리 이미지 다운로드 진행
            System.out.println("\n3. 파싱된 URL로부터 이미지 다운로드 시작...");
            downloadBinaryFile(imageUrl, DOWNLOAD_PATH);

        } catch (Exception e) {
            System.out.println("\n[실패] 전체 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * HttpClient를 활용해 네이버 이미지 검색 OpenAPI를 호출하여 JSON 응답을 얻어옵니다.
     */
    private static String searchImageFromNaver(String keyword) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // 검색어 URL 인코딩 처리
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        // 디스플레이 건수는 1개로 제한하여 첫 번째 이미지만 가져옵니다.
        String apiUri = "https://openapi.naver.com/v1/search/image?query=" + encodedKeyword + "&display=1";

        // 네이버 인증 헤더 세팅
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUri))
                .header("X-Naver-Client-Id", CLIENT_ID)
                .header("X-Naver-Client-Secret", CLIENT_SECRET)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // HTTP 상태코드가 200이 아닌 경우 예외 처리
        if (response.statusCode() != 200) {
            throw new RuntimeException("네이버 API 서버 호출 오류 (HTTP 상태코드: " + response.statusCode() 
                    + ", 응답 내용: " + response.body() + ")");
        }

        return response.body();
    }

    /**
     * 문자열 함수(indexOf, substring)를 이용하여 JSON 문자열에서 첫 번째 "link" 키의 이미지 URL을 추출합니다.
     */
    private static String extractFirstImageUrl(String json) {
        // "link" 라는 키의 위치를 찾습니다.
        int linkIndex = json.indexOf("\"link\"");
        if (linkIndex == -1) {
            return null;
        }

        // "link" 값의 시작부분인 큰따옴표(") 위치를 찾습니다.
        // "\"link\"" 단어 길이(6글자) 이후에 나오는 첫 번째 큰따옴표의 위치
        int startQuote = json.indexOf("\"", linkIndex + 6);
        if (startQuote == -1) {
            return null;
        }

        // 시작 큰따옴표 바로 다음에 위치하는 값의 닫는 큰따옴표(") 위치를 찾습니다.
        int endQuote = json.indexOf("\"", startQuote + 1);
        if (endQuote == -1) {
            return null;
        }

        // 큰따옴표 내부의 순수 URL 문자열만 잘라냅니다.
        String url = json.substring(startQuote + 1, endQuote);

        // JSON 내 이스케이프된 슬래시(\/)를 정상적인 슬래시(/)로 치환하여 리턴합니다.
        return url.replace("\\/", "/");
    }

    /**
     * HttpClient와 BodyHandlers.ofFile을 사용해 바이너리 이미지를 로컬 파일로 디스크에 다이렉트 저장합니다.
     */
    private static void downloadBinaryFile(String fileUrl, String targetFileName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .GET()
                .build();

        Path targetPath = Paths.get(targetFileName);
        System.out.println("-> 다운로드 시도 주소: " + fileUrl);
        System.out.println("-> 로컬 임시 저장 파일: " + targetPath.toAbsolutePath());

        try {
            // BodyHandlers.ofFile을 사용하여 다운로드된 바이너리를 로컬 파일 경로에 직접 기록합니다.
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(targetPath));

            if (response.statusCode() == 200) {
                System.out.println("\n[성공] 바이너리 파일 다운로드 완료! (다운로드 경로: " + response.body().toAbsolutePath() + ")");
            } else {
                System.out.println("\n[실패] HTTP 서버 에러 발생 (HTTP 상태코드: " + response.statusCode() + ")");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("\n[실패] 다운로드 중 파일 I/O 혹은 네크워크 인터럽트 에러: " + e.getMessage());
        }
    }
}
