package com.HCTR.data_portal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private String Id;
    private String Password;
    private  String Email;
    private String Name;
    private String Organization;
    private int Role;

    public UserDTO(
            String id, String password, String email,
            String name, String organization, int role){
        this.Id = id;
        this.Password = password;
        this.Email = email;
        this.Name = name;
        this.Organization = organization;
        this.Role = role;
    }
}
