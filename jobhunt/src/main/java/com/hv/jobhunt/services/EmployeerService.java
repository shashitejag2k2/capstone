package com.hv.jobhunt.services;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.JobListing;
import com.hv.jobhunt.Models.JobSeeker;

public interface EmployeerService {
	
//	 String login(Employeer employeer);
	 String postJob(JobListing postJob);
	 List<JobListing> getjobs(String email);
	 List<AppliedJobs> getjobSeekers(String postedBy);
	 String updateStatus(AppliedJobs jobApplication);
	 String deleteJob(int jobId);
	 String updateJob(JobListing jobListing);
	 String updateJobApplicationStatus(int jobId);
	 
	 List<JobSeeker> getDetails (int jobId,String employeerMail);
	 
	 Map<String, String> getCount(String employeerMail);
	
	
	String getSubscriptionType(String employeerMail);
	
	String sendmail(String employeerEmailId,String jobseekerMailId,String username,String status,int jobId);

	Employeer login(String emailId, String password);
	
	String updateemployerSubscription( Employeer employee);
	
	String register(Employeer employee);
	
	String remainingJobCount(String subscriptionType);

	
	
	 
//	
//		
	
	

}
