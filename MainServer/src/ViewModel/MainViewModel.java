package ViewModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import com.corundumstudio.socketio.SocketIOClient;
import ClientHandler.ClientHandler;
import DB.*;
import Enums.*;
import Model.MainModel;
import Requests.*;
import Responses.*;
import ResponsesEntitys.*;
import UpdateObjects.*;

public class MainViewModel extends Observable implements Observer,IController {
	private MainModel model;
	private HashMap<Integer, SocketIOClient> connections;
	
	private ResponseData LeaveEvent(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		ArrayList<User> participants = model.getDbManager().getPariticpants(((LeaveEventRequestData)reqData).getEventId());
		if(participants != null)
		{
			ClientHandler ch = new ClientHandler();
			participants.forEach(p -> {
				SocketIOClient client = connections.get(p.getId());
				if(client != null)
				{
					ch.sendToClient(client, "UserLeft", new UserLeft(user.getFullName()));
				}
			});
		}
		return null;
	}
	
	private void notifyParticipants(Event event)
	{
		ArrayList<User> participants = model.getDbManager().getPariticpants(event.getId());
		ClientHandler ch = new ClientHandler();
		if(participants != null)
		{			
			participants.forEach(p -> {
				SocketIOClient client = connections.get(p.getId());
				if(client != null)
				{
					ch.sendToClient(client, "CloseEvent", new CloseEvent(event.getId()));
				}
			});
		}
	}
	
	private ResponseData CloseEvent(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		//edit in DB
		Event event = (Event) model.getDbManager().get(((CloseEventRequestData)reqData).getEventId(), DBEntityType.Event);
		event.setIsFinished(1);
		if(model.getDbManager().editInDataBase(event.getId(), DBEntityType.Event, event))
			return new ErrorResponse(ErrorType.TechnicalError);
		else
		{			
			//notify to all pariticpants
			notifyParticipants(event);
			return new BooleanResponseData(true);
		}
	}
	
	private ResponseData EventProtocol(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		int eventId = ((EventProtocolRequestData)reqData).getEventID();
		
		return null;
	}
	
	private ResponseData PendingEvents(RequestData reqData)
	{
		User user = getUserFromDB(reqData);
		if(user == null)
			return new ErrorResponse(ErrorType.UserIsNotExist);
		LinkedList<EventData> events = model.getDbManager().getRelatedPendingEvents(user.getId());
		if(events == null)
			return new ErrorResponse(ErrorType.NoPendingEvents);
		return new PendingEventsResponseData(events);
	}
	
	private ResponseData CreateEvent(RequestData reqData)
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
		
		//create Event
		Event e = new Event(user,((CreateEventRequestData)reqData).getTitle(), new Date(Calendar.getInstance().getTime().getTime()), 0, 0);
		if (!(model.getDbManager().addToDataBase(e) > 0))
			return new ErrorResponse(ErrorType.TechnicalError);	
		//create UserEvent
		ArrayList<Integer> ids = new ArrayList<>();
		participants.forEach(p -> {
			ids.add(p.getId());
			model.getDbManager().addToDataBase(new UserEvent(p, e, 0));
		});
		
		sendInvitesToUsers(e, ids);
		
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
			return CloseEvent(reqData);
		case CreateEventRequest:
			return CreateEvent(reqData);
		case CreateUserRequest:
			return CreateUser(reqData);//checked
		case EditContactsListRequest:
			return EditContactsList(reqData);//checked
		case EditUserRequest:
			return EditUser(reqData);//checked
		case EventProtocolRequest:
			return EventProtocol(reqData);
		case EventsListRequest:
			return EventsList(reqData);//checked
		case PendingEventsRequest:
			return PendingEvents(reqData);
		case ContactsListRequest:
			return ContactList(reqData);//checked
		case ProfilePictureRequest:
			return ProfilePicture(reqData);
		case UpdateProfilePictureRequest:
			return UpdateProfilePicture(reqData);
		case LeaveEvent:
			return LeaveEvent(reqData);
		default:
			System.out.println("default");
			break;
		}
		return null;
	}
	
	private ResponseData UpdateProfilePicture(RequestData reqData)
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
	
	private ResponseData ProfilePicture(RequestData reqData)
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
		connections = new HashMap<>();
		//UpdateProfilePicture(new UpdateProfilePictureRequestData("A", "5URLNEW"));
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

	@Override
	public Boolean addConnection(SocketIOClient client, String userName , String password) {
		// TODO Auto-generated method stub
		User user = model.getDbManager().getUser(userName);
		if(user != null)
		{
			Credential credential = model.getDbManager().getCredential(user.getId());
			if(credential != null)
			{
				if(password.equals(credential.getCredntial()))
				{
					connections.put(user.getId(), client);
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void sendInvitesToUsers(Event event, ArrayList<Integer> users)
	{
		ArrayList<String> participants = new ArrayList<>();
		users.forEach(u -> {
			User user = (User) model.getDbManager().get(u, DBEntityType.User);
			participants.add(user.getFullName());
		});
		ClientHandler ch = new ClientHandler();
		users.forEach(u -> {
			SocketIOClient sock = connections.get(u);
			if(sock != null)
			{
				ch.sendToClient(sock, "Invite", new EventInvite(event.getId(), event.getTitle(), participants));
			}
		});
	}
	
	
	
	
}
