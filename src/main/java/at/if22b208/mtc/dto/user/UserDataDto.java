package at.if22b208.mtc.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDataDto {
    private String name;
    private String biography;
    private String image;
}
