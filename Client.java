package computerNetwork;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 클라이언트 프로그램.
 * Config 파일에서 서버 정보를 읽어와 접속하고, 사용자 수식을 서버에 요청합니다.
 */
public class Client {
    public static void main(String[] args) {
        // 1. Config 클래스를 사용하여 서버 IP와 Port 로드 (과제 요구사항 반영)
        Config config = new Config();
        String serverIP = config.getServerIP();
        int serverPort = config.getServerPort();

        try (
            // 소켓 연결
            Socket socket = new Socket(serverIP, serverPort);
            // 표준 입력 스트림
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            // 서버로부터의 입력 스트림
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 서버로의 출력 스트림 (단일 라인 전송을 위해 BufferedWriter 사용)
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            System.out.println("----------------------------------------");
            System.out.println("Connected to server: " + serverIP + ":" + serverPort);
            System.out.println("Enter command (ADD/SUB/MUL/DIV a b) or EXIT to quit.");
            System.out.println("----------------------------------------");

            String line;
            while (true) {
                System.out.print(">> ");
                line = userIn.readLine();
                
                // 2. EXIT 명령 처리
                if (line == null || line.trim().equalsIgnoreCase(Protocol.CMD_EXIT)) {
                    out.write(Protocol.CMD_EXIT + "\n");
                    out.flush();
                    break;
                }
                
                // 3. 요청 전송 (끝에 개행 문자 \n 포함)
                out.write(line + "\n");
                out.flush();

                // 4. 서버로부터의 단일 라인 응답 수신
                String response = in.readLine();
                
                if (response != null) {
                    System.out.println("\n---- Server Response ----");
                    displayResponse(response);
                    System.out.println("--------------------------");
                } else {
                    System.out.println("서버와의 연결이 종료되었습니다.");
                    break;
                }
            }

        } catch (ConnectException e) {
            System.err.println("오류: 서버에 연결할 수 없습니다. IP/Port를 확인하거나 서버를 실행하세요.");
        } catch (IOException e) {
            System.err.println("클라이언트 통신 오류: " + e.getMessage());
        }
    }

    /**
     * 서버 응답을 파싱하여 사용자에게 결과를 의미있게 출력합니다.
     */
    private static void displayResponse(String response) {
        StringTokenizer st = new StringTokenizer(response);
        
        if (!st.hasMoreTokens()) {
            System.out.println("수신된 응답 형식이 올바르지 않습니다: (응답 문자열 없음)");
            return;
        }

        String resType = st.nextToken();
        
        if (Protocol.RES_ANSWER.equals(resType) && st.countTokens() == 2) {
            // ANSWER 200 <value> 형식
            String code = st.nextToken();
            String value = st.nextToken();
            System.out.println("결과 (Code " + code + "): " + value);
        } else if (Protocol.RES_ERROR.equals(resType) && st.countTokens() >= 2) {
            // ERROR <code(4xx/5xx)> <message> 형식
            String code = st.nextToken();
            String message = st.nextToken("").trim(); // 나머지 문자열 전체를 오류 메시지로 처리
            System.err.println("오류 발생 (Code " + code + "): " + message);
        } else {
            // 정의되지 않은 형식
            System.out.println("알 수 없는 응답 형식: " + response);
        }
    }
}
