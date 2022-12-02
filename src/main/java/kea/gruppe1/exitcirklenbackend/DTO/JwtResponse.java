package kea.gruppe1.exitcirklenbackend.DTO;

import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String phone;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, String phone, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }

    public String getPhone(){
        return phone;
    }

}