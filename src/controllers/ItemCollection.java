package controllers;

import application.App;
import application.Router;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import library.Item;

public class ItemCollection implements Initializable {
   private String itemType = "all";
   private ObservableList<Item> itemList = FXCollections.observableArrayList();

   public void addItem(Item item) {
      this.itemList.add(item);
   }

   private Parent form;

   @FXML
   private Button addItemButton;

   @FXML
   private TextField searchByTitle, searchByAuthor;

   @FXML
   private VBox table, emptyState;

   @FXML
   private Label pageTitile, titleColumn, categoryColumn, publisherColumn, yearColumn;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      pageTitile.setText(Router.currentPage);

      if (Router.currentPage.equals("Movies")) {
         titleColumn.setText("Title & Directors");
         searchByAuthor.setPromptText("Search By Director");
      } else {
         titleColumn.setText("Title & Authors");
         searchByAuthor.setPromptText("Search By Author");
      }
      addChangeListeners();
      if (!Router.currentPage.equals("Collection")) {
         itemType = Router.currentPage.substring(0, Router.currentPage.length() - 1);
      }
      fetchItems();
   }

   private void addChangeListeners() {
      itemList.addListener(new ListChangeListener<Item>() {
         public void onChanged(Change<? extends Item> change) {
            renderTable();
         }
      });
      searchByTitle.textProperty().addListener((observable, oldValue, newValue) -> {
         fetchItems(newValue, searchByAuthor.getText());
      });
      searchByAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
         fetchItems(searchByTitle.getText(), newValue);
      });
   }

   private void fetchItems() {
      ArrayList<Item> items = null;
      if (itemType.equals("all"))
         items = App.library.getItems();
      else
         items = App.library.findItems(itemType);

      if (items.isEmpty())
         emptyState.setVisible(true);
      else
         itemList.setAll(items);
   }

   private void fetchItems(String title, String author) {
      itemList.setAll(App.library.findItems(title, author, true));
   }

   private void renderTable() {
      emptyState.setVisible(false);
      table.getChildren().clear();
      for (Item item : itemList) {
         String authorsString = item.getAuthors().toString();
         addRow(item.getItemId(), item.getTitle(), item.getCategory(),
               authorsString.substring(1, authorsString.length() - 1),
               String.valueOf(item.getPublishYear()), item.getPublisherName());
      }
   }

   public void addRow(String id, String title, String category, String authors, String publishYear, String publisher) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/rows/ItemCollectionRow.fxml"));
         Parent row = loader.load();
         DataItemCollection rowRoot = (DataItemCollection) loader.getController();
         rowRoot.setData(id, title, authors, category, publisher, publishYear);
         table.getChildren().add(row);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   void search(ActionEvent event) {
      fetchItems(searchByTitle.getText(), searchByAuthor.getText());
   }

   @FXML
   void loadAddItemForm(ActionEvent event) {
      try {
         form = FXMLLoader.load(getClass().getResource("../components/forms/AddItem.fxml"));
         Router.window.getChildren().addLast(form);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void searchBy(String title, String author) {
      searchByTitle.setText(title);
      searchByAuthor.setText(author);
   }

   public void closeForm() {
      Router.window.getChildren().remove(form);
   }
}
