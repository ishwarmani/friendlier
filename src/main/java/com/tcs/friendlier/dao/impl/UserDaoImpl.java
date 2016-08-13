package com.tcs.friendlier.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tcs.friendlier.dao.IUserDao;
import com.tcs.friendlier.pojo.Post;
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

}
