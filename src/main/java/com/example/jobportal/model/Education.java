package com.example.jobportal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Entity
@Transactional
@Data
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eduId;
    private String school;
    private String degree;
    private String field;
    private String grade;

    @JsonFormat(pattern = "yyyy-MM", shape = JsonFormat.Shape.STRING)
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM", shape = JsonFormat.Shape.STRING)
    private Date endDate;

    public Education(){

    }

    public Education(String school, String degree, String field, String grade, Date startDate, Date endDate) {
        this.school = school;
        this.degree = degree;
        this.field = field;
        this.grade = grade;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @ManyToOne
    @JoinColumn(name="userId")
    private PersonalDetails personalDetails;


}
