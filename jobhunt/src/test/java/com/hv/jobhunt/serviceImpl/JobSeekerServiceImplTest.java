package com.hv.jobhunt.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import com.hv.jobhunt.repository.JobAppliedRepository;
import com.hv.jobhunt.repository.JobRepository;
import com.hv.jobhunt.repository.JobSeekerRepo;

@ExtendWith(MockitoExtension.class)
class JobSeekerServiceImplTest {

	@Mock
	private JobAppliedRepository jobAppliedRepository;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private JobSeekerRepo jobSeekerRepo;

	@InjectMocks
	private JobSeekerServiceImpl jobSeekerServiceImpl;

	@Test
	public void register_Success() {

		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setName("John Doe");
		jobSeeker.setEmailId("john.doe@example.com");
		jobSeeker.setPassword("password123");
		jobSeeker.setCollegeName("ABC University");
		jobSeeker.setExperience("2 years");
		jobSeeker.setSkills("Java, Spring, Hibernate");

		Mockito.doNothing().when(jobSeekerRepo).save(jobSeeker);

		String result = jobSeekerServiceImpl.register(jobSeeker);

		assertEquals("Successfully saved JobSeeker", result);
	}

	@Test
	public void register_Exception() {

		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setName("John Doe");
		jobSeeker.setEmailId("john.doe@example.com");
		jobSeeker.setPassword("password123");
		jobSeeker.setCollegeName("ABC University");
		jobSeeker.setExperience("2 years");
		jobSeeker.setSkills("Java, Spring, Hibernate");

		Mockito.doThrow(new RuntimeException("Unexpected error")).when(jobSeekerRepo).save(jobSeeker);

		String result = jobSeekerServiceImpl.register(jobSeeker);

		assertEquals("Failed to register due to an unexpected error: Unexpected error", result);
	}

	@Test
	public void login_Success() {

		String email = "test@example.com";
		String password = "password123";
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setEmailId(email);
		jobSeeker.setPassword(password);

		Mockito.when(jobSeekerRepo.findByEmailIdAndPassword(email, password)).thenReturn(jobSeeker);

		String result = jobSeekerServiceImpl.login(jobSeeker);

		assertEquals("User login successful", result);
	}

	@Test
	public void login_InvalidCredentials() {

		String email = "test@example.com";
		String password = "password123";
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setEmailId(email);
		jobSeeker.setPassword(password);

		Mockito.when(jobSeekerRepo.findByEmailIdAndPassword(email, password)).thenReturn(null);

		String result = jobSeekerServiceImpl.login(jobSeeker);

		assertEquals("Invalid credentials", result);
	}

	@Test
	void testApplyJob_Success() {

		AppliedJobs jobApplication = new AppliedJobs();
		jobApplication.setAppliedBy("user@example.com");
		jobApplication.setJobId(123);

		when(jobAppliedRepository.findByAppliedByAndJobId("user@example.com", 123)).thenReturn(null);

		String result = jobSeekerServiceImpl.applyJob(jobApplication);

		assertEquals("Job application saved successfully", result);

		verify(jobAppliedRepository, times(1)).save(jobApplication);
	}

	@Test
	void testApplyJob_AlreadyApplied() {

		AppliedJobs jobApplication = new AppliedJobs();
		jobApplication.setAppliedBy("user@example.com");
		jobApplication.setJobId(123);
		jobApplication.setPostedBy("employer@example.com");
		jobApplication.setJobTitle("Software Developer");
		jobApplication.setStatus("pending");

		when(jobAppliedRepository.findByAppliedByAndJobId("user@example.com", 123)).thenReturn(jobApplication);

		String result = jobSeekerServiceImpl.applyJob(jobApplication);

		assertEquals("You have already applied for this job", result);

		verify(jobAppliedRepository, never()).save(any());
	}

	@Test
	void testApplyJob_Exception() {

		AppliedJobs jobApplication = new AppliedJobs();
		jobApplication.setAppliedBy("user@example.com");
		jobApplication.setJobId(123);
		jobApplication.setPostedBy("employer@example.com");
		jobApplication.setJobTitle("Software Developer");
		jobApplication.setStatus("pending");

		when(jobAppliedRepository.findByAppliedByAndJobId("user@example.com", 123))
				.thenThrow(new RuntimeException("Test exception"));

		String result = jobSeekerServiceImpl.applyJob(jobApplication);

		assertEquals("Failed to save job application due to an unexpected error", result);

		verify(jobAppliedRepository, never()).save(any());
	}

	@Test
	public void testGetJobs_ReturnsNonEmptyList() {

		JobListing jobListing1 = new JobListing();
		jobListing1.setJobId(1);
		jobListing1.setJobTitle("Software Engineer");
		jobListing1.setEmployeeType("Full-time");
		jobListing1.setLocation("New York");
		jobListing1.setCompanyName("ABC Company");
		jobListing1.setMinimumSalary(80000.0);
		jobListing1.setMaximumSalary(120000.0);
		jobListing1.setPostedBy("John Doe");

		JobListing jobListing2 = new JobListing();
		jobListing2.setJobId(2);
		jobListing2.setJobTitle("Product Manager");
		jobListing2.setEmployeeType("Full-time");
		jobListing2.setLocation("San Francisco");
		jobListing2.setCompanyName("XYZ Company");
		jobListing2.setMinimumSalary(100000.0);
		jobListing2.setMaximumSalary(150000.0);
		jobListing2.setPostedBy("Jane Smith");

		List<JobListing> expectedJobListings = Arrays.asList(jobListing1, jobListing2);

		when(jobRepository.findAll()).thenReturn(expectedJobListings);
		List<JobListing> actualJobListings = jobSeekerServiceImpl.getjobs();

		assertEquals(expectedJobListings, actualJobListings);

	}

	@Test
	 public void testGetJobs_ReturnsEmptyList() {
	     
	     when(jobRepository.findAll()).thenReturn(Collections.emptyList());

	     List<JobListing> actualJobListings = jobSeekerServiceImpl.getjobs();

	     assertTrue(actualJobListings.isEmpty());
	 }

	@Test
	 public void testGetJobs_ThrowsRuntimeException() {
	    
	     when(jobRepository.findAll()).thenThrow(new RuntimeException("Database error"));

	     
	     assertThrows(RuntimeException.class, () -> jobSeekerServiceImpl.getjobs());
	 }

	@Test
	public void testUpdateProfile_JobSeekerIdExists_ProfileUpdated_ReturnsSuccessMessage() {

		int jobSeekerId = 1;
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setJobSeekerId(jobSeekerId);
		jobSeeker.setName("John Doe");
		jobSeeker.setEmailId("john.doe@example.com");
		jobSeeker.setPassword("password");
		jobSeeker.setCollegeName("ABC University");
		jobSeeker.setExperience("3 years");
		jobSeeker.setSkills("Java, Python, SQL");

		when(jobSeekerRepo.findByJobSeekerId(jobSeekerId)).thenReturn(jobSeeker);

		String result = jobSeekerServiceImpl.updateProfile(jobSeeker);

		assertEquals("Profile Updated successfully", result);
		verify(jobSeekerRepo).save(jobSeeker);
	}

	@Test
	public void testUpdateProfile_JobSeekerIdDoesNotExist_ReturnsFailureMessage() {

		int jobSeekerId = 999;
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setJobSeekerId(jobSeekerId);
		jobSeeker.setName("John Doe");
		jobSeeker.setEmailId("john.doe@example.com");
		jobSeeker.setPassword("password");
		jobSeeker.setCollegeName("ABC University");
		jobSeeker.setExperience("3 years");
		jobSeeker.setSkills("Java, Python, SQL");
		when(jobSeekerRepo.findByJobSeekerId(jobSeekerId)).thenReturn(null);

		String result = jobSeekerServiceImpl.updateProfile(jobSeeker);

		assertEquals("Not saved", result);
	}

	@Test
	public void testUpdateProfile_DatabaseError_ThrowsRuntimeException() {

		int jobSeekerId = 1;
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setJobSeekerId(jobSeekerId);
		jobSeeker.setName("John Doe");
		jobSeeker.setEmailId("john.doe@example.com");
		jobSeeker.setPassword("password");
		jobSeeker.setCollegeName("ABC University");
		jobSeeker.setExperience("3 years");
		jobSeeker.setSkills("Java, Python, SQL");
		when(jobSeekerRepo.findByJobSeekerId(jobSeekerId)).thenReturn(jobSeeker);
		doThrow(new RuntimeException("Database error")).when(jobSeekerRepo).save(jobSeeker);

		assertThrows(RuntimeException.class, () -> jobSeekerServiceImpl.updateProfile(jobSeeker));
	}

	@Test
	public void testGetJobAppliedDetails_UserHasApplications_ReturnsNonEmptyList() {

		String appliedBy = "john.doe@example.com";

		AppliedJobs appliedJob1 = new AppliedJobs();

		appliedJob1.setJobId(1);
		appliedJob1.setJobTitle("Software Engineer");
		appliedJob1.setPostedBy("ABC Company");
		appliedJob1.setStatus("Pending");
		appliedJob1.setAppliedBy(appliedBy);

		AppliedJobs appliedJob2 = new AppliedJobs();

		appliedJob2.setJobId(2);
		appliedJob2.setJobTitle("Product Manager");
		appliedJob2.setPostedBy("XYZ Company");
		appliedJob2.setStatus("Rejected");
		appliedJob2.setAppliedBy(appliedBy);
		List<AppliedJobs> expectedApplications = Arrays.asList(appliedJob1, appliedJob2);

		when(jobAppliedRepository.findByAppliedBy(appliedBy)).thenReturn(expectedApplications);

		List<AppliedJobs> actualApplications = jobSeekerServiceImpl.getJobAppliedDetails(appliedBy);

		assertEquals(expectedApplications, actualApplications);
	}

	@Test
	public void testGetJobAppliedDetails_UserHasNoApplications_ReturnsEmptyList() {

		String appliedBy = "jane.doe@example.com";
		when(jobAppliedRepository.findByAppliedBy(appliedBy)).thenReturn(Collections.emptyList());

		List<AppliedJobs> actualApplications = jobSeekerServiceImpl.getJobAppliedDetails(appliedBy);

		assertTrue(actualApplications.isEmpty());
	}

	@Test
	public void testGetJobAppliedDetails_DatabaseError_ThrowsRuntimeException() {

		String appliedBy = "john.doe@example.com";
		when(jobAppliedRepository.findByAppliedBy(appliedBy)).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> jobSeekerServiceImpl.getJobAppliedDetails(appliedBy));
	}

	@Test
	public void testSearchJobListings_KeywordMatchesJobTitle_ReturnsNonEmptyList() {

		String keyword = "Software Engineer";
		JobListing jobListing1 = new JobListing();
		jobListing1.setJobId(1);
		jobListing1.setJobTitle("Software Engineer");
		jobListing1.setEmployeeType("Full-time");
		jobListing1.setLocation("New York");
		jobListing1.setCompanyName("ABC Company");
		jobListing1.setMinimumSalary(80000.0);
		jobListing1.setMaximumSalary(120000.0);
		jobListing1.setPostedBy("John Doe");

		JobListing jobListing2 = new JobListing();
		jobListing2.setJobId(2);
		jobListing2.setJobTitle("Software Engineer Intern");
		jobListing2.setEmployeeType("Part-time");
		jobListing2.setLocation("San Francisco");
		jobListing2.setCompanyName("XYZ Company");
		jobListing2.setMinimumSalary(50000.0);
		jobListing2.setMaximumSalary(60000.0);
		jobListing2.setPostedBy("Jane Smith");
		List<JobListing> expectedJobListings = Arrays.asList(jobListing1, jobListing2);
		when(jobRepository.findByJobTitleContainingIgnoreCase(keyword)).thenReturn(expectedJobListings);

		List<JobListing> actualJobListings = jobSeekerServiceImpl.searchJobListings(keyword);

		assertEquals(expectedJobListings, actualJobListings);
	}

	@Test
	public void testSearchJobListings_KeywordDoesNotMatchAnyJobTitle_ReturnsEmptyList() {

		String keyword = "Marketing";
		when(jobRepository.findByJobTitleContainingIgnoreCase(keyword)).thenReturn(Collections.emptyList());

		List<JobListing> actualJobListings = jobSeekerServiceImpl.searchJobListings(keyword);

		assertTrue(actualJobListings.isEmpty());
	}

	@Test
	public void testSearchJobListings_DatabaseError_ThrowsRuntimeException() {

		String keyword = "Software Engineer";
		when(jobRepository.findByJobTitleContainingIgnoreCase(keyword))
				.thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> jobSeekerServiceImpl.searchJobListings(keyword));
	}

	@Test
	public void testGetJobSeekerDetails_EmailIdExists_ReturnsJobSeeker() {

		String email = "john.doe@example.com";

		JobSeeker expectedJobSeeker = new JobSeeker();
		expectedJobSeeker.setJobSeekerId(123);
		expectedJobSeeker.setName("John Doe");
		expectedJobSeeker.setEmailId(email);
		expectedJobSeeker.setPassword("password");
		expectedJobSeeker.setCollegeName("ABC University");
		expectedJobSeeker.setExperience("3 years");
		expectedJobSeeker.setSkills("Java, Python, SQL");
		when(jobSeekerRepo.findByEmailId(email)).thenReturn(expectedJobSeeker);

		JobSeeker actualJobSeeker = jobSeekerServiceImpl.getJobSeekerDetails(email);

		assertEquals(expectedJobSeeker, actualJobSeeker);
	}

	@Test
	public void testGetJobSeekerDetails_EmailIdDoesNotExist_ReturnsNull() {

		String email = "nonexistent@example.com";
		when(jobSeekerRepo.findByEmailId(email)).thenReturn(null);

		JobSeeker actualJobSeeker = jobSeekerServiceImpl.getJobSeekerDetails(email);

		assertNull(actualJobSeeker);
	}

}
