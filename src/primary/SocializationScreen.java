package primary;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;

import entities.Character;
import entities.Entity;
import entities.Pixel;
import entities.Player;
import mechanics.InteractFrame;
import segments.Lab;
import segments.Score;
import segments.Social;
import segments.Station;

public class SocializationScreen extends InteractFrame{

	private final int SCREEN_WIDTH_ADJUSTMENT = 14;
	private final int SCREEN_HEIGHT_ADJUSTMENT = 36;
	private final int SCREEN_WIDTH = 1200;
	private final int SCREEN_HEIGHT = 800;
	private final int FRAME_RATE = 1000/30;
	private final int MOVEMENT_SPEED = 5;
	private final int DISTANCE_INTERACT = 15;
	
	private final String DEFAULT_BACKGROUND_PATH = "/Backgrounds/GameplayScreen/game.png";
	private final String DEFAULT_LETTER_FRAME_PATH = "/UI/LetterFrame/letterFrame.png";
	private final String DEFAULT_TITLE_FRAME_PATH = "/UI/TitleFrame/TitleFrame.png";
	private final String DEFAULT_LETTER_E_PATH = "/Letters/Alphabet/e.png";
	private final String DEFAULT_LETTER_COLON_PATH = "/Letters/SpecialCharacters/colon.png";
	private final String DEFAULT_LETTER_PERIOD_PATH = "/Letters/SpecialCharacters/period.png";
	private final String DEFAULT_TILE_PATH = "/UI/Tile/stationTile.png";
	private final String DEFAULT_SPRITE_FEM_PATH = "/sprites/Female/female.png";

	private BufferedImage map;
	private Social socialSegment;
	private Timer coordinate;		//different scheduled methods for different states; socialize, lab, score, etc.
	private Lab labSegment;
	private Score scoreSegment;
	private Player sci;
	private Character dean;
	
	private boolean keyReading;
	private int state;
	private int labStartX;
	private int labStartY;
	
	
	public SocializationScreen(String title, JFrame parentFrame, Object paused, Player play){
		super();
		parentFrame.setSize(SCREEN_WIDTH + SCREEN_WIDTH_ADJUSTMENT, SCREEN_HEIGHT + SCREEN_HEIGHT_ADJUSTMENT);
		sci = play;
		state = 0;
		sci.setX(900);
		sci.setY(450);
		sci.alterRespect(0);
		map = (BufferedImage)InteractFrame.retrieveImage(DEFAULT_BACKGROUND_PATH);
		parentFrame.add(this);
		parentFrame.setTitle(title);
		this.requestFocusInWindow();
		this.setDoubleBuffered(true);
		coordinate = new Timer();
		socialSegment = new Social(this, map);
		labSegment = new Lab(map, sci.getMaxRespect(), sci.getAssistants(), this);
		coordinate.schedule(socialSegment, 0, FRAME_RATE);
		socialSegment.relocateCharacters();
		top_loop:
		for(int i = 0; i < map.getHeight(); i++){
			for(int j = 0; j < map.getWidth(); j++){
				if(new Color(255, 255, 42).equals(new Color(map.getRGB(j, i)))){
				  boolean stationLocation = true;
					for(int k = -1; k <= 1; k++){
					  for(int l = -1; l <= 1; l++){
						if(!new Color(255, 255, 42).equals(new Color(map.getRGB(j + k, i + l)))){
						  stationLocation = false;
				}   }   }
				if(stationLocation){
				  labStartX = j * 4;
				  labStartY = i * 4;
				  break top_loop;
					}
				}
			}
		}
		repaint();
	}
	
	public void paintComponent(Graphics g){
		addShadedRegion(0,0, SCREEN_WIDTH, SCREEN_HEIGHT, new Color(1f, 1f, 1f, 1f), g);	//It's cheap, but it resets the screen.
		addPicScaled(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, DEFAULT_BACKGROUND_PATH, g, 4);

		drawGeneric(g);
		switch(state){
		  case 0: 
				  socialSegment.relocateCharacters();drawSocial(g);
				  break;	//socialize
		  case 1: drawLab(g);
				  break;  //lab
		  case 2: drawLabOutTransition(g);
			  	  break;	//end of lab, request action to transition
		  case 3: drawScore(g);
		  		  break;	//score
		  default: break;
		}

	}
	
	public void keyEvent(){
		char in = (char)this.getKeyComponent().getSelected();
		if(keyReading){
			if((in+"").toLowerCase().equals(labSegment.getCurrentLetter())){
				labSegment.correctInput();
			}
			else{
				labSegment.wrongInput();
			}
			return;
		}
		switch(in){
		  case 'A': if(!checkBlockage(sci, -MOVEMENT_SPEED, 0)) sci.moveX(-MOVEMENT_SPEED); break;
		  case 'S': if(!checkBlockage(sci, 0, MOVEMENT_SPEED)) sci.moveY(MOVEMENT_SPEED); break;
		  case 'D': if(!checkBlockage(sci, MOVEMENT_SPEED, 0)) sci.moveX(MOVEMENT_SPEED); break;
		  case 'W': if(!checkBlockage(sci, 0, -MOVEMENT_SPEED)) sci.moveY(-MOVEMENT_SPEED); break;
		  case 'E': switch(state){
		  			  case 0: 
		  				  if(Math.sqrt(Math.pow(sci.getX() - labStartX, 2) + Math.pow(sci.getY() - labStartY + sci.getHeight()/2, 2)) < DISTANCE_INTERACT){
		  		  			labSegment = new Lab(map, sci.getMaxRespect(), sci.getAssistants(), this);
		  		  			coordinate.schedule(labSegment, 0, FRAME_RATE);
		  		  			state = 1;
		  				  }
		  				  break;	//socialize
		  			  case 1: 
		  				  for(int i = 0; i < labSegment.getStations().size(); i++){
		  					  Entity labTile = labSegment.getStations().get(i).getVisual();
		  					  if(Math.sqrt(Math.pow(sci.getX() - labTile.getX(), 2) + Math.pow(sci.getY() - labTile.getY() + sci.getHeight()/2, 2)) < DISTANCE_INTERACT){
		  						  keyReading = true;
		  						  labSegment.setIndexActive(i);
		  						  labSegment.wrongInput();
		  					  }
		  				  }
		  				  break;	//lab
		  			  case 2: 
		  				  scoreSegment = new Score(this, socialSegment, labSegment, map);
		  				  labSegment.cancel();
		  				  coordinate.schedule(scoreSegment, 0);
		  				  state = 3;
		  				  repaint();
		  				  break;	//score
		  			  case 3:
		  				  state = 0;
		  				  socialSegment.relocateCharacters();
		  				  break;
		  			  default: 
		  				  break;
		  			}
			  		
			  		
			  		break;
		/*  case 'F':
		  			labSegment = new Lab(map, sci.getMaxRespect(), sci.getAssistants(), this);
		  			coordinate.schedule(labSegment, 0, FRAME_RATE);
		  			state = 1;
		  			break;
		  case 'G': labSegment.setTotalStations(0); 
		  			repaint(); 
		  			break;*/
		  default: break;
		}
	}

	public void clickEvent(){
		if(keyReading){
			if("&".equals(labSegment.getCurrentLetter())){
				labSegment.correctInput();
			}
			else{
				labSegment.wrongInput();
			}
		}
	}
	
	public boolean checkBlockage(Player ent, int moveX, int moveY){
		Color moveToBack = new Color(map.getRGB((ent.getX() - ent.getWidth()/2 + (moveX < 0 ? moveX : 0))/4, (ent.getY() + ent.getHeight()/2)/4));
		Color moveToFront = new Color(map.getRGB((ent.getX() + ent.getWidth()/2 + (moveX > 0 ? moveX : 0))/4, (ent.getY() + ent.getHeight()/2)/4));
		Color moveToBack2 = new Color(map.getRGB((ent.getX() - ent.getWidth()/2)/4, (ent.getY() + ent.getHeight()/2 + moveY)/4));
		Color moveToFront2 = new Color(map.getRGB((ent.getX() - ent.getWidth()/2)/4, (ent.getY() + ent.getHeight()/2 + moveY/2)/4));
		Color moveToBack3 = new Color(map.getRGB((ent.getX() - ent.getWidth()/2 + (moveX < 0 ? moveX/2 : 0))/4, (ent.getY() + ent.getHeight()/2)/4));
		Color moveToFront3 = new Color(map.getRGB((ent.getX() + ent.getWidth()/2 + (moveX > 0 ? moveX/2 : 0))/4, (ent.getY() + ent.getHeight()/2)/4));
		Color moveToBack4 = new Color(map.getRGB((ent.getX() - ent.getWidth()/2)/4, (ent.getY() + ent.getHeight()/2 + moveY)/4));
		Color moveToFront4 = new Color(map.getRGB((ent.getX() - ent.getWidth()/2)/4, (ent.getY() + ent.getHeight()/2 + moveY/2)/4));
		if(((moveToBack.equals(Color.black) || moveToFront.equals(Color.black) || moveToBack3.equals(Color.black) || moveToFront3.equals(Color.black)) && moveX != 0) || ((moveToBack2.equals(Color.black) || moveToFront2.equals(Color.black) || moveToFront4.equals(Color.black) || moveToBack4.equals(Color.black)) && moveY != 0)){
			return true;
		}
		return false;
	}
	
	public void setState(int newState){
		state = newState;
	}
	
	public int getState(){
		return state;
	}
	
	private void drawSocial(Graphics g){
		ArrayList<Character> cha = socialSegment.getCharacters();
		for(int i = 0; i < cha.size(); i++)
		    cha.get(i).drawToScreen(g);
		sci.drawToScreen(g);
		if(Math.sqrt(Math.pow(sci.getX() - labStartX, 2) + Math.pow(sci.getY() + sci.getHeight()/2 - labStartY, 2)) < DISTANCE_INTERACT){
			  addPic(sci.getX() + 25, sci.getY() - 25, DEFAULT_LETTER_FRAME_PATH, g);
			  addPicScaled(sci.getX() + 25, sci.getY() - 25, DEFAULT_LETTER_E_PATH, g, 3);
		  }
	}
	
	private void drawLab(Graphics g){
		  drawLabStationTiles(g);
		  drawLabAssistants(g);
		  drawLabActiveStation(g);
		  drawLabStationsRemaining(g);
	}

	private void drawLabOutTransition(Graphics g){
		String[] outcome = labSegment.calculateResults();
		String digits = "0123456789:.";
		int scale = (outcome.length * 18) / 32 + 2; 
		addPicScaled(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, DEFAULT_LETTER_FRAME_PATH, g, scale > 8 ? scale : 8);
		for(int i = 0; i < outcome.length; i++){
		  for(int j = 0; j < outcome[i].length(); j++){
			String directory = "";
			if(digits.indexOf(outcome[i].charAt(j)+"") == -1){
				directory = "/Letters/Alphabet/" + outcome[i].charAt(j) + ".png";
			}
			else if(digits.indexOf(outcome[i].charAt(j)+"") == 10){
				directory = DEFAULT_LETTER_COLON_PATH;
			}
			else if(digits.indexOf(outcome[i].charAt(j)+"") == 11){
				directory = DEFAULT_LETTER_PERIOD_PATH;
			}
			else{
				directory = "/Letters/Digits/" + outcome[i].charAt(j) + ".png";
			}
			if(outcome[i].charAt(j) != ' ')
			  addPicScaled(SCREEN_WIDTH/2 + (j - outcome[i].length()/2) * 2 * 6, SCREEN_HEIGHT/2 + (i - outcome.length/2) * 2 * 8, directory, g, 2);
		  }
		}
	}
	
	private void drawScore(Graphics g){
		for(int i = 0; i < scoreSegment.getRaters().length; i++){
			socialSegment.getCharacters().get(scoreSegment.getRaters()[i]).drawToScreen(g);
		}
		sci.drawToScreen(g);
		String[] output = scoreSegment.getScorePrintOut();
		addPicScaled(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, DEFAULT_LETTER_FRAME_PATH, g, 16);
		for(int i = 0; i < output.length; i++){
			for(int j = 0; j < output[i].length(); j++){
				addOwnText(SCREEN_WIDTH/2, SCREEN_HEIGHT/2 + (i - output.length/2) * 8 * 2, output[i], g, 2);
			}
		}
	}
	
	private void drawLabStationTiles(Graphics g){
		for(int i = 0; i < labSegment.getStations().size(); i++){
			  int count = labSegment.getCounter();
			  Station stat = labSegment.getStations().get(i);
			  if(stat.getStateOnOff()){
				  if(Math.sqrt(Math.pow(sci.getX() - stat.getVisual().getX(), 2) + Math.pow(sci.getY() + sci.getHeight()/2 - stat.getVisual().getY(), 2)) < DISTANCE_INTERACT && labSegment.getIndexActive() != i){
					  addPic(sci.getX() + 25, sci.getY() - 25, DEFAULT_LETTER_FRAME_PATH, g);
					  addPicScaled(sci.getX() + 25, sci.getY() - 25, DEFAULT_LETTER_E_PATH, g, 3);
				  }
				  Pixel[][] pArr = stat.getVisual().getVisual();
				  Pixel grey = new Pixel(Color.gray);
				  Pixel yellow = new Pixel(new Color(255, 255, 42));
				  Pixel black = new Pixel(Color.black);
				  for(int j = 0; j < pArr.length; j++){
					  for(int k = 0; k < pArr[0].length; k++){
						  Pixel curr = pArr[j][k];
						  double ratio = 2.0 * Math.PI * count / 60.0;
						  if(stat.getLifeTime() < stat.getLifeSpan()*3/4){
							  if(curr.getColor().equals(Color.gray))
							    pArr[j][k] = new Pixel((int)(grey.getRed() * (Math.cos(ratio) + 1) + yellow.getRed() * (Math.sin(ratio - Math.PI/2) + 1)) / 2, (int)(grey.getGreen() * (Math.cos(ratio) + 1) + yellow.getGreen() * (Math.sin(ratio - Math.PI/2) + 1)) / 2, (int)(grey.getBlue() * (Math.cos(ratio) + 1) + yellow.getBlue() * (Math.sin(ratio - Math.PI/2) + 1)) / 2);
							  else
								pArr[j][k] = new Pixel((int)(grey.getRed() * (Math.sin(ratio - Math.PI/2) + 1) + yellow.getRed() * (Math.cos(ratio) + 1)) / 2, (int)(grey.getGreen() * (Math.sin(ratio - Math.PI/2) + 1) + yellow.getGreen() * (Math.cos(ratio) + 1)) / 2, (int)(grey.getBlue() * (Math.sin(ratio - Math.PI/2) + 1) + yellow.getBlue() * (Math.cos(ratio) + 1)) / 2);  
						  }
						  else{
							  if(curr.getColor().equals(Color.gray))
								pArr[j][k] = new Pixel((int)(black.getRed() * (Math.cos(ratio) + 1) + yellow.getRed() * (Math.sin(ratio - Math.PI/2) + 1)) / 2, (int)(black.getGreen() * (Math.cos(ratio) + 1) + yellow.getGreen() * (Math.sin(ratio - Math.PI/2) + 1)) / 2, (int)(black.getBlue() * (Math.cos(ratio) + 1) + yellow.getBlue() * (Math.sin(ratio - Math.PI/2) + 1)) / 2);
							  else
								pArr[j][k] = new Pixel((int)(black.getRed() * (Math.sin(ratio - Math.PI/2) + 1) + yellow.getRed() * (Math.cos(ratio) + 1)) / 2, (int)(black.getGreen() * (Math.sin(ratio - Math.PI/2) + 1) + yellow.getGreen() * (Math.cos(ratio) + 1)) / 2, (int)(black.getBlue() * (Math.sin(ratio - Math.PI/2) + 1) + yellow.getBlue() * (Math.cos(ratio) + 1)) / 2);
						  }
					  }
				  }
				  stat.getVisual().setVisual(pArr);
				  stat.getVisual().drawToScreenScaled(4, g);
				  stat.getVisual().setVisual(DEFAULT_TILE_PATH);
			  }
		  }
	}

	private void drawLabAssistants(Graphics g){
		  for(int i = 0; i < sci.getAssistants().size(); i++){
			    sci.getAssistants().get(i).drawToScreen(g);
			    }	
			  sci.drawToScreen(g);
	}
	
	private void drawLabActiveStation(Graphics g){
		  if(labSegment.getIndexActive() != -1 && !labSegment.getCurrentLetter().equals(" ")){
			  Station stat = labSegment.getStations().get(labSegment.getIndexActive());
			  for(int i = 0; i < stat.getDifficulty(); i++){
				Entity frame = new Entity(stat.getVisual().getX() + (int)(25 * Math.cos(2 * Math.PI  * i / (double)stat.getDifficulty())), stat.getVisual().getY() + (int)(25 * Math.sin(2 * Math.PI * i / (double)stat.getDifficulty())), DEFAULT_LETTER_FRAME_PATH);
				if(stat.getCombo() == 0)
			      frame.drawToScreenColored(g, 1, 0, 0);
				else{
				  frame.drawToScreenColored(g, 1.0 / stat.getCombo(), stat.getCombo()/25.0 <= 1 ? stat.getCombo()/25.0 : 1, 1.0 / stat.getCombo());
				}
				if(i == labSegment.getCycle())
			      addPicScaled(stat.getVisual().getX() + (int)(25 * Math.cos(2 * Math.PI * i / (double)stat.getDifficulty())), stat.getVisual().getY() + (int)(25 * Math.sin(2 * Math.PI * i / (double)stat.getDifficulty())), "/Letters/Alphabet/" + labSegment.getCurrentLetter() + ".png", g, 3);
			  }
		  }
		  else if(labSegment.getCurrentLetter().equals(" ")){
			  keyReading = false;
			  labSegment.endRound();
		  }
	}

	private void drawLabStationsRemaining(Graphics g){
		addPicScaled(labSegment.getX(), labSegment.getY(), DEFAULT_LETTER_FRAME_PATH, g, 2);
		int repre = labSegment.getStationsRemaining();
		String transfer = repre + "";
		for(int i = 0; i < transfer.length(); i++){
			addPicScaled(labSegment.getX() + (i - transfer.length()/2) * 3 * 5, labSegment.getY(), "/Letters/Digits/" + transfer.charAt(i) + ".png", g, 3);
		}
	}
	
	private void drawGeneric(Graphics g){
		dean.drawToScreen(g);
		int scale_size = 3;
		addPicScaled(dean.getX() + 215, dean.getY() - 15, DEFAULT_TITLE_FRAME_PATH, g, scale_size+1);
		addOwnText(dean.getX() + 215, dean.getY() - scale_size * 7 - 12, dean.getTitle(), g, scale_size);
		addOwnText(dean.getX() + 215, dean.getY() + scale_size * 7 - 12, dean.getName(), g, scale_size);
	}
	
	public void adjustRespect(int adjustment){
		sci.alterRespect(adjustment);
	}

	public Player getPlayer(){
		return sci;
	}

	public void assignDean(Character den){
		dean = new Character(socialSegment.getPortraitY(), socialSegment.getPortraitX(), den.getName(), den.getTitle(), DEFAULT_SPRITE_FEM_PATH);
		dean.setVisual(den.getVisual());
	}
}
