package com.tcs.friendlier.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.friendlier.dao.IUserDao;
import com.tcs.friendlier.pojo.Messages;
import com.tcs.friendlier.pojo.Post;
import com.tcs.friendlier.pojo.User;
import com.tcs.friendlier.service.IService;
import com.tcs.friendlier.util.MessagesCopy;
import com.tcs.friendlier.util.PostCopy;

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

	@Override
	public void updateMessages(Messages msg) {
		// TODO Auto-generated method stub
		userDao.updateMessages(msg);
	}

	@Override
	public List<Messages> getAllMessages(int id) {
		// TODO Auto-generated method stub
		return userDao.getAllMessages(id);
	}

	@Override
	public List<PostCopy> getPostCopyList() {
		
		List<Post> allPost = userDao.getPostList();
		List<PostCopy> postCopies = new ArrayList<>();
		
		for (Post post : allPost) {
			PostCopy postCopy = new PostCopy();
			User user = userDao.findUserById(post.getWriterId());
			String writersName = user.getName();
			postCopy.setWriterName(writersName);
			postCopy.setWriterPhoto(user.getPhoto());
			postCopy.setContent(post.getContent());
			postCopy.setContentDate(post.getContentDate());
			
			postCopies.add(postCopy);
		}
		return postCopies;
	}

	@Override
	public List<MessagesCopy> getAllMessagesCopy(int id) {
		
		int counter = 0;
		List<Messages> allmsgs = userDao.getAllMessages(id);
		List<MessagesCopy> msgsCopy = new ArrayList<>();
		for (Messages messages : allmsgs) {
			MessagesCopy messagesCopy = new MessagesCopy();
			messagesCopy.setsNo(++counter);
			messagesCopy.setSendersName(userDao.findUserById(messages.getSenderId()).getName());
			messagesCopy.setMessage(messages.getMessage());
			messagesCopy.setMsgDate(messages.getMsgDate());
			
			msgsCopy.add(messagesCopy);
		}
		return msgsCopy;
	}

	
}
