package computerNetwork;

import java.io.*;
import java.util.*;

public class Config {
 private String serverIP;
 private int serverPort;

 public Config() {
     loadConfig();
 }

 private void loadConfig() {
     try {
         File file = new File("server_info.dat");
         if (!file.exists()) {
             this.serverIP = "localhost";
             this.serverPort = 1234;
             return;
         }
         Scanner sc = new Scanner(file);
         if (sc.hasNext()) {
             this.serverIP = sc.next();
             this.serverPort = sc.nextInt();
         } else {
             this.serverIP = "localhost";
             this.serverPort = 1234;
         }
         sc.close();
     } catch (Exception e) {
         this.serverIP = "localhost";
         this.serverPort = 1234;
     }
 }

 public String getServerIP() {
     return serverIP;
 }

 public int getServerPort() {
     return serverPort;
 }
}
