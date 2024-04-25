package com.hv.jobhunt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.JobListing;
import org.springframework.transaction.annotation.Transactional;

public interface JobAppliedRepository extends JpaRepository<AppliedJobs,Integer> {
	
	
	List<AppliedJobs> findByPostedBy(String postedBy);
	
	List<AppliedJobs> findByPostedByAndJobTitle(String postedBy,String jobTitle);
	
	List<AppliedJobs> findByAppliedByAndJobTitle(String AppliedBy,String jobTitle);
	
	List<AppliedJobs> findByJobId(int jobId);
	
	List<AppliedJobs> findByAppliedBy(String AppliedBy);
	
	AppliedJobs findByAppliedByAndJobId(String appliedBy,int jobId);
	
	@Transactional
	void deleteByAppliedByAndJobId(String appliedBy,int jobId);
	

}
