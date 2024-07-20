package com.example.jobportal.repository;

import com.example.jobportal.model.additionalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalDetailsRepository extends JpaRepository<additionalDetails, Integer> {
}
