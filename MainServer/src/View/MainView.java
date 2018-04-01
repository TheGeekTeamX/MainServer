package View;


import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import ViewModel.ConnectionData;
import ViewModel.MainViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class MainView implements Observer,Initializable {

	private MainViewModel viewModel;
	private Boolean isServerRunning;
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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		isServerRunning = false;
		initGUIComponents();
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
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0 == viewModel)
		{
			switch(arg1.toString())
			{
				case "New":
					//setConnectionsNum(connectionsNum+1);
					//setOnlineConnectionsNum(onlineConnectionsNum+1);
					break;
				case "End":
					//setOnlineConnectionsNum(onlineConnectionsNum-1);
					break;
			}
		}
	}

	@FXML	
	public void StopStart()
	{
		isServerRunning = viewModel.interrupt(isServerRunning);
		if(isServerRunning)
		{
			state.setFill(Color.LAWNGREEN);
		}
		else
		{
			state.setFill(Color.RED);
		}
		//testConnections();
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
		System.out.println(onlineConnectionsNum);
	}
}
