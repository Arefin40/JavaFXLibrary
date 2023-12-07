package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import application.Router;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public abstract class Overley implements Initializable {
   private EventHandler<KeyEvent> keyEventHandler;

   @FXML
   protected VBox overlay, overlayContainer;

   @FXML
   void close(Event event) {
      ((StackPane) overlay.getParent()).getChildren().remove(overlay);
      Router.window.removeEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
   }

   @FXML
   void consume(MouseEvent event) {
      event.consume();
   }

   @Override
   public void initialize(URL arg0, ResourceBundle arg1) {
      keyEventHandler = new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent e) {
            if (e.getCode() == KeyCode.ENTER) {
               ((StackPane) overlay.getParent()).getChildren().remove(overlay);
               Router.window.removeEventHandler(KeyEvent.KEY_PRESSED, this);
            }
         }
      };
      Router.window.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
      performScaleTransition();
   }

   private void performScaleTransition() {
      ScaleTransition st = new ScaleTransition(Duration.millis(200), overlayContainer);
      st.setFromX(0.1);
      st.setFromY(0.1);
      st.setToX(1.0);
      st.setToY(1.0);
      st.setCycleCount(1);
      st.setAutoReverse(false);
      st.setInterpolator(Interpolator.EASE_BOTH);
      st.play();
   }
}
