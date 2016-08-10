package com.tcs.friendlier.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Messages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int senderId;
	private int recieverId;
	@Type(type = "text")
	private String message;
	private Date msgDate;

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getRecieverId() {
		return recieverId;
	}

	public void setRecieverId(int recieverId) {
		this.recieverId = recieverId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}

}