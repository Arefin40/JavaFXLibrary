package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import library.InvalidMemberException;

public class Login implements Initializable {
   private boolean librarianForm = false;

   @FXML
   private VBox loginPage;

   @FXML
   private Label idLabel, idError, passwordError;

   @FXML
   private TextField idField;

   @FXML
   private PasswordField passwordField;

   @FXML
   private Button loginButton, switchRoleButton;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      idError.managedProperty().bind(idError.visibleProperty());
      passwordError.managedProperty().bind(passwordError.visibleProperty());
   }

   @FXML
   void switchRole(ActionEvent event) {
      librarianForm = !librarianForm;
      if (librarianForm) {
         idLabel.setText("Enter Username");
         loginButton.setText("Login as a Librarian");
         switchRoleButton.setText("Login as a Member");
      } else {
         idLabel.setText("Enter Member ID");
         loginButton.setText("Login as a Member");
         switchRoleButton.setText("Login as a Librarian");
      }

      resetForm();
   }

   @FXML
   void login(ActionEvent event) {
      String id = idField.getText();
      String password = passwordField.getText();
      idError.setVisible(false);
      passwordError.setVisible(false);

      if (librarianForm) {
         if (!id.equals("admin")) {
            idError.setText("Invalid Username!");
            idError.setVisible(true);
            return;
         }
         if (!password.equals("204")) {
            passwordError.setVisible(true);
            return;
         }
         Router.isLibrarian = true;
         loadApp();
      } else {
         try {
            Router.loggedInMember = App.library.findMember(id);
         } catch (InvalidMemberException e) {
            idError.setVisible(true);
            idError.setText(e.getMessage().split(":")[0]);
            return;
         }
         if (!password.equals("204")) {
            passwordError.setVisible(true);
            return;
         }
         Router.isLibrarian = false;
         loadApp();
      }
   }

   private void loadApp() {
      try {
         Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
         App.stage.getScene().setRoot(root);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void resetForm() {
      idField.clear();
      passwordField.clear();
      idError.setVisible(false);
      passwordError.setVisible(false);
   }
}
