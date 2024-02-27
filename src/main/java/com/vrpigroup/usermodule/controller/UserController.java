package com.vrpigroup.usermodule.controller;

import com.vrpigroup.usermodule.entity.ContactUs;
import com.vrpigroup.usermodule.entity.UserEntity;
import com.vrpigroup.usermodule.loginDto;
import com.vrpigroup.usermodule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vrpi-user")
public class UserController {

    private final UserService userModuleService;

    @Autowired
    public UserController(UserService userModuleService) {
        this.userModuleService = userModuleService;
    }

    @GetMapping("/all")
    public List<UserEntity> getAllUser() {
        return userModuleService.getAllUser();
    }

    @GetMapping("/{id}")
    public Optional<UserEntity> getUserById(@PathVariable Long id) {
        return userModuleService.getUserById(id);
    }

    @PostMapping("/create")
    public UserEntity createUser(@RequestBody UserEntity userModule) {
        return userModuleService.createUser(userModule);
    }

    @PostMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        userModuleService.verifyAccount(email, otp);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody loginDto loginDto) {
        UserEntity user = userModuleService.loginUser(loginDto);
        if (user != null) {
            if (user.isActive()) {
                if (validateUserForLogin(user, loginDto)) {
                    return ResponseEntity.ok("Login successful");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not verified");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    private boolean validateUserForLogin(UserEntity user, loginDto loginDto) {
        return (user.getEmail().equals(loginDto.getEmail()) || user.getUserName().equals(loginDto.getUserName()))
                && user.getRoles().equals(loginDto.getRole())
                && userModuleService.verifyEncryptedPassword(loginDto.getPassword(), user.getPassword())
                && user.getOtp().equals(loginDto.getOtp());
    }

    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity updatedUserModule) {
        return userModuleService.updateUser(id, updatedUserModule);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userModuleService.deleteUser(id);
    }

    @PostMapping("/contact-us")
    public ResponseEntity<String> contactUs(@RequestBody ContactUs contactUs) {
        userModuleService.contactUs(contactUs);
        return ResponseEntity.ok("Message sent successfully");
    }
}
