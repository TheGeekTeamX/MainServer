package DB;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeId;
	@Column(name = "FirstName")
	private String first_name;
	@Column(name = "LastName")
	private String last_name;
	private double salary;

	public Employee(String fName, String lName, double salary) {
		setFirst_name(fName);
		setLast_name(lName);
		setSalary(salary);
	}

	public Employee() {
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

@Override
public String toString() {
return "Employee [employeeId=" + employeeId + ",first_name=" + first_name + ", last_name=" + last_name+ ", salary=" + salary + "]";
}
}
