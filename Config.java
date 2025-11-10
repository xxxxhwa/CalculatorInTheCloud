package computerNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * 서버 설정 파일(server_info.dat)을 읽어 IP와 Port를 로드하는 클래스.
 * 파일이 없으면 기본값(127.0.0.1:1234)을 사용합니다.
 */
public class Config {
    private String serverIP = "127.0.0.1";
    private int serverPort = 1234;
    private static final String CONFIG_FILE_NAME = "server_info.dat";

    public Config() {
        loadConfig();
    }

    private void loadConfig() {
        File file = new File(CONFIG_FILE_NAME);
        if (!file.exists()) {
            System.out.println("Info: Configuration file not found. Using default: " + serverIP + ":" + serverPort);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() >= 2) {
                    serverIP = st.nextToken();
                    serverPort = Integer.parseInt(st.nextToken());
                    System.out.println("Info: Configuration loaded from file: " + serverIP + ":" + serverPort);
                } else {
                    System.err.println("Error: Invalid format in " + CONFIG_FILE_NAME + ". Using default.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + CONFIG_FILE_NAME + ": " + e.getMessage() + ". Using default.");
        } catch (NumberFormatException e) {
            System.err.println("Error parsing port number in " + CONFIG_FILE_NAME + ". Using default.");
        }
    }

    public String getServerIP() { return serverIP; }
    public int getServerPort() { return serverPort; }
}
