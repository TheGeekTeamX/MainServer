package ClientHandler;

import java.net.Socket;

import ViewModel.IController;

public class ClientHandler implements IClientHandler{
	private IController controller;

	public ClientHandler(IController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void handleClient(Socket socket) {
		// TODO Auto-generated method stub
		
	}
	
	

}
