package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class PersonalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    private String email;
    private String name;
    private String password;
    private String address;
    private String phoneNo;

    @OneToMany(mappedBy="employee")
    private List<Education> eduList;

    @OneToMany(mappedBy="employee")
    private List<Experience> exList;

    @OneToMany(mappedBy="employee")
    private List<Certifications> certificateList;





}
