package at.if22b208.mtc.server.util;

import at.if22b208.mtc.server.http.ContentType;
import at.if22b208.mtc.server.http.HttpStatus;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class RequestHandler implements Runnable {
    private final Socket clientSocket;
    private final Router router;
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;

    public RequestHandler(Socket clientSocket, Router router) throws IOException {
        this.clientSocket = clientSocket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.printWriter = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.router = router;
    }

    @Override
    public void run() {
        try {
            Response response;
            Request request = new RequestBuilder().buildRequest(this.bufferedReader);

            if (request.getPathname() == null) {
                response = new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "[]"
                );
            } else {
                response = this.router.resolve(request.getServiceRoute()).handleRequest(request);
            }
            printWriter.write(response.get());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}