package com.vrpigroup.usermodule.entity;

import com.vrpigroup.usermodule.annotations.email.ValidEmail;
import com.vrpigroup.usermodule.annotations.passwordAnnotation.Password;
import com.vrpigroup.usermodule.annotations.phone.Phone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id;

    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Column(name = "user_Name", nullable = false, unique = true)
    private String userName;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Password
    @Column(name = "Password", nullable = false)
    private String password;

    @Size(min = 3, max = 50, message = "Full Name must be between 3 and 50 characters")
    @Column(name = "Full_Name", nullable = false)
    private String name;

    @ValidEmail
    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Size(min = 3, max = 50, message = "Father's name must be between 3 and 50 characters")
    @Column(name = "Fathers_Name", nullable = false)
    private String fathersName;

    @Size(max = 255, message = "Address can't exceed 255 characters")
    @Column(name = "ADDRESS", nullable = false)
    private String address;


    @Column(name = "Phone_Number", nullable = false, unique = true)
    private String phoneNumber;

    @Past
    @Column(name = "DOB", nullable = false)
    private LocalDate dateOfBirth;

    //@NotBlank(message = "Pincode can't be blank")
    @Pattern(regexp = "\\d{6}", message = "Invalid pincode format")
    @Column(name = "Pincode", nullable = false)
    private String pincode;

    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN card format")
    @Column(name = "Pan_Card_Number", unique = true)
    private String panCardNumber;

    @Pattern(regexp = "\\d{12}", message = "Invalid Aadhar card format")
    @Column(name = "Aadhar_Card_Number", nullable = false, unique = true)
    private String aadharCardNumber;

    private String otp;

    private boolean active;

    @ElementCollection(targetClass = Roles.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

}
