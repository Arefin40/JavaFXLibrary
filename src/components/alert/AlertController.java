package components.alert;

import controllers.Overley;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

class ModalType {

}

public class AlertController extends Overley {
   private String ERROR = "M24.629 22.507a1.499 1.499 0 1 1-2.122 2.121L18 20.121l-4.507 4.508a1.495 1.495 0 0 1-1.635.325 1.499 1.499 0 0 1-.487-2.447L15.88 18l-4.508-4.507a1.499 1.499 0 1 1 2.122-2.122L18 15.88l4.508-4.508a1.499 1.499 0 1 1 2.12 2.122L20.122 18l4.508 4.507Z";

   private String SUCCESS = "m27.123 14.185-9.75 9.75a1.496 1.496 0 0 1-2.121 0l-4.875-4.875a1.498 1.498 0 0 1 0-2.12 1.498 1.498 0 0 1 2.12 0l3.815 3.814 8.69-8.69a1.498 1.498 0 0 1 2.12 0 1.498 1.498 0 0 1 0 2.121Z";

   @FXML
   private Label title, description;

   @FXML
   private SVGPath icon;

   @FXML
   private HBox iconContainer;

   private void danger() {
      icon.setContent(ERROR);
      iconContainer.getStyleClass().add("danger");
   }

   private void success() {
      icon.setContent(SUCCESS);
      iconContainer.getStyleClass().add("success");
   }

   public void display(String message, String informativeText, boolean error) {
      title.setText(message);
      description.setText(informativeText);

      if (error)
         danger();
      else
         success();
   }
}