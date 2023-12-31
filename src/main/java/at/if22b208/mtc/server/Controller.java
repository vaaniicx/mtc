package at.if22b208.mtc.server;

import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;

public interface Service {
    Response handleRequest(Request request);
}
