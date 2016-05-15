package MP3;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
		GridPane Meta=new GridPane();
		songModel.YearProperty().addListener(ov->
		{
			Year.setText(songModel.getYear());
			final String content = Year.getText();
			 final Animation animation = new Transition()
			    {
			        {
			            setCycleDuration(Duration.millis(6000));
			        }

			        protected void interpolate(double frac)
			        {
			            final int length = content.length();
			            final int n = Math.round(length * (float) frac);
			            Year.setText(content.substring(0, n));
			        }

			    };

			    animation.play();
		});
		songModel.ArtistProperty().addListener(ov->
		{
			Artist.setText(songModel.getArtist());
			final String content = Artist.getText();
			 final Animation animation = new Transition()
			    {
			        {
			            setCycleDuration(Duration.millis(6000));
			        }

			        protected void interpolate(double frac)
			        {
			            final int length = content.length();
			            final int n = Math.round(length * (float) frac);
			            Artist.setText(content.substring(0, n));
			        }

			    };

			    animation.play();
		});
		songModel.albumProperty().addListener(ov->
		{
			Album.setText(songModel.getAlbum());
			final String content = Album.getText();
			final Animation animation = new Transition()
			    {
			        {
			            setCycleDuration(Duration.millis(6000));
			        }

			        protected void interpolate(double frac)
			        {
			            final int length = content.length();
			            final int n = Math.round(length * (float) frac);
			            Album.setText(content.substring(0, n));
			        }

			    };

			    animation.play();

		});
		songModel.TitleProperty().addListener(ov->
		{
			Title.setText(songModel.getTitel());
			final String content = Title.getText();
			final Animation animation = new Transition()
			    {
			        {
			            setCycleDuration(Duration.millis(6000));
			        }

			        protected void interpolate(double frac)
			        {
			            final int length = content.length();
			            final int n = Math.round(length * (float) frac);
			            Title.setText(content.substring(0, n));
			        }

			    };

			    animation.play();

		});
		songModel.AlbumCoverProperty().addListener(ov->
		{
			Cover=songModel.getAlbumCover();
			AlbumCover.setImage(Cover);
			AlbumCover.setFitHeight(150);
			AlbumCover.setFitWidth(150);
		});
		Meta.setPadding(new Insets(20,0,0,20));
		VBox Col=new VBox();
		HBox raw1=new HBox();
		HBox raw2=new HBox();
		HBox raw3=new HBox();
		HBox raw4=new HBox();
		Col.setPadding(new Insets(0,0,0,20));
		raw1.getChildren().add(Title);
		Title.setStyle("-fx-font-size:25px; -fx-text-fill:#f5f6f7");
		Artist.setStyle("-fx-font-size:22px; -fx-text-fill:#f5f6f7");
		Album.setStyle("-fx-font-size:18px; -fx-text-fill:#f5f6f7");
		Year.setStyle("-fx-font-size:18px; -fx-text-fill:#f5f6f7");
		raw2.getChildren().add(Artist);
		raw3.getChildren().add(Album);
		raw4.getChildren().add(Year);
		Col.getChildren().addAll(raw1,raw2,raw3,raw4);
		GridPane.setValignment(Col, VPos.TOP);
		Meta.add(AlbumCover,0,0);
		Meta.add(Col,1,0);
		//ShowLabel(Title);
		return Meta;
	}
}
