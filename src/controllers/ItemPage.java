package controllers;

import library.*;
import application.App;
import application.Router;
import components.alert.Alert;
import components.dialog.InputDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemPage implements Initializable {
   private String itemId = null;

   @FXML
   private ImageView imageBox;

   @FXML
   private VBox yearBox, itemRecords;

   @FXML
   private Button checkinBtn, checkoutBtn, extendCheckoutBtn;

   @FXML
   private Label title, descTitle, category, authors, descAuthors, authorLabel, year, publisherLabel, publisher;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      checkoutBtn.managedProperty().bind(checkoutBtn.visibleProperty());
      extendCheckoutBtn.managedProperty().bind(extendCheckoutBtn.visibleProperty());
      checkinBtn.managedProperty().bind(checkinBtn.visibleProperty());
   }

   private void hideByDefault() {
      checkoutBtn.setVisible(false);
      extendCheckoutBtn.setVisible(false);
      checkinBtn.setVisible(false);
   }

   @FXML
   void checkin(ActionEvent event) {
      try {
         App.library.checkIn(this.itemId);
      } catch (InvalidItemException e) {
         new Alert(e.getMessage());
      }
      display(this.itemId);
   }

   @FXML
   void checkout(ActionEvent event) {
      String memberId = "";

      if (Router.isLibrarian) {
         InputDialog inputDialog = new InputDialog("Member ID", "Please enter the registered member id", "Submit");
         memberId = inputDialog.ask();
      } else
         memberId = Router.loggedInMember.getId();

      try {
         App.library.checkOut(this.itemId, memberId);
      } catch (InvalidItemException | InvalidMemberException | CheckedOutException e) {
         new Alert(e.getMessage());
      }
      display(this.itemId);
   }

   @FXML
   void extendCheckout(ActionEvent event) {
      try {
         App.library.extendCheckOut(this.itemId);
      } catch (InvalidItemException | CheckedOutException e) {
         new Alert(e.getMessage());
      }
      display(this.itemId);
   }

   private void handleLibrarianAccess(Item item) {
      if (item.isCheckedOut()) {
         checkinBtn.setVisible(true);
         extendCheckoutBtn.setVisible(true);
      } else {
         checkoutBtn.setVisible(true);
      }
   }

   private void handleMemberAccess(Item item) {
      String memberIdOfLastCheckOut = item.getLatestCheckOutRecord().getMemberId();
      Boolean isCheckOutByLoggedInMember = memberIdOfLastCheckOut.equals(Router.loggedInMember.getId());

      if (!item.isCheckedOut()) {
         checkoutBtn.setVisible(true);
         return;
      }

      if (!isCheckOutByLoggedInMember)
         checkoutBtn.setVisible(true);
      else
         extendCheckoutBtn.setVisible(true);
   }

   public void display(String itemId) {
      this.itemId = itemId;
      hideByDefault();

      try {
         Item item = App.library.findItem(itemId);
         loadItemInfo(item);
         loadCheckoutHistory(item);

         if (Router.isLibrarian)
            handleLibrarianAccess(item);
         else
            handleMemberAccess(item);

      } catch (InvalidItemException e) {
         new Alert(e.getMessage());
      }
   }

   private void loadItemInfo(Item item) {
      String authorString = item.getAuthors().toString().replaceAll("[\\[\\]]", "");
      title.setText(item.getTitle());
      descTitle.setText(item.getTitle());
      category.setText(item.getCategory());
      authors.setText(authorString);
      descAuthors.setText(authorString);
      imageBox.setImage(new Image(item.getImageURI()));

      if (item.getItemId().contains("33-")) {
         authorLabel.setText("Directors");
         publisherLabel.setText("Release year");
         publisher.setText(String.valueOf(item.getPublishYear()));
         yearBox.setVisible(false);
      } else {
         authorLabel.setText("Authors");
         publisherLabel.setText("Publisher");
         publisher.setText(item.getPublisherName() == null ? "" : item.getPublisherName());
         year.setText(String.valueOf(item.getPublishYear()));
         yearBox.setVisible(true);
      }
   }

   private void loadCheckoutHistory(Item item) {
      itemRecords.getChildren().clear();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      String formattedCheckoutDate, formattedCheckinDate;
      ArrayList<CheckOutRecord> checkOutRecords = item.getCheckOutRecords();
      try {
         if (!checkOutRecords.isEmpty()) {
            for (CheckOutRecord record : checkOutRecords) {
               Member member = App.library.findMember(record.getMemberId());
               FXMLLoader loader = new FXMLLoader(
                     getClass().getResource("../components/rows/ItemPageItemRecord.fxml"));
               Parent node = loader.load();
               ItemPageItemRecord nodeRoot = (ItemPageItemRecord) loader.getController();

               formattedCheckoutDate = record.getCheckOutDate().format(formatter);
               formattedCheckinDate = record.getCheckInDate() != null
                     ? record.getCheckInDate().format(formatter)
                     : record.getExpectedCheckInDate().format(formatter);
               nodeRoot.setData(member.getName(), formattedCheckoutDate, formattedCheckinDate);
               itemRecords.getChildren().add(node);
            }
         }
      } catch (InvalidMemberException e) {
         new Alert(e.getMessage());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
