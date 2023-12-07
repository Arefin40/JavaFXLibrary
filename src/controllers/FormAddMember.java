package controllers;

import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import application.App;
import application.Router;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import library.Member;

public class FormAddMember implements Initializable {
   @FXML
   private VBox form;

   @FXML
   private TextField memberID;

   @FXML
   private TextField memberName;

   @FXML
   void addMember(ActionEvent event) {
      String id = memberID.getText();
      String name = memberName.getText();

      if (notEmpty(id) && notEmpty(name)) {
         Member member = App.library.addMember(id, name);
         ((RegisteredMembers) Router.pageController).addMember(member);
         resetForm();
      }
   }

   @FXML
   void close(MouseEvent event) {
      ((RegisteredMembers) Router.pageController).closeForm();
   }

   @FXML
   void consume(MouseEvent event) {
      event.consume();
   }

   private void resetForm() {
      memberID.setText("");
      memberName.setText("");
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      ScaleTransition st = new ScaleTransition(Duration.millis(200), form);
      st.setFromX(0.1);
      st.setFromY(0.1);
      st.setToX(1.0);
      st.setToY(1.0);
      st.setCycleCount(1);
      st.setAutoReverse(false);
      st.setInterpolator(Interpolator.EASE_BOTH);
      st.play();
   }

   private boolean notEmpty(String value) {
      return !(value == null || value.isBlank());
   }
}
