package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Entity
@Transactional
@Data
public class PersonalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String email;
    private String name;
    private String password;
    private String address;
    private String phoneNo;

    @OneToMany(mappedBy="personalDetails")
    private List<Education> eduList;

    @OneToMany(mappedBy="personalDetails")
    private List<Experience> exList;

    @OneToMany(mappedBy="personalDetails")
    private List<Certifications> certificateList;

    public PersonalDetails(){

    }
    public PersonalDetails(String email, String name, String password, String address, String phoneNo) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phoneNo = phoneNo;
    }
}
