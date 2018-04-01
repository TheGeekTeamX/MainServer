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
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0 == viewModel)
		{
			if (arg1 instanceof ConnectionData)
			{
				
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
	
	private void testConnections()
	{
		int i = 0;
		while(i<10)
		{
			setConnectionsNumStr(""+(i+1));
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*****Get & Set*****/
	public Boolean getIsServerRunning() {
		return isServerRunning;
	}


	public void setIsServerRunning(Boolean isServerRunning) {
		this.isServerRunning = isServerRunning;
	}


	public Circle getState() {
		return state;
	}


	public void setState(Circle state) {
		this.state = state;
	}


	public Label getConnectionsNumLabel() {
		return connectionsNumLabel;
	}


	public void setConnectionsNumLabel(Label connectionsNumLabel) {
		this.connectionsNumLabel = connectionsNumLabel;
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


	public void setConnectionsNumStr(String connectionsNumStr) {
		this.connectionsNumStr.setValue(connectionsNumStr);
	}

}
