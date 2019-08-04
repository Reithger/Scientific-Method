package segments;
import java.util.TimerTask;

import primary.SocializationScreen;
import segments.Social;

import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class Score extends TimerTask{

	private SocializationScreen parent;
	private Social social;
	private double perceivedScore;
	private double[] individual;
	private int[] raters;
	String[] scorePrint;
	
	public Score(SocializationScreen par, Social soc, Lab lab, BufferedImage map){
		parent = par;
		raters = new int[5];
		individual = new double[5];
		social = soc;
		int countUp = 0;
		Random rand = new Random();
		for(int i = 0; i < raters.length; i++){
			raters[i] = rand.nextInt(soc.TIER_LIST_SIZE[i]) + countUp;
			countUp += soc.TIER_LIST_SIZE[i];	
		}
		perceivedScore = calculatePerceivedScore(lab, soc);
		int index = 0;
		for(int i = 0; i < map.getHeight(); i++){
			for(int j = 0; j < map.getWidth(); j++){
				if(new Color(map.getRGB(j, i)).equals(new Color(3, 114, 114))){
					if(index == 0){
						par.getPlayer().setX(j * 4);
						par.getPlayer().setY(i * 4 - par.getPlayer().getHeight()/2 + 15);
						index++;
						continue;
					}
					soc.getCharacters().get(raters[index-1]).setX(j * 4);
					soc.getCharacters().get(raters[index-1]).setY(i * 4);
					index++;
				}
			}
		}
	}
	
	public void run(){		
		parent.repaint();
	}
	
	public double getAugmentedScore(){
		return perceivedScore;
	}
	
	public int[] getRaters(){
		return raters;
	}
	
	public double calculatePerceivedScore(Lab lab, Social soc){
		int score = lab.calculateFinalScore();
		double bias = 0;
		for(int i = 0; i < raters.length; i++){
			individual[i] = soc.getCharacters().get(raters[i]).processResult(parent.getPlayer().getIdentity(), score)/(double)(5-i);
			bias += soc.getCharacters().get(raters[i]).processResult(parent.getPlayer().getIdentity(), score);
		}
		bias /= 15.0;
		return bias;
	}
	
	public String[] getScorePrintOut(){
		if(scorePrint == null){
		  String[] toOut = new String[9];
		  toOut[0] = "Presentation Results:";
		  for(int i = 0; i < raters.length; i++){
			toOut[i+1] = social.getCharacters().get(raters[i]).getTitle() + " " + social.getCharacters().get(raters[i]).getName().split(" ")[1] + ": " + ((int)(individual[i]*100))/100.0;
		  }
		  toOut[6] = "Respect Change: " + (perceivedScore > 0 ? "+" : "-") + ((int)(perceivedScore*100.0))/100.0;
		  int preResp = parent.getPlayer().getRespect();
		  parent.getPlayer().alterRespect((int)perceivedScore);
		  toOut[7] = "Overall Respect: " + (int)parent.getPlayer().getRespect();
		  int postResp = parent.getPlayer().getRespect();
		  if(preResp/100 != postResp/100){
			if(preResp/100 > postResp/100){
				toOut[8] = "Rank Reduced: ";// + ;
			}
			else{
				toOut[8] = "Rank Increased: ";// + ;
			}
		  }
		  else{
			toOut[8] = "No rank change";
		  }
		  scorePrint = toOut;
		}
		return scorePrint;
	}
}
