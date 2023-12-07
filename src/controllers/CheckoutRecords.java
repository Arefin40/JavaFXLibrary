package controllers;

import library.*;
import application.App;
import application.Router;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CheckoutRecords implements Initializable {
   private boolean isItemRecord;
   private String emptyStateHelperLabelText = "Currently the checkout record is empty.";
   private ObservableList<CheckOutRecord> checkOutRecords = FXCollections.observableArrayList();

   @FXML
   private Label pageTitile, column1Label, column2Label, emptyStateLabel, emptyStateHelperText;

   @FXML
   private GridPane gridPane;

   @FXML
   private TextField searchByID;

   @FXML
   private VBox table, actionBox, searchBox, pageTitleBox, emptyState;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      isItemRecord = Router.currentPage.equals("Item Checkout Record");
      handlePageApperance();

      checkOutRecords.addListener(new ListChangeListener<CheckOutRecord>() {
         public void onChanged(Change<? extends CheckOutRecord> change) {
            populateTable();
         }
      });
      if (Router.currentPage.equals("My Checkout Record"))
         display(Router.loggedInMember.getId());
   }

   private void handlePageApperance() {
      pageTitile.setText(Router.currentPage);
      emptyState.setVisible(true);
      searchBox.managedProperty().bind(searchBox.visibleProperty());
      searchByID.setPromptText(isItemRecord ? "Enter Item ID" : "Enter Member ID");

      if (!(isItemRecord || Router.isLibrarian)) {
         searchBox.setVisible(false);
         pageTitleBox.setAlignment(Pos.CENTER);
         pageTitleBox.setPadding(Insets.EMPTY);
      }

      if ((isItemRecord && !Router.isLibrarian) || (!isItemRecord && Router.isLibrarian)) {
         gridPane.getColumnConstraints().remove(5);
         gridPane.getChildren().remove(5);
      }

      emptyStateHelperLabelText = Router.currentPage.equals("My Checkout Record")
            ? "You haven't checkedout anything."
            : "The member hasn't checkedout anything.";
   }

   public void display(String id) {
      if (!isItemRecord) {
         searchByID.setText(id);
      }

      try {
         ArrayList<CheckOutRecord> records = getCheckoutRecords(id);
         checkOutRecords.setAll(records);
         showEmptyStateIfEmpty(records);
      } catch (InvalidItemException | InvalidMemberException e) {
         String[] message = e.getMessage().split(":");
         emptyState.setVisible(true);
         emptyStateLabel.setText(message[0]);
         emptyStateHelperText.setText(message[1]);
      }
   }

   private ArrayList<CheckOutRecord> getCheckoutRecords(String id) throws InvalidItemException, InvalidMemberException {
      ArrayList<CheckOutRecord> checkoutRecords = new ArrayList<>();
      checkoutRecords = isItemRecord
            ? App.library.findItem(id).getCheckOutRecords()
            : App.library.findMember(id).getChekOutRecords();
      return checkoutRecords;
   }

   private void showEmptyStateIfEmpty(ArrayList<CheckOutRecord> records) {
      if (checkOutRecords.isEmpty()) {
         emptyState.setVisible(true);
         emptyStateLabel.setText("Empty Checkout Record!");
         emptyStateHelperText.setText(emptyStateHelperLabelText);
      }
   }

   private void populateTable() {
      table.getChildren().clear();
      emptyState.setVisible(false);
      if (!(Router.isLibrarian || isItemRecord)) {
         column1Label.setText("Item Title");
         column2Label.setText("Member ID");
      } else {
         column1Label.setText("Member Name");
         column2Label.setText("Item ID");
      }
      for (CheckOutRecord record : checkOutRecords) {
         addRecord(record);
      }
   }

   private void addRecord(CheckOutRecord record) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/rows/CheckoutRecordsRow.fxml"));
         Parent recordRow = loader.load();
         DataCheckoutRecord recordRowRoot = (DataCheckoutRecord) loader.getController();
         recordRowRoot.setData(record);
         table.getChildren().add(recordRow);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   void search(ActionEvent event) {
      String id = searchByID.getText();
      if (!(id == null || id.isBlank())) {
         display(id);
      }
   }
}
