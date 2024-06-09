package at.if22b208.mtc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.if22b208.mtc.server.util.RequestHandler;
import at.if22b208.mtc.server.util.Router;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class Server {
    private int port;

    private Router router;

    public void start() {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        log.info("Start http-server...");
        log.info("http-server running at: http://localhost:{}", this.port);

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                final Socket clientConnection = serverSocket.accept();
                final RequestHandler socketHandler = new RequestHandler(clientConnection, this.router);
                executorService.submit(socketHandler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
