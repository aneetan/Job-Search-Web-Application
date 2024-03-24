package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exId;

    private String title;
    private String company;

    private String empType;
    private String locationType;

    private Date startDateEx;
    private Date endDateEx;

    @ManyToOne
    @JoinColumn(name="userId")
    private PersonalDetails details;

}
