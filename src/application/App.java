package application;

import library.Library;
import library.DataHandler;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
   public static Library library = null;
   public static Stage stage = null;

   @Override
   public void start(Stage primaryStage) {
      try {
         Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
         Scene scene = new Scene(root);
         primaryStage.setTitle("Java Library Project");
         primaryStage.setScene(scene);
         primaryStage.show();

         primaryStage.setOnCloseRequest(event -> {
            if (DataHandler.saveData(library))
               System.out.println("App has been closed. Successfully saved all data.");
            else
               System.out.println("Failed to save data.");
         });
         stage = primaryStage;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      try {
         library = DataHandler.loadData();
         System.out.println("Data loaded successfully.");
      } catch (ClassNotFoundException | IOException e) {
         System.out.println("Data loading failed! Initializing as a new instance.");
         library = new Library("UAP Central Library");
      }

      Router.start();
      launch(args);
   }
}