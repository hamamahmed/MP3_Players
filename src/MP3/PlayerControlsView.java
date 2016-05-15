package MP3;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

class PlayerControlsView extends AbstractView
{
	private List<File> Song=new ArrayList<>();
	private static int Count=0;
	private Slider volumeSlider;
	private Image pauseImg;
	private Image playImg;
	private Image LImg=new Image("LVol.png");
	private Image VLImg=new Image("VLVol.png");
	private Image MuteImg=new Image("Mute.png");;
	private Image HImg=new Image("HVol.png");
	private Image Vol = new Image("Volume.png");
	private Button playPauseIcon;
	private Node controlPanel;
	private Slider positionSlider;
	private Button openButton;
	private Button SeekFIcon;
	private Button SeekBIcon;
	private Button SkipSIcon;
	private Button SkipEIcon;
	private Button VolumeIcon;
	private Label statusLabel;
	private Label CurrentTime;
	private Label TotalTime;
	private Button playPauseButton;
	private Button SeekFroward;
	private Button SeekBackward;
	private Button SkipStart;
	private Button SkipEnd;
	private Button Volume;
	private Button PlayList;
	private ListView<File> SIteam;
	private static boolean Show=true;
	private double xOffset = 0; private double yOffset = 0;
	Scene scene;
	public PlayerControlsView(Song songModel)
	{
		super(songModel);
	}
	@Override
	protected Node initView()
	{
		controlPanel  = createControlPanel();
		final BorderPane Controls=new BorderPane();
		Controls.setBottom(controlPanel);
		HBox Spectrum=new HBox();
		Spectrum.setPrefHeight(80);
		Spectrum.setAlignment(Pos.BOTTOM_CENTER);
		Controls.setTop(Spectrum);
		songModel.mediaPlayerProperty().addListener(ov->
		{
		  songModel.getMediaPlayer().statusProperty().
		  addListener(new StatusListener());
		  /*********************/
			final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
			Spectrum.getChildren().clear();
			/***************************/
			Spectrum.getChildren().add(Spctrum(mediaPlayer));
			/********************************/
			mediaPlayer.setOnEndOfMedia(new Runnable()
			{
				@Override
				public void run()
				{

				    songModel.getMediaPlayer().stop();
					if(Count<Song.size())
					{
						if(Count>0)
					    {

							songModel.setURL(Song.get(Count).toURI().toString());
							songModel.getMediaPlayer().play();
					    	Count++;
					    }
					}
					else
					{
						Count=0;
						songModel.setURL(Song.get(Count).toURI().toString());
						songModel.getMediaPlayer().play();
						Count++;
					}
				}
			});
			/***************************/
		  mediaPlayer.volumeProperty().bind
		  (volumeSlider.valueProperty().divide(100));
		  mediaPlayer.setOnReady(new Runnable()
		  {

			@Override
			public void run()
			{
			  	final Duration totalDuration = mediaPlayer.getTotalDuration();
				positionSlider.setMin(0.0);
				positionSlider.setMax(totalDuration.toSeconds());
				TotalTime.setText(formatDuration(totalDuration));
			}
	   	 });
		  mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
			{
				@Override
				public void changed(ObservableValue<? extends Duration>
				observablevalue, Duration duration, Duration Current)
				{
					positionSlider.setValue(Current.toSeconds());
					CurrentTime.setText(formatDuration(Current));
				}
			});
		  positionSlider.setOnMousePressed(e->
			{
				Duration Seek = Duration.seconds(positionSlider.getValue());
				 mediaPlayer.seek(Seek);
			});
		  positionSlider.setOnMouseDragged(e->
			{
				Duration Seek = Duration.seconds(positionSlider.getValue());
				 mediaPlayer.seek(Seek);
			});
		});
		  volumeSlider.valueProperty().addListener(new VolListener());
		  		return Controls;
	}

	private Node createControlPanel()
	{
		BorderPane Pane=new BorderPane();
		final HBox Controls = new HBox();
		final HBox Advance = new HBox();
		//Controls.setPadding(new Insets(5,5,5,5));
		Controls.setAlignment(Pos.CENTER);
		Advance.setAlignment(Pos.CENTER);
		Controls.setFillHeight(false);
		/**********************************/
		PlayList=PlayList(PlayList);
		playPauseButton = createPlayPauseButton();
		SeekFroward = createSeekFButton();
		SeekBackward = createSeekBButton();
		SkipStart = createSkipSButton();
		SkipEnd = createSkipEButton();
		Volume = createVolButton();
		Volume.setPrefWidth(50);
		statusLabel = createLabel("Buffering", "statusDisplay");
		statusLabel.setStyle("	-fx-border-color: #356489;"
				+ "-fx-border-radius: 4;"
				+ "-fx-border-width: 2;"
				+ "-fx-text-fill: #f5f6f7;"
				+ "-fx-font-size: 8pt;");
		statusLabel.setPrefWidth(50);
		statusLabel.setAlignment(Pos.CENTER);
		CurrentTime = createLabel("00:00", "Time");
		TotalTime = createLabel("00:00", "Time");
		openButton = createOpenButton();
		volumeSlider = createSlider("volumeSlider");
		volumeSlider.setValue(50);
		volumeSlider.setPrefWidth(100);
		positionSlider = createSlider("positionSlider");
		Volume.setDisable(true);
		volumeSlider.setDisable(true);
		positionSlider.setDisable(true);
		SeekFroward.setDisable(true);
		SeekBackward.setDisable(true);
		SkipStart.setDisable(true);
		SkipEnd.setDisable(true);
		CurrentTime.setDisable(true);
		TotalTime.setDisable(true);
		/*******************************/
		Controls.getChildren().addAll(openButton,PlayList,SkipStart,SeekBackward,
				playPauseButton,SeekFroward,SkipEnd,Volume,volumeSlider,statusLabel);
		Advance.getChildren().addAll(CurrentTime,positionSlider,TotalTime);
		Advance.setStyle("-fx-background-color:#606060;");
		positionSlider.setPrefSize(450,25);
		/********************/
		Pane.setStyle("-fx-background-color:#424242;"
				+ "-fx-background-radius:0px 0px 17px 17px;");
		Controls.setPadding(new Insets(5,0,5,0));
		Pane.setBottom(Controls);
		Pane.setCenter(Advance);
		/*********************/
		return Pane;
	}
	private Label createLabel(String text, String styleClass)
	{
		final Label label = new Label(text);
		return label;
	}
	private class StatusListener implements InvalidationListener
	{
		@Override
		public void invalidated(Observable observable)
		{
			Platform.runLater(new Runnable()
			{
				@Override public void run()
				{
					updateStatus(songModel.getMediaPlayer().getStatus());
				}
			});
		}
	}
	private class VolListener implements InvalidationListener
	{
		@Override
		public void invalidated(Observable observable)
		{
			Platform.runLater(new Runnable()
			{
				@Override public void run()
				{
					updateVolImg(volumeSlider.getValue());
				}
			});
		}
	}
	private void updateVolImg(double value)
	{
		if(value==0)
			Volume.setGraphic(new ImageView(MuteImg));
		else if(value>=1&&value<=10)
			Volume.setGraphic(new ImageView(VLImg));
		else if(value>=11&&value<=35)
			Volume.setGraphic(new ImageView(LImg));
		else if(value>=36&&value<=70)
			Volume.setGraphic(new ImageView(Vol));
		else if(value>=71)
			Volume.setGraphic(new ImageView(HImg));
	}
	private void updateStatus(Status newStatus)
	{
		if (newStatus == Status.UNKNOWN || newStatus == null)
		{
			statusLabel.setText("Buffering");
		}
		else
		{
			positionSlider.setDisable(false);
			SeekFroward.setDisable(false);
			SeekBackward.setDisable(false);
			SkipStart.setDisable(false);
			SkipEnd.setDisable(false);
			Volume.setDisable(false);
			volumeSlider.setDisable(false);
			CurrentTime.setDisable(false);
			TotalTime.setDisable(false);
			statusLabel.setText(newStatus.toString());
			if (newStatus == Status.PLAYING)
			{
				playPauseIcon.setGraphic(new ImageView(pauseImg));
			}
			else
			{
				playPauseIcon.setGraphic(new ImageView(playImg));
			}
		}
	}
	private Button createVolButton()
	{
		Image Vol = new Image("Volume.png");
		VolumeIcon = new Button (null,new ImageView(Vol));
		VolumeIcon.setAlignment(Pos.CENTER);
		VolumeIcon.setOnMouseClicked(e->
		{
			final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
			if(!mediaPlayer.isMute())
			{
				VolumeIcon.setGraphic(new ImageView(MuteImg));
				mediaPlayer.setMute(true);
			}
			else
			{
				mediaPlayer.setMute(false);
				updateVolImg(volumeSlider.getValue());

			}
		});
		return VolumeIcon;
	}

	private Button createSkipEButton()
	{
		Image SkipEImg = new Image("SkipEImg.png");
		SkipEIcon = new Button (null,new ImageView(SkipEImg));
		SkipEIcon.setAlignment(Pos.CENTER);
		SkipEIcon.setOnAction(e->
		{
			if(!songModel.IsURL())
			{
				final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
				final Duration totalDuration = mediaPlayer.getTotalDuration();
				final Duration oneSecond = Duration.seconds(1);
				mediaPlayer.seek(totalDuration.subtract(oneSecond));
			}
		});
		return SkipEIcon;
	}

	private Button createSkipSButton()
	{
		Image SkipSImg = new Image("SkipSImg.png");
		SkipSIcon = new Button (null,new ImageView(SkipSImg));
		SkipSIcon.setAlignment(Pos.CENTER);
		SkipSIcon.setOnAction(e->
		{
			if(!songModel.IsURL())
			{
				final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
				mediaPlayer.seek(Duration.ZERO);
			}
		});
		return SkipSIcon;
	}

	private Button createSeekBButton()
	{
		Image SeekBImg = new Image("SeekBImg.png");
		SeekBIcon = new Button (null,new ImageView(SeekBImg));
		SeekBIcon.setAlignment(Pos.CENTER);
		SeekBIcon.setOnAction(e->
		{
			if(!songModel.IsURL())
			{
				final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
		        final Duration currentDuration = mediaPlayer.getCurrentTime();
		        final Duration Seek = Duration.seconds(5);
		        mediaPlayer.seek(currentDuration.subtract(Seek));
			}
		});
		return SeekBIcon;
	}

	private Button createSeekFButton()
	{
		Image SeekFImg = new Image("SeekFImg.png");
		SeekFIcon = new Button (null,new ImageView(SeekFImg));
		SeekFIcon.setAlignment(Pos.CENTER);
		SeekFIcon.setOnAction(e->
		{
			if(!songModel.IsURL())
			{
				final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
		        final Duration currentDuration = mediaPlayer.getCurrentTime();
		        final Duration Seek = Duration.seconds(5);
		        mediaPlayer.seek(currentDuration.add(Seek));
			}
		});
		return SeekFIcon;
	}

	private Button createPlayPauseButton()
	{
		pauseImg = new Image("Pause.png");
		playImg = new Image("Play.png");
		playPauseIcon = new Button (null,new ImageView(playImg));
		playPauseIcon.setAlignment(Pos.CENTER);
		playPauseIcon.setOnMouseClicked(e->
		{
			final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
			if (!songModel.IsURL())
			{
				if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
				{
					mediaPlayer.pause();
				}
				else
				{
					mediaPlayer.play();
				}
			}
			else
			{
				FileChooser fileDialog = new FileChooser();
				fileDialog.setTitle("Pick a Sound File");
				fileDialog.getExtensionFilters().addAll(
						new ExtensionFilter("All Audio", "*.mp3;*.wav;*.aif;*.aiff"),
						new ExtensionFilter("aif Files", "*.aif","*.aiff"),
						new ExtensionFilter("MP3 Files", "*.mp3"),
						new ExtensionFilter("wav Files", "*.wav"));
				fileDialog.setSelectedExtensionFilter(fileDialog
						.getExtensionFilters().get(0));
				Song = fileDialog.showOpenMultipleDialog(viewNode.getScene()
						.getWindow());
					if (Song != null)
					{
						songModel.setURL(Song.get(0).toURI().toString());
						songModel.getMediaPlayer().play();
						Count++;
					}

			}
		});
		return playPauseIcon;
	}
	private Slider createSlider(String id)
	{
		final Slider slider = new Slider();
		slider.setValue(0);
		return slider;
	}
	private Button createOpenButton()
	{
		Image Open=new Image("Music Folder.png");
		final Button openButton = new Button(null,new ImageView(Open));
		OpenHandler(openButton);
		return openButton;
	}
	private void OpenHandler(Button Action)
	{
		Action.setOnAction(e->
		{
			FileChooser fileDialog = new FileChooser();
			fileDialog.setTitle("Pick a Sound File");
			fileDialog.getExtensionFilters().addAll(
					new ExtensionFilter("All Audio", "*.mp3;*.wav;*.aif;*.aiff"),
					new ExtensionFilter("aif Files", "*.aif","*.aiff"),
					new ExtensionFilter("MP3 Files", "*.mp3"),
					new ExtensionFilter("wav Files", "*.wav"));
			fileDialog.setSelectedExtensionFilter(fileDialog.
					getExtensionFilters().get(0));
			Song = fileDialog.showOpenMultipleDialog(viewNode.getScene()
					.getWindow());
				if (Song != null)
				{
					songModel.setURL(Song.get(0).toURI().toString());
					songModel.getMediaPlayer().play();
					Count++;
				}
		});
	}
	private static Node Spctrum(MediaPlayer mediaPlayer)
	{
		final HBox HB=new HBox();
		HB.setRotate(180);
		final Rectangle[] rec=new Rectangle[26];
		for(int i=0;i<rec.length;i++)
		{
			rec[i]=new Rectangle();
			rec[i].setStyle("-fx-fill:#356489;-fx-stroke:grey;");
			HB.getChildren().add(rec[i]);
		}
		int bandwidth=20;
		for(Rectangle R :rec)
		{
			R.setWidth(bandwidth);
			R.setHeight(0);
		}
		mediaPlayer.setAudioSpectrumListener(new AudioSpectrumListener()
		{

			@Override
			public void spectrumDataUpdate(double timestamp, double duration,
					float[] magnitudes, float[] phases)
			{
				for (int i=0;i<rec.length;i++)
				{
					double h=0;
					 h=(magnitudes[i])*2+60;
					if(h<0)
					{
						rec[i].setHeight(-h);
					}
				}
			}
	});
		return HB;
}
	private Button PlayList(Button action)
	{
		Image list=new Image("List.png");
		action=new Button(null,new ImageView(list));
		 action.setOnAction(e->
		{
			ObservableList<File> items =FXCollections.observableArrayList();
			if(Song!=null)
			{
				for(int i=0;i<Song.size();i++)
				 {
					  items.add(Song.get(i));
				 }
				 SIteam = new ListView<>(items);
					 SIteam.getSelectionModel().selectedItemProperty().
					 					addListener(c ->
						{
							songModel.setURL(SIteam.getSelectionModel().
									getSelectedItem()
									.toURI().toString());
							songModel.getMediaPlayer().play();
						});
					 ListView(SIteam).show();
			}
		});

		return action;
	}
	private Stage ListView(Node Iteam)
	{
		Stage List=new Stage();
		List.initStyle(StageStyle.TRANSPARENT);
		List.initStyle(StageStyle.UNDECORATED);
		BorderPane View =new BorderPane();
		BorderPane Header=new BorderPane();
		//Create info bar
		HBox InfoBar=new HBox();
		Button Close=new Button();
		Button Min=new Button();
		InfoBar.getChildren().addAll(Min,Close);
		InfoBar.setPrefWidth(50);
		Min.setPrefHeight(20);
		Min.setPrefWidth(20);
		Close.setPrefHeight(20);
		Close.setPrefWidth(20);
		Min.setId("Min");
		Close.setId("Close");
		Header.setRight(InfoBar);
		Header.setPadding(new Insets(10,5,0,5));
		Label ProName=new Label("Play List");
		ProName.setStyle("-fx-font-size:25");
		Header.setLeft(ProName);
		ProName.setPadding(new Insets(0,5,10,5));
		Header.setStyle("-fx-background-color:#424242;"
				+ "-fx-background-radius:17px 17px 0px 0px;");
		View.setBottom(new Label(""));
		/*********************************/
		View.setTop(Header);
		/********************************/
		  scene=new Scene(View, 250,400);
		  scene.getStylesheets().add("Media.css");
			List.setScene(scene);
			View.setCenter(Iteam);
			scene.setOnMousePressed(new EventHandler<MouseEvent>()
	        {
	            @Override
	            public void handle(MouseEvent event)
	            {
	                xOffset = event.getSceneX();
	                yOffset = event.getSceneY();
	            }
	        });
		 scene.setOnMouseDragged(new EventHandler<MouseEvent>()
			{
	            @Override
	            public void handle(MouseEvent event)
	            {
	                List.setX(event.getScreenX() - xOffset);
	                List.setY(event.getScreenY() - yOffset);
	            }
	        });
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
				List.close();
			});

			Min.setOnAction(e->
			{
				List.setIconified(true);
			});

		return List;
	}
	private String formatDuration(Duration duration)
	{
		double millis = duration.toMillis();
		int seconds = (int) (millis / 1000) % 60;
		int minutes = (int) (millis / (1000 * 60));
		return String.format("%02d:%02d", minutes, seconds);
	}
}