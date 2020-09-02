package classes;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UIColorAndStyleSettings {

    private String mainUiBordersColor = "#91afc5";
    private String mainBGUiColor = "#FFFFFF";
    private String minimizeHeighlightButtonColor = "cfdee9";
    private String fontStyleToMenuItems = " -fx-font-size: 14px; -fx-font-family: Arial;";
    private String color1ToMarkTask = "#e2e0c8";
    private String strokeColor1ToMarkTask = "#d0cda7";
    private String color1HighlightedToMarkTask = "#f4f3e9";
    private String color2ToMarkTask = "#c8d7e2";
    private String strokeColor2ToMarkTask = "#a7bfd0";
    private String color2HighlightedToMarkTask = "#e9eff4";
    private String color3ToMarkTask = "#e2c8ca";
    private String strokeColor3ToMarkTask = "#d0a7aa";
    private String color3HighlightedToMarkTask = "#f4e9ea";
    private double strokeWidth = 1.5;

    private String defaultStyleWithBorder = "-fx-background-color: transparent;"+
            "-fx-border-color:" + mainUiBordersColor + ";" +
            "-fx-background-insets: transparent;"+
            "-fx-faint-focus-color: transparent;"+
            "-fx-border-radius: 5;"+
            "-fx-background-radius: 5;"+
            "-fx-border-width: 1.5;";

    private String defaultStyleWITHOUTBorder = "-fx-background-color: transparent;"+
            "-fx-border-color:" + mainBGUiColor + ";" +
            "-fx-background-insets: transparent;"+
            "-fx-faint-focus-color: transparent;"+
            "-fx-border-radius: 5;"+
            "-fx-background-radius: 5;"+
            "-fx-border-width: 1.5;";

    public void setButtonStyles(Button... button){
        UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
        for (int i = 0; i < button.length; i++) {
            Button currentButton = button[i];

            currentButton.setStyle( uiColorAndStyleSettings.getDefaultStyleWITHOUTBorder() );

            currentButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    currentButton.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );
                }
            });

            currentButton.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    currentButton.setStyle( uiColorAndStyleSettings.getDefaultStyleWITHOUTBorder() );
                }
            });
        }

    }

    public void setFontStyleToMenuItems(String fontStyleToMenuItems) {
        this.fontStyleToMenuItems = fontStyleToMenuItems;
    }

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

    public void setImageToMenuItem(MenuItem menuItem, String imageName, int width, int height){
        Image image = new Image(imageName);
        ImageView imageView = new ImageView(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        menuItem.setGraphic(imageView);
    }

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

    public String getFontStyleToMenuItems() {
        return fontStyleToMenuItems;
    }

    public String getColor1ToMarkTask() {
        return color1ToMarkTask;
    }

    public String getStrokeColor1ToMarkTask() {
        return strokeColor1ToMarkTask;
    }

    public String getColor1HighlightedToMarkTask() {
        return color1HighlightedToMarkTask;
    }

    public String getColor2ToMarkTask() {
        return color2ToMarkTask;
    }

    public String getStrokeColor2ToMarkTask() {
        return strokeColor2ToMarkTask;
    }

    public String getColor2HighlightedToMarkTask() {
        return color2HighlightedToMarkTask;
    }

    public String getColor3ToMarkTask() {
        return color3ToMarkTask;
    }

    public String getStrokeColor3ToMarkTask() {
        return strokeColor3ToMarkTask;
    }

    public String getColor3HighlightedToMarkTask() {
        return color3HighlightedToMarkTask;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }
}
