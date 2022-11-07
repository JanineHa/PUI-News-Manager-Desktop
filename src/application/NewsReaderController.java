/**
 * 
 */
package application;

import java.io.IOException;
import java.util.Arrays;

import application.news.Article;
import application.news.User;
import application.news.Categories;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import serverConection.ConnectionManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * @author ÃngelLucas
 *
 */
public class NewsReaderController {

	private NewsReaderModel newsReaderModel = new NewsReaderModel();
	private User usr;
	Article Article;


	@FXML
	private ListView<Article> articleListView;
	@FXML
	private ListView<String> menuOptions;
	@FXML
	private FilteredList<Article> filteredArticleList;
	@FXML
	private ComboBox<Categories> categorieList;
	@FXML
	private Button btnDetails;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnNew;
	@FXML
	private Button btnLoad;
	@FXML
	private ImageView imageView;
	@FXML
	private WebView articleAbstract;

	// TODO add attributes and methods as needed

	public NewsReaderController() {
		// TODO
		// Uncomment next sentence to use data from server instead dummy data
		newsReaderModel.setDummyData(true);
		// Get text Label

	}

	private void getData() {
		// TODO retrieve data and update UI
		// The method newsReaderModel.retrieveData() can be used to retrieve data

		newsReaderModel.retrieveData();
		categorieList.getItems().addAll(newsReaderModel.getCategories());
		categorieList.getSelectionModel().selectFirst();

		filteredArticleList = new FilteredList<>(newsReaderModel.getArticles(), article -> true);
		this.articleListView.setItems(filteredArticleList);
		articleListView.getSelectionModel().selectFirst();
		

    	if(this.usr == null) {
    		this.btnNew.setVisible(false);
    		this.btnDelete.setVisible(false);
    		this.btnEdit.setVisible(false);
    		this.btnLoad.setVisible(false);
    	}else {
    		this.btnNew.setVisible(true);
    		this.btnDelete.setVisible(true);
    		this.btnEdit.setVisible(true);
    		this.btnLoad.setVisible(true);
    	}

	}

	@FXML
	private void filterCategorie(ActionEvent event) {
		String filterText = this.categorieList.getValue().toString();
	
		if (filterText == "All") {
			filteredArticleList.setPredicate(article -> true);

		} else {
			filteredArticleList.setPredicate(article -> article.getCategory().equals(filterText));
		}

		this.articleListView.setItems(filteredArticleList);
		articleListView.getSelectionModel().selectFirst();
	}

	/**
	 * @return the usr
	 */
	User getUsr() {
		return usr;
	}

	void setConnectionManager(ConnectionManager connection) {
		this.newsReaderModel.setDummyData(false); // System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		this.getData();
	}

	/**
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {

		this.usr = usr;
		// Reload articles
		this.getData();
		// TODO Update UI
	}

	@FXML
	void initialize() {
		assert articleListView != null : "the List is empty";

		this.articleListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Article>() {
			@Override
			public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
				if (newValue != null) {
					Article = newValue;
					articleAbstract.getEngine().loadContent(Article.getAbstractText());
					imageView.setImage(Article.getImageData());
					Article.setIdArticle(newValue.getIdArticle());
					
				} else { // Nothing selected

				}
			}

		});
	}

	@FXML
	void onOpenDetails(ActionEvent event) {
		try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.NEWS_DETAILS.getFxmlFile()));
			Scene articleScene = new Scene(loader.load());
			ArticleDetailsController controller = loader.<ArticleDetailsController>getController();
			Article article = newsReaderModel.getFullArticle(Article.getIdArticle());
			controller.setArticle(article);
			controller.setUsr(usr);
			primaryStage.setScene(articleScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	void onEdit(ActionEvent event) {
		try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));
			Scene articleScene = new Scene(loader.load());
			ArticleEditController controller = loader.<ArticleEditController>getController();
			Article article = newsReaderModel.getFullArticle(Article.getIdArticle());
			controller.setArticle(article);
			controller.setUsr(usr);
			primaryStage.setScene(articleScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@FXML
	void onNew(ActionEvent event) {
		try {
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));
			Scene articleScene = new Scene(loader.load());
			ArticleEditController controller = loader.<ArticleEditController>getController();
			controller.setUsr(usr);
			primaryStage.setScene(articleScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	void onDelete(ActionEvent event) {
		//please try carefully and only if creating new article is working
		//newsReaderModel.deleteArticle(this.Article);
		getData();
	}
	@FXML
	void onExit(ActionEvent event) {
		System.exit(0);
	}

}
