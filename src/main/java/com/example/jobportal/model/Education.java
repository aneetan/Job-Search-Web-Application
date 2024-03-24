package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eduId;
    private String school;
    private String degree;
    private String field;
    private float grade;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name="userId")
    private PersonalDetails details;

}
