package View;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ClientHandler.ClientHandler;
import ClientHandler.IClientHandler;
import ViewModel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class MainView implements Observer,Initializable {

	private MainViewModel viewModel;
	private Boolean isServerRunning;
	private Thread serverThread;
	private Boolean isNeedToStop;
	private ServerSocket socket;
	private ThreadPoolExecutor executionPool;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
					try {
						Socket newConnection = socket.accept();
						IClientHandler ch = new ClientHandler(null);
						ch.handleClient(newConnection);
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
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}


	
	public MainViewModel getViewModel() {
		return viewModel;
	}

	public void setViewModel(MainViewModel viewModel) {
		this.viewModel = viewModel;
	}
	@FXML
	public void StopStart()
	{
		if (isServerRunning)
			stopServerThread();
		else	
			startServerThread();
	}
	
}
