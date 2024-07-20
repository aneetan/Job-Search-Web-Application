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

    @OneToMany(mappedBy="personalDetails", cascade = CascadeType.ALL)
    private List<Education> eduList;

    @OneToMany(mappedBy="personalDetails", cascade = CascadeType.ALL)
    private List<Experience> exList;

    @OneToOne(mappedBy = "personalDetails", cascade = CascadeType.ALL)
    private additionalDetails additionalDetails;

    @OneToOne(mappedBy = "personalDetails", cascade = CascadeType.ALL)
    private userDocs userDocs;

    public PersonalDetails(){

    }
    public PersonalDetails(String email, String name, String password, String address, String phoneNo) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    public PersonalDetails(String email, String name, String address, String phoneNo) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNo = phoneNo;
    }
}
