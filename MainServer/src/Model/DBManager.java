package Model;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import DB.*;
import Enums.*;

public class DBManager {
	private static SessionFactory factory;
	private Session session;
	private static DBManager instance = null;
	
	public void test()
	{
		
	}

	private IDBEntity get(int id, DBEntityType entityType)
	{
		session = factory.openSession();
		Transaction tx = null;
		IDBEntity entity = null;
		try {
			tx = session.beginTransaction();
			switch (entityType)
			{
			case User:
				entity= session.get(User.class, id);
				break;
			case UserEvent:
				entity = session.get(UserEvent.class, id);
				break;
			case Event:
				entity = session.get(Event.class, id);
				break;
			case Contact:
				entity = session.get(Contact.class, id);
				break;
			default:
				break;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			return null;
		} finally {
			session.close();
		}
		return entity;
	}
	private int addToDataBase(Object obj)
	{
		session = factory.openSession();
		Transaction tx = null;
		int id=0;
		try {
			tx = session.beginTransaction();
			id = (int)session.save(obj);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			return -1;
		} finally {
			session.close();
		}
		return id;
	}
	
	private Boolean deleteFromDataBase(int id,DBEntityType entityType)
	{
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			IDBEntity entity;
			switch (entityType)
			{
			case User:
				entity= session.get(User.class, id);
				break;
			case UserEvent:
				entity = session.get(UserEvent.class, id);
				break;
			case Event:
				entity = session.get(Event.class, id);
				break;
			case Contact:
				entity = session.get(Contact.class, id);
				break;
			default:
				return false;
			}
			if(entity != null)
			{
				session.delete(entity);
				tx.commit();
			}
			else
				return false;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			return false;
		} finally {
			session.close();
		}
		return true;
	}
	
	private Boolean editInDataBase(int id,DBEntityType entityType,IDBEntity updatedObj)
	{
		session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			IDBEntity entity;
			switch (entityType)
			{
			case User:
				entity= session.get(User.class, id);
				break;
			case UserEvent:
				entity = session.get(UserEvent.class, id);
				break;
			case Event:
				entity = session.get(Event.class, id);
				break;
			case Contact:
				entity = session.get(Contact.class, id);
				break;
			default:
				return false;
			}
			if(entity != null)
			{
				entity.update(updatedObj);
				session.update(entity);
				tx.commit();
			}
			else
				return false;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			return false;
		} finally {
			session.close();
		}
		return true;
	}
	
	
	public void execute(DBAction action)
	{
		switch(action)
		{
		case Add:
			break;
		case Edit:
			break;
		case Delete:
			break;
		}
		
	}
	public static DBManager getInstance()
	{
		if (instance == null)
		{
			instance = new DBManager();
			instance.connectToDataBase();
		}
		return instance;
	}
	private void connectToDataBase()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		Configuration configuration = (new Configuration()).configure();
		factory = configuration.buildSessionFactory(); 
	}

}
