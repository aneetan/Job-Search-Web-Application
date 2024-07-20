//package com.example.jobportal.RestController;
//
//import com.example.jobportal.model.*;
//import com.example.jobportal.repository.EducationRepository;
//import com.example.jobportal.repository.FileRepository;
//import com.example.jobportal.repository.JobDetailsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class RestControllerImplements {
//
//    @Autowired
//    private EducationRepository eRepo;
//
//
//    @PostMapping("/submitEducation")
//    @ResponseBody
//    public String submitEducation(@RequestBody Education formData, Model model) {
//        // Save the education data
//        Education education = new Education(formData.getSchool(), formData.getDegree(), formData.getField(), formData.getGrade(), formData.getStartDate(), formData.getEndDate());
//
//          education.setPersonalDetails(formData.getPersonalDetails());
//          eRepo.save(education);
//
//          PersonalDetails details = new PersonalDetails();
//          List<Education> educationList = new ArrayList<>();
//          educationList.add(education);
//
//          details.setEduList(educationList);
//
//        // Return a success response
//        model.addAttribute("educationList", educationList);
//        return "additional.html";
//    }
//
//    private FileRepository fRepo;
//    private JobDetailsRepository jRepo;
//
//    public RestControllerImplements(FileRepository fRepo, JobDetailsRepository jRepo) {
//        this.fRepo = fRepo;
//        this.jRepo = jRepo;
//    }
//
//    @PostMapping("/demo")
//    public userDocs saveUser(@RequestBody userDocs userDocs){
//        return fRepo.save(userDocs);
//    }
//
//}
