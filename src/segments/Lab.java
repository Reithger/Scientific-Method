package segments;

import java.util.TimerTask;

import entities.Character;
import primary.SocializationScreen;

import java.awt.image.*;
import java.awt.*;
import java.util.*;

public class Lab extends TimerTask{
	
	private final Color STATION_COLOR = new Color(255, 255, 42);
	private final int STATION_VOLUME = 4;
	private final int BASE_WAIT_TIME = 45;
	private final int STATION_LIFE_TIME = 450;
	
	private ArrayList<Station> stations;
	private ArrayList<Character> assistants;
	private SocializationScreen parent;
	private String currentLetter;
	private double[] allScores;
	private int scoreFront;
	private int rankInstance;
	private int counter;
	private int upStations;
	private int indexActive;
	private int sizeRound;
	private double maxScore;
	private double userScore;
	private int totalStations;
	private int waitTime;
	private int xUI;
	private int yUI;

	public Lab(BufferedImage map, int ranking, ArrayList<Character> incomingHelp, SocializationScreen soc){
		int width = map.getWidth();
		int height = map.getHeight();
		indexActive = -1;
		stations = new ArrayList<Station>();
		assistants = incomingHelp;
		parent = soc;
		rankInstance = 1 + ranking/100;
		Random rand = new Random();
		currentLetter = "";
		waitTime = BASE_WAIT_TIME;
		totalStations = rankInstance * STATION_VOLUME + rand.nextInt(rankInstance * STATION_VOLUME / 2);
		allScores = new double[totalStations];
		maxScore = totalStations * .8;
		int count = 0;
		for(int i = 0; i < height; i++){
		  for(int j = 0; j < width; j++){
			if(STATION_COLOR.equals(new Color(map.getRGB(j, i)))){
			  boolean stationLocation = true;
				for(int k = -1; k <= 1; k++){
				  for(int l = -1; l <= 1; l++){
					if(!STATION_COLOR.equals(new Color(map.getRGB(j + k, i + l)))){
					  stationLocation = false;
			}   }   }
				if(stationLocation){
				  if(count == 0){
					count++;
					continue;}
				  stations.add(new Station(1 + rankInstance / 5 + rand.nextInt(1 + ranking / 100), j * 4, i * 4, STATION_LIFE_TIME));
				  count++;
		}	}	}	}
		count--;
		xUI = stations.get(stations.size()-1).getVisual().getX();
		yUI = stations.get(stations.size()-1).getVisual().getY();
		stations.remove(stations.size()-1);
	}
	
	public void run(){
		if(totalStations <= 0){
			parent.setState(2);
			parent.repaint();
			return;
		}
		counter = (counter + 1) % waitTime;
		for(int i = 0; i < stations.size(); i++){
			if(stations.get(i).getStateOnOff()){
				stations.get(i).incrementLife();
				if(stations.get(i).getLifeTime() > STATION_LIFE_TIME && indexActive != i){
					scoreFront++;
					upStations--;
					totalStations--;
					stations.get(i).resetInit();
				}
			}
		}
		Random rand = new Random();
		if(upStations < rankInstance && counter == 0 && totalStations - 1 >= 0){
			int index = rand.nextInt(stations.size());
			if(!stations.get(index).getStateOnOff()){
			  stations.get(index).turnOn();
			  upStations++;
			  waitTime = BASE_WAIT_TIME + rand.nextInt(rankInstance * BASE_WAIT_TIME);
			}
			else{
				counter --;
			}
		}
		parent.repaint();
	}
	
	public void correctInput(){
		stations.get(indexActive).incrementScore();
		currentLetter = stations.get(indexActive).nextValue()+ "";
		sizeRound++;
	}
	
	public void wrongInput(){
		stations.get(indexActive).resetCombo();
		currentLetter = stations.get(indexActive).nextValue()+ "";
		sizeRound++;
	}
	
	public void endRound(){
		if(indexActive == -1)
		  return;
		double score = stations.get(indexActive).getScore() / sizeRound;
		allScores[scoreFront] = score;
		scoreFront++;
		stations.get(indexActive).resetInit();
		setIndexActive(-1);
		userScore += score;
		upStations--;
		totalStations--;
	}

	public void setIndexActive(int index){
		indexActive = index;
		sizeRound = 0;
	}
	
	public ArrayList<Station> getStations(){
		return stations;
	}

	public String getCurrentLetter(){
		return currentLetter;
	}
	
	public int getIndexActive(){
		return indexActive;
	}

	public int getCounter(){
		return counter;
	}

	public int getCycle(){
		return stations.get(indexActive).getCycle();
	}	

	public int getX(){
		return xUI;
	}
	
	public int getY(){
		return yUI;
	}
	
	public int getStationsRemaining(){
		return totalStations;
	}
	
	public String[] calculateResults(){
		double ratioOfVictory = userScore / maxScore;
		int finalScore = (int)(ratioOfVictory * 100);
		String[] stationResults = new String[allScores.length+2];
		stationResults[0] = "Results:";
		for(int i = 1; i < stationResults.length-1; i++){
			stationResults[i] = "Experiment " + i + ": " + ((int)((allScores[i-1] / ratioOfVictory)*100))/100.0;
		}
		stationResults[stationResults.length-1] = "Overall Value: " + finalScore;
		return stationResults;
	}
	
	public int calculateFinalScore(){
		return (int)(userScore/maxScore)*100;
	}

	public void setTotalStations(int in){
		totalStations = in;
	}
}
