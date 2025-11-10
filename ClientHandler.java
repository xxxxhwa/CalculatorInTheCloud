package computerNetwork;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 * 서버에 연결된 각 클라이언트의 요청을 처리하는 Runnable 클래스.
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private static final String CMD_EXIT = "EXIT";

    public ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.println("[Server] 클라이언트 연결됨: " + socket.getInetAddress());
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 단일 라인 응답을 위해 BufferedWriter 사용 (out.write + out.flush)
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("[Server] 수신: " + line);
                
                // EXIT 명령 처리
                if (line.trim().equalsIgnoreCase(CMD_EXIT)) {
                    System.out.println("[Server] 클라이언트가 종료를 요청했습니다.");
                    break;
                }
                
                // 요청 처리 및 응답 전송
                String response = processRequest(line);
                out.write(response + "\n"); // 응답 전문과 끝을 알리는 개행문자 전송
                out.flush();
                System.out.println("[Server] 응답: " + response);
            }
        } catch (IOException e) {
            // 클라이언트 연결 강제 종료 등의 오류 처리
            System.err.println("[Server] 클라이언트 통신 오류: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("[Server] 연결 종료됨: " + socket.getInetAddress());
            } catch (IOException e) {
                // close 오류 무시
            }
        }
    }

    /**
     * 클라이언트 요청 메시지를 분석하고 계산하여 프로토콜 응답 문자열을 생성합니다.
     */
    private String processRequest(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        double result;

        try {
            // 1. 인자 개수 체크 (CMD 포함 총 3개여야 함)
            if (st.countTokens() != 3) {
                 String errorMsg = (st.countTokens() < 3) ? "Too few arguments (Expected: 3)" : "Too many arguments (Expected: 3)";
                 // CODE_INVALID_ARG(401) 매핑
                 return buildError(Protocol.CODE_INVALID_ARG, errorMsg); 
            }
            
            String command = st.nextToken().toUpperCase();
            double a = Double.parseDouble(st.nextToken());
            double b = Double.parseDouble(st.nextToken());

            // 2. 명령어에 따른 계산 수행 및 예외 처리
            switch (command) {
                case Protocol.CMD_ADD: result = a + b; break;
                case Protocol.CMD_SUB: result = a - b; break;
                case Protocol.CMD_MUL: result = a * b; break;
                case Protocol.CMD_DIV:
                    if (b == 0) {
                        // 0으로 나누기 예외 처리 (CODE_DIV_ZERO: 402)
                        return buildError(Protocol.CODE_DIV_ZERO, "Division by zero");
                    }
                    result = a / b;
                    break;
                default:
                    // 알 수 없는 명령어 처리 (CODE_BAD_REQUEST: 400)
                    return buildError(Protocol.CODE_BAD_REQUEST, "Unsupported command: " + command);
            }
            
            // 3. 계산 성공 응답 반환
            return buildResult(result);

        } catch (NumberFormatException e) {
            // 피연산자가 숫자가 아닐 경우 (CODE_INVALID_ARG: 401)
            return buildError(Protocol.CODE_INVALID_ARG, "Non-numeric operand in expression");
        } catch (Exception e) {
            // 기타 예상치 못한 오류 발생 시 (CODE_INTERNAL_ERROR: 500)
            return buildError(Protocol.CODE_INTERNAL_ERROR, "Server internal processing error");
        }
    }

    private String buildResult(double value) {
        return Protocol.RES_ANSWER + " " + Protocol.CODE_OK + " " + String.format("%.2f", value); // 소수점 두 자리로 포맷
    }

    private String buildError(int code, String msg) {
        return Protocol.RES_ERROR + " " + code + " " + msg;
    }
}
