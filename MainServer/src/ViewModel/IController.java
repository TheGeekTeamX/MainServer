package ViewModel;

import Enums.RequestType;

public interface IController {
	ResponseData execute(RequestType reqType,RequestData ReqData);

}
