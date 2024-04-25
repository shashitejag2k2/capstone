package com.hv.jobhunt.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.repository.JobAppliedRepository;
import com.hv.jobhunt.repository.JobRepository;
import com.hv.jobhunt.services.EmployeerService;

@ExtendWith(MockitoExtension.class)
class EmployeerControllerTest {
	
	 	@Mock
	    private EmployeerService employerService;
	 	
	 	@Mock
	 	private JobAppliedRepository jobAppliedRepository;
	 	
	 	@Mock
		private JobRepository jobListingRepository;

	    @InjectMocks
	    private EmployeerController employeerController;

	 @Test
	    public void testLoginSuccess() {
	        when(employerService.login(any(), any())).thenReturn("Login successful");
	        
	        Employeer validEmployeer = new Employeer();
	        validEmployeer.setEmailId("valid@example.com");
	        validEmployeer.setPassword("validpassword");
	        ResponseEntity<Object> response = employeerController.login(validEmployeer);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals("Login successful", response.getBody());
	    }

	    @Test
	    public void testLoginFailure() {
	       when(employerService.login(any(), any())).thenThrow(new RuntimeException("Invalid credentials"));
	       Employeer invalidEmployeer = new Employeer();
	        invalidEmployeer.setEmailId("invalid@example.com");
	        invalidEmployeer.setPassword("invalidpassword");
	        ResponseEntity<Object> response = employeerController.login(invalidEmployeer);
	        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	        assertEquals("Invalid email or password", response.getBody());
	    }
	    
	    @Test
	    public void testGetAllJobsSuccess() {
	       List<JobListing> mockJobs = new ArrayList<>();
	        mockJobs.add(new JobListing(/* provide necessary parameters */));
	        when(employerService.getjobs(any())).thenReturn(mockJobs);
	        ResponseEntity<List<JobListing>> response = employeerController.getAppliedData("employeer@example.com");
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(mockJobs, response.getBody());
	    }

	    @Test
	    public void testGetAllJobsFailure() {
	        when(employerService.getjobs(any())).thenThrow(new RuntimeException("Failed to retrieve job listings"));
	        ResponseEntity<List<JobListing>> response = employeerController.getAppliedData("employeer@example.com");
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	        assertEquals(0, response.getBody().size()); // Ensure the list is empty
	    }
	    
	    @Test
	    public void testGetJobSeekersSuccess() {
	        // Mocking the behavior of jobAppliedRepository.findByPostedBy() method to return a list of applied jobs
	        List<AppliedJobs> mockAppliedJobs = new ArrayList<>();
	        // Add sample applied jobs to the list
	        mockAppliedJobs.add(new AppliedJobs(/* provide necessary parameters */));
	        // Add more applied jobs if needed

	        // Specify the behavior of the mock repository method
	        when(jobAppliedRepository.findByPostedBy(any())).thenReturn(mockAppliedJobs);

	        // Call the service method
	        List<AppliedJobs> result = employerService.getjobSeekers("employeer@example.com");

	        // Verify the result
	        assertEquals(mockAppliedJobs, result);
	    }

	    @Test
	    public void testGetJobSeekersFailure() {
	        when(jobAppliedRepository.findByPostedBy(any())).thenThrow(new RuntimeException("Failed to retrieve job seekers"));
	        List<AppliedJobs> result = employerService.getjobSeekers("employeer@example.com");
	        assertEquals(0, result.size());
	    }

}