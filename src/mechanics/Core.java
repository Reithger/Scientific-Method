package mechanics;
import javax.swing.*;

import entities.Player;
import primary.CustomizeScreen;
import primary.SocializationScreen;
import primary.TitleScreen;


	/*
	 *  - Make a display screen of varying dimensions (no stupid glitches like the WGST project) - DONE; STATIC SIZE
	 *  - Should do multiple sizes for smaller computers.
	 *  - Title menu, Character customization menu, Socialization map (shifts around larger map), Research event screen
	 *  - Use pre-generated images corresponding to internal invisible walls.
	 *  - User control of character entity WASD movement, text navigation in Socialization
	 *  - User control of actions during Research Event
	 *  - Sub-system of Respect/Reputation attributes that impact/are impacted by character action
	 *  - NPCs with unique characteristics/visuals (use character generation from characteristics for everyone?)
	 *  
	 */


public class Core {

    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = 800;
    
    
	public static void main(String[] args) throws InterruptedException{
		JFrame frame = readyFrame();
		Object waiting = new Object();
		Player sci = null;
		TitleScreen title = new TitleScreen("Title", frame, waiting);
		synchronized(waiting){
		  waiting.wait();
		}
		frame.remove(title);
		CustomizeScreen custom = new CustomizeScreen("Customization", frame, waiting);
		synchronized(waiting){
		  waiting.wait();
		}
		frame.remove(custom);
		sci = custom.getPlayer();
		SocializationScreen social = new SocializationScreen("Institution", frame, waiting, sci);
	}
	
	public static JFrame readyFrame(){
		JFrame frame = new JFrame();
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setVisible(true);
		return frame;
	}
	
}
