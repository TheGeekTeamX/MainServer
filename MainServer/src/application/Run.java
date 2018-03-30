package application;

import Model.MainModel;
import View.MainView;
import ViewModel.MainViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import DB.Employee;

public class Run extends Application {
	private static SessionFactory factory;

	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		BorderPane rootView;
		try {
			MainModel model = MainModel.getInstance();// MODEL
			MainViewModel viewModel = new MainViewModel(model);// VIEWMODEL
			model.addObserver(viewModel);
			rootView = loader.load(getClass().getResource("View.fxml").openStream());
			MainView view = loader.getController();// VIEW
			viewModel.addObserver(view);
			view.setViewModel(viewModel);

			// BorderPane root = new BorderPane();
			Scene scene = new Scene(rootView, 800, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private static int addEmployee(String fName, String lName, double salary) {
		Employee emp = new Employee(fName, lName, salary);
		Transaction tx = null;
		int empID = 0;
		Session session = factory.openSession();
		try {
			tx = session.beginTransaction();
			empID = (Integer) session.save(emp);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return empID;
	}

	// Method to print all employees
	private static void printEmployees() {
		Session session = factory.openSession();
		try {
			Query<Employee> query = session.createQuery("from Employees");
			List<Employee> list = query.list();
			for (Employee e : list) {
				System.out.println(e);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	// Method to print all employees whose names start with specified prefix

	private static void printEmployeesWhoseNameStartsWith(String prefix) {
		Session session = factory.openSession();
		Query query = session.createQuery("from Employees E where E.first_name LIKE :prefix");
		query.setParameter("prefix", prefix + "%");
		List<Employee> list = query.list();
		for (Employee e : list) {
			System.out.println(e);
		}
		session.close();
	}

	// Method to update a salary for an employee
	private static void updateSalary(int empId, double salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee emp = session.get(Employee.class, empId);
			emp.setSalary(salary);
			session.update(emp);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	// Method to delete an employee
	private static void deleteEmployee(int empId) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee emp = session.get(Employee.class, empId);
			session.delete(emp);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
