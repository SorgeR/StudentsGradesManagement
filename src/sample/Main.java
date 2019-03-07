package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.domain.WeekHelper;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./gui/gui/StudentGUI.fxml"));
        Scene scene=new Scene(root);
        String css=Main.class.getResource("./gui/css/StudentGUIStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Students management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        WeekHelper.calculateCurrentWeek();
        launch(args);
    }
}
