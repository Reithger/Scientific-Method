package entities;
import java.awt.*;
import java.util.*;

public class Player extends Entity{

	private final Color[] altHair = new Color[]{new Color(255, 255, 0),
												new Color(0, 0, 254),
												new Color(0, 57, 60),
												new Color(178, 1, 25),
												new Color(134, 180, 30),};
	
	private final Color[] altShoe = new Color[]{new Color(65, 65, 65),
												new Color(123, 0, 123), 
												new Color(123, 57, 60),
												new Color(111, 173, 249)};
	
	private final Color[] altSkins = new Color[]{new Color(255, 237, 150, 255), 
												new Color(166, 66, 17, 255),
												new Color(241,194,125, 255),
												new Color(208, 138, 86, 255), 
												new Color(99, 21, 0, 255)};
												//white, black, latin, asian, indigenous
	private final String[] TIER_LIST_NAMES = {"President", "Dean", "Department Head", "Researcher", "Research Assistant"};
	private final int[] TIER_LIST_REQUIREMENTS = {0, 100, 300, 600, 1000};
	
	private final String DEFAULT_FEM_SKIN_PATH = "/sprites/Female/female.png";
	private final String DEFAULT_MAL_SKIN_PATH = "/sprites/Male/male.png";
	private final String DEFAULT_NON_SKIN_PATH = "/sprites/Nonbinary/female.png";
	
	private ArrayList<Character> assistants;
	private boolean[][] identity;
	private boolean[] aware;
	private int ranking;
	private int respect;
	private int maxRespect;
	
	private String name;
	private String title;
	private Color skinMap;
	private Color hairMap;
	private int hairCycle;
	private Color shoeMap;
	private int shoeCycle;
	private Color resetSkinMap;
	private Color resetHairMap;
	private Color resetShoeMap;
	private boolean leftRight;
	
	public Player(int x, int y, String imageLocation){
		super(y, x, imageLocation);
		identity = new boolean[6][];
		assistants = new ArrayList<Character>();
		int[] size = new int[]{3, 3, 5, 6, 7, 3};
		for(int i = 0; i < size.length; i++)
			identity[i] = new boolean[size[i]];
		aware = new boolean[6];
		skinMap = new Color(Color.orange.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 255);
		hairMap = new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), 255);
		shoeMap = new Color(Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), 255);
		resetSkinMap = new Color(Color.orange.getRed(), Color.orange.getGreen(), Color.orange.getBlue(), 255);
		resetHairMap = new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), 255);
		resetShoeMap = new Color(Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), 255); 
		leftRight = false;
	}
	
	public void adjustVisual(){
		int loc = 0;
		for(int k = 0; k < identity[2].length; k++)
		  if(identity[2][k] == true)
			loc = k;
		Pixel[][] visual = this.getVisual();
		for(int i = 0; i < visual.length; i++){
			for(int j = 0; j < visual[0].length; j++){
				if(visual[i][j].getColor().equals(skinMap)){
				  adjustVisual(i, j, altSkins[loc]);
				}
				if(visual[i][j].getColor().equals(hairMap)){
					adjustVisual(i, j, altHair[hairCycle]);
				}
				if(visual[i][j].getColor().equals(shoeMap)){
					adjustVisual(i, j, altShoe[shoeCycle]);
				}
			}
		}
		skinMap = altSkins[loc];
		hairMap = altHair[hairCycle];
		shoeMap = altShoe[shoeCycle];
	}

	public void drawToScreen(Graphics g){
		Pixel[][] visual = this.getVisual();
		for(int i = 0; i < visual[0].length; i++){
			  for(int j = 0; j < visual.length; j++){
				Pixel p = !leftRight ? visual[j][i] : visual[visual.length - j - 1][i];
				if(p.getAlpha() != 0){
				  g.setColor(new Color(p.getRed(), p.getGreen(), p.getBlue(), p.getAlpha()));
				  g.drawRect(getX() + j - visual.length/2, getY() + i - visual[0].length/2, 1, 1);
				}
			  }
		}
	}
	
	public void alterRespect(int resp){
		respect += respect + resp > 0 ? respect + resp : resp;
		if(resp > 0){
			maxRespect += resp;
		}
		for(int i = 0; i < TIER_LIST_REQUIREMENTS.length; i++){
			if(respect >= TIER_LIST_REQUIREMENTS[i]){
				continue;
			}
			else{
				assignRanking(i);
			}
		}
	}
	
	public int getRank(){
		return ranking;
	}
	
	public void assignRanking(int in){
		ranking = in;
		assignTitle(TIER_LIST_NAMES[ranking]);
	}
	
	public void assignTitle(String in){
		title = in;
	}
	
	public int getRespect(){
		return respect;
	}
	
	public int getMaxRespect(){
		return maxRespect;
	}
	
	public String getTitle(){
		return title;
	}

	public String getName(){
		return name;
	}
	
	public boolean[][] getIdentity(){
		return identity;
	}

	public ArrayList<Character> getAssistants(){
		return assistants;
	}
	
	public void setState(int ident, int index){
		switch(ident){
		  case 0: setSex(index); break;
		  case 1: setGender(index); break;
		  case 2: setRace(index); break;
		  case 3: setSexuality(index); break;
		  case 4: setDisability(index); break;
		  case 5: setEcoSocBack(index); break;
		  default: break;
		}
	}
	
	public void setName(String nom){
		name = nom;
	}

	private void setSex(int index){							//Identity matrix 0'th decides Sex data
		for(int i = 0; i < identity[0].length; i++)
		  identity[0][i] = false;
		identity[0][index] = true;
	}
	
	private void setGender(int index){						//Identity matrix 1'st decides Gender data
		for(int i = 0; i < identity[1].length; i++)
		  identity[1][i] = false;
		identity[1][index] = true;
		int loc = 0;
		for(int i = 0; i < identity[1].length; i++)
			if(identity[1][i])
				loc = i;
		switch(loc){
		  case 0: setVisual(DEFAULT_MAL_SKIN_PATH); break;
		  case 1: setVisual(DEFAULT_FEM_SKIN_PATH); break;
		  case 2: setVisual(DEFAULT_NON_SKIN_PATH); break;
		  default: break;
		}
		skinMap = resetSkinMap;
		hairMap = resetHairMap;
		shoeMap = resetShoeMap;
		adjustVisual();
	}
	
	private void setRace(int index){							//Identity matrix 2'nd decides Race data
		for(int i = 0; i < identity[2].length; i++)
		  identity[2][i] = false;
		identity[2][index] = true;
	}
	
	private void setSexuality(int index){					//Identity matrix 3'rd decides Sexuality data
		for(int i = 0; i < identity[3].length; i++)
		  identity[3][i] = false;
		identity[3][index] = true;
	}
	
	private void setDisability(int index){				//Identity matrix 4'th decides Disability data, multiple true options
		for(int i = 0; i < identity[4].length; i++)
			identity[4][i] = false;
		identity[4][index] = true;
	}
	
	private void setEcoSocBack(int index){					//Identity matrix 5'th decides EcoSocBackground data
		for(int i = 0; i < identity[5].length; i++)
		  identity[5][i] = false;
		identity[5][index] = true;
	}
	
	public void moveX(int x){
		leftRight = x > 0 ? true : x < 0 ? false : leftRight;
		
		setX(getX() + x);
	}
	
	public void moveY(int y){
		setY(getY() + y);
	}

	public void cycleShoe(){
		shoeCycle = (1 + shoeCycle) % altShoe.length;
	}
	
	public void cycleHair(){
		hairCycle = (1 + hairCycle) % altHair.length;
	}
}
