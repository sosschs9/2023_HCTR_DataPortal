package com.HCTR.data_portal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("Id")
    private String Id;
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("Email")
    private String Email;
    @JsonProperty("UserName")
    private String UserName;
    @JsonProperty("Organization")
    private String Organization;
    @JsonProperty("Role")
    private int Role;

    public UserDTO(
            String id, String password, String email,
            String userName, String organization, int role){
        this.Id = id;
        this.Password = password;
        this.Email = email;
        this.UserName = userName;
        this.Organization = organization;
        this.Role = role;
    }
}
