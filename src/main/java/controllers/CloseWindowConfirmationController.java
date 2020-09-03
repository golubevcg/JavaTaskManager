package controllers;

import Main.Main;
import additionalClasses.LanguageSwitcher;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
        import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CloseWindowConfirmationController extends ControllerParent{

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Label areYouShureLabel;

    @FXML
    private Label appLabel;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    Stage mainControllerStage;
    UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    LanguageSwitcher languageSwitcher;
    MainWindowController mainWindowController;

    public CloseWindowConfirmationController() {

    }

    public void setMainController(MainWindowController mainWindowController) {
        this.mainControllerStage = Main.getStageObj();
        this.mainWindowController = mainWindowController;
        languageSwitcher = mainWindowController.getLanguageSwitcher();
    }

    @FXML
    void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(AnchorPane);

        areYouShureLabel.setText(languageSwitcher.getAreYouShureThatYouWantToClose());
        appLabel.setText(languageSwitcher.getApp());

        yesButton.setText(languageSwitcher.getYes());

        noButton.setText(languageSwitcher.getNo());

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

