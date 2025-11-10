package computerNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 계산기 서버 메인 클래스.
 */
public class Server {
    private static final int DEFAULT_PORT = 1234;
    private static final int THREAD_POOL_SIZE = 5;

    public static void main(String[] args) {
        Config config = new Config();
        int serverPort = config.getServerPort();
        
        if (serverPort <= 0) {
            serverPort = DEFAULT_PORT;
        }

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("Calculator Server started. Listening on port " + serverPort);

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server Main Error: Could not listen on port " + serverPort + " or internal error.");
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}
