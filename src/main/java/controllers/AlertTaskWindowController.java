package controllers;

import additionalClasses.LanguageSwitcher;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import database.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.spi.LocaleNameProvider;

public class AlertTaskWindowController extends ControllerParent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Label label1;

    @FXML
    private Button okButton;


    private MainWindowController mainWindowController;
    private Worker worker;
    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    private LanguageSwitcher languageSwitcher;

    public AlertTaskWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        languageSwitcher = mainWindowController.getLanguageSwitcher();
    }

    @FXML
    void initialize() {


        label1.setText(languageSwitcher.getSuchTaskAlreadyExists());

        WindowEffects.makePaneMoovable(AnchorPane);

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);

        AnchorPane.setStyle("-fx-background-color: "+ uiColorAndStyleSettings.getMainBGUiColor() + ";" +
                "-fx-border-color:" + uiColorAndStyleSettings.getMainUiBordersColor() + ";" +
                "-fx-background-insets: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;");

        okButton.setOnAction(e->{
            okButton.getScene().getWindow().hide();
        });

        uiColorAndStyleSettings.setButtonStyles(okButton);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}

