package ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import DB.*;
import Enums.*;
import Model.MainModel;
import Requests.*;
import Responses.*;
import ResponsesEntitys.*;

public class MainViewModel extends Observable implements Observer,IController {
	private MainModel model;

	public ResponseData CreateEvent(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		String[] participantsEmail = ((CreateEventRequestData)reqData).getUsersEmails().split(",");
		int i=0;
		LinkedList<User> participants = new LinkedList<>();
		do {
			User u = model.getDbManager().getUser(participantsEmail[i]);
			if(u!=null)
				participants.add(u);
			i++;
		}while(i < participantsEmail.length);
		
		
		return null;
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
			return Login(reqData);//checked
		case ProfilePictureRequest:
			return ProfilePicture(reqData);
		case UpdateProfilePictureRequest:
			break;
		default:
			System.out.println("default");
			break;
		}
		return null;
	}
	
	public ResponseData UpdateProfilePicture(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		ProfilePicture pp = model.getDbManager().getUserProfilePicture(user.getId());
		if (pp == null)
			return new ErrorResponse(ErrorType.UserHasNoProfilePicture);
		pp.setProfilePictureUrl(((UpdateProfilePictureRequestData)reqData).getNewProfilePictureUrl());
		return new BooleanResponseData(model.getDbManager().editInDataBase(pp.getId(), DBEntityType.ProfilePicture, pp));
	}
	
	public ResponseData ProfilePicture(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		ProfilePicture pp = model.getDbManager().getUserProfilePicture(user.getId());
		if (pp == null)
			return new ErrorResponse(ErrorType.UserHasNoProfilePicture);
		else
			/*Attach Image To Response*/
		return null;
	}
	
	public ResponseData Login(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		Credential credential = model.getDbManager().getCredential(user.getId());
		if (credential == null)
			return new ErrorResponse(ErrorType.TechnicalError);
		if(reqData.getUserEmail().equals((credential.getUser().getEmail())) && ((LoginRequestData)reqData).getPassword().equals(credential.getCredntial()))
			return new BooleanResponseData(true);
		else
			return new ErrorResponse(ErrorType.IncorrectCredentials);
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
		UpdateProfilePicture(new UpdateProfilePictureRequestData("A", "5URLNEW"));
	}
	
	private ResponseData CreateUser(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user != null)
			return new ErrorResponse(ErrorType.EmailAlreadyRegistered);
		User u = new User(reqData.getUserEmail(), ((CreateUserRequestData)reqData).getFullName(), ((CreateUserRequestData)reqData).getPhoneNumber(),((CreateUserRequestData)reqData).getCountry());
		if(model.getDbManager().addToDataBase(u) < 0)
			return new ErrorResponse(ErrorType.TechnicalError);
		addProfilePicture(reqData);//TODO
		model.getDbManager().addToDataBase(new Credential(u,((CreateUserRequestData)reqData).getCredential()));			
		return new BooleanResponseData(true);
	}

	
	private ResponseData ContactList(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
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
			return new ErrorResponse(ErrorType.UserIsNotExist);
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
			return new ErrorResponse(ErrorType.UserIsNotExist);
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
			return new ErrorResponse(ErrorType.UserIsNotExist);
		Credential credential = model.getDbManager().getCredential(user.getId());
		if(credential == null)
			return new ErrorResponse(ErrorType.TechnicalError);

		if (!credential.getCredntial().equals(oldPass))
			return new ErrorResponse(ErrorType.WrongPreviousPassword);

		if (newPass.equals(oldPass))
			return new ErrorResponse(ErrorType.BothPasswordsEquals);
		credential.setCredntial(newPass);
		Boolean res =model.getDbManager().editInDataBase(credential.getId(), DBEntityType.Credential, credential);
		return new BooleanResponseData(true);
	}
	
	private ResponseData addFriend(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		User friend = model.getDbManager().getUser(((AddFriendRequestData)reqData).getFriendMail());
		
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		else if(friend == null)
			return new ErrorResponse(ErrorType.FriendIsNotExist);
		else if(user.getId() == friend.getId())
			return new ErrorResponse(ErrorType.BothUsersEquals);
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
			return new ErrorResponse(ErrorType.UserIsNotExist);
		LinkedList<EventData> eventsList = model.getDbManager().getEventsList(user.getId());
		if(eventsList == null)
			return new ErrorResponse(ErrorType.UserHasNoEvents);
		return new EventsListResponseData(eventsList);
	}
}
