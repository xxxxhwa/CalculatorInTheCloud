package computerNetwork;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.StringTokenizer;

public class Server {
 private static final int PORT = 1234;

 public static void main(String[] args) {
     ExecutorService pool = Executors.newFixedThreadPool(5);
     System.out.println("Server started on port " + PORT);

     try (ServerSocket serverSocket = new ServerSocket(PORT)) {
         while (true) {
             Socket clientSocket = serverSocket.accept();
             pool.execute(new ClientHandler(clientSocket));
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
}

class ClientHandler implements Runnable {
 private Socket socket;

 public ClientHandler(Socket socket) {
     this.socket = socket;
 }

 @Override
 public void run() {
     try (
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
     ) {
         String line;
         while ((line = in.readLine()) != null) {
             String response = processRequest(line);
             out.write(response + "\n");
             out.flush();
         }
     } catch (IOException e) {
         System.out.println("Client disconnected");
     }
 }

 private String processRequest(String msg) {
     try {
         StringTokenizer st = new StringTokenizer(msg);
         String command = st.nextToken().toUpperCase();
         double a = Double.parseDouble(st.nextToken());
         double b = Double.parseDouble(st.nextToken());
         double result;

         switch (command) {
             case "ADD": result = a + b; break;
             case "SUB": result = a - b; break;
             case "MUL": result = a * b; break;
             case "DIV":
                 if (b == 0) return buildError("DIV_ZERO", "Cannot divide by zero");
                 result = a / b;
                 break;
             default:
                 return buildError("UNKNOWN_CMD", "Unsupported command");
         }
         return buildResult(result);

     } catch (NumberFormatException e) {
         return buildError("INVALID_ARG", "Non-numeric operand");
     } catch (Exception e) {
         return buildError("FORMAT_ERR", "Incorrect format");
     }
 }

 private String buildResult(double value) {
     return Protocol.RES_TYPE + ": " + Protocol.TYPE_RESULT + "\n" +
            Protocol.VALUE + ": " + value;
 }

 private String buildError(String code, String msg) {
     return Protocol.RES_TYPE + ": " + Protocol.TYPE_ERROR + "\n" +
            Protocol.ERROR_CODE + ": " + code + "\n" +
            Protocol.MESSAGE + ": " + msg;
 }
}
