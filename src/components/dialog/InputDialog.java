package components.dialog;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputDialog {
   String title, informativeText, buttonLabel, value;

   public void setTitle(String title) {
      this.title = title;
   }

   public void setInformativeText(String informativeText) {
      this.informativeText = informativeText;
   }

   public void setButtonLabel(String buttonLabel) {
      this.buttonLabel = buttonLabel;
   }

   public InputDialog(String title, String buttonLabel) {
      this.title = title;
      this.buttonLabel = buttonLabel;
   }

   public InputDialog(String title, String informativeText, String buttonLabel) {
      this.title = title;
      this.informativeText = informativeText;
      this.buttonLabel = buttonLabel;
   }

   private boolean notEmpty(String value) {
      return !(value == null || value.isBlank());
   }

   public String ask() {
      String inputText = null;
      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      FXMLLoader loader = new FXMLLoader(getClass().getResource("./InputDialog.fxml"));

      try {
         Parent dialog = loader.load();
         InputDialogController dialogController = (InputDialogController) loader.getController();
         if (notEmpty(title))
            dialogController.setTitle(title);
         if (notEmpty(informativeText))
            dialogController.setInformativeText(informativeText);
         if (notEmpty(buttonLabel))
            dialogController.setButtonLabel(buttonLabel);

         dialogStage.setScene(new Scene(dialog));
         dialogStage.showAndWait();
         inputText = dialogController.getText();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return inputText;
   }
}
