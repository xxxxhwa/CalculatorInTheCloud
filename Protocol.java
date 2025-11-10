package computerNetwork;

/**
 * 애플리케이션 계층 프로토콜 정의 클래스.
 * 클라이언트와 서버 간의 통신에 사용되는 명령어와 응답 코드를 정의합니다.
 */
public class Protocol {
    // ------------------- 명령어 (Request Commands) -------------------
    public static final String CMD_ADD = "ADD";
    public static final String CMD_SUB = "SUB";
    public static final String CMD_MUL = "MUL";
    public static final String CMD_DIV = "DIV";
    public static final String CMD_EXIT = "EXIT"; // 클라이언트 종료 명령어

    // ------------------- 응답 유형 (Response Types) -------------------
    public static final String RES_ANSWER = "ANSWER"; // 성공적인 계산 결과
    public static final String RES_ERROR = "ERROR";   // 오류 발생

    // ------------------- 응답 코드 (Response Codes - HTTP Status 유사) -------------------
    // 성공 코드
    public static final int CODE_OK = 200; 

    // 클라이언트 오류 (4xx)
    public static final int CODE_BAD_REQUEST = 400;     // 알 수 없는 명령어
    public static final int CODE_INVALID_ARG = 401;     // 인자 개수 오류, 비숫자 인자
    public static final int CODE_DIV_ZERO = 402;        // 0으로 나누기 오류
    
    // 서버 오류 (5xx)
    public static final int CODE_INTERNAL_ERROR = 500;  // 서버 내부 처리 오류
}
