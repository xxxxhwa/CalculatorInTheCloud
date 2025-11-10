package computerNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 메인 서버 클래스.
 * 스레드 풀을 사용하여 다중 클라이언트 요청을 동시에 처리합니다.
 */
public class Server {
    // Config에 정의된 기본 포트를 사용합니다. (파일이 없을 경우)
    private static final int PORT = 1234; 
    private static final int THREAD_POOL_SIZE = 5; // 동시 처리 가능한 최대 클라이언트 수

    public static void main(String[] args) {
        // 스레드 풀 생성 (5개 고정 크기)
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("----------------------------------------");
        System.out.println("Calculator Server started.");
        System.out.println("Listening on port " + PORT + " with a thread pool size of " + THREAD_POOL_SIZE);
        System.out.println("----------------------------------------");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // 클라이언트 연결 대기
                Socket clientSocket = serverSocket.accept();
                // 연결이 수락되면, 클라이언트 핸들러(Runnable)를 스레드 풀에 할당하여 실행
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("메인 서버 소켓 오류: " + e.getMessage());
            pool.shutdown(); // 서버 오류 시 풀 종료
        }
    }
}
