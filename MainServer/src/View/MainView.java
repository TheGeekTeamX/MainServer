package View;


import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import ViewModel.ConnectionData;
import ViewModel.MainViewModel;
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		isServerRunning = false;
		state.setFill(Color.RED);
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


	
	public MainViewModel getViewModel() {
		return viewModel;
	}

	public void setViewModel(MainViewModel viewModel) {
		this.viewModel = viewModel;
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
	}
	
}
