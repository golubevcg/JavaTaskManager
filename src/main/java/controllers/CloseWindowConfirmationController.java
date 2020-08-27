package controllers;

import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CloseWindowConfirmationController extends ControllerParent{

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    Stage mainControllerStage;
    UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();

    public CloseWindowConfirmationController(Stage stage) {
        this.mainControllerStage = stage;
    }


    @FXML
    void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(AnchorPane);

        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((Stage)noButton.getScene().getWindow()).close();
            }
        });

        this.setStylesToButtons();

        AnchorPane.setStyle("-fx-background-color: "+ uiColorAndStyleSettings.getMainBGUiColor() + ";" +
                "-fx-border-color:" + uiColorAndStyleSettings.getMainUiBordersColor() + ";" +
                "-fx-background-insets: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;");
    }

    @Override
    public void setStylesToButtons() {
        uiColorAndStyleSettings.setButtonStyles(noButton,yesButton);
    }
}

