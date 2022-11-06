/**
 * 
 */
package application;


import java.io.IOException;

import application.news.Article;
import application.news.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * @author AngelLucas
 *
 */
public class ArticleDetailsController {
	//TODO add attributes and methods as needed
		/**
		 * Registered user
		 */
	    private User usr;
	    
	    /**
	     * Article to be shown
	     */
	    private Article article;
	    
	    private Boolean showBody;
	    
	    @FXML
		private Label username;
	    @FXML
	    private Label title;
	    @FXML
	    private Label subtitle;
	    @FXML
	    private Label category;
	    @FXML
	    private ImageView image;
	    @FXML
	    private TextArea text;
	    @FXML
	    private Button btnText;

	    @FXML
		void initialize() { 
			this.showBody = false;
	    }
	    

		/**
		 * @param usr the usr to set
		 */
		void setUsr(User usr) {
			this.usr = usr;
			if (usr == null) {
				return; //Not logged user
			}
			this.username.setText(usr.getLogin());
		}

		/**
		 * @param article the article to set
		 */
		void setArticle(Article article) {
			this.article = article;
			
			if (article.getImageData() != null) {
				this.image.setImage(article.getImageData());
			}
			this.title.setText("Title: " + article.getTitle());
			this.subtitle.setText("Subtitle: " + article.getSubtitle());
			this.category.setText("Category: " + article.getCategory());
			this.text.setText(article.getAbstractText());
		}
		
		@FXML
		public void switchText (ActionEvent event) {
			if (!showBody) {
				this.showBody = true;
				this.text.setText(this.article.getBodyText());
				this.btnText.setText("show abstract");
				return;
			}
			this.showBody = false;
			this.text.setText(this.article.getAbstractText());
			this.btnText.setText("show body");
		}
		
		@FXML
		public void goBack (ActionEvent event) {
	    	try {
				Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
				Scene articleScene = new Scene(loader.load());
				primaryStage.setScene(articleScene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
