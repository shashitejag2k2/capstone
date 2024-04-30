package com.hv.jobhunt.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import com.hv.jobhunt.Models.Subscription;

import com.hv.jobhunt.repository.EmployeerRepo;
import com.hv.jobhunt.repository.JobAppliedRepository;
import com.hv.jobhunt.repository.JobRepository;
import com.hv.jobhunt.repository.JobSeekerRepo;
import com.hv.jobhunt.repository.SubscriptionRepo;
import com.hv.jobhunt.services.EmployeerService;

//import jakarta.mail.internet.MimeMessage;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import javax.transaction.Transactional;

@Service
public class EmployeerServiceImpl implements EmployeerService {

	@Autowired
	private EmployeerRepo employeerRepo;

	@Autowired
	private JobAppliedRepository jobAppliedRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobSeekerRepo jobSeekerRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SubscriptionRepo subscriptionRepo;

	@Autowired
	@Qualifier("emailConfigBean")
	private Configuration emailConfig;

	@Override
	public String register(Employeer employee) {

		try {
			boolean exist = employeerRepo.existsByEmailId(employee.getEmailId());
			if (exist) {

				return "You have already registered. Please try to log in.";
			} else {
				employee.setStatus("pending");
				employee.setSubscriptionType("Basic");
				employeerRepo.save(employee);
				return "Successfully registered";
			}
		} catch (Exception e) {

			e.printStackTrace();
			return "Failed to register due to an unexpected error.";
		}

	}

	@Override
	public Employeer login(String emailId, String password) {
		try {
			if (emailId == null || password == null || emailId.isEmpty() || password.isEmpty()) {
				throw new IllegalArgumentException("Email and password cannot be empty");
			}
			Employeer employerUser = employeerRepo.findByEmailIdAndPassword(emailId, password);

			if (employerUser != null && employerUser.getEmailId().equals(emailId)
					&& employerUser.getPassword().equals(password)) {
				String status = employerUser.getStatus();
				if ("pending".equals(status)) {
					throw new RuntimeException("You are not onboarded yet");
				} else if ("reject".equals(status)) {
					throw new RuntimeException("Your request has been rejected by the admin");
				} else {
					return employerUser;
				}
			} else {
				throw new RuntimeException("Invalid email or password");
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("An unexpected error occurred while logging in");
		}

	}

	@Override
	public String postJob(JobListing postJob) {
		if (postJob == null) {
			return "Failed to post job due to invalid job listing.";
		}
		try {

			postJob.setJobApplicationStatus("open");
			jobRepository.save(postJob);
			return "Job Posted";
		} catch (Exception e) {

			e.printStackTrace();
			return "Failed to post job due to an unexpected error";
		}
	}

	@Override
	public List<JobListing> getjobs(String email) {
		try {
			return jobRepository.findByPostedBy(email);
		} catch (Exception e) {

			e.printStackTrace();

			return Collections.emptyList();
		}
	}

	@Override
	public List<AppliedJobs> getjobSeekers(String email) {
		try {
			return jobAppliedRepository.findByPostedBy(email);
		} catch (Exception e) {

			throw new RuntimeException("Error retrieving job seekers");
		}
	}

	@Override
	public String updateStatus(AppliedJobs jobApplication) {
		try {
			String appliedBy = jobApplication.getAppliedBy();
			int jobId = jobApplication.getJobId();

			AppliedJobs existingJobApplied = jobAppliedRepository.findByAppliedByAndJobId(appliedBy, jobId);

			existingJobApplied.setStatus(jobApplication.getStatus());

			jobAppliedRepository.save(existingJobApplied);
			return "Saved successfully";
		} catch (Exception e) {

			throw new RuntimeException("Failed to update: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public String deleteJob(int jobId) {

		try {
			JobListing job = jobRepository.findByjobId(jobId);
			if (job != null && job.getJobTitle() != null) {
				jobRepository.deleteByjobId(jobId);
				return "Record Deleted";
			} else {
				throw new IllegalArgumentException("Job with ID " + jobId + " does not exist or its title is null");
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete job: " + e.getMessage());
		}
	}

	@Override
	public String updateJob(JobListing jobListing) {
		int jobId = jobListing.getJobId();
		JobListing jobDetails = jobRepository.findByjobId(jobId);

		if (jobDetails != null) {
			jobDetails.setCompanyName(jobListing.getCompanyName());
			jobDetails.setEducationalQualification(jobListing.getEducationalQualification());
			jobDetails.setEmployeeType(jobListing.getEmployeeType());
			jobDetails.setJobDescription(jobListing.getJobDescription());
			jobDetails.setJobId(jobListing.getJobId());
			jobDetails.setJobMode(jobListing.getJobMode());

			jobDetails.setJobTitle(jobListing.getJobTitle());
			jobDetails.setKeySkills(jobListing.getKeySkills());
			jobDetails.setLocation(jobListing.getLocation());
			jobDetails.setMaximumSalary(jobListing.getMaximumSalary());
			jobDetails.setMaximumWorkExperience(jobListing.getMaximumWorkExperience());
			jobDetails.setMinimumSalary(jobListing.getMinimumSalary());
			jobDetails.setMaximumWorkExperience(jobListing.getMaximumWorkExperience());
			jobDetails.setPostedBy(jobListing.getPostedBy());
			jobRepository.save(jobDetails);
			return "updated Successfully";
		} else {
			throw new IllegalArgumentException("Job with jobId " + jobId + " not found");
		}

	}

	@Override
	public String updateJobApplicationStatus(int jobId) {
		try {
			JobListing job = jobRepository.findByjobId(jobId);
			if (job == null) {
				throw new NoSuchElementException("Job not found with ID: " + jobId);
			}
			if ("open".equals(job.getJobApplicationStatus())) {
				job.setJobApplicationStatus("closed");

			}
			jobRepository.save(job);
			return "Job Application Updated";
		} catch (DataAccessException e) {

			throw new RuntimeException("Failed to update job application status: " + e.getMessage());
		}
	}

	@Override
	public List<JobSeeker> getDetails(int jobId, String employeerMail) {// gets the job_id from applied table and fetch
																		// the emailids from which are applied for this
																		// job and take the email ids and search from
																		// the jobseeker table
		try {
			List<AppliedJobs> appliedTableDetails = jobAppliedRepository.findByJobId(jobId);

			List<String> emailIds = new ArrayList<>();
			for (AppliedJobs appliedJob : appliedTableDetails) {
				emailIds.add(appliedJob.getAppliedBy());
			}

			List<JobSeeker> jobSeekers = new ArrayList<>();
			for (String emailId : emailIds) {
				JobSeeker jobSeeker = jobSeekerRepo.findByEmailId(emailId);
				if (jobSeeker != null) {
					jobSeekers.add(jobSeeker);
				}
			}
			return jobSeekers;
		} catch (Exception e) {

			throw new RuntimeException("Failed to get job seekers by job: " + e.getMessage());
		}
	}

	@Override
	public String updateemployerSubscription(Employeer employee) {
		try {

			String email = employee.getEmailId();
			Employeer emp = employeerRepo.findByEmailId(email);

			if (emp == null) {
				return "Employee with email " + email + " not found";
			}

			emp.setSubscriptionType(employee.getSubscriptionType());
			employeerRepo.save(emp);

			return "Successfully updated subscription";
		} catch (RuntimeException e) {

			e.printStackTrace();
			return "Failed to update subscription: " + e.getMessage();
		}
	}

	@Override
	public Map<String, String> getCount(String employeerMail) {
		try {
			List<JobListing> jobs = getjobs(employeerMail);
			if (employeerMail == null) {
				throw new IllegalArgumentException("employer Mail not found");
			}
			Employeer employeerDetails = employeerRepo.findByEmailId(employeerMail);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			int jobCount = jobs.size();
			String dateStirng = Integer.toString(jobCount);
			Map<String, String> countMap = new HashMap<>();
			countMap.put("jobCount", dateStirng);
			return countMap;
		} catch (Exception e) {

			throw new RuntimeException("Failed to get application count: " + e.getMessage());
		}
	}

	@Override
	public String getSubscriptionType(String employeerMail) {
		try {

			Employeer subscription = employeerRepo.findByEmailId(employeerMail);

			if (subscription.getSubscriptionType() == null) {
				throw new IllegalArgumentException("Employer with email " + employeerMail + " not found");
			}
			String subscriptionType = subscription.getSubscriptionType();
			Date subscription_Expiry_Date = subscription.getSubscriptionExprirationDate();
			return subscriptionType;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "Employer with email " + employeerMail + " not found";
		} catch (Exception e) {

			e.printStackTrace();

			return "Failed to get subscription type: " + e.getMessage();
		}
	}

	@Override
	public String remainingJobCount(String subscriptionType) {

		try {

			Subscription sub = subscriptionRepo.findNoOfJobsBySubscriptionType(subscriptionType);

			if (sub == null) {
				throw new IllegalArgumentException("Subscription not found for subscription type: " + subscriptionType);
			}

			int remainingJobs = sub.getNumberOfJobs();

			return String.valueOf(remainingJobs);
		} catch (IllegalArgumentException e) {

			e.printStackTrace();

			return "Subscription not found for subscription type: " + subscriptionType;
		} catch (Exception e) {

			e.printStackTrace();

			return "Failed to get remaining job count: " + e.getMessage();
		}
	}

	@Override
	public String sendmail(String employeerEmailId, String jobseekerMailId, String username, String status, int jobId) {

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setSubject("Job Hunt");
			helper.setTo(jobseekerMailId);
			Employeer employee = employeerRepo.findByEmailId(employeerEmailId);
			if (employee == null) {
				throw new IllegalArgumentException("Employer with email " + employeerEmailId + " not found");
			}
			String company = employee.getCompanyName();
			String emailContent = getEmailContent(employeerEmailId, company, username, status);

			helper.setText(emailContent, true);
			javaMailSender.send(mimeMessage);
			return "Mail sent successfully";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "Employer with email " + employeerEmailId + " not found";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while Sending Mail";
		}

	}

	String getEmailContent(String employeerEmailId, String companyName, String username, String status)
			throws TemplateException, IOException {
		Map<String, Object> model = new HashMap<>();
		model.put("companyName", companyName);
		model.put("username", username);
		model.put("employeerId", employeerEmailId);
		Template template = emailConfig.getTemplate(status + ".html");
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	}

}
