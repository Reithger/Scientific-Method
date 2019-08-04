package mechanics;
import javax.swing.*;
import java.awt.event.*;

public class keyComponent extends JComponent implements KeyListener{

	public int activeSelect;
	public InteractFrame containerFrame;
	
	public keyComponent(){
		activeSelect = -1;
		containerFrame = null;
	}
	
	public int getSelected(){
		return activeSelect;
	}
	
	public void resetSelected(){
		activeSelect = -1;
	}
	
	public void setParentFrame(InteractFrame reference){
		containerFrame = reference;
	}
	
	public void keyPressed(KeyEvent e) {
		activeSelect = e.getKeyCode();
		containerFrame.keyEvent();
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
