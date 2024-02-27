package com.vrpigroup.usermodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserModuleApplication.class, args);
    }

}
/*
# Security
security.jwt.uri=/auth/**
security.jwt.header=Authorization
security.jwt.prefix=Bearer
security.jwt.expiration=604800
security.jwt.secret=JwtSecretKey



# Logging
logging.level.org.springframework=INFO
logging.level.com.aman=DEBUG

DB_CLOSE_ON_EXIT=FALSE



# Jwt Token
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
*/