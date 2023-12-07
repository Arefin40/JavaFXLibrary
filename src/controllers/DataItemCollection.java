package controllers;

import application.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DataItemCollection {
   private String itemId;

   @FXML
   private Label title, authors, category, publisher, year;

   @FXML
   void viewDetails(ActionEvent event) {
      Router.url.set("ItemPage:ItemPage");
      ((ItemPage) (Router.pageController)).display(itemId);
   }

   public void setData(String itemId, String title, String authors, String category, String publisher, String year) {
      this.itemId = itemId;
      this.title.setText(title);
      this.authors.setText(authors);
      this.category.setText(category);
      this.publisher.setText(publisher);
      this.year.setText(year);
   }
}
