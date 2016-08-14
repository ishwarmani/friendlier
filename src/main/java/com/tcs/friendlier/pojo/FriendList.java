package com.tcs.friendlier.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FriendList")
public class FriendList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fId;

	private int userId1, userId2, status;

	public int getfId() {
		return fId;
	}

	public void setfId(int fId) {
		this.fId = fId;
	}

	public int getUserId1() {
		return userId1;
	}

	public void setUserId1(int userId1) {
		this.userId1 = userId1;
	}

	public int getUserId2() {
		return userId2;
	}

	public void setUserId2(int userId2) {
		this.userId2 = userId2;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
