package ClientHandler;

import java.net.Socket;

import com.corundumstudio.socketio.SocketIOClient;

import ViewModel.IController;

public class ClientHandler implements IClientHandler{

	public ClientHandler() {
		super();
	}

	@Override
	public void handleClient(SocketIOClient client, IController controller, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionSetUp(SocketIOClient client, IController controller, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendToClient(SocketIOClient client, String event, Object obj) {
		// TODO Auto-generated method stub
		
	}
	

	
	

}
