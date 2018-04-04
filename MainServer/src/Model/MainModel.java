package Model;

import java.util.Observable;

public class MainModel extends Observable {
	private static MainModel instance;
	private DBManager dbManager;
	public static MainModel getInstance()
	{
		if(instance == null)
		{
			instance = new MainModel();
			instance.init();
		}
		return instance;
	}
	private void init()
	{
		dbManager = DBManager.getInstance();
	}
	public void testDB()
	{
		dbManager.test();
	}

}
