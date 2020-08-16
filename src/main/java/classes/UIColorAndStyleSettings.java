package classes;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UIColorAndStyleSettings {

    private String mainUiBordersColor = "#91afc5";

    private String mainBGUiColor = "#FFFFFF";

    private String defaultStyleWithBorder = "-fx-background-color: transparent;"+
            "-fx-border-color:" + mainUiBordersColor + ";" +
            "-fx-background-insets: transparent;"+
            "-fx-faint-focus-color: transparent;"+
            "-fx-border-radius: 5;"+
            "-fx-background-radius: 5;"+
            "-fx-border-width: 1.5;"+
            "-fx-padding: 10 10 10 10;";

    private String defaultStyleWITHOUTBorder = "-fx-background-color: transparent;"+
            "-fx-border-color:" + mainBGUiColor + ";" +
            "-fx-background-insets: transparent;"+
            "-fx-faint-focus-color: transparent;"+
            "-fx-border-radius: 5;"+
            "-fx-background-radius: 5;"+
            "-fx-border-width: 1.5;"+
            "-fx-padding: 10 10 10 10;";

    public String getMainUiBordersColor() {
        return mainUiBordersColor;
    }

    public String getDefaultStyleWithBorder() {
        return defaultStyleWithBorder;
    }

    public String getMainBGUiColor() {
        return mainBGUiColor;
    }

    public String getDefaultStyleWITHOUTBorder() {
        return defaultStyleWITHOUTBorder;
    }

    public void setImageToButton(Button button, String imageName, int width, int height){
        Image image = new Image(imageName);
        ImageView imageView = new ImageView(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        button.setGraphic(imageView);
    }

    private String minimizeHeighlightButtonColor = "cfdee9";

    public void setCloseAndMinimizeButtonStylesAndIcons(Button closeButton, Button minimiseButton){

        this.setImageToButton(closeButton, "cross.png", 11,20);
        this.setImageToButton(minimiseButton, "minimize.png", 13,40);


        closeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeButton.setStyle("-fx-background-color:#F87272");
            }
        });
        closeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeButton.setStyle("-fx-background-color: transparent");
            }
        });

        minimiseButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                minimiseButton.setStyle("-fx-background-color:#" + minimizeHeighlightButtonColor);
            }
        });
        minimiseButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                minimiseButton.setStyle("-fx-background-color: transparent");
            }
        });

    }

    public void setDefaultStylesToButtonsAndOnMouseEnteredAndExited(Button button){

        UIColorAndStyleSettings uiColorAndStyleSettings = this;

        button.setStyle( uiColorAndStyleSettings.getDefaultStyleWITHOUTBorder());


        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setStyle( uiColorAndStyleSettings.getDefaultStyleWITHOUTBorder() );
            }
        });
    }
}
