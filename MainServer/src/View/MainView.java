package View;


import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import ViewModel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;



public class MainView implements Observer,Initializable {

	private MainViewModel viewModel;
	private Boolean isServerRunning;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		isServerRunning = false;
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
		isServerRunning = viewModel.interrupt(isServerRunning);
	}
	
}
