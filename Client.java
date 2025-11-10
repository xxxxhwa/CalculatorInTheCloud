package computerNetwork;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 계산기 클라이언트 애플리케이션.
 */
public class Client {
    public static void main(String[] args) {
        Config config = new Config();
        String serverIP = config.getServerIP();
        int serverPort = config.getServerPort();
        
        if (serverIP == null || serverPort <= 0) {
            System.err.println("Error: Server configuration missing or invalid.");
            return;
        }

        try (
            Socket socket = new Socket(serverIP, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server: " + serverIP + ":" + serverPort);
            
            String userInput;
            while (true) {
                System.out.print(">> Enter command (ADD/SUB/MUL/DIV a b) or EXIT to quit: ");
                if (scanner.hasNextLine()) userInput = scanner.nextLine();
                else break;

                if (userInput.trim().equalsIgnoreCase(Protocol.CMD_EXIT)) {
                    out.println(Protocol.CMD_EXIT); 
                    break;
                }

                out.println(userInput);
                String response = in.readLine();
                if (response != null) processResponse(response);
                else {
                    System.out.println("Server disconnected.");
                    break;
                }
            }

        } catch (ConnectException e) {
            System.err.println("Error: Connection refused. Ensure server is running.");
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }
    
    private static void processResponse(String response) {
        StringTokenizer st = new StringTokenizer(response);
        if (!st.hasMoreTokens()) {
            System.out.println("Invalid server response format.");
            return;
        }
        
        String responseType = st.nextToken();
        int code = -1;
        String message = "";
        
        try {
            if (st.hasMoreTokens()) code = Integer.parseInt(st.nextToken());

            if (responseType.equals(Protocol.RES_ANSWER) && code == Protocol.CODE_OK) {
                if (st.hasMoreTokens()) message = st.nextToken();
                System.out.printf("✅ 결과 (Code %d): %s\n", code, message);
            } else if (responseType.equals(Protocol.RES_ERROR)) {
                while (st.hasMoreTokens()) message += st.nextToken() + " ";
                System.out.printf("❌ 오류 발생 (Code %d): %s\n", code, message.trim());
            } else {
                System.out.println("Unknown response type: " + response);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing response code or value: " + response);
        }
    }
}
