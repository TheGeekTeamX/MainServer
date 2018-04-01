package ViewModel;

import Enums.RequestType;

public abstract class RequestData {
	private RequestType type;

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

}
