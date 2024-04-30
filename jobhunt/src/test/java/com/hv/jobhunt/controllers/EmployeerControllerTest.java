package com.hv.jobhunt.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
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
	public void login_Success() {

		Employeer employeer = new Employeer();
		employeer.setEmailId("valid@example.com");
		employeer.setPassword("validPassword");

		Employeer loginResult = new Employeer();

		Mockito.when(employerService.login("valid@example.com", "validPassword")).thenReturn(loginResult);

		ResponseEntity<Employeer> responseEntity = employeerController.login(employeer);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(loginResult, responseEntity.getBody());
	}

	@Test
	public void login_InvalidEmailOrPassword() {

		Employeer employeer = new Employeer();
		employeer.setEmailId("invalid@example.com");
		employeer.setPassword("invalidPassword");

		Mockito.when(employerService.login("invalid@example.com", "invalidPassword")).thenReturn(null);

		ResponseEntity<Employeer> responseEntity = employeerController.login(employeer);

		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}

	@Test
	void register_Success() {

		Employeer employeer = new Employeer();
		employeer.setEmployeeId(123);
		employeer.setName("shashi teja");
		employeer.setEmailId("shashi@email.com");
		employeer.setPassword("password123");
		employeer.setCompanyName("Hitachi Company");
		employeer.setSubscriptionType("Premium");

		employeer.setStatus("Active");
		when(employerService.register(employeer)).thenReturn("Successfully registered");

		ResponseEntity<String> response = employeerController.register(employeer);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Successfully registered", response.getBody());
		verify(employerService, times(1)).register(employeer);
	}

	@Test
	void register_Failure_InternalServerError() {

		Employeer employeer = new Employeer();
		employeer.setEmployeeId(123);
		employeer.setName("Shashi teja");
		employeer.setEmailId("shashi@email.com");
		employeer.setPassword("password123");
		employeer.setCompanyName("Hitachi Company");
		employeer.setSubscriptionType("Premium");

		employeer.setStatus("Active");
		when(employerService.register(employeer)).thenThrow(new RuntimeException("Internal Server Error"));

		ResponseEntity<String> response = employeerController.register(employeer);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Failed to add Employeer: Internal Server Error", response.getBody());
		verify(employerService, times(1)).register(employeer);
	}

	@Test
	public void addJob_Success() {
		// Arrange
		JobListing job = new JobListing();
		job.setJobTitle("Software Engineer");
		job.setEmployeeType("Full-time");
		job.setJobDescription("Develop and maintain software applications.");
		job.setKeySkills("Java, Spring, Hibernate");
		job.setMinimumWorkExperience(2);
		job.setMaximumWorkExperience(5);
		job.setLocation("New York");
		job.setJobMode("Remote");
		job.setEducationalQualification("Bachelor's degree in Computer Science");
		job.setCompanyName("Hitachi Company");
		job.setMinimumSalary(50000.0);
		job.setMaximumSalary(80000.0);
		job.setPostedBy("John Doe");
		job.setJobApplicationStatus("Open");
		Mockito.doNothing().when(employerService).postJob(job);

		ResponseEntity<String> responseEntity = employeerController.addJob(job);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Job added successfully", responseEntity.getBody());
	}

	@Test
	public void addJob_Failure_InternalServerError() {

		JobListing job = new JobListing();
		job.setJobTitle("Software Engineer");
		job.setEmployeeType("Full-time");
		job.setJobDescription("Develop and maintain software applications.");
		job.setKeySkills("Java, Spring, Hibernate");
		job.setMinimumWorkExperience(2);
		job.setMaximumWorkExperience(5);
		job.setLocation("New York");
		job.setJobMode("Remote");
		job.setEducationalQualification("Bachelor's degree in Computer Science");
		job.setCompanyName("Hitachi Company");
		job.setMinimumSalary(50000.0);
		job.setMaximumSalary(80000.0);
		job.setPostedBy("John Doe");
		job.setJobApplicationStatus("Open");
		Mockito.doThrow(new RuntimeException("Internal Server Error")).when(employerService).postJob(job);

		ResponseEntity<String> responseEntity = employeerController.addJob(job);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals("Failed to add job: Internal Server Error", responseEntity.getBody());
	}

	@Test
	public void testGetAllJobsSuccess() {
		List<JobListing> mockJobs = new ArrayList<>();
		mockJobs.add(new JobListing());
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

		List<AppliedJobs> mockAppliedJobs = new ArrayList<>();

		mockAppliedJobs.add(new AppliedJobs());

		when(jobAppliedRepository.findByPostedBy(any())).thenReturn(mockAppliedJobs);

		List<AppliedJobs> result = employerService.getjobSeekers("employeer@example.com");

		assertEquals(mockAppliedJobs, result);
	}

	@Test
	    public void testGetJobSeekersFailure() {
	        when(jobAppliedRepository.findByPostedBy(any())).thenThrow(new RuntimeException("Failed to retrieve job seekers"));
	        List<AppliedJobs> result = employerService.getjobSeekers("employeer@example.com");
	        assertEquals(0, result.size());
	    }

	@Test
	public void testDeleteJobSuccess() {
		int jobId = 1;
		when(employerService.deleteJob(jobId)).thenReturn("Job deleted successfully");

		ResponseEntity<String> response = employeerController.deleteJob(jobId);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Successfully Deleted Job", response.getBody());

		verify(employerService, times(1)).deleteJob(jobId);
	}

	@Test
	public void testDeleteJobFailure() {
		int jobId = -1;

		when(employerService.deleteJob(jobId)).thenReturn("Failed to delete job");

		ResponseEntity<String> response = employeerController.deleteJob(jobId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("Failed to Delete"));

		verify(employerService, times(1)).deleteJob(jobId);
	}

	@Test
	public void testUpdateJobSuccess() {
		JobListing jobListing = new JobListing();
		jobListing.setJobId(1);
		jobListing.setJobTitle("Software Engineer");
		jobListing.setJobDescription("Design and develop software applications.");

		when(employerService.updateJob(jobListing)).thenReturn("Job updated successfully");

		ResponseEntity<String> response = employeerController.updateJob(jobListing);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Successfully Updated Job", response.getBody());

		verify(employerService, times(1)).updateJob(jobListing);
	}

	@Test
	public void testUpdateJobFailure() {
		JobListing jobListing = new JobListing();
		jobListing.setJobId(0);
		jobListing.setJobTitle(null);
		jobListing.setJobDescription(null);

		when(employerService.updateJob(jobListing))
				.thenThrow(new IllegalArgumentException("Job with jobId 0 not found"));

		try {
			ResponseEntity<String> response = employeerController.updateJob(jobListing);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Job with jobId " + jobListing.getJobId() + "+ not found", e.getMessage());
		}

		verify(employerService, times(1)).updateJob(jobListing);
	}

	@Test
	public void testGetSeekersByJobSuccess() {
		JobSeeker jobSeeker1 = new JobSeeker();
		jobSeeker1.setJobSeekerId(1);
		jobSeeker1.setName("John Doe");
		jobSeeker1.setEmailId("john.doe@example.com");
		jobSeeker1.setPassword("password");
		jobSeeker1.setCollegeName("University of XYZ");
		jobSeeker1.setExperience("1 year");
		jobSeeker1.setSkills("Java, Python");

		JobSeeker jobSeeker2 = new JobSeeker();
		jobSeeker2.setJobSeekerId(2);
		jobSeeker2.setName("Jane Smith");
		jobSeeker2.setEmailId("jane.smith@example.com");
		jobSeeker2.setPassword("password");
		jobSeeker2.setCollegeName("University of ABC");
		jobSeeker2.setExperience("2 years");
		jobSeeker2.setSkills("JavaScript, React");

		List<JobSeeker> jobSeekers = new ArrayList<>();
		jobSeekers.add(jobSeeker1);
		jobSeekers.add(jobSeeker2);

		when(employerService.getDetails(1, "employeer@example.com")).thenReturn(jobSeekers);

		ResponseEntity<List<JobSeeker>> response = employeerController.getSeekersByJob(1, "employeer@example.com");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(jobSeekers, response.getBody());

		verify(employerService, times(1)).getDetails(1, "employeer@example.com");
	}

	@Test
	public void testGetSeekersByJobFailureInvalidJobId() {
		int jobId = 0;
		String employerEmail = "employer@example.com";

		when(employerService.getDetails(jobId, employerEmail))
				.thenThrow(new IllegalArgumentException("Invalid job ID"));

		try {
			ResponseEntity<List<JobSeeker>> response = employeerController.getSeekersByJob(jobId, employerEmail);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid job ID", e.getMessage());
		}

		verify(employerService, times(1)).getDetails(jobId, employerEmail);
	}

	@Test
	public void testGetSeekersByJobFailureInvalidEmployerEmail() {
		int jobId = 1;
		String employerEmail = null;

		when(employerService.getDetails(jobId, employerEmail))
				.thenThrow(new IllegalArgumentException("Invalid employer email"));

		try {
			ResponseEntity<List<JobSeeker>> response = employeerController.getSeekersByJob(jobId, employerEmail);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid employer email", e.getMessage());
		}

		verify(employerService, times(1)).getDetails(jobId, employerEmail);
	}

	@Test
	public void testGetSeekersByJobFailureInternalServerError() {
		int jobId = 1;
		String employerEmail = "employer@example.com";

		when(employerService.getDetails(jobId, employerEmail)).thenThrow(new RuntimeException("Internal server error"));

		try {
			ResponseEntity<List<JobSeeker>> response = employeerController.getSeekersByJob(jobId, employerEmail);
			fail("Expected RuntimeException to be thrown");
		} catch (RuntimeException e) {
			assertEquals("Internal server error", e.getMessage());
		}

		verify(employerService, times(1)).getDetails(jobId, employerEmail);
	}

	@Test
	public void testGetApplicationCountSuccess() {
		String employerEmail = "employer@example.com";

		Map<String, String> countMap = new HashMap<>();
		countMap.put("job1", "3");
		countMap.put("job2", "5");

		when(employerService.getCount(employerEmail)).thenReturn(countMap);

		ResponseEntity<Map<String, String>> response = employeerController.getApplicationCount(employerEmail);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(countMap, response.getBody());

		verify(employerService, times(1)).getCount(employerEmail);
	}

	@Test
	public void testGetApplicationCountFailureInvalidEmployerEmail() {
		String employerEmail = null;

		when(employerService.getCount(employerEmail)).thenThrow(new IllegalArgumentException("Invalid employer email"));

		try {
			ResponseEntity<Map<String, String>> response = employeerController.getApplicationCount(employerEmail);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid employer email", e.getMessage());
		}

		verify(employerService, times(1)).getCount(employerEmail);
	}

	@Test
	public void testGetApplicationCountFailureInternalServerError() {
		String employerEmail = "employer@example.com";

		when(employerService.getCount(employerEmail)).thenThrow(new RuntimeException("Internal server error"));

		try {
			ResponseEntity<Map<String, String>> response = employeerController.getApplicationCount(employerEmail);
			fail("Expected RuntimeException to be thrown");
		} catch (RuntimeException e) {
			assertEquals("Internal server error", e.getMessage());
		}

		verify(employerService, times(1)).getCount(employerEmail);
	}

	@Test
	public void testGetSubscriptionSuccess() {
		String employerEmail = "employer@example.com";
		String subscriptionType = "Premium";

		when(employerService.getSubscriptionType(employerEmail)).thenReturn(subscriptionType);

		ResponseEntity<String> response = employeerController.getSubscription(employerEmail);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(subscriptionType, response.getBody());

		verify(employerService, times(1)).getSubscriptionType(employerEmail);
	}

	@Test
	public void testGetSubscriptionFailureInvalidEmployerEmail() {
		String employerEmail = null;

		when(employerService.getSubscriptionType(employerEmail))
				.thenThrow(new IllegalArgumentException("Invalid employer email"));

		try {
			ResponseEntity<String> response = employeerController.getSubscription(employerEmail);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid employer email", e.getMessage());
		}

		verify(employerService, times(1)).getSubscriptionType(employerEmail);
	}

	@Test
	public void testGetSubscriptionFailureInternalServerError() {
		String employerEmail = "employer@example.com";

		when(employerService.getSubscriptionType(employerEmail))
				.thenThrow(new RuntimeException("Internal server error"));

		try {
			ResponseEntity<String> response = employeerController.getSubscription(employerEmail);
			fail("Expected RuntimeException to be thrown");
		} catch (RuntimeException e) {
			assertEquals("Internal server error", e.getMessage());
		}

		verify(employerService, times(1)).getSubscriptionType(employerEmail);
	}

	@Test
	public void testUpdateJobApplicationStatusSuccess() {
		int jobId = 1;

		when(employerService.updateJobApplicationStatus(jobId)).thenReturn("Successfully Updated Job");

		ResponseEntity<String> response = employeerController.updateJobApplicationStatus(jobId);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Successfully Updated Job", response.getBody());

		verify(employerService, times(1)).updateJobApplicationStatus(jobId);
	}

	@Test
	public void testUpdateJobApplicationStatusFailureNotFound() {
		int jobId = 1;

		when(employerService.updateJobApplicationStatus(jobId))
				.thenThrow(new NoSuchElementException("Job not found with ID: " + jobId));

		ResponseEntity<String> response = employeerController.updateJobApplicationStatus(jobId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Job not found with ID: " + jobId, response.getBody());

		verify(employerService, times(1)).updateJobApplicationStatus(jobId);
	}

	@Test
	public void testUpdateJobApplicationStatusFailureInternalServerError() {
		int jobId = 1;

		when(employerService.updateJobApplicationStatus(jobId))
				.thenThrow(new RuntimeException("Internal server error"));

		try {
			ResponseEntity<String> response = employeerController.updateJobApplicationStatus(jobId);
			fail("Expected RuntimeException to be thrown");
		} catch (RuntimeException e) {
			assertEquals("Failed to Update: Internal server error", e.getMessage());
		}

		verify(employerService, times(1)).updateJobApplicationStatus(jobId);
	}

	@Test
	public void testUpdateStatusSuccess() {
		AppliedJobs jobApplication = new AppliedJobs();
		jobApplication.setAppliedBy("johndoe@example.com");
		jobApplication.setStatus("Reviewing");
		jobApplication.setJobId(1);

		when(employerService.updateStatus(jobApplication)).thenReturn("Job application status updated successfully");

		ResponseEntity<String> response = employeerController.updateStatus(jobApplication);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Job application status updated successfully", response.getBody());

		verify(employerService, times(1)).updateStatus(jobApplication);
	}

	@Test
	public void testUpdateStatusFailureInternalServerError() {
		AppliedJobs jobApplication = new AppliedJobs();
		jobApplication.setAppliedBy("johndoe@example.com");
		jobApplication.setStatus("Reviewing");
		jobApplication.setJobId(1);

		when(employerService.updateStatus(jobApplication)).thenThrow(new RuntimeException("Internal server error"));

		try {
			ResponseEntity<String> response = employeerController.updateStatus(jobApplication);
			fail("Expected RuntimeException to be thrown");
		} catch (RuntimeException e) {
			assertEquals("Failed to updated: Internal server error", e.getMessage());
		}

		verify(employerService, times(1)).updateStatus(jobApplication);
	}

}
