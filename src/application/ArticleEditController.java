/**
 * 
 */
package application;

import java.io.FileWriter;
import java.io.IOException;

import javax.json.JsonObject;

import application.news.Article;
import application.news.Categories;
import application.news.User;
import application.utils.JsonArticle;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import serverConection.ConnectionManager;
import serverConection.exceptions.ServerCommunicationError;

/**
 * 
 * @author AngelLucas Manon Epplée Janine Haschke
 * 
 */
public class ArticleEditController {
	/**
	 * Connection used to send article to server after editing process
	 */
	private ConnectionManager connection;

	/**
	 * Instance that represent an article when it is editing
	 */
	private ArticleEditModel editingArticle;
	/**
	 * User whose is editing the article
	 */
	private User usr;

	// TODO add attributes and methods as needed
	@FXML
	private Label username;

	// TODO add attributes and methods as needed

	@FXML
	private TextField title;
	@FXML
	private TextField subtitle;
	@FXML
	private MenuButton category;
	@FXML
	private ImageView image;
	@FXML
	private HTMLEditor htmlEditor;
	@FXML
	private TextArea textEditor;

	private Boolean showBody;
	private Boolean showHTML;
	@FXML
	private Button btnText;
	@FXML
	private Button btnEditor;
	@FXML
	private Button btnWrite;

	@FXML
	void initialize() {
		this.showBody = true;
		this.showHTML = true;

	}

	@FXML
	void onImageClicked(MouseEvent event) {
		if (event.getClickCount() >= 2) {
			Scene parentScene = ((Node) event.getSource()).getScene();
			FXMLLoader loader = null;
			try {
				loader = new FXMLLoader(getClass().getResource(AppScenes.IMAGE_PICKER.getFxmlFile()));
				Pane root = loader.load();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Window parentStage = parentScene.getWindow();
				Stage stage = new Stage();
				stage.initOwner(parentStage);
				stage.setScene(scene);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.showAndWait();
				ImagePickerController controller = loader.<ImagePickerController>getController();
				Image image = controller.getImage();
				if (image != null) {
					editingArticle.setImage(image);
					this.image.setImage(image);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@FXML
	public void onSend(ActionEvent event) {
		if (this.sendBack()) {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
		}
		return;

	}

	@FXML
	public void onWrite(ActionEvent event) {
		if (this.write()) {
			Alert alert = new Alert(AlertType.INFORMATION, "File successfully saved", ButtonType.OK);
			alert.showAndWait();
		}
		;
	}

	/**
	 * Send and article to server, Title and category must be defined and category
	 * must be different to ALL
	 * 
	 * @return true if only if article was been correctly send
	 */
	private boolean sendBack() {

		String textTitle = this.title.getText(); // TODO Get article title
		Categories choosenCategory = Categories.valueOf(this.category.getText().toUpperCase()); // TODO Get article
																								// category
		if (textTitle == null || choosenCategory == null || textTitle.equals("") || choosenCategory == Categories.ALL) {
			Alert alert = new Alert(AlertType.ERROR, "Imposible send the article!! Title and categoy are mandatory",
					ButtonType.OK);
			alert.showAndWait();
			return false;
		}

		// TODO prepare and send using connection.saveArticle( ...)
		String textAbstract = null;
		String textBody = null;
		Article article = new Article();

		if (this.textEditor.isVisible()) {
			textAbstract = this.textEditor.getText();
			textBody = this.textEditor.getText();
		} else {
			textAbstract = this.htmlEditor.getHtmlText();
			textBody = this.htmlEditor.getHtmlText();
		}

		this.editingArticle.titleProperty().set(textTitle);
		this.editingArticle.subtitleProperty().set(this.subtitle.getText());
		this.editingArticle.abstractTextProperty().set(textAbstract);
		this.editingArticle.bodyTextProperty().set(textBody);
		this.editingArticle.setCategory(choosenCategory);
		System.out.println(textAbstract);
		System.out.println(textBody);

		Image imageData = image.getImage();
		if (imageData != null) {
			article.setImageData(imageData);
		}
		try {
			this.editingArticle.commit();
			connection.saveArticle(this.getArticle());
		} catch (ServerCommunicationError e) {
			e.printStackTrace();
		}

		return true;

	}

	/**
	 * This method is used to set the connection manager which is needed to save a
	 * news
	 * 
	 * @param connection connection manager
	 */
	void setConnectionMannager(ConnectionManager connection) {
		this.connection = connection;
		// TODO enable send and back button
	}

	/**
	 * 
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		this.usr = usr;

		// TODO Update UI and controls
		if (usr == null) {
			return; // Not logged user
		}
		this.username.setText(usr.getLogin());

	}

	/**
	 * Get the article without changes since last commit
	 * 
	 * @return article without changes since last commit
	 */
	Article getArticle() {
		Article result = null;
		if (this.editingArticle != null) {
			result = this.editingArticle.getArticleOriginal();
		}
		return result;
	}

	/**
	 * PRE: User must be set
	 * 
	 * @param article the article to set
	 */
	void setArticle(Article article) {
		this.editingArticle = (article != null) ? new ArticleEditModel(article) : new ArticleEditModel(usr);

		if (article.getImageData() != null) {
			this.image.setImage(article.getImageData());
		}

		if (article != null) {
			this.title.setText(article.getTitle());
			this.subtitle.setText(article.getSubtitle());
			this.category.setText(article.getCategory());

			this.textEditor.setText(article.getAbstractText());
			this.htmlEditor.setHtmlText(article.getAbstractText());
			this.htmlEditor.setHtmlText(article.getBodyText());
		}

	}

	/**
	 * Save an article to a file in a json format Article must have a title
	 * 
	 * @return
	 */
	private boolean write() {
		// TODO prepare and send using connection.saveArticle( ...)

		String textTitle = this.title.getText();
		if (textTitle == null || textTitle.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION, "Enter a title to save the article.");
			alert.showAndWait();
		} else {
			Categories choosenCategory = Categories.valueOf(this.category.getText().toUpperCase());
			String textAbstract;
			String textBody;
			if (this.textEditor.isVisible()) {
				textAbstract = this.textEditor.getText();
				textBody = this.textEditor.getText();
			} else {
				textAbstract = this.htmlEditor.getHtmlText();
				textBody = this.htmlEditor.getHtmlText();
			}

			this.editingArticle.titleProperty().set(textTitle);
			this.editingArticle.subtitleProperty().set(this.subtitle.getText());
			this.editingArticle.abstractTextProperty().set(textAbstract);
			this.editingArticle.bodyTextProperty().set(textBody);
			this.editingArticle.setCategory(choosenCategory);

			this.editingArticle.commit();
			// Removes special characters not allowed for filenames
			String name = this.getArticle().getTitle().replaceAll("\\||/|\\\\|:|\\?", "");
			String fileName = "saveNews//" + name + ".news";
			JsonObject data = JsonArticle.articleToJson(this.getArticle());
			try (FileWriter file = new FileWriter(fileName)) {
				file.write(data.toString());
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@FXML
	public void switchText(ActionEvent event) {
		if (this.textEditor.isVisible()) {
			if (showBody) {
				this.showBody = false;
				this.textEditor.setText(this.editingArticle.getBodyText());
				this.btnText.setText("Show abstract");
				return;
			} else {
				this.showBody = true;
				this.textEditor.setText(this.editingArticle.getAbstractText());
				this.btnText.setText("Show body");
			}

		} else if (this.htmlEditor.isVisible()) {
			if (showBody) {
				this.showBody = false;
				this.htmlEditor.setHtmlText(this.editingArticle.getBodyText());
				this.btnText.setText("Show abstract");
				return;
			} else {
				this.showBody = true;
				this.htmlEditor.setHtmlText(this.editingArticle.getAbstractText());
				this.btnText.setText("Show body");
			}

		} else {
			return;
		}
	}

	@FXML
	public void switchEditor(ActionEvent event) {
		if (showHTML) {
			this.showHTML = false;
			this.textEditor.setVisible(false);
			this.htmlEditor.setVisible(true);
			this.btnEditor.setText("Show Text");

		} else {
			this.showHTML = true;
			this.textEditor.setVisible(true);
			this.htmlEditor.setVisible(false);
			this.btnEditor.setText("Show HTML");
		}
	}

	@FXML
	public void selectAll(ActionEvent event) {
		this.editingArticle.setCategory(Categories.ALL);
		this.category.setText("All");
	}

	@FXML
	public void selectNational(ActionEvent event) {
		this.editingArticle.setCategory(Categories.NATIONAL);
		this.category.setText("National");
	}

	@FXML
	public void selectInternational(ActionEvent event) {
		this.editingArticle.setCategory(Categories.INTERNATIONAL);
		this.category.setText("International");
	}

	@FXML
	public void selectSport(ActionEvent event) {
		this.editingArticle.setCategory(Categories.SPORTS);
		this.category.setText("Sport");
	}

	@FXML
	public void selectEconomy(ActionEvent event) {
		this.editingArticle.setCategory(Categories.ECONOMY);
		this.category.setText("Economy");
	}

	@FXML
	public void goBack(ActionEvent event) {
		// this.editingArticle.discardChanges();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
