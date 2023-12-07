package application;

import library.Member;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class Router {
   public static boolean isLibrarian = false;
   public static String currentPage = "Login";
   public static Member loggedInMember = null;
   public static StackPane window = null;
   public static VBox rootContainer = null;
   public static AppController appController = null;
   public static Object pageController = null;
   public static StringProperty url = new SimpleStringProperty("Loginpage");

   public static void start() {
      url.addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observable, String oldPage, String newPage) {
            if (window != null) {
               window.getStyleClass().removeAll("home");
            }

            if (newPage.equals("Loginpage")) {
               isLibrarian = false;
               loggedInMember = null;
               try {
                  Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                  App.stage.getScene().setRoot(root);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            } else {
               String[] pageURL = newPage.split(":");
               currentPage = pageURL[0];
               String fxmlName = pageURL[1];

               // Load Page
               try {
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/" + fxmlName + ".fxml"));
                  Parent page = loader.load();
                  pageController = loader.getController();
                  rootContainer.getChildren().setAll(page);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
      });
   }
}
