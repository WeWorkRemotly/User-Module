package com.vrpigroup.usermodule;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class loginDto {

    private String userName;
    private String password;
    private String email;
    private String otp;
    private String role;
}
