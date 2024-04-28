package com.hv.jobhunt.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.mail.internet.MimeMessage;
import javax.xml.bind.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import com.hv.jobhunt.Models.Subscription;
import com.hv.jobhunt.configure.EmailMarkerConfig;
import com.hv.jobhunt.repository.EmployeerRepo;
import com.hv.jobhunt.repository.JobAppliedRepository;
import com.hv.jobhunt.repository.JobRepository;
import com.hv.jobhunt.repository.JobSeekerRepo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Template;

@ExtendWith(MockitoExtension.class)
class EmployeerServiceImplTest {
	
	 @Qualifier("emailConfigBean")
	 @Mock
	 private Configuration emailConfig;

	@Mock
    private JobRepository jobRepository;
	
	  @Mock
	    private JavaMailSender javaMailSender;
	
	@Mock
	private EmployeerRepo employeerRepo;
	
	@Mock
	private JobAppliedRepository jobAppliedRepository;

    @InjectMocks
    private EmployeerServiceImpl employeerServiceImpl;
    
    @Mock
    private JobSeekerRepo jobSeekerRepo;
    
    @Mock
    private EmailMarkerConfig emailMarkerConfig;
    
    @Mock
    private Subscription subscriptionRepo;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void register_Success() {
        // Arrange
        Employeer employee = new Employeer();
        employee.setEmailId("test@example.com");
        employee.setEmployeeId(123);
        employee.setName("John Doe");
       employee.setPassword("password123");
        employee.setCompanyName("ABC Company");
        employee.setSubscriptionType("Premium");
        employee.setSubscriptionExprirationDate(new Date()); // Assuming you have a Date object
        employee.setStatus("Active");

        // Mock the behavior of employeerRepo.existsByEmailId to return false
        Mockito.when(employeerRepo.existsByEmailId("test@example.com")).thenReturn(false);

        // Act
        String result = employeerServiceImpl.register(employee);

        // Assert
        assertEquals("Successfully registered", result);
        Mockito.verify(employeerRepo, Mockito.times(1)).save(employee);
    }
    @Test
    public void register_AlreadyRegistered() {
        // Arrange
        Employeer employee = new Employeer();
        employee.setEmailId("test@example.com");
        employee.setEmployeeId(123);
        employee.setName("John Doe");
       employee.setPassword("password123");
        employee.setCompanyName("ABC Company");
        employee.setSubscriptionType("Premium");
        employee.setSubscriptionExprirationDate(new Date()); // Assuming you have a Date object
        employee.setStatus("Active");

        // Mock the behavior of employeerRepo.existsByEmailId to return true
        Mockito.when(employeerRepo.existsByEmailId("test@example.com")).thenReturn(true);

        // Act
        String result = employeerServiceImpl.register(employee);

        // Assert
        assertEquals("You have already registered. Please try to log in.", result);
        // Verify that employeerRepo.save is not called
        Mockito.verify(employeerRepo, Mockito.never()).save(employee);
    }
    @Test
    public void register_Failure() {
        // Arrange
        Employeer employee = new Employeer();
        employee.setEmailId("test@example.com");

        // Mock the behavior of employeerRepo.existsByEmailId to throw an exception
        Mockito.when(employeerRepo.existsByEmailId("test@example.com")).thenThrow(new RuntimeException());

        // Act
        String result = employeerServiceImpl.register(employee);

        // Assert
        assertEquals("Failed to register due to an unexpected error.", result);
        // Verify that employeerRepo.save is not called
        Mockito.verify(employeerRepo, Mockito.never()).save(employee);
    }
    
    @Test
    public void login_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        Employeer mockEmployeer = new Employeer();
        mockEmployeer.setEmailId(email);
        mockEmployeer.setPassword(password);
        mockEmployeer.setStatus("active");

        Mockito.when(employeerRepo.findByEmailIdAndPassword(email, password)).thenReturn(mockEmployeer);

        // Act
        Employeer result = employeerServiceImpl.login(email, password);

        // Assert
        assertEquals(mockEmployeer, result);
    }

    @Test
    public void login_WrongCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        Employeer mockEmployeer = new Employeer();
        mockEmployeer.setEmailId(email);
        mockEmployeer.setPassword("differentpassword"); // Different password

        Mockito.when(employeerRepo.findByEmailIdAndPassword(email, password)).thenReturn(mockEmployeer);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        	employeerServiceImpl.login(email, password);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }


    
    @Test
    public void testPostJobSuccess() {
        JobListing jobListing = new JobListing();
        jobListing.setJobTitle("Software Engineer");
        
        jobListing.setEmployeeType("Full-time");
        jobListing.setJobDescription("Design, develop, and maintain software systems.");
        jobListing.setKeySkills("Java, Spring, Hibernate, SQL");
        jobListing.setMinimumWorkExperience(2);
        jobListing.setMaximumWorkExperience(5);
        jobListing.setLocation("New York");
        jobListing.setJobMode("On-site");
        jobListing.setEducationalQualification("Bachelor's degree in Computer Science");
        jobListing.setCompanyName("ABC Company");
        jobListing.setMinimumSalary(80000.0);
        jobListing.setMaximumSalary(120000.0);
        jobListing.setPostedBy("John Doe");
        jobListing.setJobApplicationStatus("open");

        when(jobRepository.save(jobListing)).thenReturn(jobListing);

        String result = employeerServiceImpl.postJob(jobListing);

        assertEquals("Job Posted", result);
        verify(jobRepository, times(1)).save(jobListing);
    }
    
    @Test
    public void testPostJobNullObject() {
        assertThrows(NullPointerException.class, () -> employeerServiceImpl.postJob(null));
}
    
    @Test
    public void testPostJobInvalidJobTitle() {
        JobListing jobListing = new JobListing();
        jobListing.setJobTitle(null);
       
        jobListing.setEmployeeType("Full-time");
        jobListing.setJobDescription("Design, develop, and maintain software systems.");
        jobListing.setKeySkills("Java, Spring, Hibernate, SQL");
        jobListing.setMinimumWorkExperience(2);
        jobListing.setMaximumWorkExperience(5);
        jobListing.setLocation("New York");
        jobListing.setJobMode("On-site");
        jobListing.setEducationalQualification("Bachelor's degree in Computer Science");
        jobListing.setCompanyName("ABC Company");
        jobListing.setMinimumSalary(80000.0);
        jobListing.setMaximumSalary(120000.0);
        jobListing.setPostedBy("John Doe");
        jobListing.setJobApplicationStatus("open");

        assertThrows(ValidationException.class, () -> employeerServiceImpl.postJob(jobListing));}
    
    @Test
    public void testGetJobs_ByPostedBy() {
        // Create a job listing with a postedBy email
        JobListing jobListing = new JobListing();
        jobListing.setJobTitle("Test Job");
        jobListing.setPostedBy("test@example.com");
        jobRepository.save(jobListing);

        // Call the getjobs method
        List<JobListing> jobListings = employeerServiceImpl.getjobs("test@example.com");

        // Assert that the job listing is returned
        assertNotNull(jobListings);
        assertEquals(1, jobListings.size());
        assertEquals("Test Job", jobListings.get(0).getJobTitle());
    }
    
    @Test
    public void testGetJobs_NoJobsFound() {
    	String email = "nonexistent@example.com";
        // Call the getjobs method with an email that doesn't have any job listings
        List<JobListing> jobListings = employeerServiceImpl.getjobs(email);

        // Assert that an empty list is returned
        assertNotNull(jobListings);
        assertEquals(0, jobListings.size());
    }
    @Test
    public void testGetJobs_MultipleJobsFound() {
        // Create multiple job listings with the same postedBy email
        JobListing jobListing1 = new JobListing();
        jobListing1.setJobTitle("Test Job 1");
        jobListing1.setPostedBy("test@example.com");
        jobRepository.save(jobListing1);

        JobListing jobListing2 = new JobListing();
        jobListing2.setJobTitle("Test Job 2");
        jobListing2.setPostedBy("test@example.com");
        jobRepository.save(jobListing2);

        // Call the getjobs method
        List<JobListing> jobListings = employeerServiceImpl.getjobs("test@example.com");

        // Assert that both job listings are returned
        assertNotNull(jobListings);
        assertEquals(2, jobListings.size());
        assertEquals("Test Job 1", jobListings.get(0).getJobTitle());
        assertEquals("Test Job 2", jobListings.get(1).getJobTitle());
    }
    
    @Test
    void testGetJobSeekersSuccess() {
        String email = "test@example.com";
        List<AppliedJobs> appliedJobs = new ArrayList<>();
        when(jobAppliedRepository.findByPostedBy(email)).thenReturn(appliedJobs);

        List<AppliedJobs> result = employeerServiceImpl.getjobSeekers(email);

        assertEquals(appliedJobs, result);
        verify(jobAppliedRepository, times(1)).findByPostedBy(email);
    }
    
    @Test
    void testGetJobSeekersFailure() {
        String email = "test@example.com";
        when(jobAppliedRepository.findByPostedBy(email)).thenThrow(new RuntimeException("Error retrieving job seekers"));

        assertThrows(RuntimeException.class, () -> employeerServiceImpl.getjobSeekers(email));
        verify(jobAppliedRepository, times(1)).findByPostedBy(email);
    }
    
    @Test
    void testUpdateStatusSuccess() {
        String appliedBy = "test@example.com";
        int jobId = 1;
        String newStatus = "APPROVED";

        AppliedJobs existingJobApplied = new AppliedJobs();
        existingJobApplied.setAppliedBy(appliedBy);
        existingJobApplied.setJobId(jobId);
        existingJobApplied.setStatus("PENDING");

        when(jobAppliedRepository.findByAppliedByAndJobId(appliedBy, jobId))
        .thenAnswer(invocation -> Optional.of(existingJobApplied));

        	
        assertThrows(RuntimeException.class, () -> employeerServiceImpl.updateStatus(existingJobApplied));

// Verifying that findByAppliedByAndJobId is called once with the correct arguments
        verify(jobAppliedRepository, times(1)).findByAppliedByAndJobId(appliedBy, jobId);

// Verifying that save is never called
verify(jobAppliedRepository, never()).save(existingJobApplied);
    }

    @Test
    void testUpdateStatusFailure() {
        String appliedBy = "test@example.com";
        int jobId = 1;
        String newStatus = "APPROVED";

        AppliedJobs existingJobApplied = new AppliedJobs();
        existingJobApplied.setAppliedBy(appliedBy);
        existingJobApplied.setJobId(jobId);
        existingJobApplied.setStatus("PENDING");

        when(jobAppliedRepository.findByAppliedByAndJobId(appliedBy, jobId)).thenAnswer(invocation -> Optional.empty());

        assertThrows(RuntimeException.class, () -> employeerServiceImpl.updateStatus(existingJobApplied));
        verify(jobAppliedRepository, times(1)).findByAppliedByAndJobId(appliedBy, jobId);
        verify(jobAppliedRepository, never()).save(existingJobApplied);
    }
    
    @Test
    public void testDeleteJob_Success() {
        // Setup: Create a mock JobListing object
        JobListing job = new JobListing();
        job.setJobId(1);
        job.setJobTitle("Software Engineer");

        // Mock the repository's findByjobId method to return the mock JobListing object
        Mockito.when(jobRepository.findByjobId(1)).thenReturn(job);

        // Test: Call the deleteJob method with the job ID
        String result = employeerServiceImpl.deleteJob(1);

        // Verify: Check if the job listing is deleted and the appropriate message is returned
        Mockito.verify(jobRepository).deleteByjobId(1);
        assertEquals("Record Deleted", result);
    }
    
//    @Test
//    void testDeleteJobNotFound() {
//        int jobId = 1;
//
//        Optional<JobListing> optionalJob = Optional.empty();
//        when(jobRepository.findByjobId(jobId)).thenReturn(null);
//
//        assertThrows(IllegalArgumentException.class, () -> employeerServiceImpl.deleteJob(jobId));
//        verify(jobRepository, times(1)).findByjobId(jobId);
//        verify(jobRepository, never()).deleteByjobId(jobId);
//    }
    @Test
    public void testDeleteJobNotFound() {
        // Mock the jobListingRepositor to return null for the job ID
        when(jobRepository.findByjobId(2)).thenReturn(null);

        // Test: call the deleteJob method with the job ID
        String result = employeerServiceImpl.deleteJob(2);

        // Verify: an appropriate error message should be returned
        assertEquals("Job with ID 2 does not exist or its title is null", result);
    }
    
    @Test
    void testUpdateJobSuccess() {
        // Prepare test data
        JobListing jobListing = new JobListing();
        jobListing.setJobId(1);
        jobListing.setCompanyName("Test Company");
        // Mock the behavior of findByjobId to return the jobListing when job exists
        when(jobRepository.findByjobId(1)).thenReturn(jobListing);

        // Call the updateJob method
        when(jobRepository.findByjobId(1)).thenReturn(jobListing);

        // Call the updateJob method
        employeerServiceImpl.updateJob(jobListing);

        // Verify that the jobListing is updated and saved
        verify(jobRepository, times(1)).save(jobListing);
    }
    
    @Test
    void testUpdateJobNotFound() {
        // Prepare test data
        JobListing jobListing = new JobListing();
        jobListing.setJobId(1);

        // Mock the behavior of findByjobId to return null when job does not exist
        when(jobRepository.findByjobId(1)).thenReturn(null);

        // Call the updateJob method and assert that it throws an IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeerServiceImpl.updateJob(jobListing));

        // Verify that the correct exception message is thrown
        assertEquals("Job with jobId 1 not found", exception.getMessage());

        // Verify that save method is never called
        verify(jobRepository, never()).save(jobListing);
    }
    
    @Test
    void testUpdateJobApplicationStatusSuccess() {
        // Prepare test data
        int jobId = 1;
        JobListing job = new JobListing();
        job.setJobId(jobId);
        job.setJobApplicationStatus("open");

        // Mock the behavior of findByjobId to return the job when job exists
        when(jobRepository.findByjobId(jobId)).thenReturn(job);

        // Call the updateJobApplicationStatus method
        String result = employeerServiceImpl.updateJobApplicationStatus(jobId);

        // Verify that the job application status is updated and saved
        assertEquals("Job Application Updated", result);
        assertEquals("closed", job.getJobApplicationStatus());
        verify(jobRepository, times(1)).save(job);
    }
    @Test
    void testUpdateJobApplicationStatusJobNotFound() {
        // Prepare test data
        int jobId = 1;

        // Mock the behavior of findByjobId to return null when job does not exist
        when(jobRepository.findByjobId(jobId)).thenReturn(null);

        // Call the updateJobApplicationStatus method and assert that it throws NoSuchElementException
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> employeerServiceImpl.updateJobApplicationStatus(jobId));

        // Verify that the correct exception message is thrown
        assertEquals("Job not found with ID: " + jobId, exception.getMessage());

        // Verify that save method is never called
        verify(jobRepository, never()).save(any());
    }
    @Test
    void testGetDatailsSuccess() {
        int jobId = 1;
        String employeerMail = "test@example.com";

        AppliedJobs appliedJob1 = new AppliedJobs();
        appliedJob1.setJobId(jobId);
        appliedJob1.setAppliedBy("test1@example.com");

        AppliedJobs appliedJob2 = new AppliedJobs();
        appliedJob2.setJobId(jobId);
        appliedJob2.setAppliedBy("test2@example.com");

        List<AppliedJobs> appliedTableDetails = Arrays.asList(appliedJob1, appliedJob2);

        when(jobAppliedRepository.findByJobId(jobId)).thenReturn(appliedTableDetails);

        JobSeeker jobSeeker1 = new JobSeeker();
        jobSeeker1.setEmailId("test1@example.com");
        jobSeeker1.setName("Test User 1");

        JobSeeker jobSeeker2 = new JobSeeker();
        jobSeeker2.setEmailId("test2@example.com");
        jobSeeker2.setName("Test User 2");

        List<JobSeeker> jobSeekers = Arrays.asList(jobSeeker1, jobSeeker2);

        when(jobSeekerRepo.findByEmailId(appliedJob1.getAppliedBy())).thenReturn(jobSeeker1);
        when(jobSeekerRepo.findByEmailId(appliedJob2.getAppliedBy())).thenReturn(jobSeeker2);

        List<JobSeeker> result = employeerServiceImpl.getDatails(jobId, employeerMail);

        assertEquals(jobSeekers, result);
        verify(jobAppliedRepository, times(1)).findByJobId(jobId);
        verify(jobSeekerRepo, times(2)).findByEmailId(anyString());
    }
    
    @Test
    void testGetDatailsJobSeekerNotFound() {
        int jobId = 1;
        String employeerMail = "test@example.com";

        AppliedJobs appliedJob1 = new AppliedJobs();
        appliedJob1.setJobId(jobId);
        appliedJob1.setAppliedBy("test1@example.com");

        List<AppliedJobs> appliedTableDetails = Arrays.asList(appliedJob1);

        when(jobAppliedRepository.findByJobId(jobId)).thenReturn(appliedTableDetails);

        when(jobSeekerRepo.findByEmailId(appliedJob1.getAppliedBy())).thenReturn(null);

        List<JobSeeker> result = employeerServiceImpl.getDatails(jobId, employeerMail);

        assertTrue(result.isEmpty());
        verify(jobAppliedRepository, times(1)).findByJobId(jobId);
        verify(jobSeekerRepo, times(1)).findByEmailId(appliedJob1.getAppliedBy());
    }
    
    @Test
    void testUpdateEmployerSubscriptionSuccess() {
        String email = "test@example.com";
        String newSubscriptionType = "Test Subscription";

        // Create a mock Employeer object returned by the repository
        Employeer originalEmployeer = new Employeer();
        originalEmployeer.setEmailId(email);
        originalEmployeer.setSubscriptionType("Original Subscription");

        // Create the updated Employeer object
        Employeer updatedEmployeer = new Employeer();
        updatedEmployeer.setEmailId(email);
        updatedEmployeer.setSubscriptionType(newSubscriptionType);

        // Mocking the repository method to return the original Employeer
        when(employeerRepo.findByEmailId(email)).thenAnswer(invocation ->Optional.of(originalEmployeer));

        // Calling the service method
        String result = employeerServiceImpl.updateemployerSubscription(updatedEmployeer);

        // Assertions
        assertEquals("Successfully updated subscription", result);
        assertEquals(newSubscriptionType, originalEmployeer.getSubscriptionType());

        // Verify that the updated Employeer object is saved
        verify(employeerRepo, times(1)).findByEmailId(email);
        verify(employeerRepo, times(1)).save(originalEmployeer);
    }
    
    @Test
    void testUpdateemployerSubscriptionNotFound() {
        String email = "test@example.com";
        Employeer employee = new Employeer();
        employee.setEmailId(email);
        employee.setSubscriptionType("Test Subscription");

        when(employeerRepo.findByEmailId(email)).thenAnswer(invocation ->Optional.empty());

        String result = employeerServiceImpl.updateemployerSubscription(employee);

        assertEquals("Employee with email " + email + " not found", result);
        verify(employeerRepo, times(1)).findByEmailId(email);
        verify(employeerRepo, never()).save(any(Employeer.class));
    }
    @Test
    void testUpdateemployerSubscriptionException() {
        String email = "test@example.com";
        Employeer employee = new Employeer();
        employee.setEmailId(email);
        employee.setSubscriptionType("Test Subscription");

        Employeer emp = new Employeer();
        emp.setEmailId(email);
        emp.setSubscriptionType("Original Subscription");

        Optional<Employeer> optionalEmp = Optional.of(emp);
        when(employeerRepo.findByEmailId(email)).thenAnswer(invocation -> optionalEmp);
        doThrow(new RuntimeException("Test exception")).when(employeerRepo).save(emp);

        String result = employeerServiceImpl.updateemployerSubscription(employee);

        assertEquals("Failed to update subscription: Test exception", result);
        verify(employeerRepo, times(1)).findByEmailId(email);
        verify(employeerRepo, times(1)).save(emp);
    }
    
    @Test
    void testGetCountSuccess() {
        String employeerMail = "test@example.com";
        List<JobListing> jobs = new ArrayList<>();
        jobs.add(new JobListing());
        jobs.add(new JobListing());
        Employeer employeerDetails = new Employeer();
        employeerDetails.setEmailId(employeerMail);
        employeerDetails.setSubscriptionExprirationDate(new Date());

        when(jobRepository.findByPostedBy(employeerMail)).thenReturn(jobs);
        when(employeerRepo.findByEmailId(employeerMail)).thenReturn(employeerDetails);

        Map<String, String> result = employeerServiceImpl.getCount(employeerMail);

        assertEquals(2, Integer.parseInt(result.get("jobCount")));
        assertNotNull(result.get("SubscriptionExprirationDate"));
        verify(jobRepository, times(1)).findByPostedBy(employeerMail);
        verify(employeerRepo, times(1)).findByEmailId(employeerMail);
    }
    @Test
    void testGetCountNoJobs() {
        String employeerMail = "test@example.com";
        List<JobListing> jobs = new ArrayList<>();
        Employeer employeerDetails = new Employeer();
        employeerDetails.setEmailId(employeerMail);
        employeerDetails.setSubscriptionExprirationDate(new Date());

        when(jobRepository.findByPostedBy(employeerMail)).thenReturn(jobs);
        when(employeerRepo.findByEmailId(employeerMail)).thenReturn(employeerDetails);

        Map<String, String> result = employeerServiceImpl.getCount(employeerMail);

        assertEquals(0, Integer.parseInt(result.get("jobCount")));
        assertNotNull(result.get("SubscriptionExprirationDate"));
        verify(jobRepository, times(1)).findByPostedBy(employeerMail);
        verify(employeerRepo, times(1)).findByEmailId(employeerMail);
    }
    
    @Test
    void testGetCountEmployeerNotFound() {
            String employeerMail = "test@example.com";
            Employeer employeerDetails = null;

            when(employeerRepo.findByEmailId(employeerMail)).thenReturn(employeerDetails);

            assertThrows(IllegalArgumentException.class, () -> employeerServiceImpl.getCount(employeerMail));
            verify(employeerRepo, times(1)).findByEmailId(employeerMail);
        }
    
    @Test
    void testGetSubscriptionType_EmployerFound() {
        // Test case for employer found scenario

        String employeerMail = "test@example.com";
        String subscriptionType = "Test Subscription";
        Date subscriptionExpiryDate = new Date(); // Mock subscription expiry date as needed

        // Mocking the behavior of employeerRepo.findByEmailId
        Employeer employeer = new Employeer();
        employeer.setSubscriptionType(subscriptionType);
        employeer.setSubscriptionExprirationDate(subscriptionExpiryDate);
        when(employeerRepo.findByEmailId(employeerMail)).thenReturn(employeer);

        // Calling the service method
        String result = employeerServiceImpl.getSubscriptionType(employeerMail);

        // Assertions
        assertEquals(subscriptionType, result);

        // Verify interactions
        verify(employeerRepo, times(1)).findByEmailId(employeerMail);
    }
    
    @Test
    void testGetSubscriptionType_EmployerNotFound() {
        // Test case for employer not found scenario

        String employeerMail = "test@example.com";

        // Mocking the behavior of employeerRepo.findByEmailId to return null
        when(employeerRepo.findByEmailId(employeerMail)).thenReturn(null);

        // Calling the service method
        String result = employeerServiceImpl.getSubscriptionType(employeerMail);

        // Assertions
        assertEquals("Employer with email " + employeerMail + " not found", result);

        // Verify interactions
        verify(employeerRepo, times(1)).findByEmailId(employeerMail);
    }
    
    @Test
    void testSendMail_Success() throws Exception {
        // Test case for successful mail sending

        String employeerEmailId = "employeer@example.com";
        String jobseekerMailId = "jobseeker@example.com";
        String username = "John Doe";
        String status = "approve";
        int jobId = 123;

        // Mocking the behavior of employeerRepo.findByEmailId
        Employeer employeer = new Employeer();
        employeer.setCompanyName("ABC Corp");
        when(employeerRepo.findByEmailId(employeerEmailId)).thenReturn(employeer);

        // Mocking the behavior of emailConfig.getTemplate
        Template template = mock(Template.class);
        when(emailConfig.getTemplate(status + ".html")).thenReturn(template);

        // Mocking the behavior of FreeMarkerTemplateUtils.processTemplateIntoString
        String emailContent = "Test email content";
        when(FreeMarkerTemplateUtils.processTemplateIntoString(template, any())).thenReturn(emailContent);

        // Calling the service method
        String result = employeerServiceImpl.sendmail(employeerEmailId, jobseekerMailId, username, status, jobId);

        // Assertions
        assertEquals("Mail sent successfully", result);

        // Verify interactions
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
    
    @Test
    void testSendMail_Exception() {
        // Test case for handling exception during mail sending

        String employeerEmailId = "employeer@example.com";
        String jobseekerMailId = "jobseeker@example.com";
        String username = "John Doe";
        String status = "approved";
        int jobId = 123;

        // Mocking the behavior of employeerRepo.findByEmailId
        when(employeerRepo.findByEmailId(employeerEmailId)).thenThrow(new RuntimeException("Test exception"));

        // Calling the service method
        String result = employeerServiceImpl.sendmail(employeerEmailId, jobseekerMailId, username, status, jobId);

        // Assertions
        assertEquals("Error while Sending Mail", result);

        // Verify interactions
        verifyNoInteractions(javaMailSender);
    }
    
//    @Test
//    public void testRemainingJobCountSuccess() {
//        // Setup: create a subscription object with a subscription type of "Premium" and 5 remaining jobs
//        Subscription subscription = new Subscription();
//        subscription.setSubscriptionType("Premium");
//        subscription.setNumberOfJobs(5);
//
//        // Mock the subscriptionRepo to return the subscription object when the findNoOfJobsBySubscriptionType method is called with the argument "Premium"
//        when(subscriptionRepo.findNoOfJobsBySubscriptionType("Premium")).thenReturn(subscription);
//
//        // Test: call the remainingJobCount method with the argument "Premium"
//        String result = subscriptionService.remainingJobCount("Premium");
//
//        // Verify: the remaining job count should be returned
//        assertEquals("5", result);
//    }
//    @Test
//    public void testRemainingJobCountException() {
//        // Mock the subscriptionRepo to throw an exception when the findNoOfJobsBySubscriptionType method is called with the argument "Standard"
//        when(subscriptionRepo.findNoOfJobsBySubscriptionType("Standard")).thenThrow(new RuntimeException("Failed to retrieve subscription"));
//
//        // Test: call the remainingJobCount method with the argument "Standard"
//        try {
//            subscriptionService.remainingJobCount("Standard");
//            fail("Expected an exception to be thrown");
//        } catch (RuntimeException e) {
//            // Verify: an appropriate error message should be returned
//            assertEquals("Failed to get remaining job count: Failed to retrieve subscription", e.getMessage());
//        }
//    }

    


    
    
    
    
}




    
    
    
    


