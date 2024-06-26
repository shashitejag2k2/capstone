package com.hv.jobhunt.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import com.hv.jobhunt.services.JobSeekerService;
import org.mockito.junit.MockitoJUnitRunner;
@ExtendWith(MockitoExtension.class)
class JobSeekerControllerTest {

	  	@Mock
	    private JobSeekerService jobSeekerService;

	    @InjectMocks
	    private JobSeekerController JobSeekerController;

	    @Test
	    public void jobSeekerRegister_Success() {
	        
	        
	        JobSeeker jobSeeker = new JobSeeker();
	        jobSeeker.setName("shashi teja");
	        jobSeeker.setEmailId("shashi@email.com");
	        jobSeeker.setPassword("password123");
	        jobSeeker.setCollegeName("Gitam University");
	        jobSeeker.setExperience("2 years");
	        jobSeeker.setSkills("Java, Spring, Hibernate");
	        

	        Mockito.doNothing().when(jobSeekerService).register(jobSeeker);

	        // Act
	        ResponseEntity<String> responseEntity = JobSeekerController.jobSeekerRegister(jobSeeker);

	        // Assert
	        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	        assertEquals("New JobSeeker Added Successfully", responseEntity.getBody());
	    }
	    @Test
	    public void jobSeekerRegister_Failure() {
	        // Arrange
	       
	        JobSeeker jobSeeker = new JobSeeker();
	        jobSeeker.setName("shashi teja");
	        jobSeeker.setEmailId("shashi@email.com");
	        jobSeeker.setPassword("password123");
	        jobSeeker.setCollegeName("Gitam University");
	        jobSeeker.setExperience("2 years");
	        jobSeeker.setSkills("Java, Spring, Hibernate");
	        // Set jobSeeker properties as needed for a failed registration

	        Mockito.doThrow(new RuntimeException("Failed to register job seeker")).when(jobSeekerService).register(jobSeeker);

	        // Act
	        ResponseEntity<String> responseEntity = JobSeekerController.jobSeekerRegister(jobSeeker);

	        // Assert
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	        assertEquals("Failed to add JobSeeker: Failed to register job seeker", responseEntity.getBody());
	    }
	    
	    @Test
	    public void jobSeekerLogin_Success() {
	        // Arrange
	        JobSeeker jobSeeker = new JobSeeker();
	        jobSeeker.setEmailId("shashi@email.com");
	        jobSeeker.setPassword("password");

	        Mockito.when(jobSeekerService.login(jobSeeker)).thenReturn("User login successful");

	        // Act
	        ResponseEntity<String> responseEntity = JobSeekerController.jobSeekerLogin(jobSeeker);

	        // Assert
	        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	        assertEquals("User login successful", responseEntity.getBody());
	    }
	    @Test
	    public void jobSeekerLogin_Unauthorized() {
	        // Arrange
	        JobSeeker jobSeeker = new JobSeeker();
	        jobSeeker.setEmailId("example@gmail.com");
	        jobSeeker.setPassword("password");
	        // Set jobSeeker properties as needed for an unauthorized login

	        Mockito.when(jobSeekerService.login(jobSeeker)).thenReturn("Invalid username or password");

	        // Act
	        ResponseEntity<String> responseEntity = JobSeekerController.jobSeekerLogin(jobSeeker);

	        // Assert
	        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	        assertEquals("Invalid username or password", responseEntity.getBody());
	    }

	    @Test
	    public void testGetAllJobsAppliedByJobSeekerSuccess() {
	        // Setup: create some applied jobs objects for a job seeker with email "john.doe@example.com"
	        AppliedJobs jobApplied1 = new AppliedJobs();
	        jobApplied1.setAppliedBy("shashi@email.com");
	        jobApplied1.setPostedBy("Gitam Company");
	        jobApplied1.setJobTitle("Software Engineer");
	        jobApplied1.setStatus("Applied");
	        jobApplied1.setJobId(1);

	        AppliedJobs jobApplied2 = new AppliedJobs();
	        jobApplied2.setAppliedBy("shashi@email.com");
	        jobApplied2.setPostedBy("Gitam Company");
	        jobApplied2.setJobTitle("Product Manager");
	        jobApplied2.setStatus("Applied");
	        jobApplied2.setJobId(2);

	        List<AppliedJobs> expectedJobs = Arrays.asList(jobApplied1, jobApplied2);

	        
	        when(jobSeekerService.getJobAppliedDetails("shashi@email.com")).thenReturn(expectedJobs);

	        
	        ResponseEntity<List<AppliedJobs>> result = JobSeekerController.getAllJobsAppliedByJobSeeker("shashi@email.com");

	        
	        assertEquals(expectedJobs, result.getBody());
	        assertEquals(HttpStatus.OK, result.getStatusCode());
	    }
	    @Test
	    public void testGetAllJobsAppliedByJobSeekerException() {
	      
	        when(jobSeekerService.getJobAppliedDetails("shashi@email.com")).thenThrow(new RuntimeException("Failed to retrieve jobs applied by job seeker"));

	        
	        try {
	        	JobSeekerController.getAllJobsAppliedByJobSeeker("shashi@email.com");
	            fail("Expected an exception to be thrown");
	        } catch (RuntimeException e) {
	            
	            assertEquals("Failed to retrieve jobs applied by job seeker: Failed to retrieve jobs applied by job seeker", e.getMessage());
	        }
	    }


	    @Test
	    public void testApplyJob_Success() {
	        // Mocking
	        AppliedJobs jobApplication = new AppliedJobs();
	        jobApplication.setAppliedBy("user@example.com");
	        jobApplication.setJobId(123);

	        when(jobSeekerService.applyJob(any(AppliedJobs.class))).thenReturn("Job application saved successfully");

	        
	        ResponseEntity<String> responseEntity = JobSeekerController.applyJob(jobApplication);

	      
	        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	        assertEquals("Job application saved successfully", responseEntity.getBody());
	    }

	    @Test
	    public void testApplyJob_Failure() {
	       
	        AppliedJobs jobApplication = new AppliedJobs();
	        jobApplication.setAppliedBy("user@example.com");
	        jobApplication.setJobId(123);

	        when(jobSeekerService.applyJob(any(AppliedJobs.class))).thenReturn("Failed to save job application: Some error occurred");

	       
	        ResponseEntity<String> responseEntity = JobSeekerController.applyJob(jobApplication);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	        assertEquals("Failed to save job application: Some error occurred", responseEntity.getBody());
	    }
	    
	    @Test
	    public void testUpdateProfileSuccess() {
	        
	        JobSeeker jobSeeker = new JobSeeker();
	        when(jobSeekerService.updateProfile(jobSeeker)).thenReturn("Profile Updated successfully");

	        
	        ResponseEntity<String> responseEntity = JobSeekerController.updateProfile(jobSeeker);

	       
	        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

	        
	        assertEquals("Profile Updated successfully", responseEntity.getBody());
	    }
	    
	    @Test
	    public void testUpdateProfileFailure() {
	        
	        JobSeeker jobSeeker = new JobSeeker();

	       
	        when(jobSeekerService.updateProfile(jobSeeker)).thenThrow(new RuntimeException("Error updating profile"));

	       
	        ResponseEntity<String> responseEntity = JobSeekerController.updateProfile(jobSeeker);

	        
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

	        
	        assertEquals("Failed to Update Profile: Error updating profile", responseEntity.getBody());
	    }
	    
	    @Test
	    public void testUpdateProfileJobSeekerNotFound() {
	       
	        JobSeeker jobSeeker = new JobSeeker();

	        
	        when(jobSeekerService.updateProfile(jobSeeker)).thenReturn("Job seeker with ID " + jobSeeker.getJobSeekerId() + " not found");

	        
	        ResponseEntity<String> responseEntity = JobSeekerController.updateProfile(jobSeeker);

	       
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	        
	        assertEquals("Job seeker with ID " + jobSeeker.getJobSeekerId() + " not found", responseEntity.getBody());
	    }
	    
	    @Test
	    public void testSearchJobListings_HappyPath() throws Exception {
	        
	        String keyword = "java developer";
	        List<JobListing> expectedResults = new ArrayList<>();
	        JobListing jobListing1 = new JobListing();
	        jobListing1.setJobId(1);
	        jobListing1.setJobTitle("Java Developer");
	        
	        jobListing1.setEmployeeType("Full-time");
	        jobListing1.setJobDescription("Java developer job description");
	        jobListing1.setKeySkills("Java, Spring, Hibernate");
	        jobListing1.setMinimumWorkExperience(2);
	        jobListing1.setMaximumWorkExperience(5);
	        jobListing1.setLocation("New York");
	        jobListing1.setJobMode("On-site");
	        jobListing1.setEducationalQualification("Bachelor's degree");
	        jobListing1.setCompanyName("Hitachi Corporation");
	        jobListing1.setMinimumSalary(80000);
	        jobListing1.setMaximumSalary(120000);
	        jobListing1.setPostedBy("John Doe");
	        jobListing1.setJobApplicationStatus("Open");

	        JobListing jobListing2 = new JobListing();
	        jobListing2.setJobId(2);
	        jobListing2.setJobTitle("Senior Java Developer");
	        
	        jobListing2.setEmployeeType("Full-time");
	        jobListing2.setJobDescription("Senior Java developer job description");
	        jobListing2.setKeySkills("Java, Spring, Hibernate, Cloud");
	        jobListing2.setMinimumWorkExperience(5);
	        jobListing2.setMaximumWorkExperience(10);
	        jobListing2.setLocation("San Francisco");
	        jobListing2.setJobMode("Remote");
	        jobListing2.setEducationalQualification("Master's degree");
	        jobListing2.setCompanyName("Hitachi Corporation");
	        jobListing2.setMinimumSalary(120000);
	        jobListing2.setMaximumSalary(180000);
	        jobListing2.setPostedBy("Jane Doe");
	        jobListing2.setJobApplicationStatus("Open");

	        expectedResults.add(jobListing1);
	        expectedResults.add(jobListing2);

	        when(jobSeekerService.searchJobListings(keyword)).thenReturn(expectedResults);

	        
	        ResponseEntity<List<JobListing>> response = JobSeekerController.searchJobListings(keyword);

	        
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(expectedResults, response.getBody());
	    }
	    
	    @Test
	    public void testSearchJobListings_ServiceThrowsException() throws Exception {
	       
	        String keyword = "java developer";
	        when(jobSeekerService.searchJobListings(keyword)).thenThrow(new RuntimeException("Error searching for job listings"));

	        
	        ResponseEntity<List<JobListing>> response = JobSeekerController.searchJobListings(keyword);

	        
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	        assertEquals(null, response.getBody());
	    }
	    
	    @Test
	    public void testSearchJobListings_NoResults() throws Exception {
	        
	        String keyword = "java developer";
	        List<JobListing> expectedResults = new ArrayList<>();

	        when(jobSeekerService.searchJobListings(keyword)).thenReturn(expectedResults);

	        
	        ResponseEntity<List<JobListing>> response = JobSeekerController.searchJobListings(keyword);

	        
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(expectedResults, response.getBody());
	    }
	    @Test
	    public void testGetProfile_HappyPath() throws Exception {
	       
	        String email = "shashi@email.com";
	        JobSeeker expectedProfile = new JobSeeker();
	        expectedProfile.setJobSeekerId(1);
	        expectedProfile.setName("shashi");
	        expectedProfile.setEmailId(email);
	        expectedProfile.setPassword("password123");
	        expectedProfile.setCollegeName("CMR");
	        expectedProfile.setExperience("2 years");
	        expectedProfile.setSkills("Java, Spring, Hibernate");

	        when(jobSeekerService.getJobSeekerDetails(email)).thenReturn(expectedProfile);

	        ResponseEntity<JobSeeker> response = JobSeekerController.getProfile(email);

	        
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(expectedProfile, response.getBody());
	    }
	    @Test
	    public void testGetProfile_NotFound() throws Exception {
	        
	        String email = "shashi.@email.com";
	        when(jobSeekerService.getJobSeekerDetails(email)).thenReturn(null);

	        
	        ResponseEntity<JobSeeker> response = JobSeekerController.getProfile(email);

	      
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(null, response.getBody());
	    }
	    @Test
	    public void testGetProfile_ServiceThrowsException() throws Exception {
	       
	        String email = "shashi.teja@email.com";
	        when(jobSeekerService.getJobSeekerDetails(email)).thenThrow(new RuntimeException("Error getting job seeker details"));

	        
	        ResponseEntity<JobSeeker> response = JobSeekerController.getProfile(email);

	        
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	        assertEquals(null, response.getBody());
	    }
	    
	    @Test
	    public void testGetAllJobs() throws Exception {
	        
	        List<JobListing> expectedJobs = new ArrayList<>();
	        JobListing jobListing1 = new JobListing();
	        jobListing1.setJobId(1);
	        jobListing1.setJobTitle("Java Developer");
	        
	        jobListing1.setEmployeeType("Full-time");
	        jobListing1.setJobDescription("Java developer job description");
	        jobListing1.setKeySkills("Java, Spring, Hibernate");
	        jobListing1.setMinimumWorkExperience(2);
	        jobListing1.setMaximumWorkExperience(5);
	        jobListing1.setLocation("New York");
	        jobListing1.setJobMode("On-site");
	        jobListing1.setEducationalQualification("Bachelor's degree");
	        jobListing1.setCompanyName("Hitachi Corporation");
	        jobListing1.setMinimumSalary(80000);
	        jobListing1.setMaximumSalary(120000);
	        jobListing1.setPostedBy("John Doe");
	        jobListing1.setJobApplicationStatus("Open");

	        JobListing jobListing2 = new JobListing();
	        jobListing2.setJobId(2);
	        jobListing2.setJobTitle("Senior Java Developer");
	       
	        jobListing2.setEmployeeType("Full-time");
	        jobListing2.setJobDescription("Senior Java developer job description");
	        jobListing2.setKeySkills("Java, Spring, Hibernate, Cloud");
	        jobListing2.setMinimumWorkExperience(5);
	        jobListing2.setMaximumWorkExperience(10);
	        jobListing2.setLocation("San Francisco");
	        jobListing2.setJobMode("Remote");
	        jobListing2.setEducationalQualification("Master's degree");
	        jobListing2.setCompanyName("Hitachi Corporation");
	        jobListing2.setMinimumSalary(120000);
	        jobListing2.setMaximumSalary(180000);
	        jobListing2.setPostedBy("Jane Doe");
	        jobListing2.setJobApplicationStatus("Open");

	        expectedJobs.add(jobListing1);
	        expectedJobs.add(jobListing2);

	        when(jobSeekerService.getjobs()).thenReturn(expectedJobs);

	     
	        ResponseEntity<Iterable<JobListing>> response = JobSeekerController.getAllJobs();

	      
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(expectedJobs, response.getBody());
	    }
	    
	    @Test
	    public void testGetAllJobs_ServiceThrowsException() throws Exception {
	        
	        when(jobSeekerService.getjobs()).thenThrow(new RuntimeException("Error getting jobs"));

	      
	        ResponseEntity<Iterable<JobListing>> response = JobSeekerController.getAllJobs();

	        
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	        assertEquals(null, response.getBody());
	    }


	    
	    


}
