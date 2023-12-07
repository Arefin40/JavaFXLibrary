package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemPageItemRecord {
   @FXML
   private Label memberName, checkoutDate, checkinDate;

   public void setData(String memberName, String checkoutDate, String checkinDate) {
      this.memberName.setText(memberName);
      this.checkoutDate.setText(checkoutDate);
      this.checkinDate.setText(checkinDate);
   }
}
