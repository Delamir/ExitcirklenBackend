package kea.gruppe1.exitcirklenbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class SignupRequest {

    private String email;
    private Set<String> role;
    private String password;
}
