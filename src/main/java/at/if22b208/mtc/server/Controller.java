package at.if22b208.mtc.server;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;

public interface Controller {
    Response handleRequest(Request request)
            throws JsonProcessingException;
}
