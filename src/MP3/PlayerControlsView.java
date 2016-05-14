package MP3;

import java.io.File;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

class PlayerControlsView extends AbstractView
{
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
	private Button playPauseButton;
	private Button SeekFroward;
	private Button SeekBackward;
	private Button SkipStart;
	private Button SkipEnd;
	private Button Volume;
	private Label TotalTime;
	private Label CurrentTime;
	
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
		songModel.mediaPlayerProperty().addListener(ov->
		{
		  songModel.getMediaPlayer().statusProperty().
		  addListener(new StatusListener());
	    /*********************/
		final MediaPlayer mediaPlayer = songModel.getMediaPlayer();
		/***************************/
			mediaPlayer.setOnEndOfMedia(new Runnable()
			{
				@Override
				public void run()
				{
				    songModel.getMediaPlayer().stop();
				}
			});
			/***********************/
		  mediaPlayer.volumeProperty().bind
		  (volumeSlider.valueProperty().divide(100));
		  /******************/
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
		playPauseButton = createPlayPauseButton();
		SeekFroward = createSeekFButton();
		SeekBackward = createSeekBButton();
		SkipStart = createSkipSButton();
		SkipEnd = createSkipEButton();
		Volume = createVolButton();
		CurrentTime = createLabel("00:00", "time");
		TotalTime = createLabel("00:00", "time");
		statusLabel = createLabel("Buffering", "statusDisplay");
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
		/*******************************/
		Controls.getChildren().addAll(openButton,SkipStart,SeekBackward,
				playPauseButton,SeekFroward,SkipEnd,Volume,volumeSlider,statusLabel);
		Advance.getChildren().addAll(CurrentTime,positionSlider,TotalTime);
		Advance.setStyle("-fx-background-color:#606060;");
		
		positionSlider.setPrefSize(450, 25);
		/********************/
		Pane.setStyle("-fx-background-color:#424242;"
				+ "-fx-background-radius:0px 0px 17px 17px;");
		
		
	
		Pane.setBottom(Controls);
		Pane.setCenter(Advance);
		/*********************/
		return Pane;
	}
	private String formatDuration(Duration duration)
	{
		    double millis = duration.toMillis();
		    int seconds = (int) (millis / 1000) % 60;
		    int minutes = (int) (millis / (1000 * 60));
		    return String.format("%02d:%02d", minutes, seconds);
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
				FileChooser fc = new FileChooser();
				fc.setTitle("Pick a Sound File");
				File song = fc.showOpenDialog(viewNode.getScene().getWindow());
				if (song != null)
				{
					
					//fc.selectedExtensionFilterProperty().getValue().getExtensions();
					songModel.setURL(song.toURI().toString());
				
					songModel.getMediaPlayer().play();
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
			FileChooser fc = new FileChooser();
			fc.setTitle("Pick a Sound File");
			File song = fc.showOpenDialog(viewNode.getScene().getWindow());
			if (song != null)
			{
				songModel.setURL(song.toURI().toString());
				//songModel.getMediaPlayer().play();
			}

		});
	}
//============================================================
	
//============================================================

}