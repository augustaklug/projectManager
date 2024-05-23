package com.klug.projectmanager.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private String email;
    private String password;
    private Long[] assignedTaskIds;
    private Long[] projectIds;
}