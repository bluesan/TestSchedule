package schedule;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		Schedule schedule = new Schedule(stage);
		schedule.prefWidthProperty().bind(stage.widthProperty());
		schedule.prefHeightProperty().bind(stage.heightProperty());
		
		stage.setMinWidth(600);
		stage.setMinHeight(500);
		stage.setScene(new Scene(schedule));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
