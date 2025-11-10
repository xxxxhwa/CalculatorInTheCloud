package computerNetwork;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
 public static void main(String[] args) {
     Config config = new Config();
     String serverIP = config.getServerIP();
     int serverPort = config.getServerPort();

     try (Socket socket = new Socket(serverIP, serverPort);
          BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

         System.out.println("Connected to server: " + serverIP + ":" + serverPort);
         System.out.println("Enter command (ADD/SUB/MUL/DIV a b) or EXIT to quit.");

         while (true) {
             System.out.print(">> ");
             String line = userIn.readLine();
             if (line == null || line.equalsIgnoreCase("EXIT")) break;

             out.write(line + "\n");
             out.flush();

             String response;
             System.out.println("---- Server Response ----");
             while ((response = in.readLine()) != null) {
                 if (response.trim().isEmpty()) break;
                 System.out.println(response);
                 if (!in.ready()) break;
             }
             System.out.println("--------------------------");
         }

     } catch (IOException e) {
         System.out.println("Connection failed: " + e.getMessage());
     }
 }
}
