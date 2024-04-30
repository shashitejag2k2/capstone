package com.hv.jobhunt.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import com.hv.jobhunt.repository.JobAppliedRepository;
import com.hv.jobhunt.repository.JobRepository;
import com.hv.jobhunt.repository.JobSeekerRepo;
import com.hv.jobhunt.services.JobSeekerService;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

	@Autowired
	private JobSeekerRepo jobSeekerRepo;

	@Autowired
	private JobAppliedRepository jobAppliedRepo;

	@Autowired
	private JobRepository jobRepository;

	@Override
	public String applyJob(AppliedJobs jobApplication) {
		try {
			String appliedBy = jobApplication.getAppliedBy();
			int jobId = jobApplication.getJobId();
			AppliedJobs userExistence = jobAppliedRepo.findByAppliedByAndJobId(appliedBy, jobId);

			if (userExistence == null) {
				jobApplication.setStatus("pending");
				jobAppliedRepo.save(jobApplication);
				return "Job application saved successfully";
			} else {

				if (userExistence.getAppliedBy().equals(appliedBy) && userExistence.getJobId() == jobId) {
					throw new RuntimeException("You have already applied for this job");
				}
			}
		} catch (RuntimeException e) {
			String errorMessage = e.getMessage();

			return errorMessage;
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to save job application due to an unexpected error";
		}
		return "Failed to save job application";
	}

	@Override
	public String register(JobSeeker jobSeeker) {
		try {
			boolean exist = jobSeekerRepo.existsByEmailId(jobSeeker.getEmailId());
			if (exist) {
				System.out.println("bool");
				return "You have already registered. Please try to log in.";
			} else {

				jobSeekerRepo.save(jobSeeker);
				return "Successfully saved JobSeeker";
			}

		} catch (DataIntegrityViolationException e) {

			return "Failed to register: " + e.getMessage();
		} catch (Exception e) {

			return "Failed to register due to an unexpected error: " + e.getMessage();
		}
	}

	@Override
	public String login(JobSeeker jobSeeker) {
		try {
			String email = jobSeeker.getEmailId();
			String password = jobSeeker.getPassword();
			JobSeeker seeker = jobSeekerRepo.findByEmailIdAndPassword(email, password);
			if (seeker != null && password.equals(seeker.getPassword())) {
				return "User login successful";
			}
			return "Invalid credentials";
		} catch (Exception e) {

			throw new RuntimeException("Failed to login: " + e.getMessage());
		}
	}

	@Override
	public List<JobListing> getjobs() {
		try {
			List<JobListing> alljobs = jobRepository.findAll();
			return alljobs;
		} catch (Exception e) {

			throw new RuntimeException("Failed to retrieve job listings: " + e.getMessage());
		}
	}

//	@Override
//	public JobListing getIndividualJob(int jobId) {
//		try {
//	        JobListing job = jobRepository.findByjobId(jobId);
//	        return job;
//	    } catch (Exception e) {
//	        // Log the exception or handle it as needed
//	        throw new RuntimeException("Failed to retrieve job: " + e.getMessage());
//	    }
//	}

	@Override
	public String updateProfile(JobSeeker jobSeeker) {
		try {
			int id = jobSeeker.getJobSeekerId();
			System.out.println("Job seeker id " + id);
			JobSeeker existingJobSeeker = jobSeekerRepo.findByJobSeekerId(id);
			if (existingJobSeeker != null) {
				existingJobSeeker.setCollegeName(jobSeeker.getCollegeName());
				existingJobSeeker.setExperience(jobSeeker.getExperience());
				existingJobSeeker.setSkills(jobSeeker.getSkills());
				jobSeekerRepo.save(existingJobSeeker);
				return "Profile Updated successfully";
			} else {
				return "Not saved";
			}
		} catch (Exception e) {

			throw new RuntimeException("Failed to Update Profile: " + e.getMessage());
		}
	}

	@Override
	public List<AppliedJobs> getJobAppliedDetails(String appliedBy) {
		try {
			List<AppliedJobs> jobApplications = jobAppliedRepo.findByAppliedBy(appliedBy);
			return jobApplications;
		} catch (Exception e) {

			throw new RuntimeException("Failed to retrieve job applications: " + e.getMessage());
		}
	}

	@Override
	public List<JobListing> searchJobListings(String keyword) {
		try {

			return jobRepository.findByJobTitleContainingIgnoreCase(keyword);

		} catch (Exception e) {

			throw new RuntimeException("Failed to search for job listings: " + e.getMessage());
		}
	}

	@Override
	public JobSeeker getJobSeekerDetails(String email) {
		try {
			return jobSeekerRepo.findByEmailId(email);
		} catch (Exception e) {

			throw new RuntimeException("Failed to retrieve job seeker details: " + e.getMessage());
		}
	}

//	@Override
//	public List<JobListing> getHighPayingJobs() {
//		try {
//	        int highpay = 20;
//	        List<JobListing> HighPayingJobs = jobRepository.findByMinimumSalaryGreaterThanEqual(highpay);
//	        return HighPayingJobs;
//	    } catch (Exception e) {
//	        // Log the exception or handle it as needed
//	        throw new RuntimeException("Failed to retrieve high paying jobs: " + e.getMessage());
//	    }
//	}

}
