package com.hv.jobhunt.Models;


import javax.persistence.*;

@Entity
@Table(name = "jobapplied")
public class AppliedJobs {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	    private int id;
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "applied_by")
    private String appliedBy;
    
    @Column(name = "posted_by")
    private String postedBy;

    @Column(name = "job_title")
    private String jobTitle;
    
    @Column(name="status")
    private String status;
    
    @Column(name="job_id")
    private int jobId;
    
    public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AppliedJobs() {
    	
    }

   

	
	public String getAppliedBy() {
		return appliedBy;
	}

	public void setAppliedBy(String appliedBy) {
		this.appliedBy = appliedBy;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	

}
