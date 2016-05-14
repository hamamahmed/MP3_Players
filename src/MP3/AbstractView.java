package MP3;
import javafx.scene.Node;

public abstract class AbstractView 
{
	protected final Song songModel;
	protected final Node viewNode;
	public AbstractView(Song songModel) 
	{
		this.songModel = songModel;
		this.viewNode = initView();
	}
	public Node getViewNode() 
	{
		return viewNode;
	}
	protected abstract Node initView();
}