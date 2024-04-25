package com.hv.jobhunt.controllers;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import com.hv.jobhunt.repository.JobAppliedRepository;
import com.hv.jobhunt.services.JobSeekerService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class JobSeekerController {
	 @Autowired
	    private JobSeekerService jobSeekerService;
	 
	 
	 
	 
	 @PostMapping("/jobSeekerRegister")
	    public ResponseEntity<String> jobSeekerRegister(@RequestBody JobSeeker jobSeeker) {
	        try {
	        	jobSeekerService.register(jobSeeker);
	            return new ResponseEntity<>("New JobSeeker Added Successfully", HttpStatus.CREATED);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Failed to add JobSeeker: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 
	 @PostMapping("/jobSeekerLogin")
	 public ResponseEntity<String> jobSeekerLogin(@RequestBody JobSeeker jobSeeker) {
		 try {
		        String loginResult = jobSeekerService.login(jobSeeker);
		        if (loginResult.equals("User login successful")) {
		            return ResponseEntity.ok().body(loginResult);
		        } else {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResult);
		        }
		        
		    } catch (IllegalArgumentException e) {
		        return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
		    }
	 }
	
	@PostMapping("/applyJob")
    public ResponseEntity<String> applyJob(@RequestBody AppliedJobs jobApplication) {
        try {
        	System.out.println(jobApplication.getStatus());
        	
        	String result=jobSeekerService.applyJob(jobApplication);
        	if (result.startsWith("Failed")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
            
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create job application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	 @GetMapping("/getAlljobs")//for jobseekers screen
	    public ResponseEntity<Iterable<JobListing>> getAllJobs() {
		 try {
		        List<JobListing> getJobs = jobSeekerService.getjobs();
		        return ResponseEntity.ok().body(getJobs);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		    }	             
	    }
	 
	 @GetMapping("/getIndividualJob")
	    public ResponseEntity<Iterable<JobListing>> getIndividualJob(@RequestParam("jobId") int jobId) {
		 
			JobListing Job = jobSeekerService.getIndividualJob(jobId);
			System.out.println(Job.getCompanyName());
			return new ResponseEntity<>(List.of(Job), HttpStatus.OK);
	    }
	 
	 @PutMapping("/updateProfile")
	    public ResponseEntity<String> updateProfile(@RequestBody JobSeeker jobSeeker) {
		 try {
		        String result = jobSeekerService.updateProfile(jobSeeker);
		        if (result.equals("Profile Updated successfully")) {
		            return ResponseEntity.status(HttpStatus.CREATED).body(result);
		        } else {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job seeker with ID " + jobSeeker.getJobSeekerId() + " not found");
		        }
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Update Profile: " + e.getMessage());
		    }
	    }
	 
	 @GetMapping("/getAllJobsAppliedByJobSeeker")//returns the jobs which are applied by jobseeker
	    public List<AppliedJobs> getAllJobsAppliedByJobSeeker(@RequestParam("email") String email) {
		 List<AppliedJobs> getJobAppliedDetails = jobSeekerService.getJobAppliedDetails(email);
			return getJobAppliedDetails;
	    }
	 
	 @GetMapping("/searchJobListings")
	    public ResponseEntity<List<JobListing>> searchJobListings(@RequestParam("keyword") String keyword) {
	        // Invoke the service method to search for job listings
		 try {
		        // Invoke the service method to search for job listings
		        List<JobListing> searchResults = jobSeekerService.searchJobListings(keyword);
		        return ResponseEntity.ok().body(searchResults);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		    }
	    }
	 
	 @GetMapping("/getProfile")
	    public ResponseEntity<JobSeeker> getProfile(@RequestParam("email") String email) {
	        // Invoke the service method to search for job listings
		 try {
		        // Invoke the service method to search for job seeker details
		        JobSeeker profileDetails = jobSeekerService.getJobSeekerDetails(email);
		        if (profileDetails != null) {
		            return ResponseEntity.ok().body(profileDetails);
		        } else {
		            return ResponseEntity.notFound().build();
		        }
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		    }
	    }
	 @GetMapping("/getParse")
	    public ResponseEntity<String> getParse() {
	        // Invoke the service method to search for job listings
	        String profileDetails = jobSeekerService.parseResume();
	        return new ResponseEntity<>(profileDetails, HttpStatus.CREATED);
	    }
	 
//	 @PostMapping("/parse-resume")
//	    public ResponseEntity<String> parseResume(@RequestParam("file") MultipartFile file) {
//	        try {
//	            String parsedContent = jobSeekerService.parseResume(file);
//	            return new ResponseEntity<>(parsedContent, HttpStatus.OK);
//	        } catch (IOException e) {
//	            return new ResponseEntity<>("Error parsing resume: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//	        }
//	    }
	 
//	 @GetMapping("/getJobSeekerIndividualApplication")//returns the jobs which are applied by jobseeker
//	    public List<AppliedJobs> getAllJobsAppliedByJobSeeker(@RequestParam("appliedBy") String appliedBy) {
//		 
//			List<AppliedJobs> getJobAppliedDetails = jobSeekerService.getJobAppliedDetails(appliedBy);
//			return getJobAppliedDetails;
//	    }
}
