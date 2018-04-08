package ClientHandler;

import java.net.Socket;

import ViewModel.IController;

public interface IClientHandler {
	void handleClient(Socket socket , IController controller);

}
