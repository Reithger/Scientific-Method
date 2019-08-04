package segments;
import java.awt.Color;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.TimerTask;

import entities.Character;
import primary.SocializationScreen;

import java.util.*;

public class Social extends TimerTask{

	private final int SIZE_OF_INSTITUTE = 15;
	public final int[] TIER_LIST_SIZE = {1, 2, 3, 4, 5};
	private final String[] TIER_LIST_NAMES = {"President", "Dean", "Department Head", "Researcher", "Research Assistant"};
	private final int[] CORRELATE = {0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4};
	private final int NPC_MOVE = 5;
	
	private final String DEFAULT_SPRITE_FEM_PATH = "/sprites/Female/female.png";
	
	private ArrayList<Character> cha;
	private SocializationScreen parent;
	private BufferedImage map;
	private int portraitX;
	private int portraitY;
	
	
	public Social(SocializationScreen soc, BufferedImage mapIn){
		parent = soc;
		cha = new ArrayList<Character>();
		map = mapIn;
		for(int i = 0; i < map.getWidth(); i++){
			for(int j = 0; j < map.getHeight(); j++){
				if(new Color(map.getRGB(i, j)).equals(new Color(178, 1, 25))){
					cha.add(new Character(j*4 - 35, i*4, DEFAULT_SPRITE_FEM_PATH, CORRELATE[cha.size()], TIER_LIST_NAMES[CORRELATE[cha.size()]]));
				}
				if(new Color(map.getRGB(i, j)).equals(new Color(255, 255, 111))){
					portraitX = i * 4;
					portraitY = j * 4;
				}
			}
		}
	}
	
	public void relocateCharacters(){
		int countUp = 0;
		for(int i = 0; i < map.getWidth(); i++){
			for(int j = 0; j < map.getHeight(); j++){
				if(new Color(map.getRGB(i, j)).equals(new Color(178, 1, 25))){
					cha.get(countUp).setX(i * 4);
					cha.get(countUp).setY(j * 4 - 35);
					countUp++;
				}
			}
		}
		parent.assignDean(cha.get(0));
	}
	
	public void run(){
		Random rand = new Random();
		for(int i = 0; i < cha.size(); i++){
			int moveX = 0;
			int moveY = 0;
			switch(rand.nextInt(1000)){
			  case 0: moveX = NPC_MOVE; break;
			  case 1: moveX = -NPC_MOVE; break;
			  case 2: moveY = NPC_MOVE; break;
			  case 3: moveY = -NPC_MOVE; break;
			  case 4: break;
			  default: break;
			}
			if(!parent.checkBlockage(cha.get(i), moveX, moveY) && Math.sqrt(Math.pow(cha.get(i).getX() + moveX - cha.get(i).getHomeY(), 2) + Math.pow(cha.get(i).getY() + moveY - cha.get(i).getHomeX(),2)) < 50){
				cha.get(i).moveX(moveX);
				cha.get(i).moveY(moveY);
			}
			
		}
		parent.repaint();
	}
	
	public int getPortraitX(){
		return portraitX;
	}
	
	public int getPortraitY(){
		return portraitY;
	}
	
	public ArrayList<Character> getCharacters(){
		return cha;
	}
}
