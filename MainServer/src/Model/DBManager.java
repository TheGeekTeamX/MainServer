package Model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import DB.*;
import Enums.*;
import ResponsesEntitys.*;


public class DBManager {
	private static ReentrantLock lock;
	private static SessionFactory factory;
	private Session session;
	private static DBManager instance = null;
	
	private Transaction startSession()
	{
		lock.lock();
		session = factory.openSession();
		return session.beginTransaction();
	}
	private void closeSession()
	{
		session.close();
		lock.unlock();
	}
	
	public void test()
	{
		User u = getUser("Test123@gmail.com");
		System.out.println(u.toString());
	}
	
	public ProfilePicture getUserProfilePicture(int userId)
	{
		startSession();
		ArrayList<ProfilePicture> list = (ArrayList<ProfilePicture>)session.createQuery(String.format("from ProfilePictures where UserId = " + userId)).list();
		closeSession();
		return list != null ? (list.size() != 0 ? list.get(0) : null) : null;
	}
	
	public LinkedList<EventData> getEventsList(int userId)
	{
		startSession();
		ArrayList<UserEvent> userEventList = (ArrayList<UserEvent>)session.createQuery(String.format("from UserEvents where UserId = " + userId)).list();
		if(userEventList == null)
			return null;
		LinkedList<EventData> eventsList = new LinkedList<>();
		userEventList.forEach(ue->{
			ArrayList<UserEvent> participants = (ArrayList<UserEvent>)session.createQuery(String.format("from UserEvents where EventId = " + ue.getEvent().getId())).list();
			LinkedList<String> participantsNames = new LinkedList<>();
			participants.forEach(p -> {participantsNames.add(p.getUser().getFullName());});
			eventsList.add(new EventData(ue.getId(), participantsNames, ue.getEvent().getDateCreated()));

		});
		closeSession();
		return eventsList;
	}
	
	public ArrayList<Contact> getContactsList(int userId)
	{
		startSession();
		ArrayList<Contact> list = (ArrayList<Contact>)session.createQuery(String.format("from Contacts where UserId = " + userId)).list();
		closeSession();
		return list;
	}
	public User getUser(String email)
	{
		startSession();
		ArrayList<User> list = (ArrayList<User>)session.createQuery(String.format("from Users where Email like '%1$s'", email)).list();
		closeSession();
		return list != null ? (list.size() != 0 ? list.get(0) : null) : null;
	}
	public Credential getCredential(int userId)
	{
		startSession();
		ArrayList<Credential> list = (ArrayList<Credential>)session.createQuery("from Credentials where UserId = " + userId).list();
		closeSession();
		return list != null ? list.get(0) : null;
	}
	
	public IDBEntity get(int id, DBEntityType entityType)
	{
		Transaction tx = null;
		IDBEntity entity = null;
		try {
			tx = startSession();
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
			case Credential:
				entity = session.get(Credential.class, id);
				break;
			case ProfilePicture:
				entity = session.get(ProfilePicture.class, id);
				break;
			default:
				break;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			return null;
		} finally {
			closeSession();
		}
		return entity;
	}
	public int addToDataBase(Object obj)
	{
		Transaction tx = null;
		int id=0;
		try {
			tx = startSession();
			id = (int)session.save(obj);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			return -1;
		} finally {
			closeSession();
		}
		return id;
	}
	
	public Boolean deleteFromDataBase(int id,DBEntityType entityType)
	{
		IDBEntity entity = get(id, entityType);
		Transaction tx = null;
		try {
			tx = startSession();
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
			closeSession();
		}
		return true;
	}
	
	public Boolean editInDataBase(int id,DBEntityType entityType,IDBEntity updatedObj)
	{
		IDBEntity entity = get(id, entityType);
		Transaction tx = null;
		try {
			tx = startSession();
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
			closeSession();
		}
		return true;
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
		lock = new ReentrantLock();
	}

}
