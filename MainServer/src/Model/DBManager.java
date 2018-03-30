package Model;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import DB.Employee;
import Enums.DBAction;
import javafx.fxml.Initializable;

public class DBManager {
	private static SessionFactory factory;
	private Session session;
	private static DBManager instance = null;
	

	public void dbTest()
	{
		System.out.println("Test start");
		Employee emp = new Employee("test", "test", 300.0);
		int empID = addToDataBase(emp);
		System.out.println("Result: "+empID);
	}
	private int addToDataBase(Object obj)
	{
		Transaction tx = null;
		int empID = 0;
		session = factory.openSession();
		try {
			tx = session.beginTransaction();
			empID = (Integer) session.save(obj);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return empID;
	}
	
	private void editInDataBase()
	{
		
	}
	
	private void deleteFromDataBase()
	{
		
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
