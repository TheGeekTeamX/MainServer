package ViewModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ClientHandler.ClientHandler;
import ClientHandler.IClientHandler;
import Enums.RequestType;
import Model.MainModel;

public class MainViewModel extends Observable implements Observer,IController {
	private MainModel model;
	private Boolean isServerRunning;
	private Thread serverThread;
	private Boolean isNeedToStop;
	private ServerSocket socket;
	private ThreadPoolExecutor executionPool;

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public MainViewModel(MainModel model) {
		super();
		this.model = model;
		model.testDB();
		isServerRunning = false;
		isNeedToStop = false;
		executionPool = new ThreadPoolExecutor(10,10, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
		try {
			socket = new ServerSocket(8888);//CONFIG
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createNewClientHandler(Socket newConnection)
	{
		IClientHandler ch = new ClientHandler(this);
		ch.handleClient(newConnection);
		setChanged();
		notifyObservers(new ConnectionData(UUID.randomUUID().toString(), new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), ""));
	}
	public void startServerThread()
	{
		isServerRunning = true;
		isNeedToStop = false;
		
		serverThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!isNeedToStop)
				{
					System.out.println("test");
					try {
						Socket newConnection = socket.accept();
						createNewClientHandler(newConnection);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//Socket newConnection = socket.accept();
					//create new client handler
					if (isNeedToStop)
					{
						//handle client
						
					}
					else
					{
						//Can't handle client right now..Server need to stop
						 
					}
				}
				System.out.println("Server thread is finish");
				
			}
		});
		serverThread.start();
		System.out.println("Server is on");
		
	}
	public void stopServerThread()
	{
		isServerRunning = false;
		isNeedToStop = true;
		System.out.println("Server is off");
	}
	public Boolean interrupt(Boolean flag)
	{
		isServerRunning = !flag;
		if (flag)//ThreadWasRunning
		{
			stopServerThread();
		}
		else//ThreadWasNotRunning
		{
			startServerThread();
		}
		return isServerRunning;
		
	}

	@Override
	public ResponseData execute(RequestData ReqData) {
		// TODO Auto-generated method stub
		int x=0;
		switch (ReqData.getType()) {
		case DataEditing:
			System.out.println("DataEditing");
			break;
		case DataRequest:
			System.out.println("DataRequest");
			break;
		case OpenEvent:
			System.out.println("OpenEvent");
			break;
		case ProtocolRequest:
			System.out.println("ProtocolRequest");
			break;
		case RecordSending:
			System.out.println("RecordSending");
			break;
		default:
			System.out.println("default");
			break;
		}
		return null;
	}
	
	

}
