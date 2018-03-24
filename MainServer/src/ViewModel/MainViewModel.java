package ViewModel;

import java.util.Observable;
import java.util.Observer;

import Model.MainModel;

public class MainViewModel extends Observable implements Observer {
	private MainModel model;

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public MainViewModel(MainModel model) {
		super();
		this.model = model;
	}

}
