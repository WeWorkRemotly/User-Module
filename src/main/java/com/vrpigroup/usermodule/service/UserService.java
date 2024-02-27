package com.vrpigroup.usermodule.service;
import com.vrpigroup.usermodule.annotations.email.EmailValidation;
import com.vrpigroup.usermodule.annotations.email.EmailValidationServiceImpl;
import com.vrpigroup.usermodule.entity.ContactUs;
import com.vrpigroup.usermodule.entity.Roles;
import com.vrpigroup.usermodule.entity.UserEntity;
import com.vrpigroup.usermodule.loginDto;
import com.vrpigroup.usermodule.repo.ContactUsRepo;
import com.vrpigroup.usermodule.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userModuleRepository;

    private final ContactUsRepo contactUsRepo;

    private final EmailValidationServiceImpl emailValidationService;

    private final EmailValidation emailValidation;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userModuleRepository, ContactUsRepo contactUsRepo, EmailValidationServiceImpl emailValidationService, EmailValidation emailValidation) {
        this.userModuleRepository = userModuleRepository;
        this.contactUsRepo = contactUsRepo;
        this.emailValidationService = emailValidationService;
        this.emailValidation = emailValidation;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> getAllUser() {
        return userModuleRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userModuleRepository.findById(id);
    }

    public UserEntity createUser(UserEntity userEntity) {
        String otp = generateOtp();
        userEntity.setOtp(otp);
        userEntity.setActive(false);
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.USER);
        userEntity.setRoles(roles);
        emailValidation.isEmailValid(userEntity.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        sendOtpByEmail(userEntity.getEmail(), otp);
        if(verifyAccount(userEntity.getEmail(), otp)) userEntity.setActive(true);
        UserEntity savedUser = userModuleRepository.save(userEntity);

        return savedUser;
    }

    private void sendOtpByEmail(String email, String otp){
        emailValidationService.sendVerificationEmail(email, otp);
        System.out.println("Sending OTP to " + email + " : " + otp);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public UserEntity updateUser(Long id, UserEntity updatedUserModule) {
        if (userModuleRepository.existsById(id)) {
            updatedUserModule.setId(id);
            return userModuleRepository.save(updatedUserModule);
        } else {
            return null;
        }
    }

    public void deleteUser(Long id) {
        userModuleRepository.deleteById(id);
    }

    public UserEntity loginUser(loginDto userModule) {
        Optional<UserEntity> userByEmail = userModuleRepository.findByEmail(userModule.getEmail());
        Optional<UserEntity> userByUsername = userModuleRepository.findByUserName(userModule.getUserName());
        if (userByEmail.isPresent() && verifyLogin(userByEmail.get(), userModule)) {
            return userByEmail.get();
        }
        if (userByUsername.isPresent() && verifyLogin(userByUsername.get(), userModule)) {
            return userByUsername.get();
        }
        return null;
    }

    private boolean verifyLogin(UserEntity user, loginDto userModule) {
        if (user.getOtp().equals(userModule.getOtp()) && user.isActive()
                && passwordEncoder.matches(userModule.getPassword(), user.getPassword())) {
            return true;
        }
        return false;
    }

    public boolean verifyEncryptedPassword(String password, String password1) {
        return passwordEncoder.matches(password, password1);
    }

    public String contactUs(ContactUs contactUs) {
        var c =    contactUsRepo.save(contactUs);
        if(c != null){
            return "Message sent successfully";
        }else {
            return "Message not sent";
        }
    }

    public boolean verifyAccount(String email, String otp) {
        Optional<UserEntity> user = userModuleRepository.findByEmail(email);
        if (user.isPresent() && user.get().getOtp().equals(otp)) {
            user.get().setActive(true);
            userModuleRepository.save(user.get());
        }
        return false;
    }
}