package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import application.App;
import application.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import library.Member;

public class RegisteredMembers implements Initializable {
   private ObservableList<Member> members = FXCollections.observableArrayList();

   public void addMember(Member member) {
      this.members.add(member);
   }

   private Parent form;

   @FXML
   private Label pageTitile;

   @FXML
   private TextField searchByMemberID;

   @FXML
   private VBox table, emptyState;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      members.addListener(new ListChangeListener<Member>() {
         public void onChanged(Change<? extends Member> change) {
            if (!members.isEmpty()) {
               emptyState.setVisible(false);
               renderTable();
            } else {
               emptyState.setVisible(true);
            }
         }
      });
      members.setAll(App.library.getMembers());
      searchByMemberID.textProperty().addListener((observable, oldValue, newValue) -> filterMembers(newValue));
   }

   private void filterMembers(String id) {
      if (!id.isBlank()) {
         ArrayList<Member> foundMembers = new ArrayList<Member>();
         for (Member member : App.library.getMembers()) {
            if (member.getId().startsWith(id)) {
               foundMembers.add(member);
            }
         }
         members.setAll(foundMembers);
      } else
         members.setAll(App.library.getMembers());
   }

   private void renderTable() {
      table.getChildren().clear();
      for (Member member : members) {
         addRow(member.getId(), member.getName(), member.getChekOutRecords().size());
      }
   }

   private void addRow(String id, String name, int nCheckouts) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/rows/RegisteredMembersRow.fxml"));
         Parent row = loader.load();
         DataRegisteredMembers rowRoot = (DataRegisteredMembers) loader.getController();
         rowRoot.setData(id, name, nCheckouts);
         table.getChildren().add(row);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   void search(ActionEvent event) {
      String id = searchByMemberID.getText();
      filterMembers(id);
   }

   @FXML
   void loadAddMemberForm(ActionEvent event) {
      try {
         form = FXMLLoader.load(getClass().getResource("../components/forms/AddMember.fxml"));
         Router.window.getChildren().addLast(form);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void closeForm() {
      Router.window.getChildren().remove(form);
   }
}