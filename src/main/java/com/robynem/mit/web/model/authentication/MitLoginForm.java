/**
 * 
 */
package com.robynem.mit.web.model.authentication;

import java.io.Serializable;

/**
 * @author roberto
 *
 */
public class MitLoginForm implements Serializable {
	
	private String accessToken;
	
	private long facebookUserId;

	private String redirectView;

	private String emailAddress;

	private String password;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getFacebookUserId() {
		return facebookUserId;
	}

	public void setFacebookUserId(long facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public String getRedirectView() {
		return redirectView;
	}

	public void setRedirectView(String redirectView) {
		this.redirectView = redirectView;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
