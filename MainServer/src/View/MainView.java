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

import ViewModel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


public class MainView implements Observer,Initializable {

	private MainViewModel viewModel;
	private Boolean isServerRunning;
	private Thread serverThread;
	private Boolean isNeedToStop;
	private ServerSocket Socket;
	private Socket newConnection;
	private ThreadPoolExecutor executionPool;
	@FXML
	public void StopStart()
	{
		if (!isServerRunning)
		{
			isServerRunning = true;
			serverThread.start();
		}
		else	
		{
			isServerRunning = false;
			isNeedToStop =true;
			serverThread.interrupt();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		isServerRunning = false;
		isNeedToStop = false;
		executionPool = new ThreadPoolExecutor(10,10, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
		try {
			Socket = new ServerSocket(8888);//CONFIG
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverThread = new Thread(()->{
			System.out.println("Server is running");
			while (!isNeedToStop)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Server Is Stop");
		});
	}

	
	public MainViewModel getViewModel() {
		return viewModel;
	}

	public void setViewModel(MainViewModel viewModel) {
		this.viewModel = viewModel;
	}

	
}
