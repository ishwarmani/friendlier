package com.tcs.friendlier.dao.impl;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tcs.friendlier.dao.IUserDao;
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

}
