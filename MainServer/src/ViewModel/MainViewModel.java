package ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import DB.Contact;
import DB.Credential;
import DB.Event;
import DB.User;
import Enums.DBEntityType;
import Model.MainModel;
import Requests.*;
import Responses.*;
import ResponsesEntitys.EventData;
import ResponsesEntitys.UserData;

public class MainViewModel extends Observable implements Observer,IController {
	private MainModel model;
	private ResponseData CreateUser(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user != null)
			return new ErrorResponse("This Email Is Registered");
		User u = new User(reqData.getUserEmail(), ((CreateUserRequestData)reqData).getFullName(), ((CreateUserRequestData)reqData).getPhoneNumber(),((CreateUserRequestData)reqData).getCountry());
		if(model.getDbManager().addToDataBase(u) < 0)
			return new ErrorResponse("Technical Error");
		addProfilePicture(reqData);//TODO
		model.getDbManager().addToDataBase(new Credential(u,((CreateUserRequestData)reqData).getCredential()));			
		return new BooleanResponseData(true);
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
			return CreateUser(reqData);//checked
		case EditContactsListRequest:
			return EditContactsList(reqData);//checked
		case EditUserRequest:
			return EditUser(reqData);//checked
		case EventProtocolRequest:
			break;
		case EventsListRequest:
			return EventsList(reqData);//checked
		case ContactsListRequest:
			return ContactList(reqData);//checked
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
		CreateUser(new CreateUserRequestData("B", "BPassword", "OutTalk test", "99999999", "blabla", ""));
	}
	
	private ResponseData ContactList(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		ArrayList <Contact> contactsList = model.getDbManager().getContactsList(user.getId());
		LinkedList<UserData> list = new LinkedList<>();
		contactsList.forEach(c -> {
			list.add(new UserData(c.getFriend().getFullName(), c.getFriend().getEmail(), "TODO"));
		});
		return new ContactsListResponseData(list);
	}
	
	private ResponseData EditUser(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		addProfilePicture(reqData);//TODO
		user.setCountry(((EditUserRequestData)reqData).getCountry());
		user.setPhoneNumber(((EditUserRequestData)reqData).getPhoneNumber());
		user.setFullName(((EditUserRequestData)reqData).getFullName());	
		return new BooleanResponseData(model.getDbManager().editInDataBase(user.getId(), DBEntityType.User, user));
	}
	
	private ResponseData EditContactsList(RequestData reqData)
	{
		User user = model.getDbManager().getUser(reqData.getUserEmail());
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		ArrayList <Contact> currentContactsList = model.getDbManager().getContactsList(user.getId());
		if (currentContactsList.size() == 0)
			return new BooleanResponseData(false);
		LinkedList <String> newContactsList = ((EditContactsListRequestData)reqData).getUpdatedFriendsList();
		if (newContactsList.size() == 0)
			return new BooleanResponseData(false);
		currentContactsList.forEach(contact -> {
			if(!newContactsList.contains(""+contact.getFriend().getId()))
			{				
				System.out.println(contact.getId());
				model.getDbManager().deleteFromDataBase(contact.getId(), DBEntityType.Contact);
			}
		});
		
		return new BooleanResponseData(true);
	}
	
	private ResponseData changePassword(RequestData reqData)
	{
		String oldPass = ((ChangePasswordRequestData)reqData).getOldPassword();
		String newPass = ((ChangePasswordRequestData)reqData).getNewPassword();
		User user = getUserFromDB(reqData);
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
		return new BooleanResponseData(true);
	}
	
	private ResponseData addFriend(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
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
	
	private User getUserFromDB(RequestData reqData)
	{
		return model.getDbManager().getUser(reqData.getUserEmail());
	}
	private void addProfilePicture(RequestData reqData)//TODO
	{
		
	}
	private ResponseData EventsList(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse("User Is Not Exist");
		LinkedList<EventData> eventsList = model.getDbManager().getEventsList(user.getId());
		if(eventsList == null)
			return new ErrorResponse("User Has No Events");
		return new EventsListResponseData(eventsList);
	}
}
