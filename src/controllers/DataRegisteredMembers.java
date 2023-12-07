package controllers;

import application.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DataRegisteredMembers {

   @FXML
   private Label memberID, memberName, totalCheckouts;

   @FXML
   void viewCheckoutRecords(ActionEvent event) {
      Router.url.set("Member Checkout Record:CheckoutRecords");
      ((CheckoutRecords) Router.pageController).display(memberID.getText());
   }

   public void setData(String id, String name, int nCheckouts) {
      memberID.setText(id);
      memberName.setText(name);
      totalCheckouts.setText(String.valueOf(nCheckouts));
   }
}
