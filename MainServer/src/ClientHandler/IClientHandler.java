package ClientHandler;


import com.corundumstudio.socketio.SocketIOClient;

import ViewModel.IController;

public interface IClientHandler {
	void handleClient(SocketIOClient client , IController controller, String data);
	void connectionSetUp(SocketIOClient client, IController controller, String data);
	void sendToClient(SocketIOClient client,String event, Object obj);

}
