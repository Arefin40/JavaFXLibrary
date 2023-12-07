package controllers;

import java.time.format.DateTimeFormatter;
import application.App;
import application.Router;
import components.alert.Alert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import library.CheckOutRecord;
import library.CheckedOutException;
import library.InvalidItemException;
import library.InvalidMemberException;

public class DataCheckoutRecord {
   private String itemId, memberId;

   @FXML
   private HBox actionBox;

   @FXML
   private GridPane gridPane;

   @FXML
   private Label column1, column2, checkoutDate, expectedCheckoutDate, checkinDate;

   @FXML
   void checkin(ActionEvent event) {
      try {
         App.library.checkIn(itemId);
      } catch (InvalidItemException e) {
         new Alert(e.getMessage());
      }
   }

   @FXML
   void extendCheckout(ActionEvent event) {
      try {
         App.library.extendCheckOut(itemId);
      } catch (InvalidItemException | CheckedOutException e) {
         new Alert(e.getMessage());
      }
   }

   private boolean isItemRecord() {
      return Router.currentPage.equals("Item Checkout Record");
   }

   private void handleControls() {
      if ((isItemRecord() && !Router.isLibrarian) || (!isItemRecord() && Router.isLibrarian)) {
         gridPane.getColumnConstraints().remove(5);
         gridPane.getChildren().remove(5);
      }
   };

   private void handleDates(CheckOutRecord record) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      this.checkoutDate.setText(record.getCheckOutDate().format(formatter));
      this.expectedCheckoutDate.setText(record.getExpectedCheckInDate().format(formatter));
      if (record.getCheckInDate() != null) {
         this.checkinDate.setText(record.getCheckInDate().format(formatter));
      } else {
         this.checkinDate.setText("");
      }
   }

   public void setData(CheckOutRecord record) {
      handleControls();
      try {
         itemId = record.getItemId();
         memberId = record.getMemberId();
         String itemTitle = App.library.findItem(itemId).getTitle();
         String memberName = App.library.findMember(memberId).getName();
         if (!(Router.isLibrarian || isItemRecord())) {
            column1.setText(itemTitle);
            column2.setText(memberId);
         } else {
            column1.setText(memberName + " (" + memberId + ")");
            column2.setText(itemId);
         }
         handleDates(record);
      } catch (InvalidItemException | InvalidMemberException e) {
         new Alert(e.getMessage());
      }
   }
}
