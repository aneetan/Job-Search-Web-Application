package com.example.jobportal.repository;

import com.example.jobportal.model.Company;
import com.example.jobportal.model.CompanyDocs;
import com.example.jobportal.model.PersonalDetails;
import com.example.jobportal.model.userDocs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<userDocs, Integer> {

//    userDocs getuserDocsByPersonalDetails(PersonalDetails personalDetails);
    userDocs getAllByPersonalDetails(PersonalDetails personalDetails);

//    List<userDocs> findByPersonalDetailsContaining(String name);

    @Query("SELECT u FROM userDocs u WHERE u.personalDetails.name LIKE %:name%")
    Page<userDocs> findByPersonalDetailsNameContainingIgnoreCase(String name, Pageable pageable);






}
