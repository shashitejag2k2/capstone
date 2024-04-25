package com.hv.jobhunt.Models;



import javax.persistence.*;


@Entity
@Table(name = "jobseeker")
public class JobSeeker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobseeker_id")
    private int jobSeekerId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "email_id")
    private String emailId;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "college_name")
    private String collegeName;
    
    @Column(name = "experience")
    private int experience;
    
    @Column(name = "skills")
    private String skills;
    
    public int getJobSeekerId() {
		return jobSeekerId;
	}

	public void setJobSeekerId(int jobSeekerId) {
		this.jobSeekerId = jobSeekerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public JobSeeker(){
		
	}
    

    
}

