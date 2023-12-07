package components.alert;

import java.io.IOException;
import application.Router;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Alert {
   String message, informativeText;

   public Alert(String message) {
      String alert[] = message.split(":");
      this.message = alert[0];
      this.informativeText = alert[1];
      load();
   }

   private void load() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("./Alert.fxml"));
         Parent modal = loader.load();
         AlertController controller = (AlertController) loader.getController();
         controller.display(message, informativeText, true);
         Router.window.getChildren().addLast(modal);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
