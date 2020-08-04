package controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBoxController {

    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(650);
        window.setMaxHeight(400);
        Label label = new Label();
        label.setText(message);

        Button closeButton = new Button();
        closeButton.setText("Close the window");
        closeButton.setOnAction(e -> window.close());


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMaxWidth(650);
        anchorPane.setMaxHeight(400);
        anchorPane.getChildren().addAll(label, closeButton);
        anchorPane.setStyle("-fx-background-color: #fafafa;");

        Scene scene = new Scene(anchorPane);
        window.setScene(scene);
        window.showAndWait();


    }
}
