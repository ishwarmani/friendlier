package com.tcs.friendlier.pojo;

import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class Post {
	private int writerId;
	@Type(type = "text")
	private String content;
	private Date contentDate;

	public int getWriterId() {
		return writerId;
	}

	public void setWriterId(int writerId) {
		this.writerId = writerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getContentDate() {
		return contentDate;
	}

	public void setContentDate(Date contentDate) {
		this.contentDate = contentDate;
	}

}
