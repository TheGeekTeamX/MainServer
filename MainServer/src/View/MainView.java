package View;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import ClientHandler.ClientHandler;
import ClientHandler.IClientHandler;
import DB.User;
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

	private static SocketIOServer server;
	private MainViewModel viewModel;
	private Boolean isServerRunning;
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
	}

	public void createNewConnection(SocketIOClient client, String data)
	{
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setConnectionsNum(getConnectionsNum()+1);
				setOnlineConnectionsNum(getOnlineConnectionsNum()+1);			
			}
		});	
		IClientHandler ch = new ClientHandler();
		executionPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ch.connectionSetUp(client ,viewModel, data);
				
			}
		});
	}
	
	public void newRequest(SocketIOClient client, String data)
	{
		IClientHandler ch = new ClientHandler();
		executionPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ch.handleClient(client , viewModel , data);
				
			}
		});
	}
	
	
	
	public void startServerThread()
	{
		isServerRunning = true;
		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(8888);
		server = new SocketIOServer(config);
		
		server.addEventListener("Connect", String.class, new DataListener<String>() {

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
				// TODO Auto-generated method stub
				if(isServerRunning)
					createNewConnection(client, data);
			}
		});
		
		server.addEventListener("Request", String.class, new DataListener<String>() {

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
				// TODO Auto-generated method stub
				if (isServerRunning)
				{
					IClientHandler ch = new ClientHandler();
					executionPool.execute(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ch.handleClient(client ,viewModel, data);
							
						}
					});
				}
				
			}
		});
		server.start();
		
		
		
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
