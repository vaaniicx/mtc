package at.if22b208.mtc.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDto {
    private String name;

    @JsonProperty("bio")
    private String biography;

    private String image;
}
