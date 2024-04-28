package com.hv.jobhunt.services;

import java.util.List;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;
import org.springframework.web.multipart.MultipartFile;
public interface JobSeekerService {
	String applyJob(AppliedJobs jobApplication);
	
	
	String register(JobSeeker jobSeeker);
	
	String login(JobSeeker jobSeeker);
	
	String updateProfile(JobSeeker jobSeeker);
	
	List<JobListing> getjobs();
	
//	JobListing getIndividualJob(int jobId);
	
	List<AppliedJobs> getJobAppliedDetails(String email);
	
	List<JobListing> searchJobListings(String keyword);


	JobSeeker getJobSeekerDetails(String email);
	
//	String parseResume();
	
//	List<JobListing>getHighPayingJobs();
	
}
