package components.ItemFrame;

import library.Item;
import library.CheckOutRecord;
import application.Router;
import controllers.ItemPage;
import java.time.temporal.ChronoUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemFrame {
   private String itemId;

   @FXML
   private ImageView image;

   @FXML
   private Label title, time;

   @FXML
   void viewDetails(MouseEvent event) {
      Router.url.set("ItemPage:ItemPage");
      ((ItemPage) (Router.pageController)).display(itemId);
   }

   public void setData(Item item) {
      itemId = item.getItemId();
      image.setImage(new Image(item.getImageURI()));
      title.setText(item.getTitle());
      CheckOutRecord latestRecord = item.getLatestCheckOutRecord();
      time.setText(String.valueOf(ChronoUnit.DAYS.between(
            latestRecord.getCheckOutDate(),
            latestRecord.getExpectedCheckInDate())) + " days");
   }
}
