package com.tcs.friendlier.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tcs.friendlier.dao.IUserDao;
import com.tcs.friendlier.pojo.FriendList;
import com.tcs.friendlier.pojo.Messages;
import com.tcs.friendlier.pojo.Post;
import com.tcs.friendlier.pojo.Status;
import com.tcs.friendlier.pojo.User;

@Repository
public class UserDaoImpl implements IUserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int save(User user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(user);
			transaction.commit();
			session.close();
			return 1;
		} catch (PersistenceException e) {
			transaction.rollback();
			e.printStackTrace();
			session.close();
			return -1;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			session.close();
			return 0;
		}
	}

	@Override
	public User findUserById(int id, String password) {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("id", id));
			user = (User) criteria.uniqueResult();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (user != null) {
			if (new BasicPasswordEncryptor().checkPassword(password, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	@Override
	public User findUserByEmail(String email, String password) {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("email", email));
			user = (User) criteria.uniqueResult();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (user != null) {
			if (new BasicPasswordEncryptor().checkPassword(password, user.getPassword())) {
				return user;
			}
		}
		return null;
	}
	
	@Override
	public User findUserByEmail(String email) {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("email", email));
			user = (User) criteria.uniqueResult();
			transaction.commit();
			return  user;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return null;
	}
	
	@Override
	public boolean updateStatus(int writerId, String content) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Post post = new Post();
		post.setWriterId(writerId);
		post.setContent(content);
		post.setContentDate(new Date());
		try {
			session.save(post);
			transaction.commit();
			return true;
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public List<User> getUserList() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(User.class);
			@SuppressWarnings("unchecked")
			List<User> list = criteria.list();
			transaction.commit();
			return list;
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Post> getPostList() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Post.class).addOrder(Order.desc("contentDate"));
			criteria.setMaxResults(10);
			@SuppressWarnings("unchecked")
			List<Post> list = criteria.list();
			transaction.commit();
			return list;
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public void sendRequest(int senderId, int recieverId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		FriendList friendList = new FriendList();
		friendList.setUserId1(senderId);
		friendList.setUserId2(recieverId);
		friendList.setStatus(Status.PENDING.getValue());
		try {
			session.save(friendList);
			transaction.commit();
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		
	}

	@Override
	public void updateRequest(int senderId, int friendId) {
		FriendList friendList = null;
//		User user1 = findUserById(senderId);
//		User user2 = findUserById(senderId);
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		try {
			Criteria criteria = session.createCriteria(FriendList.class).add(Restrictions.eq("userId1", senderId)).
					add(Restrictions.eq("userId2", friendId));
			friendList = (FriendList) criteria.uniqueResult();
			friendList.setStatus(Status.ACCEPTED.getValue());
//			user1.setFriendCount(user1.getFriendCount()+1);
//			user2.setFriendCount(user2.getFriendCount()+1);
			session.update(friendList);
//			session.update(user1);
//			session.update(user2);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void declineRequest(int senderId, int friendId) {
		FriendList friendList = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(FriendList.class).add(Restrictions.eq("userId1", senderId)).
					add(Restrictions.eq("userId2", friendId));
			friendList = (FriendList) criteria.uniqueResult();
			friendList.setStatus(Status.DECLINED.getValue());
			session.update(friendList);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public List<User> getFriendRequests(int id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(FriendList.class).add(Restrictions.eq("userId2",id))
					.add(Restrictions.eq("status", 0));
//			Disjunction or = Restrictions.disjunction();
//			or.add(Restrictions.eq("userId1",id));
//			or.add(Restrictions.eq("userId2",id));
//			criteria.add(or).add(Restrictions.eq("status", 0));
			
			/*Criteria criteria = session.createCriteria(FriendList.class)
		    .add(Restrictions.disjunction()
		        .add(Restrictions.eq("userId1",id))
		        .add(Restrictions.eq("userId2",id))
		    );*/
			
			criteria.setMaxResults(10);
			@SuppressWarnings("unchecked")
			List<FriendList> list = criteria.list();
			int temp = 0;
			List<User> listUser = new ArrayList<>();
			for (FriendList friendList : list) {
				temp = friendList.getUserId1();
				listUser.add(findUserById(temp));
			}
			transaction.commit();
			return listUser;
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<User> getFriends(int id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
//			Criteria criteria = session.createCriteria(FriendList.class).add(Restrictions.eq("userId2",id))
//					.add(Restrictions.eq("status", 1));
//			Disjunction or = Restrictions.disjunction();
//			or.add(Restrictions.eq("userId1",id));
//			or.add(Restrictions.eq("userId2",id));
//			criteria.add(or).add(Restrictions.eq("status", 0));
			
			Criteria criteria = session.createCriteria(FriendList.class)
		    .add(Restrictions.disjunction()
		        .add(Restrictions.eq("userId1",id))
		        .add(Restrictions.eq("userId2",id))
		    );
			criteria.add(Restrictions.eq("status",1));
			criteria.setMaxResults(10);
			@SuppressWarnings("unchecked")
			List<FriendList> list = criteria.list();
			int temp = 0;
			List<User> listUser = new ArrayList<>();
			for (FriendList friendList : list) {
				if(friendList.getUserId1() == id)
					temp = friendList.getUserId2();
				else
					temp = friendList.getUserId1();
				listUser.add(findUserById(temp));
			}
			transaction.commit();
			return listUser;
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
	
	@Override
	public User findUserById(int id) {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("id", id));
			user = (User) criteria.uniqueResult();
			transaction.commit();
			return  user;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return null;
	}

	@Override
	public void updateMessages(Messages msg) {
		msg.setMsgDate(new Date());
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(msg);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Messages> getAllMessages(int id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Messages.class).add(Restrictions.eq("recieverId", id)).
					addOrder(Order.desc("msgDate"));
			criteria.setMaxResults(10);
			@SuppressWarnings("unchecked")
			List<Messages> list = criteria.list();
			transaction.commit();
			return list;
		}catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
		}

}
