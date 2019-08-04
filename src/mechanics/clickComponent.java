package mechanics;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class clickComponent extends JComponent implements MouseListener{
	
	private final int HEADER_THICKNESS = 60;
	
	private int activeSelect;
	private ArrayList<Integer[]> eventSquare;
	private InteractFrame containerFrame;

	public clickComponent(){
		activeSelect = -1;
		eventSquare = null;
		containerFrame = null;
	}
	
	public int getSelected(){
		return activeSelect;
	}
	
	public void resetSelected(){
		activeSelect = -1;
	}
	
	public void setEventSquare(ArrayList<Integer[]> updated){
		eventSquare = updated;
	}
	
	public void setParentFrame(InteractFrame reference){
		containerFrame = reference;
	}
	
	public void addClickRegion(Integer[] values){
		for(int i = 0; i < eventSquare.size(); i++){
			if(eventSquare.get(i)[4].equals(values[4])){
				return;
			}
		}
		eventSquare.add(values);
	}
	
	public void mouseClicked(MouseEvent e){
		Integer x = e.getX();
		Integer y = e.getY();
		for(int i = 0; i < eventSquare.size(); i++){
		  Integer[] comp = eventSquare.get(i);
		  if(x > comp[0] && x < comp[2] && y > comp[1] && y < comp[3]){
			  activeSelect = eventSquare.get(i)[4];
			  break;
		  }
		}
		containerFrame.clickEvent();
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}

}
