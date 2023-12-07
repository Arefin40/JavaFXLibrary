package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

public class AppController implements Initializable {
   @FXML
   private HBox appbar;

   @FXML
   private VBox container;

   @FXML
   private MenuButton checkoutRecordsButton;

   @FXML
   private Button membersButton, myCheckoutRecordButton;

   @FXML
   private StackPane window;

   @FXML
   void logout(ActionEvent event) {
      Router.url.set("Loginpage");
   }

   @FXML
   void switchPage(ActionEvent e) {
      Button menuItem = ((Button) e.getSource());
      Router.url.set(menuItem.getText() + ":" + menuItem.getId());
   }

   @FXML
   void showCheckoutRecord(ActionEvent e) {
      MenuItem menuItem = (MenuItem) e.getSource();
      Router.url.set(menuItem.getText() + ":CheckoutRecords");
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      Router.rootContainer = container;
      Router.appController = this;
      Router.window = this.window;
      Router.url.set("Home:Homepage");

      membersButton.managedProperty().bind(membersButton.visibleProperty());
      myCheckoutRecordButton.managedProperty().bind(myCheckoutRecordButton.visibleProperty());
      checkoutRecordsButton.managedProperty().bind(checkoutRecordsButton.visibleProperty());

      if (Router.isLibrarian) {
         membersButton.setVisible(true);
         checkoutRecordsButton.setVisible(true);
         myCheckoutRecordButton.setVisible(false);
      } else {
         membersButton.setVisible(false);
         checkoutRecordsButton.setVisible(false);
         myCheckoutRecordButton.setVisible(true);
      }
   }
}
