package com.example.jobportal.repository;

import com.example.jobportal.model.Education;
import com.example.jobportal.model.Experience;
import com.example.jobportal.model.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository  extends JpaRepository<Experience, Integer> {
    List<Experience> findAllByPersonalDetails(PersonalDetails personalDetails);


}
