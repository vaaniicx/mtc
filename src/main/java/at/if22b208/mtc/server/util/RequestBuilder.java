package at.if22b208.mtc.server.util;

import at.if22b208.mtc.server.http.Method;
import at.if22b208.mtc.server.http.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

public class RequestBuilder {
    public Request buildRequest(BufferedReader bufferedReader) {
        Request request = new Request();

        try {
            String line = bufferedReader.readLine();
            if (line == null) {
                return request;
            }

            String[] splitFirstLine = line.split(" ");
            request.setMethod(getMethod(splitFirstLine[0]));
            setPathname(request, splitFirstLine[1]);

            line = bufferedReader.readLine();
            while (!line.isEmpty()) {
                request.getHeader().ingest(line);
                line = bufferedReader.readLine();
            }

            if (request.getHeader().getContentLength() > 0) {
                char[] charBuffer = new char[request.getHeader().getContentLength()];
                bufferedReader.read(charBuffer, 0, request.getHeader().getContentLength());
                request.setBody(new String(charBuffer));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return request;
    }

    private Method getMethod(String methodString) {
        return Method.valueOf(methodString.toUpperCase(Locale.ROOT));
    }

    private void setPathname(Request request, String path) {
        boolean hasParams = path.contains("?");

        if (hasParams) {
            String[] pathParts = path.split("\\?");
            request.setPathname(pathParts[0]);
            request.setParams(pathParts[1]);
        } else {
            request.setPathname(path);
            request.setParams(null);
        }
    }
}