package computerNetwork;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 * 서버에 연결된 각 클라이언트의 요청을 처리하는 Runnable 클래스.
 * (과제 요구사항: 서버 Thread 처리)
 */
public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.println("[Server] 클라이언트 연결됨: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("[Server] 수신 (" + socket.getPort() + "): " + line);
                
                if (line.trim().equalsIgnoreCase(Protocol.CMD_EXIT)) {
                    System.out.println("[Server] 클라이언트 (" + socket.getPort() + ")가 종료를 요청했습니다.");
                    break;
                }
                
                String response = processRequest(line);
                out.write(response + "\n");
                out.flush();
                System.out.println("[Server] 응답 (" + socket.getPort() + "): " + response);
            }
        } catch (IOException e) {
            System.err.println("[Server] 클라이언트 통신 오류 (" + socket.getPort() + "): " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("[Server] 연결 종료됨: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                }
            } catch (IOException e) {
            }
        }
    }

    private String processRequest(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        double result;

        try {
            if (st.countTokens() != 3) {
                 String errorMsg = (st.countTokens() < 3) ? "Too few arguments (Expected: 3)" : "Too many arguments (Expected: 3)";
                 return buildError(Protocol.CODE_INVALID_ARG, errorMsg); 
            }
            
            String command = st.nextToken().toUpperCase();
            double a = Double.parseDouble(st.nextToken());
            double b = Double.parseDouble(st.nextToken());

            switch (command) {
                case Protocol.CMD_ADD: result = a + b; break;
                case Protocol.CMD_SUB: result = a - b; break;
                case Protocol.CMD_MUL: result = a * b; break;
                case Protocol.CMD_DIV:
                    if (b == 0) return buildError(Protocol.CODE_DIV_ZERO, "Division by zero");
                    result = a / b;
                    break;
                default:
                    return buildError(Protocol.CODE_BAD_REQUEST, "Unsupported command: " + command);
            }
            
            return buildResult(result);

        } catch (NumberFormatException e) {
            return buildError(Protocol.CODE_INVALID_ARG, "Non-numeric operand in expression");
        } catch (Exception e) {
            System.err.println("Internal Server Error: " + e.getMessage());
            return buildError(Protocol.CODE_INTERNAL_ERROR, "Server internal processing error");
        }
    }

    private String buildResult(double value) {
        return Protocol.RES_ANSWER + " " + Protocol.CODE_OK + " " + String.format("%.2f", value);
    }

    private String buildError(int code, String msg) {
        return Protocol.RES_ERROR + " " + code + " " + msg;
    }
}
