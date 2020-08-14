package classes;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowEffects {

    public static void setDropShadowToWindow(AnchorPane forDropShadowTopAnchorPane){
        int insets = 10;
        forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        forDropShadowTopAnchorPane.setPadding(new Insets(insets,insets, insets,insets));
        forDropShadowTopAnchorPane.setEffect(new DropShadow());
    }



    private static double xOffset;
    private static double yOffset;
    public static void makePaneMoovable(AnchorPane anchorPane){
        anchorPane.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        anchorPane.setOnMouseDragged(e->{
            ((Stage)(anchorPane.getScene().getWindow())).setX(e.getScreenX() - xOffset);
            ((Stage)(anchorPane.getScene().getWindow())).setY(e.getScreenY() - yOffset);
        });
    }
}
