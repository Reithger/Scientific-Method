package segments;
import java.util.*;

import entities.Entity;

/*
 * Handles individual cases of the 'game' portion; key inputs at varying levels of difficulty.
 * Gets refreshed each time the user completes it, only being present at 'active' locations
 * Can be taken care of by an assistant; in that case, different state; probability for success
 * Takes in the user input from outside the class, needs to display to the user what to press.
 * Need library of keys to display. Useful for implementing text communication.
 * 
 */

public class Station {
	
	private final String[][] KEYBOARD = {{"q","w","e","r","t","y","&","u","i","o"},{"a","s","&","d","f","g","h","j","k","l",},{"z","x","c","&","v","b","n","m","p","&"}};
	private final int BASE_VOLUME = 4;
	
	private final String DEFAULT_TILE_PATH = "/UI/Tile/stationTile.png";

	private int difficulty;
	private boolean onOff;
	private boolean activeState;	//NPC or PC?
	private double score;
	private int consecutive;
	public String[] gamePattern;
	private Entity visualDisplay;
	private int cycle;
	private int lifeTime;
	private int lifeSpan; 
	
	public Station(int diff, int x, int y, int age){
		difficulty = diff;
		lifeSpan = age;
		visualDisplay = new Entity(x, y, DEFAULT_TILE_PATH);
		gamePattern = new String[diff];
		setStation();
		resetInit();
	}
	
	public void resetInit(){
		setStation();
		onOff = false;
		activeState = false;
		consecutive = 0;
		score = 0;
	}
	
	public void setStation(){
		for(int i = 0; i < difficulty; i++)
		  gamePattern[i] = "";
		Random rand = new Random();
		int startX = rand.nextInt(KEYBOARD.length);
		int startY = rand.nextInt(KEYBOARD[startX].length);
		for(int h = 0; h < difficulty; h++){
		  for(int i = 0; i < difficulty * BASE_VOLUME; i++){
			for(int j = 0; j < difficulty; j++){
				int prob = rand.nextInt(5);
				switch(prob){
				  case 0: break;
				  case 1: startX += (startX + 1 < KEYBOARD.length) ? 1 : 0; break;
				  case 2: startX -= (startX - 1 >= 0) ? 1 : 0; break;
				  case 3: startY += (startY + 1 < KEYBOARD[startX].length) ? 1 : 0; break;
				  case 4: startY -= (startY - 1 >= 0) ? 1 : 0; break;
				  default: break;
				}
			}
			gamePattern[h] += KEYBOARD[startX][startY];
		  }
		}
		onOff = true;
	}
	
	public void setDifficulty(int diff){
		difficulty = diff;
	}
	
	public void turnOn(){
		onOff = true;
	}
	
	public void incrementLife(){
		lifeTime++;
	}
	
	public int getLifeTime(){
		return lifeTime;
	}
	
	public boolean getStateOnOff(){
		return onOff;
	}
	
	public boolean getStateNPC(){
		return activeState;
	}

	public void incrementScore(){
		consecutive++;
		score += 1 * (1.0 + consecutive/20.0);
	}
	
	public void resetCombo(){
		consecutive = 0;
	}
	
	public int getCombo(){
		return consecutive;
	}
	
	public double getScore(){
		return score;
	}
	
	public int getDifficulty(){
		return difficulty;
	}

	public char nextValue(){
		if(gamePattern[cycle].length() != 0){
		  char out = gamePattern[cycle].charAt(0);
		  gamePattern[cycle] = gamePattern[cycle].substring(1);
		  cycle = (cycle + 1) % difficulty;
		  return out;}
		else{
		  for(int i = 0; i < difficulty; i++){
			  cycle = (cycle + 1) % difficulty;
			  if(gamePattern[cycle].length() != 0)
				return nextValue();
		  }
		  onOff = false;
		  return ' ';
		}
	}

	public Entity getVisual(){
		return visualDisplay;
	}
	
	public int getCycle(){
		return cycle;
	}

	public int getLifeSpan(){
		return lifeSpan;
	}
}
