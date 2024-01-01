package at.if22b208.mtc.server;

import at.if22b208.mtc.exception.HashingException;
import at.if22b208.mtc.server.http.Request;
import at.if22b208.mtc.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface Controller {
    Response handleRequest(Request request) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException, HashingException;
}
