package controllers;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import application.App;
import application.Router;
import library.Item;
import javafx.fxml.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FormAddItem implements Initializable {
   private String currentForm = "Book";
   private String imageURI = "";

   @FXML
   private VBox form, journalCheckbox, publisherBox;

   @FXML
   private Label formTitle, authorLabel, yearLabel;

   @FXML
   private HBox formSwitchGroup;

   @FXML
   private ImageView imageViewField;

   @FXML
   private CheckBox isJournalField;

   @FXML
   private TextField titleField, authorsField, categoryField, publisherField, yearField;

   @FXML
   private Button addItemButton, bookBtn, movieBtn, publicationBtn;

   @FXML
   void close(MouseEvent event) {
      ((ItemCollection) Router.pageController).closeForm();
   }

   @FXML
   void consume(MouseEvent event) {
      event.consume();
   }

   private boolean notEmpty(String value) {
      return !(value == null || value.isBlank());
   }

   @FXML
   void addItem(ActionEvent event) {
      ArrayList<String> authors = new ArrayList<>();
      int publishYear = 2023;
      Item item = null;

      String title = titleField.getText();
      String authorString = authorsField.getText();
      String category = categoryField.getText();
      String yearString = yearField.getText();
      String publisher = publisherField.getText();
      boolean isJournal = isJournalField.isSelected();

      if (yearString.matches("\\d{4}"))
         publishYear = Integer.parseInt(yearString);

      if (notEmpty(title) && notEmpty(authorString) && notEmpty(category)) {
         authors = new ArrayList<>(Arrays.asList(authorString.split(", ")));

         switch (currentForm) {
            case "Book":
               if (notEmpty(publisher)) {
                  item = App.library.addItem(title, category, authors, publishYear, publisher, imageURI);
               }
               break;
            case "Publication":
               if (notEmpty(publisher)) {
                  item = App.library.addItem(title, category, authors, publishYear, isJournal, publisher, imageURI);
               }
               break;
            case "Movie":
               item = App.library.addItem(title, category, authors, publishYear, imageURI);
               break;
         }

         ((ItemCollection) Router.pageController).addItem(item);
         resetForm();
      }
   }

   @FXML
   private boolean chooseImage(ActionEvent event) {
      boolean isSuccessful = false;
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
      File selectedFile = fileChooser.showOpenDialog(null);

      if (selectedFile != null) {
         imageViewField.setImage(new Image(selectedFile.toURI().toString()));
         Random rnd = new Random();
         String imageId = "img-" + rnd.nextInt(10) + rnd.nextInt(10) + rnd.nextInt(10) + rnd.nextInt(10);

         try {
            File destinationFolder = new File("src/images/products");
            if (!destinationFolder.exists()) {
               destinationFolder.mkdirs();
            }
            String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            Path destinationPath = new File(destinationFolder, imageId + fileExtension).toPath();
            Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            imageURI = destinationPath.toUri().toString();
            isSuccessful = true;
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return isSuccessful;
   }

   @FXML
   void switchForm(ActionEvent event) {
      currentForm = ((Button) event.getSource()).getText();
      loadForm();
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      this.currentForm = Router.currentPage.equals("Collection")
            ? "Book"
            : Router.currentPage.substring(0, Router.currentPage.length() - 1);
      formSwitchGroup.setDisable(!Router.currentPage.equals("Collection"));
      loadForm();
      scaleForm();
   }

   private void loadForm() {
      formTitle.setText("Add New " + currentForm);
      addItemButton.setText("Add " + currentForm);

      switch (currentForm) {
         case "Book":
            authorLabel.setText("Authors");
            yearLabel.setText("Publish Year");
            journalCheckbox.setVisible(false);
            publisherBox.setVisible(true);
            publicationBtn.getStyleClass().removeAll("active");
            movieBtn.getStyleClass().removeAll("active");
            bookBtn.getStyleClass().add("active");
            break;

         case "Publication":
            authorLabel.setText("Authors");
            yearLabel.setText("Publish Year");
            journalCheckbox.setVisible(true);
            publisherBox.setVisible(true);
            bookBtn.getStyleClass().removeAll("active");
            movieBtn.getStyleClass().removeAll("active");
            publicationBtn.getStyleClass().add("active");
            break;

         case "Movie":
            authorLabel.setText("Directors");
            yearLabel.setText("Release Year");
            journalCheckbox.setVisible(false);
            publisherBox.setVisible(false);
            bookBtn.getStyleClass().removeAll("active");
            publicationBtn.getStyleClass().removeAll("active");
            movieBtn.getStyleClass().add("active");
            break;
      }
   }

   private void scaleForm() {
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

   private void resetForm() {
      titleField.clear();
      authorsField.clear();
      categoryField.clear();
      yearField.clear();
      publisherField.clear();
      isJournalField.setSelected(false);
      imageViewField.setImage(null);
   }
}
