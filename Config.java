package computerNetwork;

import java.io.*;
import java.util.*;

/**
 * 서버 접속 정보를 관리하는 클래스.
 * server_info.dat 파일에서 IP와 Port를 읽어오며, 파일이 없을 경우 기본값을 사용합니다.
 */
public class Config {
    private String serverIP;
    private int serverPort;
    private static final String CONFIG_FILE = "server_info.dat";
    private static final String DEFAULT_IP = "127.0.0.1"; // localhost
    private static final int DEFAULT_PORT = 1234;

    public Config() {
        loadConfig();
    }

    /**
     * 설정 파일을 읽어 서버 IP와 Port를 로드합니다.
     * 파일이 없거나 오류 발생 시 기본값으로 설정합니다.
     */
    private void loadConfig() {
        try {
            File file = new File(CONFIG_FILE);
            
            // 1. 파일이 존재하지 않을 경우 기본값 사용
            if (!file.exists()) {
                System.out.println("[Config] 설정 파일(" + CONFIG_FILE + ")이 없어 기본값(" + DEFAULT_IP + ":" + DEFAULT_PORT + ")을 사용합니다.");
                this.serverIP = DEFAULT_IP;
                this.serverPort = DEFAULT_PORT;
                return;
            }
            
            // 2. 파일이 존재할 경우 스캔하여 정보 읽기
            try (Scanner sc = new Scanner(file)) {
                if (sc.hasNext()) {
                    this.serverIP = sc.next();
                    // Port 번호는 정수형으로 읽습니다.
                    this.serverPort = sc.hasNextInt() ? sc.nextInt() : DEFAULT_PORT;
                } else {
                    System.out.println("[Config] 파일 내용이 비어 기본값(" + DEFAULT_IP + ":" + DEFAULT_PORT + ")을 사용합니다.");
                    this.serverIP = DEFAULT_IP;
                    this.serverPort = DEFAULT_PORT;
                }
            }

        } catch (Exception e) {
            System.err.println("[Config] 파일 처리 중 오류 발생. 기본값 사용: " + e.getMessage());
            this.serverIP = DEFAULT_IP;
            this.serverPort = DEFAULT_PORT;
        }
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }
}
