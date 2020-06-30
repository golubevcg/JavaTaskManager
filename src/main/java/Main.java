import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Authenticator;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "192.168.0.242");


//        System.setProperty("http.proxyHost", "belyaev_a:1234@192.168.0.242");
//        System.setProperty("http.proxyPort", "8080");
//        System.setProperty("proxySet", "true");   //Obsolete ?
//        System.setProperty("http.keepAlive", "false");
//        System.setProperty("java.net.useSystemProxies", "false");
//        System.setProperty("https.proxyHost", "192.168.0.242");
//        System.setProperty("https.proxyPort", "8080");
//        System.setProperty("https.proxyUser", "belyaev_a");
//        System.setProperty("https.proxyPassword", "1234");

//        Authenticator.setDefault(new NtlmAuthenticator(httpsProxyUser, httpsProxyPassword));


        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginWindow.fxml"));
        stage.setScene(new Scene(root));
        stage.show();

//        System.out.println(getClass().getResource("fxml/loginWindow.fxml"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}