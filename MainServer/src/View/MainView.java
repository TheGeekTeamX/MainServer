package View;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import ClientHandler.ClientHandler;
import ClientHandler.IClientHandler;
import DB.User;
import ViewModel.ConnectionData;
import ViewModel.MainViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class MainView implements Observer,Initializable {

	private MainViewModel viewModel;
	private Boolean isServerRunning;
	private Boolean isNeedToStop;
	private Thread serverThread;	
	private ServerSocket socket;
	private PausableThreadPoolExecutor executionPool;
	@FXML
	private Circle state;
	@FXML
	private Label connectionsNumLabel;
	private StringProperty connectionsNumStr;
	private int connectionsNum;
	@FXML
	private Label onlineConnectionsNumLabel;
	private StringProperty onlineConnectionsNumStr;
	private int onlineConnectionsNum;
	@FXML
	private Button AddUserButton;
	@FXML
	private TextField userEmail;
	@FXML
	private TextField userName;
	@FXML
	private TextField userPhoneNumber;
	@FXML
	private TextField userCountry;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		initGUIComponents();
		initServer();

	}
	
	public void initServer()
	{
		isServerRunning = false;
		isNeedToStop = false;
		try {
			socket = new ServerSocket(8888);//CONFIG
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executionPool = new PausableThreadPoolExecutor(10, 1000, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
	}
	
	public void initGUIComponents()
	{
		state.setFill(Color.RED);
		connectionsNumStr = new SimpleStringProperty();
		setConnectionsNumStr(""+0);
		connectionsNumLabel.textProperty().bind(connectionsNumStr);
		onlineConnectionsNumStr = new SimpleStringProperty();
		setOnlineConnectionsNumStr(""+0);
		onlineConnectionsNumLabel.textProperty().bind(onlineConnectionsNumStr);
		AddUserButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				User u = new User(userEmail.getText(),userName.getText(),userPhoneNumber.getText(),userCountry.getText());
				viewModel.getModel().getDbManager().addToDataBase(u);
			}
		});
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0 == viewModel)
		{

		}
	}

	@FXML	
	public void StopStart()
	{
		isServerRunning = interrupt(isServerRunning);
		state.setFill(isServerRunning ? Color.LAWNGREEN : Color.RED);
	}
	public Boolean interrupt(Boolean flag)
	{
		
		if (flag)
			stopServerThread();
		else
			startServerThread();
		return !flag;
		
	}
	public void stopServerThread()
	{
		isServerRunning = false;
		isNeedToStop = true;
	}
	public void createNewClientHandler(Socket newConnection)
	{
		
		IClientHandler ch = new ClientHandler();
		executionPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ch.handleClient(newConnection , viewModel);
				
			}
		});;
		
		//ch.handleClient(newConnection);
	}
	public void createNewConnection(Socket socket)
	{
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setConnectionsNum(getConnectionsNum()+1);
				setOnlineConnectionsNum(getOnlineConnectionsNum()+1);			
			}
		});
		createNewClientHandler(socket);
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
						createNewConnection(newConnection);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (isNeedToStop)
					{
						//handle client
						
					}
					else
					{
						//Can't handle client right now..Server need to stop
						 
					}
				}
				
			}
		});
		serverThread.start();
		
	}
	
	/*****Get & Set*****/
	public Boolean getIsServerRunning() {
		return isServerRunning;
	}

	public void setIsServerRunning(Boolean isServerRunning) {
		this.isServerRunning = isServerRunning;
	}

	public void setState(Circle state) {
		this.state = state;
	}

	public MainViewModel getViewModel() {
		return viewModel;
	}

	public void setViewModel(MainViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	public String getConnectionsNumStr() {
		return connectionsNumStr.get();
	}

	public void setConnectionsNumStr(String ConnectionsNumStr) {
		this.connectionsNumStr.setValue(ConnectionsNumStr);
	}
	
	public String getOnlineConnectionsNumStr() {
		return onlineConnectionsNumStr.get();
	}

	public void setOnlineConnectionsNumStr(String onlineConnectionsNumStr) {
		this.onlineConnectionsNumStr.setValue(onlineConnectionsNumStr);
	}

	public int getConnectionsNum() {
		return connectionsNum;
	}
	
	public void setConnectionsNum(int connectionsNum) {
		this.connectionsNum = connectionsNum;
		setConnectionsNumStr(""+connectionsNum);
	}
	
	public int getOnlineConnectionsNum() {
		return onlineConnectionsNum;
	}
	
	public void setOnlineConnectionsNum(int onlineConnectionsNum) {
		this.onlineConnectionsNum = onlineConnectionsNum;
		setOnlineConnectionsNumStr(""+onlineConnectionsNum);
	}
}
