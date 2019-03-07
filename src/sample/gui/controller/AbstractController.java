package sample.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class AbstractController {


    protected void goToGrades() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../gui/GradeGUI.fxml"));
            Scene scene = new Scene(root);
            String css = StudentController.class.getResource("../css/GradeGUIStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Grades management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void goToStudents() {

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../gui/StudentGUI.fxml"));
            Scene scene = new Scene(root);
            String css = StudentController.class.getResource("../css/StudentGUIStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Grades management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void goToExemptions() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../gui/ExemptionGUI.fxml"));
            Scene scene = new Scene(root);
            String css = StudentController.class.getResource("../css/ExemptionGUIStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Grades management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void goToHomework() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../gui/HomeworkGUI.fxml"));
            Scene scene = new Scene(root);
            String css = StudentController.class.getResource("../css/HomeworkGUIStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Homeworks management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error");
        message.setContentText(text);
        message.showAndWait();
    }

    void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }
}
