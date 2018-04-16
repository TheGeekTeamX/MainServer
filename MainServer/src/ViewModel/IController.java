package ViewModel;

import com.corundumstudio.socketio.SocketIOClient;

import Enums.RequestType;
import Requests.RequestData;
import Responses.ResponseData;

public interface IController {
	ResponseData execute(RequestData ReqData);
	Boolean addConnection(SocketIOClient client, String userName, String password);

}
