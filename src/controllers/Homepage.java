package controllers;

import application.App;
import application.Router;
import library.Item;
import library.CheckOutRecord;
import library.InvalidItemException;
import components.ItemFrame.ItemFrame;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class Homepage implements Initializable {
   private ArrayList<Item> uncheckedInItems = new ArrayList<>();

   @FXML
   private VBox recentCheckoutSection;

   @FXML
   private TilePane itemContainer;

   @FXML
   private TextField searchByAuthor, searchByTitle;

   @FXML
   void search(ActionEvent event) {
      String title = searchByTitle.getText();
      String author = searchByAuthor.getText();
      Router.url.set("Collection:ItemCollection");
      ((ItemCollection) Router.pageController).searchBy(title, author);
   }

   @FXML
   void showBooks(MouseEvent event) {
      Router.url.set("Books:ItemCollection");
   }

   @FXML
   void showMovies(MouseEvent event) {
      Router.url.set("Movies:ItemCollection");
   }

   @FXML
   void showPublications(MouseEvent event) {
      Router.url.set("Publications:ItemCollection");
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      Router.window.getStyleClass().add("home");
      recentCheckoutSection.managedProperty().bind(recentCheckoutSection.visibleProperty());

      if (Router.isLibrarian)
         return;

      getUncheckedInItems();

      if (uncheckedInItems.isEmpty())
         return;

      recentCheckoutSection.setVisible(true);
      for (Item item : uncheckedInItems) {
         addItemToRecentCollections(item);
      }
   }

   private void getUncheckedInItems() {
      try {
         for (CheckOutRecord record : Router.loggedInMember.getChekOutRecords()) {
            if (record.getCheckInDate() == null) {
               uncheckedInItems.add(App.library.findItem(record.getItemId()));
            }
         }
      } catch (InvalidItemException e) {
         e.printStackTrace();
      }
   }

   private void addItemToRecentCollections(Item item) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/ItemFrame/ItemFrame.fxml"));
         Parent node = loader.load();
         ItemFrame itemFrame = (ItemFrame) loader.getController();
         itemFrame.setData(item);
         itemContainer.getChildren().add(node);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
