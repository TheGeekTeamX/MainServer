package DB;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Users")
public class User implements IDBEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
	@Column(name = "Email")
	private String email;
	@Column(name = "FullName")
	private String fullName;
	@Column(name = "PhoneNumber")
	private String phoneNumber;
	@Column(name = "Country")
	private String country;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public User(String email, String fullName, String phoneNumber, String country) {
		super();
		this.email = email;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.country = country;
	}
	
	@Override
	public String toString(){
		return "User [id="+id+",email="+email+",fullName="+fullName+",phoneNumber="+phoneNumber+",country="+country+"]";
	}
	@Override
	public void update(IDBEntity other) {
		// TODO Auto-generated method stub
		if(other.getClass() == this.getClass())
		{
			this.email = ((User)other).email;
			this.fullName = ((User)other).fullName;
			this.phoneNumber = ((User)other).phoneNumber;
			this.country = ((User)other).country;
		}
		
	}
}
