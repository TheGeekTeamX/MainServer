package ClientHandler;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.gson.Gson;
import ClientHandler.IClientHandler;
import Requests.*;
import ViewModel.IController;

public class ClientHandler implements IClientHandler {
	private Gson gson;

	public ClientHandler() {
		this.gson = new Gson();
	}

	public void handleClient(SocketIOClient client, IController controller, String data) {

		RequestData rd = gson.fromJson(data, RequestData.class);
		switch (rd.getType()) {
		case AddFriendRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, AddFriendRequestData.class)));
		case ChangePasswordRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, ChangePasswordRequestData.class)));
		case CloseEventRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, CloseEventRequestData.class)));
			break;
		case CreateEventRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, CreateEventRequestData.class)));
			break;
		case CreateUserRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, CreateUserRequestData.class)));
		case EditContactsListRequest:
			sendToClient(client, "Response",controller.execute(gson.fromJson(data, EditContactsListRequestData.class)));
		case EditUserRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, EditUserRequestData.class)));
		case EventProtocolRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, EventProtocolRequestData.class)));
			break;
		case EventsListRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, EventsListRequestData.class)));
		case ContactsListRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, ContactsListRequestData.class)));
		case ProfilePictureRequest:
			sendToClient(client, "Response", controller.execute(gson.fromJson(data, ProfilePictureRequestData.class)));
		case UpdateProfilePictureRequest:
			sendToClient(client, "Response",
					controller.execute(gson.fromJson(data, UpdateProfilePictureRequestData.class)));
			break;
		default:
			System.out.println("default");
			break;
		}
	}

	@Override
	public void connectionSetUp(SocketIOClient client, IController controller, String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendToClient(SocketIOClient client, String event, Object obj) {
		// TODO Auto-generated method stub

		String jsonString = gson.toJson(obj);
		client.sendEvent(event, jsonString);
	}

	
	
}
