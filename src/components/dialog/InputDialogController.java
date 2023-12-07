package components.dialog;

import javafx.fxml.FXML;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InputDialogController {

   @FXML
   private BorderPane inputDialog;

   @FXML
   private TextField input;

   @FXML
   private Label title, informativeText, buttonLabel;

   @FXML
   void close(Event event) {
      handleClose();
   }

   @FXML
   void closeByEnter(KeyEvent e) {
      if (e.getCode() == KeyCode.ENTER)
         handleClose();
   }

   public String getText() {
      return input.getText();
   }

   public void setTitle(String title) {
      this.title.setText(title);
   }

   public void setInformativeText(String informativeText) {
      this.informativeText.setText(informativeText);
   }

   public void setButtonLabel(String buttonLabel) {
      this.buttonLabel.setText(buttonLabel);
   }

   void handleClose() {
      String value = input.getText();
      if (!(value == null || value.isBlank())) {
         Stage dialogStage = (Stage) inputDialog.getScene().getWindow();
         dialogStage.close();
      }
   }

}
