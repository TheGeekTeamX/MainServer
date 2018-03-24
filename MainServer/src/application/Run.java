package application;
	
import Model.MainModel;
import View.MainView;
import ViewModel.MainViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Run extends Application {
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		BorderPane rootView;
		try {
			MainModel model = new MainModel();//MODEL
			MainViewModel viewModel = new MainViewModel(model);//VIEWMODEL
			model.addObserver(viewModel);
			rootView = loader.load(getClass().getResource("View.fxml").openStream());
			MainView view = loader.getController();//VIEW
			viewModel.addObserver(view);
			view.setViewModel(viewModel);
			
			//BorderPane root = new BorderPane();
			Scene scene = new Scene(rootView,800,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
