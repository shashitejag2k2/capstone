package com.hv.jobhunt.Models;

public class EmailRequest {
	
	    private String to; // Recipient's email address
	    private String subject; // Subject of the email
	    public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private String name; // Name of the recipient (or any other relevant information)
	    // You can add more attributes as needed, such as sender, attachments, etc.

	    // Constructors, getters, and setters
		
		public EmailRequest() {
			
		}
	}


