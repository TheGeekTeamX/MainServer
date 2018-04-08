package ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import DB.Contact;
import DB.Credential;
import DB.User;
import Enums.DBEntityType;
import Model.MainModel;
import Requests.*;
import Responses.*;
import ResponsesEntitys.UserData;

public class MainViewModel extends Observable implements Observer,IController {
	private MainModel model;
	
	public ResponseData EditUser(RequestData reqData)
	{
		User user = model.getDbManager().getUser(reqData.getUserEmail());
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		//edit profile picture
		user.setCountry(((EditUserRequestData)reqData).getCountry());
		user.setPhoneNumber(((EditUserRequestData)reqData).getPhoneNumber());
		user.setFullName(((EditUserRequestData)reqData).getFullName());	
		return new EditResponseData(model.getDbManager().editInDataBase(user.getId(), DBEntityType.User, user));
	}
	
	private ResponseData EditContactsList(RequestData reqData)
	{
		User user = model.getDbManager().getUser(reqData.getUserEmail());
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		ArrayList <Contact> currentContactsList = model.getDbManager().getContactsList(user.getId());
		if (currentContactsList.size() == 0)
			return new EditResponseData(false);
		LinkedList <String> newContactsList = ((EditContactsListRequestData)reqData).getUpdatedFriendsList();
		if (newContactsList.size() == 0)
			return new EditResponseData(false);
		currentContactsList.forEach(contact -> {
			if(!newContactsList.contains(""+contact.getFriend().getId()))
			{				
				System.out.println(contact.getId());
				model.getDbManager().deleteFromDataBase(contact.getId(), DBEntityType.Contact);
			}
		});
		
		return new EditResponseData(true);
	}
	
	private ResponseData changePassword(RequestData reqData)
	{
		String oldPass = ((ChangePasswordRequestData)reqData).getOldPassword();
		String newPass = ((ChangePasswordRequestData)reqData).getNewPassword();
		User user = model.getDbManager().getUser(reqData.getUserEmail());
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		Credential credential = model.getDbManager().getCredential(user.getId());
		if(credential == null)
			return new ErrorResponse("Credential Is Not Exist");

		if (!credential.getCredntial().equals(oldPass))
			return new ErrorResponse("Old Password Is Incorrect");

		if (newPass.equals(oldPass))
			return new ErrorResponse("Both Credentials Are The Same");
		credential.setCredntial(newPass);
		Boolean res =model.getDbManager().editInDataBase(credential.getId(), DBEntityType.Credential, credential);
		return new EditResponseData(true);
	}
	
	private ResponseData addFriend(RequestData reqData)
	{
		User user = model.getDbManager().getUser(reqData.getUserEmail());
		User friend = model.getDbManager().getUser(((AddFriendRequestData)reqData).getFriendMail());
		
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		else if(friend == null)
			return new ErrorResponse("Friend Is Not Exist");
		else if(user.getId() == friend.getId())
			return new ErrorResponse("Both Users Are The Same");
		else
		{
			model.getDbManager().addToDataBase(new Contact(user, friend));
			return new AddFriendResponseData(new UserData(friend.getFullName(), friend.getEmail(), "Image URL"));
		}
			
	}
	
	@Override
	public ResponseData execute(RequestData reqData) {
		// TODO Auto-generated method stub
		switch (reqData.getType()) {
		case AddFriendRequest:
			return addFriend(reqData);//checked
		case ChangePasswordRequest:
			return changePassword(reqData);//checked
		case CloseEventRequest:
			break;
		case CreateEventRequest:
			break;
		case CreateUserRequest:
			break;
		case EditContactsListRequest:
			return EditContactsList(reqData);//checked
		case EditUserRequest:
			return EditUser(reqData);//checked
		case EventProtocolRequest:
			break;
		case EventsListRequest:
			break;
		case ContactsListRequest:
			break;
		case LoginRequest:
			break;
		case ProfilePictureRequest:
			break;
		case UpdateProfilePictureRequest:
			break;
		default:
			System.out.println("default");
			break;
		}
		return null;
	}
	
	@Override	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public MainModel getModel() {
		return model;
	}

	public MainViewModel(MainModel model) {
		super();
		this.model = model;
		//model.testDB();
		EditUser(new EditUserRequestData("A", "Sahar Changed", "99999999", "Change", ""));
	}
	
}
