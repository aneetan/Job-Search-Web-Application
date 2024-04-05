package com.example.jobportal.controller;

import com.example.jobportal.model.*;
import com.example.jobportal.repository.*;
import com.example.jobportal.service.ApplicantService;
import com.example.jobportal.service.FileService;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.MailService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class JobController {
    @Autowired
    private CompanyRepository cRepo;
    @Autowired
    private CompanyDetailsRepository cdRepo;
    @Autowired
    private companyDocsRepository docsRepo;

    @GetMapping("/")
    public String frontPage()
    {
        return "index.html";
    }

    @GetMapping("/adminLogin")
    public String adminLogin(){
        return "adminLogin.html";
    }

    @PostMapping("/companyLogin")
    public String companyLogin(@ModelAttribute Company c, HttpSession session, Model model){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashPw = encoder.encode(c.getCompanyPassword());

        Company company = cRepo.findByCompanyEmail(c.getCompanyEmail());
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        CompanyDetails companyDetails = cdRepo.findByCompany(company);

        if (company != null && encoder.matches(c.getCompanyPassword(), company.getCompanyPassword())){
            session.setAttribute("activeCompany", company);
            session.setAttribute("activeDetails", companyDetails);
//            session.setMaxInactiveInterval(30);
            model.addAttribute("data", docs);

            // Retrieve job details for the company
            List<JobDetails> jobDetailsList = jdRepo.findAllByCompany(company);

            // Create a map to hold job details and associated applicants
            List<Applicant> selectedApplicants = new ArrayList<>();

            // Iterate through each job posted by the company
            for (JobDetails job : jobDetailsList) {
                // Retrieve applicants with status "pending" for the current job
                List<Applicant> applicantsForJob = appRepo.findByJobDetailsAndStatus(job, "pending");

                // Add applicants to the list of selected applicants
                selectedApplicants.addAll(applicantsForJob);
            }

            List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);

            // Add unique job locations to the model
            model.addAttribute("uniqueJob", uniqueJob);
            model.addAttribute("applicants", selectedApplicants);
            model.addAttribute("jobTitle", "All jobs");


            return "companylanding.html";
        } else {
            model.addAttribute("login", "Invalid username or password");
            return "adminLogin.html";
        }
    }

    @GetMapping("/adminRegister")
    public String companySignup(){
        return "adminRegister.html";
    }

    @PostMapping("/adminReg")
    public String companyRegister(@ModelAttribute("c") Company c, Model model){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Hash the password
        String hashedPassword = passwordEncoder.encode(c.getCompanyPassword());

        // Check if email already exists in the database
        if (cRepo.existsByCompanyEmail(c.getCompanyEmail())) {
            model.addAttribute("error", "Email already exists");
            return "adminRegister.html";
        }

        Company company = new Company(c.getCompanyName(), c.getCompanyEmail(), hashedPassword);
        company.setStatus("Pending");
        cRepo.save(company);

        model.addAttribute("Company", company);
        return "companyDetails.html";
    }

    @PostMapping("/companyDetails")
    public String goToCompanyDocs(@ModelAttribute CompanyDetails cd,@ModelAttribute("Company") Company company, Model model){
        CompanyDetails companyDetails = new CompanyDetails(cd.getCompanyAddress(), cd.getIndustry(), cd.getEmployeeNo(), cd.getCompanyContact(), cd.getCompanyUrl());

        companyDetails.setCompany(company);
        System.out.println(company);
        System.out.println(companyDetails);

        model.addAttribute("Company", company );
        cdRepo.save(companyDetails);

        model.addAttribute("cd", companyDetails);
        return "companyDocs.html";
    }

    @PostMapping("/saveCompanyData")
    public String registerCompany(@ModelAttribute CompanyDocs doc, @ModelAttribute("Company") Company company,@ModelAttribute("cd") CompanyDetails cd, Model model,
                                  @RequestParam("logo") MultipartFile logo, @RequestParam("docName") MultipartFile docName ) throws IOException {

        String logoName = logo.getOriginalFilename();
        fileService.saveFile(logo, logoName);

        String verfiedDocName = docName.getOriginalFilename();
        fileService.saveFile(docName, verfiedDocName);

        CompanyDocs companyDocs = new CompanyDocs(logoName, verfiedDocName);
        companyDocs.setCompany(company);
        companyDocs.setCompanyDetails(cd);

        docsRepo.save(companyDocs);

        model.addAttribute("login", "Please login to Continue");
        return "adminLogin.html";
    }


//    ------------------user login and registration -----------------------------------
    @Autowired
    private PersonalDetailsRepository pRepo;

    @Autowired
    private AdditionalDetailsRepository addRepo;
    @Autowired
    private FileRepository uRepo;
    @GetMapping("/userLogin")
    public String login(){
        return "login.html";
    }

    @GetMapping("/goToUserRegistration")
    public String goToUserRegistration(){
        return "registration.html";
    }

    @PostMapping("/goToAdditional")
    public  String goToAdditional(@ModelAttribute("personalDetails") PersonalDetails details, Model model){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(details.getPassword());

        // Check if email already exists in the database
        if (pRepo.existsByEmail(details.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "registration.html";
        }

        PersonalDetails pd = new PersonalDetails(details.getEmail(), details.getName(), hashedPassword, details.getAddress(), details.getPhoneNo());
        pRepo.save(pd);

        model.addAttribute("pDetails", pd);
        return "additional.html";
    }


    @PostMapping("/goTodocs")
    public String goToDocs(@ModelAttribute additionalDetails add, @ModelAttribute("pDetails") PersonalDetails details, Model model){
        additionalDetails addDetails = new additionalDetails(add.getBio(), add.getJobTitle());
        addDetails.setPersonalDetails(details);

        model.addAttribute("pDetails", details);
        addRepo.save(addDetails);

        model.addAttribute("add", addDetails);

        return "docs.html";
    }

    @Autowired
    private EducationRepository eduRepo;

    @PostMapping(value = "/submitEducation", produces = "text/plain", consumes = "application/json")
    @ResponseBody
    public String submitEducation(@RequestBody Education formData, Model model) {
        Education education = new Education(formData.getSchool(), formData.getDegree(), formData.getField(), formData.getGrade(), formData.getStartDate(), formData.getEndDate());
        education.setPersonalDetails(formData.getPersonalDetails());
        eduRepo.save(education);

        return "additional.html";
    }

    @Autowired
    private ExperienceRepository exRepo;

    @PostMapping(value = "/submitExperience", produces = "text/plain", consumes = "application/json")
    @ResponseBody
    public Experience submitExperience(@RequestBody Experience formData, Model model) {
        // Save the experience data
        Experience experience = new Experience(formData.getTitle(), formData.getCompany(), formData.getEmpType(), formData.getLocationType(), formData.getStartDateEx(), formData.getEndDateEx());
        experience.setPersonalDetails(formData.getPersonalDetails());
        exRepo.save(experience);

        List<Experience> experienceList = new ArrayList<>(); // Assuming findAll() retrieves all experiences
        experienceList.add(experience);
        return formData;
    }

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fRepo;

    @PostMapping("/userRegister")
    public String registerUser( @ModelAttribute userDocs u,  @ModelAttribute("pDetails") PersonalDetails details,
                                @ModelAttribute("add") additionalDetails add, @RequestParam("cv") MultipartFile cv,
                                @RequestParam("profile") MultipartFile profile, Model model) throws IOException {

        String cvFileName = cv.getOriginalFilename();
        fileService.saveFile(cv, cvFileName);

        String profileFileName = profile.getOriginalFilename();
        fileService.saveFile(profile, profileFileName);

        System.out.println(details);
        userDocs docs = new userDocs(cvFileName, profileFileName);
        docs.setPersonalDetails(details);
        docs.setAdditionalDetails(add);

        fRepo.save(docs);

        model.addAttribute("login", "Please login to continue");

        // Redirect to success page
        return "login.html";
    }
    @Autowired
    private JobDetailsRepository jdRepo;


    @PostMapping("/loginUser")
    public String userLogin(@ModelAttribute PersonalDetails p, @ModelAttribute CompanyDetails cd, HttpSession session, Model model, @ModelAttribute CompanyDocs d){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashPw = encoder.encode(p.getPassword());

        String adminEmail = "admin@gmail.com";
        String adminPw = encoder.encode("admin@123");

        if (p.getEmail().equals(adminEmail) && encoder.matches(p.getPassword(), adminPw)) {
            session.setAttribute("activeUser", adminEmail);
            session.setMaxInactiveInterval(30);


            model.addAttribute("dataList", docsRepo.findByCompany_Status("Pending"));

            return "superAdmin.html";

        } else {
            PersonalDetails user = pRepo.findByEmail(p.getEmail());
            userDocs docs = fRepo.getAllByPersonalDetails(user);

            if (user != null && encoder.matches(p.getPassword(), user.getPassword())){
                session.setAttribute("activeUser", user);

//                session.setMaxInactiveInterval(30);

                model.addAttribute("data", docs);


                List<JobDetails> unappliedJobs = applicantService.findUnappliedJobs(docs);
                Map<JobDetails, Long> daysRemainingMap = jobService.calculateDaysRemainingForJobs(unappliedJobs);

                if (unappliedJobs.isEmpty()){
                    model.addAttribute("noJobs", "No recent jobs!!");
                } else{
                    model.addAttribute("days", daysRemainingMap);

                    //pass the attribute of unapplied jobs by the user in dashboard
                    model.addAttribute("job", applicantService.findUnappliedJobs(docs));
                }


                return "userLanding.html";
            } else {
                model.addAttribute("login", "Invalid username or password");
                return "login.html";
            }
        }
    }


//    ------------------------------ Super Admin -------------------------------------------------


    @GetMapping("/approvedComp")
    public String approvedCompanies(Model model, @ModelAttribute CompanyDocs doc){

        model.addAttribute("dataList", docsRepo.findByCompany_Status("Approved"));

        return "approvedComp.html";
    }

    @GetMapping("/superAdmin")
    public String superAdmin(Model model){
        model.addAttribute("dataList", docsRepo.findByCompany_Status("Pending"));


        return "superAdmin.html";
    }

    @Autowired
    private MailService mailService;


    @GetMapping("/approve/{id}")
    public String approveComp(@PathVariable("id") int id, Model model,
                              @ModelAttribute MailStructure mailStructure, HttpSession session){
        if(session.getAttribute("activeUser") == null) {
            session.setAttribute("login", "Please login first");
            return "login.html";
        }

        Company company = cRepo.getById(id);
        company.setStatus("Approved");

        cRepo.save(company);

        mailStructure.setSubject("Response of Company Submission");
        mailStructure.setMessage("Dear concern,\n\n" +
                "Based on the proof you have provided, your company has been approved. You can now post jobs! Have a great time!!\n\n"+
                "Warm regards,\n" +
                "JobSearch\n");
        mailService.sendMail(company.getCompanyEmail(), mailStructure);
//        mailService.sendMail("anee.neu15@gmail.com", mailStructure);

        model.addAttribute("dataList", docsRepo.findByCompany_Status("Approved"));

        return "approvedComp.html";
    }

    @GetMapping("/decline/{id}")
    public String declineComp(@PathVariable("id") int id, Model model, HttpSession session, @ModelAttribute MailStructure mailStructure){
        if(session.getAttribute("activeUser") == null) {
            session.setAttribute("login", "Please login first");
            return "login.html";
        }

        Company company = cRepo.getById(id);
        company.setStatus("Declined");

        cRepo.save(company);

        mailStructure.setSubject("Response of Company Submission");
        mailStructure.setMessage("Dear concern,\n\n" +
                "After careful review and consideration, we regret to inform you that we will not be moving forward with your company at this time. Please provide us the effective document in this email!\n\n"+
                "Warm regards,\n" +
                "JobSearch\n");

        mailService.sendMail(company.getCompanyEmail(), mailStructure);
//        mailService.sendMail("anee.neu15@gmail.com", mailStructure);

        model.addAttribute("dataList", docsRepo.findByCompany_Status("Declined"));
        return "declinedCompany.html";
    }

    @GetMapping("/declinedCompany")
    public String declinedCompany(Model model){
        model.addAttribute("dataList", docsRepo.findByCompany_Status("Declined"));
        return "declinedCompany.html";
    }

    @GetMapping("/logoutAdmin")
    public String logoutAdmin(HttpSession session, Model model){
        session.invalidate();
        return "login.html";
    }

//------------------------------ Company Landing ----------------------------------------------
    @GetMapping("/companyLanding")
    public String goToCompanyLanding(HttpSession session, Model model){
        if(session.getAttribute("activeUser") == null) {
            session.setAttribute("login", "Please login first");
            return "adminLogin.html";
        }

        Company company = (Company) session.getAttribute("activeCompany");
        session.setAttribute("activeCompany", company);
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        model.addAttribute("data", docs);

        // Retrieve job details for the company
        List<JobDetails> jobDetailsList = jdRepo.findAllByCompany(company);

        // Create a map to hold job details and associated applicants
        List<Applicant> selectedApplicants = new ArrayList<>();

        // Iterate through each job posted by the company
        for (JobDetails job : jobDetailsList) {
            // Retrieve applicants with status "pending" for the current job
            List<Applicant> applicantsForJob = appRepo.findByJobDetailsAndStatus(job, "pending");

            // Add applicants to the list of selected applicants
            selectedApplicants.addAll(applicantsForJob);
        }

        List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);

        // Add unique job locations to the model
        model.addAttribute("uniqueJob", uniqueJob);
        model.addAttribute("applicants", selectedApplicants);
        model.addAttribute("jobTitle", "All jobs");

        return "companylanding.html";
    }

    @GetMapping("/viewJob")
    public String viewJobCandidate(HttpSession session, Model model){

        Company company = (Company) session.getAttribute("activeCompany");
        session.setAttribute("activeCompany", company);
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        model.addAttribute("data", docs);

        List<JobDetails> jobDetails = jdRepo.findAllByCompany(company);
        model.addAttribute("jobDetails", jobDetails);

        return "viewJobsCompany.html";
    }

    @GetMapping("/dismiss/{jobId}")
    private String dissmissJob(HttpSession session, Model model, @PathVariable int jobId){
        JobDetails details = jdRepo.findAllByJobId(jobId);
        details.setJobStatus("Expired");

        jdRepo.save(details);

        Company company = (Company) session.getAttribute("activeCompany");
        session.setAttribute("activeCompany", company);
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        model.addAttribute("data", docs);

        List<JobDetails> jobDetails = jdRepo.findAllByCompany(company);
        model.addAttribute("jobDetails", jobDetails);

        return "viewJobsCompany.html";
    }

    @GetMapping("/close/{jobId}")
    private String closeJob(HttpSession session, Model model, @PathVariable int jobId){
        JobDetails details = jdRepo.findAllByJobId(jobId);
        details.setJobStatus("Inactive");

        jdRepo.save(details);

        Company company = (Company) session.getAttribute("activeCompany");

        List<JobDetails> jobDetails = jdRepo.findAllByCompany(company);
        model.addAttribute("jobDetails", jobDetails);

        return "viewJobsCompany.html";
    }

    @GetMapping("/addJobs")
    public String addJobs(HttpSession session, Model model) {
        Company activeCompany = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.findAllByCompany(activeCompany);
        System.out.println(docs);

        if (activeCompany == null) {
            model.addAttribute("login", "Please login first");
            return "adminLogin.html";
        }

        if(activeCompany.getStatus().equals("Pending")) {
            model.addAttribute("error", "Your company request is under approval! Please wait for confirmation mail.");

        } else if (activeCompany.getStatus().equals("Declined")) {

            model.addAttribute("error", "Sorry! Your approval is declined.");
        }

        model.addAttribute("Company", activeCompany);
        model.addAttribute("data", docs);
        model.addAttribute("details", docs.getCompanyDetails());
        return "addJobsCompany.html";
    }

    @PostMapping("/postJob")
    public String postJob(@ModelAttribute JobDetails details, @ModelAttribute("Company") Company company,
                          HttpSession session,@ModelAttribute("data") CompanyDocs docs, @ModelAttribute("details") CompanyDetails companyDetails, Model model){

        Company activeCompany = (Company) session.getAttribute("activeCompany");
        System.out.println(company);
        System.out.println(docs);
        System.out.println(companyDetails);

//        if(activeCompany == null) {
//            model.addAttribute("login", "Please login first");
//
//            return "adminLogin.html";
//        }
        if(activeCompany.getStatus().equals("Pending")){
            model.addAttribute("error", "Your company request is under approval! Please wait for confirmation mail to add job details.");

        } else if(activeCompany.getStatus().equals("Declined")){
            model.addAttribute("error", "Sorry! Your approval is declined.");
        } else {

            LocalDate today = LocalDate.now();
            LocalDate deadline = details.getDeadline();
            long daysRemaining = ChronoUnit.DAYS.between(today, deadline);

            JobDetails jd = new JobDetails(details.getTitle(), details.getLocation(), details.getEmpType(), details.getJobDescription(), details.getQualification(), details.getSkills(), details.getSalary(), details.getResponsibilities(), details.getDeadline());

            if (deadline.isAfter(today)) {
                jd.setJobStatus("Active");

            } else {
                jd.setJobStatus("Expired");
            }

            jd.setCompany(company);
            jd.setCompanyDocs(docs);

            jdRepo.save(jd);

            model.addAttribute("days", daysRemaining);
            model.addAttribute("success", "Your job has been posted");
        }
        return "addJobsCompany.html";
    }

    @GetMapping("/logoutCompany")
    public String logoutCompany(HttpSession session){
        session.invalidate();
        return "adminLogin.html";
    }


//    ------------------------------------------- User ---------------------------------------------------

    @GetMapping("/viewJobsUser")
    public String viewJobs(@ModelAttribute PersonalDetails p, Model model, HttpSession session){
        PersonalDetails personalDetails = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(personalDetails);
        model.addAttribute("data", docs);

        List<JobDetails> unappliedJobs = applicantService.findUnappliedJobs(docs);

        if (unappliedJobs.isEmpty()){
            model.addAttribute("noJobs", "No recent jobs!!");
        } else{
            //pass the attribute of unapplied jobs by the user in dashboard
            model.addAttribute("job", applicantService.findUnappliedJobs(docs));
        }

        return "userLanding.html";
    }

    @Autowired
    private ApplicantRepository appRepo;

    @GetMapping("/seeAppliedJobs/{userId}")
    public String viewAppliedJobs(@PathVariable int userId, @ModelAttribute PersonalDetails p, Model model){
        PersonalDetails user = pRepo.findAllByUserId(userId);
        userDocs docs = fRepo.getAllByPersonalDetails(user);
        model.addAttribute("data", docs);

        List<JobDetails> appliedJobs = applicantService.getAppliedJobsForUser(docs);
        List<Applicant> jobApplications = appRepo.findByUserDocs(docs);

        model.addAttribute("job", appliedJobs);
        if (jobApplications.isEmpty()){
            model.addAttribute("noJobs", "You haven't applied for any jobs!!" );
        } else {
            Map<JobDetails, String> jobStatusMap = new HashMap<>();

            for (Applicant applicant : jobApplications) {
                // Find the corresponding job details for the current application
                JobDetails jobDetails = appliedJobs.stream()
                        .filter(job -> job.getJobId() == applicant.getJobDetails().getJobId())
                        .findFirst()
                        .orElse(null);

                if (jobDetails != null) {
                    // Add the status of the application to the job details
                    jobStatusMap.put(jobDetails, applicant.getStatus());
                }
            }

            model.addAttribute("jobStatusMap", jobStatusMap);
            model.addAttribute("applicant", jobApplications);
        }

        return "appliedJobs.html";
    }

    @Autowired
    private ApplicantService applicantService;

    @GetMapping("/{jobId}/apply/{userId}")
    public String applyForJob(@ModelAttribute PersonalDetails p, @PathVariable int userId, @PathVariable int jobId, Model model, HttpSession session) {

        userDocs userDoc = fRepo.getAllByPersonalDetails(pRepo.findAllByUserId(userId));
        JobDetails jobDetails = jdRepo.findAllByJobId(jobId);

        boolean hasApplied = applicantService.hasUserAppliedForJob(userDoc, jobDetails);

        if (hasApplied) {
            model.addAttribute("error", "You have already applied for job!");

        } else {
            applicantService.applyForJob(userDoc, jobDetails);
            model.addAttribute("success", "Job applied successfully!!");
        }

        PersonalDetails personalDetails = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(personalDetails);
        model.addAttribute("data", docs);

        List<JobDetails> unappliedJobs = applicantService.findUnappliedJobs(docs);
        if (unappliedJobs.isEmpty()){
            model.addAttribute("noJobs", "No recent jobs!!");
        } else{
            //pass the attribute of unapplied jobs by the user in dashboard
            model.addAttribute("job", applicantService.findUnappliedJobs(docs));
        }

        return "userLanding.html";
    }

    @GetMapping("/logoutUser")
    public String logoutUser(HttpSession session){
        session.invalidate();
        return "login.html";
    }

    @GetMapping("/companyList")
    public String seeCompanyList(Model model, HttpSession session){
        PersonalDetails personalDetails = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(personalDetails);
        model.addAttribute("data", docs);

        model.addAttribute("companyList", docsRepo.findByCompany_Status("Approved"));
        return "seeCompanyList.html";
    }

    @GetMapping("/viewCompanyDetails/{companyId}")
    public String viewCompanyDetails(@PathVariable int companyId, Model model, HttpSession session){
        PersonalDetails personalDetails = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(personalDetails);
        model.addAttribute("data", docs);

        Company company = cRepo.findByCompanyId(companyId);
        CompanyDocs companyDocs = docsRepo.getCompanyDocsByCompany(company);

        List<JobDetails> allJobs = jdRepo.findByCompanyAndJobStatus(company, "Active");

        // Filter out the jobs the user has already applied for
        List<JobDetails> unappliedJobs = allJobs.stream()
                .filter(job -> !applicantService.hasUserAppliedForJob(docs, job))
                .collect(Collectors.toList());

        if (unappliedJobs.isEmpty()){
            model.addAttribute("noJobs", "No recent un-applied jobs from this company!!");
        } else {
            model.addAttribute("jobs", unappliedJobs);

        }
        model.addAttribute("companyList", companyDocs);

        return "viewCompanyDetails.html";
    }

    @Autowired
    private JobService jobService;

    @GetMapping("/search")
    public String searchJobs(@RequestParam("query") String query, Model model, HttpSession session) {
        PersonalDetails personalDetails = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(personalDetails);
        model.addAttribute("data", docs);

        List<JobDetails> searchResults = jobService.searchJobs(query);
        if (searchResults.isEmpty()){
            model.addAttribute("noJobs", "No results found!!");
        } else {
            model.addAttribute("job", searchResults);
        }

        return "userLanding.html";
    }

    @GetMapping("/searchAppliedJobs")
    public String searchAppliedJobs(@RequestParam("query") String query, Model model, HttpSession session){
        PersonalDetails user = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(user);
        model.addAttribute("data", docs);

        List<JobDetails> appliedJobs = applicantService.getAppliedJobsForUser(docs);
        List<Applicant> jobApplications = appRepo.findByUserDocs(docs);

        // Filter applied jobs based on the search query
        List<JobDetails> filteredAppliedJobs = jdRepo.findByTitleContainingIgnoreCase(query);

        //if no jobs found
        if (filteredAppliedJobs.isEmpty()) {
            model.addAttribute("noJobs", "No applied jobs found for the search query!!");
        } else {

            //map jobdetails with status
            Map<JobDetails, String> jobStatusMap = new HashMap<>();
            for (Applicant applicant : jobApplications) {
                //foreach applicant retrieved jobdetails
                JobDetails jobDetails = jdRepo.findByJobId(applicant.getJobDetails().getJobId());

                if (jobDetails != null) {
                    //save the status of the associated job found
                    jobStatusMap.put(jobDetails, applicant.getStatus());
                }
            }

            model.addAttribute("job", filteredAppliedJobs);
            model.addAttribute("jobStatusMap", jobStatusMap);
            model.addAttribute("applicant", jobApplications);
        }

        return "appliedJobs.html";
    }

    @GetMapping("/searchCompany")
    private String searchCompany(@RequestParam("query") String query, Model model, HttpSession session){
        PersonalDetails personalDetails = (PersonalDetails) session.getAttribute("activeUser");
        userDocs docs = fRepo.getAllByPersonalDetails(personalDetails);
        model.addAttribute("data", docs);

        List<CompanyDocs> companyList = docsRepo.findByCompany_Status("Approved");

        // Filter the company list based on the search query
        List<CompanyDocs> filteredCompanyList =
                docsRepo.findByCompany_CompanyNameContainingIgnoreCaseOrCompanyDetails_CompanyAddressContainingIgnoreCaseAndCompany_Status(
                query, query, "Approved");

        if (filteredCompanyList.isEmpty()) {
            model.addAttribute("noCompanies", "No companies found for the search query!!");
        } else {
            model.addAttribute("companyList", filteredCompanyList);
        }
        return "seeCompanyList.html";
    }

    @GetMapping("/getApplicants")
    public String getApplicantsForJob(@RequestParam("jobTitle") String jobTitle, Model model, HttpSession session) {
        // Retrieve applicants for the selected job title
        List<Applicant> applicants = jobService.getApplicantsByJobTitle(jobTitle);

        // Pass the list of applicants to the view
        model.addAttribute("applicants", applicants);

        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);

        model.addAttribute("data", docs);

        List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);

        // Add unique job locations to the model
        model.addAttribute("uniqueJob", uniqueJob);

        model.addAttribute("jobTitle", jobTitle);

        // Return the view name
        return "companyLanding.html";
    }

    @GetMapping("/approvedApplicants")
    private String approvedApplicants(HttpSession session, Model model){
        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.findAllByCompany(company);
        model.addAttribute("data", docs);

        // Retrieve job details for the company
        List<JobDetails> jobDetailsList = jdRepo.findAllByCompany(company);

        // Create a map to hold job details and associated applicants
        List<Applicant> selectedApplicants = new ArrayList<>();

        // Iterate through each job posted by the company
        for (JobDetails job : jobDetailsList) {
            // Retrieve applicants for the current job
            List<Applicant> applicantsForJob = appRepo.findByJobDetailsAndStatus(job, "Approved");

            // Add applicants to the list of selected applicants
            selectedApplicants.addAll(applicantsForJob);
        }

        if (selectedApplicants.isEmpty()){
            model.addAttribute("noApplicant", "No approved Applicants!!");
        } else {
            model.addAttribute("applicants", selectedApplicants);

        }

        List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);

        // Add unique job locations to the model
        model.addAttribute("uniqueJob", uniqueJob);
        model.addAttribute("jobTitle", "All jobs");

        return "approvedApplicants.html";
    }

    @GetMapping("/getApprovedApplicants")
    public String getApprovedApplicantsForJob(@RequestParam("jobTitle") String jobTitle, Model model, HttpSession session) {
        // Retrieve applicants for the selected job title
        List<Applicant> applicants = jobService.getApprovedApplicantsByJobTitle(jobTitle);

        // Pass the list of applicants to the view
        model.addAttribute("applicants", applicants);

        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);

        model.addAttribute("data", docs);

        List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);

        // Add unique job locations to the model
        model.addAttribute("uniqueJob", uniqueJob);

        model.addAttribute("jobTitle", jobTitle);

        // Return the view name
        return "approvedApplicants.html";
    }


    @GetMapping("{applicantId}/applicantProfile/{userId}")
    public String applicantProfile(HttpSession session, Model model, @PathVariable int userId, @PathVariable int applicantId){
        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);

        model.addAttribute("data", docs);

        PersonalDetails personal = pRepo.findAllByUserId(userId);
        userDocs user = fRepo.getAllByPersonalDetails(personal);

        model.addAttribute("education", eduRepo.findAllByPersonalDetails(personal));
        model.addAttribute("experience", exRepo.findAllByPersonalDetails(personal));

        model.addAttribute("applicantId", applicantId);
        model.addAttribute("user", user);


        return "applicantProfile.html";

    }

    @GetMapping("{applicantId}/approveApplicant/{userId}")
    public String approveApplicant(HttpSession session, Model model, @PathVariable int userId,
                                   @ModelAttribute MailStructure mailStructure, @PathVariable int applicantId){
        PersonalDetails personal = pRepo.findAllByUserId(userId);
        userDocs userDocs = fRepo.getAllByPersonalDetails(personal);

        JobDetails jobDetails = jdRepo.findByUserDocs(userDocs);
        Applicant applicant = appRepo.findByUserDocsAndApplicantId(userDocs, applicantId);
        applicant.setStatus("Approved");
        appRepo.save(applicant);

        mailService.sendApprovalEmail(personal.getEmail(), personal.getName(), applicant.getJobDetails().getTitle(), applicant.getJobDetails().getCompany().getCompanyName());

//        mailService.sendMail(personal.getEmail(), mailStructure);
//        mailService.sendMail("anee.neu15@gmail.com", mailStructure);

        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        model.addAttribute("data", docs);

        // Retrieve job details for the company
        List<JobDetails> jobDetailsList = jdRepo.findAllByCompany(company);

        // Create a map to hold job details and associated applicants
        List<Applicant> selectedApplicants = new ArrayList<>();

        // Iterate through each job posted by the company
        for (JobDetails job : jobDetailsList) {
            // Retrieve applicants for the current job
            List<Applicant> applicantsForJob = appRepo.findByJobDetails(job);

            // Add applicants to the list of selected applicants
            selectedApplicants.addAll(applicantsForJob);
        }

        List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);




        // Add unique job locations to the model
        model.addAttribute("status", "Applicant has been approved successfully");
        model.addAttribute("uniqueJob", uniqueJob);
        model.addAttribute("applicants", selectedApplicants);
        model.addAttribute("jobTitle", "All jobs");

        return "companylanding.html";
    }

    @GetMapping("{applicantId}/declineApplicant/{userId}")
    public String declineApplicant(HttpSession session, Model model, @PathVariable int userId,
                                   @ModelAttribute MailStructure mailStructure, @PathVariable int applicantId){
        PersonalDetails personal = pRepo.findAllByUserId(userId);
        userDocs userDocs = fRepo.getAllByPersonalDetails(personal);

        JobDetails jobDetails = jdRepo.findByUserDocs(userDocs);
        Applicant applicant = appRepo.findByUserDocsAndApplicantId(userDocs, applicantId);
        applicant.setStatus("Declined");

        mailService.sendRejectionEmail(personal.getEmail(), personal.getName(), applicant.getJobDetails().getTitle(), applicant.getJobDetails().getCompany().getCompanyName());

        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        model.addAttribute("data", docs);

        // Retrieve job details for the company
        List<JobDetails> jobDetailsList = jdRepo.findAllByCompany(company);

        // Create a map to hold job details and associated applicants
        List<Applicant> selectedApplicants = new ArrayList<>();

        // Iterate through each job posted by the company
        for (JobDetails job : jobDetailsList) {
            // Retrieve applicants with status "pending" for the current job
            List<Applicant> applicantsForJob = appRepo.findByJobDetailsAndStatus(job, "pending");

            // Add applicants to the list of selected applicants
            selectedApplicants.addAll(applicantsForJob);
        }

        List<String> uniqueJob = jdRepo.findDistinctTitlesByCompanyAndActiveOrInactiveStatus(company);




        // Add unique job locations to the model
        model.addAttribute("error", "Applicant has been declined successfully");
        model.addAttribute("uniqueJob", uniqueJob);
        model.addAttribute("applicants", selectedApplicants);
        model.addAttribute("jobTitle", "All jobs");

        return "companylanding.html";
    }

    @GetMapping("/seeCompanyProfile")
    public String seeCompanyProfile(HttpSession session, Model model){
        Company company = (Company) session.getAttribute("activeCompany");
        CompanyDocs docs = docsRepo.getCompanyDocsByCompany(company);
        model.addAttribute("data", docs);
        model.addAttribute("companyList", docs);

        return "companyProfile.html";
    }
}
