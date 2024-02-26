package at.if22b208.mtc.server.session;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSession {
    private final String username;

    private final long startTime;
}
