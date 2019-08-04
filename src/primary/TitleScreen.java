package primary;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.*;

import mechanics.InteractFrame;
import mechanics.TimerRefresh;

import java.util.Timer;
/*
 * addPic(int x, int y String path, Graphics g)
 * addText(int x, int y, String phrase, Graphics g)
 * addShadedRegion(int x, int y, int wid, int hei, Color col, Graphics g)
 * addButton(int x, int y, int wid, int hei, String phrase, Color col, Graphics g, int Key)
 * addClickPic(int x, int y, String path, Graphics g, int Key)
 * 
 */

public class TitleScreen extends InteractFrame{

	private final int SCREEN_WIDTH = 1200;
	private final int SCREEN_HEIGHT = 800;
	private final int SCREEN_WIDTH_ADJUSTMENT = 14;
	private final int SCREEN_HEIGHT_ADJUSTMENT = 36;
	private final String[] TITLE = {"The", "Scientific", "Method"};
	private final String CONTINUE_TEXT = "Start";
	private final int TITLE_SCALE = 8;
	private final int REFRESH_RATE = 1000/15;
	
	private final String DEFAULT_BACKGROUND_PATH = "/Backgrounds/TitleScreen/title.png";
	private final String DEFAULT_TITLE_FRAME_PATH = "/UI/TitleFrame/TitleFrame.png";
	
	private boolean active;
	private Object waiting;
	private Timer timer;
	
	public TitleScreen(String title, JFrame parentFrame, Object paused){
		super();
	    waiting = paused;
		active = true;
		parentFrame.add(this);
		parentFrame.setTitle(title);
		this.requestFocusInWindow();
		this.setDoubleBuffered(true);
		parentFrame.setSize(SCREEN_WIDTH + SCREEN_WIDTH_ADJUSTMENT, SCREEN_HEIGHT + SCREEN_HEIGHT_ADJUSTMENT);
		timer = new Timer();
		timer.schedule(new TimerRefresh(this), 0, REFRESH_RATE);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman", Font.BOLD, 44));
		addPicScaled(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, DEFAULT_BACKGROUND_PATH, g, 4);
		
		
		addShadedRegion(SCREEN_WIDTH/5, 0, SCREEN_WIDTH*3/5, SCREEN_HEIGHT, new Color(0,0,0,.8f), g);
		addPicScaled(SCREEN_WIDTH/2, 150, DEFAULT_TITLE_FRAME_PATH, g, TITLE_SCALE);
		for(int i = 0; i < TITLE.length; i++){
		  addOwnText(SCREEN_WIDTH/2, 150 + (i - TITLE.length/2) * TITLE_SCALE * 8, TITLE[i], g, TITLE_SCALE);
		}
		g.setFont(new Font("Times New Roman", Font.BOLD, 24));
		addClickPicScaled(SCREEN_WIDTH/2, SCREEN_HEIGHT*4/5, DEFAULT_TITLE_FRAME_PATH, g, 0, TITLE_SCALE/2);
		addOwnText(SCREEN_WIDTH/2, SCREEN_HEIGHT*4/5, CONTINUE_TEXT, g, TITLE_SCALE/2);
		
		
	}
	
	public boolean getActiveState(){
		return active;
	}
	
	public void clickEvent(){	//Correspond index to key values attributed to click-able objects upon creation.
		switch(this.getClickComponent().getSelected()){
		  case 0: active = false;
		          synchronized(waiting){
		        	  waiting.notifyAll();
		          }
		          break;
		  default: break;
		}
		this.getClickComponent().resetSelected();
		repaint();
	}
	
	public void keyEvent(){
		switch(this.getKeyComponent().getSelected()){
		  default: break;
		}
		this.getKeyComponent().resetSelected();
	}
}
