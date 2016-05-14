package MP3;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MetadataView extends AbstractView
{

	public static Image Cover;
	public static ImageView AlbumCover=new ImageView(Cover);
	public static Label Album=new Label();
	public static Label Artist=new Label();
	public static Label Title=new Label();
	public static Label Year=new Label();

	public MetadataView(Song songModel)
	{
		super(songModel);
	}

	@Override
	protected Node initView()
	{
		BorderPane Meta=new BorderPane();
		HBox hbox =new HBox();
		songModel.YearProperty().addListener(ov->
		{
			Year.setText(songModel.getYear());
		});
		songModel.ArtistProperty().addListener(ov->
		{
			Artist.setText(songModel.getArtist());
		});
		songModel.albumProperty().addListener(ov->
		{
			Album.setText(songModel.getAlbum());
		});
		songModel.TitleProperty().addListener(ov->
		{
			Title.setText(songModel.getTitel());
		});
		songModel.AlbumCoverProperty().addListener(ov->
		{
			Cover=songModel.getAlbumCover();
			AlbumCover.setImage(Cover);
			AlbumCover.setStyle("-fx-width:700");
			AlbumCover.setFitWidth(120);
			AlbumCover.setFitHeight(120);
			
		});
		Meta.setPadding(new Insets(15,15,15,15));
		hbox.getChildren().addAll(AlbumCover);
		
		hbox.setStyle("-fx-width:300;-fx-background-color:red");
		Meta.setLeft(AlbumCover);

		return Meta;
	}


}
