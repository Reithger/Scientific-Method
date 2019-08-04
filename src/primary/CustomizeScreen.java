package primary;
import javax.swing.*;

import entities.Player;
import mechanics.InteractFrame;
import mechanics.TimerRefresh;

import java.awt.*;
import java.util.Timer;

public class CustomizeScreen extends InteractFrame{

	private final int SCREEN_WIDTH = 1200;
	private final int SCREEN_HEIGHT = 800;
	private final int SCREEN_WIDTH_ADJUSTMENT = 14;
	private final int SCREEN_HEIGHT_ADJUSTMENT = 36;
	private final String[][] sliders = new String[][]{		//3 3 5 6 7 3
		{"Masculine", "Feminine", "Intersex"},
		{"Male", "Female", "Non-Binary"},
		{"White", "African American", "Hispanic/ Latino", "Asian", "Indigenous"},
		{"Heterosexual","Gay/Lesbian", "Bisexual", "Asexual", "Aromantic", "Pansexual"},
		{"None", "Deaf", "Blind", "Mute", "Mobility (Wheelchair)", "Mobility (Cane)", "Cognizant"},
		{"Lower-Class", "Middle-Class", "Upper-Class"}	
	};
	private final String[] titles = new String[]{
			"Sex", "Gender", "Race", "Sexuality", "Disability", "Socio-Economic Status"
	};
	private final int SMALL_TEXT_SCALE = 2;
	private final int MEDIUM_TEXT_SCALE = 3;
	private final int REFRESH_RATE = 1000/15;
	private final int SLIDER_X_BASE = 225;
	private final int SLIDER_Y_BASE = 160;
	private final int DISPLACE_CENTRAL_BUTTON = 85;
	//Need a region that the user can click to cycle through options for that spot
	
	private final String DEFAULT_VISUAL_PATH = "/sprites/Male/male.png";
	private final String DEFAULT_TITLE_FRAME_PATH = "/UI/TitleFrame/TitleFrame.png";
	private final String DEFAULT_FRAME_PATH = "/UI/Frame/frame.png";
	private final String DEFAULT_BACKGROUND_PATH = "/Backgrounds/CustomizeScreen/customize.png";
	private final String DEFAULT_LETTER_FRAME_PATH = "/UI/LetterFrame/letterFrame.png";
	private final String DEFAULT_FORWARD_ARROW_PATH = "/UI/FwdArrow/rtArr.png";
	private final String DEFAULT_BACK_ARROW_PATH = "/UI/BckArrow/ltArr.png";
	
	private int[] stateSlider;
	private Player sci;
	private Object waiting;
	private boolean typeState;
	private String characterName;
	private Timer timer;
	
	//Need collection of pixel-arrays with variable parts based on changing user input which feed into basic matrix of characteristics.
	
	public CustomizeScreen(String title, JFrame parentFrame, Object paused){
		super();
		parentFrame.setSize(SCREEN_WIDTH + SCREEN_WIDTH_ADJUSTMENT, SCREEN_HEIGHT + SCREEN_HEIGHT_ADJUSTMENT);
		waiting = paused;
		stateSlider = new int[6];
		this.setDoubleBuffered(true);
		sci = new Player(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, DEFAULT_VISUAL_PATH);
		sci.adjustVisual();
		parentFrame.add(this);
		parentFrame.setTitle(title);
		this.requestFocusInWindow();
		characterName = "";
		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, REFRESH_RATE);
		sci.setX(SCREEN_WIDTH/2);
		sci.setY(SCREEN_HEIGHT/2);
		repaint();
	}
	
	public void paintComponent(Graphics g){
		addShadedRegion(0,0, SCREEN_WIDTH, SCREEN_HEIGHT, new Color(1f, 1f, 1f, 1f), g);	//It's cheap, but it resets the screen.
		g.setColor(Color.black);
		addPic(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, DEFAULT_BACKGROUND_PATH, g);
		addSlider(-SLIDER_X_BASE, SLIDER_Y_BASE, 0, g);
		addSlider(SLIDER_X_BASE, SLIDER_Y_BASE, 1, g);
		addSlider(-(int)(SLIDER_X_BASE * 1.75), (int)(SLIDER_Y_BASE * 2.1), 2, g);
		addSlider((int)(SLIDER_X_BASE * 1.75), (int)(SLIDER_Y_BASE * 2.1), 3, g);
		addSlider(-SLIDER_X_BASE*3/2, (int)(SLIDER_Y_BASE * 3.33), 4, g);
		addSlider(SLIDER_X_BASE*3/2, (int)(SLIDER_Y_BASE * 3.33), 5, g);
		addClickPic(SCREEN_WIDTH/2, SCREEN_HEIGHT - 150, DEFAULT_FRAME_PATH, g, 13);
		addOwnText(SCREEN_WIDTH/2, SCREEN_HEIGHT-150, characterName, g, 3);
		addClickPicScaled(SCREEN_WIDTH * 5/6, SCREEN_HEIGHT *4/5 + 40, DEFAULT_TITLE_FRAME_PATH, g, 12,3);
		addOwnText(SCREEN_WIDTH * 5/6, SCREEN_HEIGHT * 4/5 + 40, "Start Game", g, MEDIUM_TEXT_SCALE);
		
		addClickPicScaled(SCREEN_WIDTH/2 + DISPLACE_CENTRAL_BUTTON, SCREEN_HEIGHT/2 - DISPLACE_CENTRAL_BUTTON, DEFAULT_LETTER_FRAME_PATH, g, 14, 2);
		addOwnText(SCREEN_WIDTH/2 + DISPLACE_CENTRAL_BUTTON, SCREEN_HEIGHT/2 - DISPLACE_CENTRAL_BUTTON, "Hair", g, 2);
		addClickPicScaled(SCREEN_WIDTH/2 - DISPLACE_CENTRAL_BUTTON, SCREEN_HEIGHT/2 + DISPLACE_CENTRAL_BUTTON, DEFAULT_LETTER_FRAME_PATH, g, 15, 2);
		addOwnText(SCREEN_WIDTH/2 - DISPLACE_CENTRAL_BUTTON, SCREEN_HEIGHT/2 + DISPLACE_CENTRAL_BUTTON, "Shoes", g, 2);
		
		
		sci.drawToScreenScaled(4, g);
		
	}
	
	public void clickEvent(){
		int key = this.getClickComponent().getSelected();
		if(key == 14){
			sci.cycleHair();
		}
		if(key == 15){
			sci.cycleShoe();
		}
		if(key == 13){
			typeState = true;
			return;
		}
		typeState = false;
		if(key == 12){
		  sci.setName(characterName);
	      synchronized(waiting){
	        waiting.notifyAll();
	      }
	      return;
		}
		if(key < 12){
		  if(key % 2 == 0 && key != -1)
			stateSlider[key/2] = (stateSlider[key/2] - 1) % sliders[key/2].length;
		  else if (key != -1)
			stateSlider[key/2] = (stateSlider[key/2] + 1) % sliders[key/2].length;
		  if(stateSlider[key/2] < 0)
			stateSlider[key/2] = sliders[key/2].length - 1;
		  sci.setState(key/2, stateSlider[key/2]);
		}
		sci.adjustVisual();
		repaint();
		this.getClickComponent().resetSelected();
	}
	
	public void keyEvent(){
		int pressed = this.getKeyComponent().getSelected();
		if(typeState){
			if(pressed == 8)
				characterName = characterName.substring(0, characterName.length()-1);
			else if (pressed >= 65 && pressed <= 90 || pressed == 32)
				characterName += pressed == 32 ? " " : ((char)pressed+"").toLowerCase();
		}
		this.getKeyComponent().resetSelected();
		repaint();
	}
	
	private void addSlider(int x, int y, int index, Graphics g){
		addPic(SCREEN_WIDTH/2 + x, y, DEFAULT_FRAME_PATH, g);
		for(int i = 0; i < titles[index].split(" ").length; i++){
			addOwnText(SCREEN_WIDTH/2 + x, y - 30 + (i - titles[index].split(" ").length/2) * MEDIUM_TEXT_SCALE * 8, titles[index].split(" ")[i], g, MEDIUM_TEXT_SCALE);
		}
		for(int i = 0; i < sliders[index][stateSlider[index]].split(" ").length; i++){
			addOwnText(SCREEN_WIDTH/2 + x, y + 20 + (i - sliders[index][stateSlider[index]].split(" ").length/2) * SMALL_TEXT_SCALE * 8, sliders[index][stateSlider[index]].split(" ")[i], g, SMALL_TEXT_SCALE);	
		}
		addClickPic(SCREEN_WIDTH/2 + x - 125, y + 25, DEFAULT_BACK_ARROW_PATH, g, index*2);
		addClickPic(SCREEN_WIDTH/2 + x + 125, y + 25, DEFAULT_FORWARD_ARROW_PATH, g, index*2 + 1);
	}
	
	public Player getPlayer(){
		return sci;
	}
}
