package com.example.jobportal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM", shape = JsonFormat.Shape.STRING)
    private Date startDateEx;

    @JsonFormat(pattern = "yyyy-MM", shape = JsonFormat.Shape.STRING)
    private Date endDateEx;

    public Experience(){

    }

    public Experience(String title, String company, String empType, String locationType, Date startDateEx, Date endDateEx) {
        this.title = title;
        this.company = company;
        this.empType = empType;
        this.locationType = locationType;
        this.startDateEx = startDateEx;
        this.endDateEx = endDateEx;
    }

    @ManyToOne
    @JoinColumn(name="userId")
    private PersonalDetails personalDetails;

}
