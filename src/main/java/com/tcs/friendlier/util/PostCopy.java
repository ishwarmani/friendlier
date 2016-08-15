package com.tcs.friendlier.util;

import java.util.Date;

public class PostCopy {
	
	private int writerId;
	private String writerName;
	private String content;
	private Date contentDate;
	private byte[] writerPhoto;
	
	public int getWriterId() {
		return writerId;
	}
	
	public void setWriterId(int writerId) {
		this.writerId = writerId;
	}
	
	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
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

	public byte[] getWriterPhoto() {
		return writerPhoto;
	}

	public void setWriterPhoto(byte[] writerPhoto) {
		this.writerPhoto = writerPhoto;
	}
	
	

}
