package DB;

import javax.persistence.*;
@Entity(name = "Contacts")
public class Contact implements IDBEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "FriendId")
    private User friend;
    
    
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUserId() {
		return user;
	}
	public void setUserId(User userId) {
		this.user = userId;
	}
	public User getFriendId() {
		return friend;
	}
	public void setFriendId(User friendId) {
		this.friend = friendId;
	}
	public Contact(User userId, User friendId) {
		super();
		this.user = userId;
		this.friend = friendId;
	}
	@Override
	public void update(IDBEntity other) {
		// TODO Auto-generated method stub
		if(other.getClass() == this.getClass())
		{
			this.user = ((Contact)other).user;
			this.friend = ((Contact)other).friend;
		}
		
	}
	
    
}
