package MP3;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AudioPlayer extends Application
{
	private double xOffset = 0; private double yOffset = 0;
	private final Song songModel;
	private MetadataView metaDataView;
	private PlayerControlsView playerControlsView;
	public static void main(String[] args)
	{
		launch(args);
	}
	public AudioPlayer()
	{
		songModel = new Song();
	}

	@Override
	public void start(Stage primaryStage)
	{

		metaDataView = new MetadataView(songModel);
		playerControlsView = new PlayerControlsView(songModel);
		final BorderPane root = new BorderPane();
		BorderPane Header=new BorderPane();
		//Create info bar
		HBox InfoBar=new HBox();
	    InfoBar.setPrefWidth(80);
		Button Close=new Button();
		Button Min=new Button();
		Button Max=new Button();
		InfoBar.getChildren().addAll(Min,Max,Close);
		Min.setPrefHeight(20);
		Min.setPrefWidth(20);
		Min.setId("Min");
		Max.setPrefHeight(20);
		Max.setPrefWidth(20);
		Max.setId("Max");
		Close.setPrefHeight(20);
		Close.setPrefWidth(20);
		Close.setId("Close");
		Header.setRight(InfoBar);
		Label ProName=new Label("Audio Player");
		ProName.setStyle("-fx-font-size:25");
		Image Icon=new Image("Icon1.png");
		Header.setCenter(ProName);
		Header.setLeft(new ImageView(Icon));
		Header.setPadding(new Insets(5,5,5,5));
		Header.setStyle("-fx-background-color:#424242;"
				+ "-fx-background-radius:17px 17px 0px 0px;");
		/*******************/
		root.setTop(Header);
		root.setCenter(metaDataView.getViewNode());
		root.setBottom(playerControlsView.getViewNode());
		final Scene scene = new Scene(root, 550, 400);
		initSceneDragAndDrop(scene);
		Image Logo=new Image("Logo.png");
		scene.getStylesheets().add("Media.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Audio Player");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
		primaryStage.getIcons().add(Logo);
		/*******************/
		//Button Actions
				Close.setOnMouseEntered(e->
				{
					Close.setStyle("-fx-background-radius:15px 15px 15px 15px;");
					Close.setPrefHeight(30);
					Close.setPrefWidth(30);
				});
				Close.setOnMouseExited(e->
				{
					Close.setPrefHeight(20);
					Close.setPrefWidth(20);
				});
				Max.setOnMouseEntered(e->
				{
					Max.setStyle("-fx-background-radius:15px 15px 15px 15px;");
					Max.setPrefHeight(30);
					Max.setPrefWidth(30);
				});
				Max.setOnMouseExited(e->
				{
					Max.setPrefHeight(20);
					Max.setPrefWidth(20);
				});
				Min.setOnMouseEntered(e->
				{
					Min.setStyle("-fx-background-radius:15px 15px 15px 15px;");
					Min.setPrefHeight(30);
					Min.setPrefWidth(30);
				});
				Min.setOnMouseExited(e->
				{
					Min.setPrefHeight(20);
					Min.setPrefWidth(20);
				});
				Close.setOnAction(e->
				{
					primaryStage.close();
					System.exit(0);

				});

				Min.setOnAction(e->
				{
					primaryStage.setIconified(true);
				});
				Max.setOnAction(e->
				{
						if(primaryStage.getHeight()==400)
						{
							primaryStage.setWidth(700);
							primaryStage.setHeight(550);
						}
						else
						{
							primaryStage.setWidth(550);
							primaryStage.setHeight(400);
						}
				});
				 root.setOnMousePressed(new EventHandler<MouseEvent>()
			        {
			            @Override
			            public void handle(MouseEvent event)
			            {
			                xOffset = event.getSceneX();
			                yOffset = event.getSceneY();
			            }
			        });
				 root.setOnMouseDragged(new EventHandler<MouseEvent>()
					{
			            @Override
			            public void handle(MouseEvent event)
			            {
			                primaryStage.setX(event.getScreenX() - xOffset);
			                primaryStage.setY(event.getScreenY() - yOffset);
			            }
			        });
	}
		private void initSceneDragAndDrop(Scene scene)
		{
			scene.setOnDragOver(new EventHandler<DragEvent>()
			{
				@Override
				public void handle(DragEvent event)
				{
					Dragboard db = event.getDragboard();
					if (db.hasFiles() || db.hasUrl())
					{
						event.acceptTransferModes(TransferMode.ANY);
					}
					event.consume();
				}
			});
			scene.setOnDragDropped(new EventHandler<DragEvent>()
			{
				@Override
				public void handle(DragEvent event)
				{
					Dragboard db = event.getDragboard();
					String url = null;
					if (db.hasFiles())
					{
						url = db.getFiles().get(0).toURI().toString();
					}
					else if (db.hasUrl())
					{
						url = db.getUrl();
					}
					if (url != null)
					{
						songModel.setURL(url);
						songModel.getMediaPlayer().play();
					}
					event.setDropCompleted(url != null);
					event.consume();
				}
			});
		}
}