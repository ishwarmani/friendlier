package com.tcs.friendlier.service.impl;

import java.util.List;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.friendlier.dao.IUserDao;
import com.tcs.friendlier.pojo.Post;
import com.tcs.friendlier.pojo.User;
import com.tcs.friendlier.service.IService;

@Service
public class ServiceImpl implements IService {

	@Autowired
	private IUserDao userDao;

	@Override
	public int save(User user) {
		BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
		user.setPassword(basicPasswordEncryptor.encryptPassword(user.getPassword()));
		int success = userDao.save(user);
		return success;
	}

	@Override
	public User findUserById(int id, String password) {
		// TODO Auto-generated method stub
		return userDao.findUserById(id, password);
	}

	@Override
	public User findUserByEmail(String email, String password) {
		// TODO Auto-generated method stub
		return userDao.findUserByEmail(email, password);
	}
	
	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.findUserByEmail(email);
	}

	@Override
	public boolean updateStatus(int writerId, String content) {
		// TODO Auto-generated method stub
		return userDao.updateStatus(writerId,content);
	}

	@Override
	public List<User> getUserList() {
		// TODO Auto-generated method stub
		return userDao.getUserList();
	}

	@Override
	public List<Post> getPostList() {
		// TODO Auto-generated method stub
		return userDao.getPostList();
	}

	@Override
	public void sendRequest(int senderId, int recieverId) {
		// TODO Auto-generated method stub
		userDao.sendRequest(senderId,recieverId);
	}

	@Override
	public void updateRequest(int senderId, int friendId) {
		// TODO Auto-generated method stub
		userDao.updateRequest(senderId,friendId);
	}

	@Override
	public void declineRequest(int senderId, int friendId) {
		// TODO Auto-generated method stub
		userDao.declineRequest(senderId,friendId);
	}

	@Override
	public List<User> getFriendRequests(int id) {
		// TODO Auto-generated method stub
		return userDao.getFriendRequests(id);
	}

	@Override
	public List<User> getFriends(int id) {
		// TODO Auto-generated method stub
		return userDao.getFriends(id);
	}

}
