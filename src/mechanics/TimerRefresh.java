package mechanics;
import java.util.TimerTask;

public class TimerRefresh extends TimerTask{

	private InteractFrame parent;
	
	public TimerRefresh(InteractFrame par){
		parent = par;
	}
	
	public void run(){
		parent.repaint();
	}
}
