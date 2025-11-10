package computerNetwork;

/**
 * 애플리케이션 계층 프로토콜 정의 클래스.
 */
public class Protocol {
    public static final String CMD_ADD = "ADD";
    public static final String CMD_SUB = "SUB";
    public static final String CMD_MUL = "MUL";
    public static final String CMD_DIV = "DIV";
    public static final String CMD_EXIT = "EXIT"; // 클라이언트 종료 명령어

    public static final String RES_ANSWER = "ANSWER"; // 성공적인 계산 결과
    public static final String RES_ERROR = "ERROR";   // 오류 발생

    // 성공 코드
    public static final int CODE_OK = 200; 

    // 클라이언트 오류 (4xx)
    public static final int CODE_BAD_REQUEST = 400;     // 알 수 없는 명령어 (UNKNOWN_CMD)
    public static final int CODE_INVALID_ARG = 401;     // 인자 개수 오류, 비숫자 인자 (TOO_MANY/FEW_ARG, INVALID_ARG)
    public static final int CODE_DIV_ZERO = 402;        // 0으로 나누기 오류 (DIV_ZERO)
    
    // 서버 오류 (5xx)
    public static final int CODE_INTERNAL_ERROR = 500;  // 서버 내부 처리 오류
}
