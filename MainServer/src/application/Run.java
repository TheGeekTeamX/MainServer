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

	
}
