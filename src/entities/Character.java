package entities;

import java.util.*;

/**
 * This class models a non-player character, handling their generation while using inherited methods
 * for much of their behavior. Generation includes the assigning of their identity traits, their
 * biases, and their name.
 * 
 * This class is a part of the entities package.
 * 
 * @author Reithger
 *
 */

public class Character extends Player{
	
//---  Constant Values   ----------------------------------------------------------------------

	/** Constant String[] object containing potential First Names for Characters.*/
	private String[] NAME_LIST_FIRST = {"Sam", "Jordan", "Aerin", "Jamie", "Morgan", "Taylor", "Billie", "Cameron", "Robin", "Leo", "Fran", "Bailey",
										"Dakota", "Devin", "Drew", "Ashton", "Jesse", "Kelsey", "Logan", "Mackenzie", "Marley", "Mason", "Quinn",
										"Reagan", "Rowan", "Ryan", "Shawn", "Ashley", "Casey", "Chris", "Clem", "Connie", "Denny", "Gail", "Izzy"};
	/** Constant String[] object containing potential Last Names for Characters.*/
	private String[] NAME_LIST_LAST = {"Jones", "Myers", "Seamus", "Moroaner", "Douglas", "Brown", "Miller", "Stewart", "Sanchez", "Davis", "Perez",
									   "Martinez", "Thompson", "Robinson", "Clark", "Campbell", "Alexander", "Foster", "Patterson", "Baker", "Kelly",
									   "Lewis", "Lee", "Walker", "Watson", "Russel", "Bryant", "Carter", "Green", "Hayes", "Diaz", "Taylor", "Garcia"};
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** double[][] instance variable describing the biases held by this Character.*/
	private double[][] bias;
	/** int value describing their natural x-coordinate location (where they should be on the map.)*/
	private int boundX;
	/** int value describing their natural y-coordinate location (where they should be on the map.)*/
	private int boundY;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for Character object that assigns a default location to the Character and initializes
	 * their identity and bias matrices; it also takes a default image for them, names them, and assign
	 * the provided titles and ranking.
	 * 
	 * @param x - int value describing the x coordinate location that this Character is set to.
	 * @param y - int value describing the y coordinate location that this Character is set to.
	 * @param imageLoc - String object denoting a file path to the default image for this Character.
	 * @param newRanking - int value describing the ranking in the institution for this Character.
	 * @param newTitle - String object representing the title assigned to this Character.
	 */
	
	public Character(int x, int y, String imageLoc, int newRanking, String newTitle){
		super(x, y, imageLoc);
		boundX = x;
		boundY = y;
		fillIdent();
		fillBias();
		Random rand = new Random();
		setName(NAME_LIST_FIRST[rand.nextInt(NAME_LIST_FIRST.length)] + " " + NAME_LIST_LAST[rand.nextInt(NAME_LIST_LAST.length)]);
		adjustVisual();
		assignRanking(newRanking);
		assignTitle(newTitle);
	}
	
	/**
	 * Simple Constructor for Character objects; assigns the provided location, name, and title to
	 * the Character, as well as using the specified file path for their image.
	 * 
	 * @param x - int value describing the x coordinate location that this Character is set to.
	 * @param y - int value describing the y coordinate location that this Character is set to.
	 * @param name - String object representing the desired name for this Character.
	 * @param title - String object representing the title assigned to this Character.
	 * @param imageLoc - String object denoting a file path to the default image for this Character.
	 */
	
	public Character(int x, int y, String name, String title, String imageLoc){
		super(x, y, imageLoc);
		setName(name);
		assignTitle(title);
	}
	
//---  Assignation   --------------------------------------------------------------------------
	
	/**
	 * This method assists in the initialization of a new Character by assigning values to their identity
	 * matrix and defining their visual appearance. Helper methods handle the assigning of their sex, gender,
	 * race, sexuality, disability status, and socio-economic status.
	 */
	
	private void fillIdent(){
		Random rand = new Random();
		setState(0, randomizeSexGender(getRank(), rand));	//sex
		setState(1, randomizeSexGender(getRank(), rand));	//gender
		setState(2, randomizeRace(getRank(), rand));	//race
		setState(3, randomizeSexuality(getRank(), rand));	//sexuality
		setState(4, randomizeDisability(getRank(), rand));	//disability
		setState(5, randomizeEconomicSocial(getRank(), rand));	//economic social
		int randHair = rand.nextInt(10);
		int randShoe = rand.nextInt(10);
		for(int i = 0; i < randHair; i++)
		  cycleHair();
		for(int i = 0; i < randShoe; i++)
		  cycleShoe();
	}
	
	/**
	 * This method assists in the initialization of a new Character by assigning values to their bias matrix as
	 * affected by their already generated identity and a roll of the dice. Values default to an even 50 and from
	 * there are shifted. Typically one will be more mindful towards their own identity and less so to others,
	 * with the additional randomized value being added/subtracted from that.
	 */
	
	private void fillBias(){
		int[] count = {3, 3, 5, 6, 7, 3, 1};
		bias = new double[7][];			//sex gender race sexuality disability socio-economic disparity(sex/gender)
		for(int i = 0; i < bias.length; i++){
			bias[i] = new double[count[i]];
			for(int j = 0; j < bias[i].length; j++){
				bias[i][j] = 50;
			}
		}
		boolean[][] iden = getIdentity();
		Random rand = new Random();
		for(int i = 0; i < iden.length; i++){
			for(int j = 0; j < iden[i].length; j++){
				if(iden[i][j]){
					bias[i][j] += 10 + rand.nextInt(10);
				}
				else{
					bias[i][j] -= 10 + rand.nextInt(10);
				}
			}
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method that requests the assigned location of this Character's x coordinate.
	 * 
	 * @return - Returns an int value describing the x coordinate that has been assigned as this Character's home.
	 */

	public int getHomeX(){
		return boundX;
	}
	
	/**
	 * Getter method that requests the assigned location of this Character's y coordinate.
	 * 
	 * @return - Returns an int value describing the y coordinate that has been assigned as this Character's home.
	 */
	
	public int getHomeY(){
		return boundY;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * Setter method that assigns a new value as the x coordinate of the Character's home location.
	 * 
	 * @param in - int value provided as the new x coordinate of the Character object's home location.
	 */
	
	public void setHomeX(int in){
		boundX = in;
	}
	
	/**
	 * Setter method that assigns a new value as the y coordinate of the Character's home location.
	 * 
	 * @param in - int value provided as the new y coordinate of the Character object's home location.
	 */
	
	public void setHomeY(int in){
		boundY = in;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method handles the calculation of how inclined the Character object is towards the player's
	 * performance as a derivative of both the player's score and the Character's opinion towards their
	 * identity, applying their Bias Matrix values to the player's Identity Matrix. 
	 * 
	 * @param iden - boolean[][] object representing the identity of the player.
	 * @param score - double value representing the quality of the player's work.
	 * @return - Returns a double value as the calculated perceived score given to the player.
	 */
	
	public double processResult(boolean[][] iden, double score){
		double base = 1.0;
		for(int i = 0; i < iden.length; i++){
			for(int j = 0; j < iden[i].length; j++){
				if(iden[i][j]){
					base *= bias[i][j]/50.0;
				}
			}
		}
		return base * score * (5 - getRank());
	}
	
//---  Assignation Helper   -------------------------------------------------------------------
	
	/**
	 * This method handles the assigning of a Character object's sex and gender identity values.
	 * 
	 * @param rank - int value describing their position in the institute; it affects likelihood of certain outcomes.
	 * @param rand - Random object provided to save the time of generating a new one very often.
	 * @return - Returns an int value defining the sex and gender identity values.
	 */
	
	private int randomizeSexGender(int rank, Random rand){
		int hold = rand.nextInt(100);
		switch(rank){
		    case 0: return multiItemProbability(hold, 72, 97);		//president
		    case 1: return multiItemProbability(hold, 67, 97);		//dean
		    case 2: return multiItemProbability(hold, 67, 97);		//department head
		    case 3:	return multiItemProbability(hold, 67, 97);  	//researcher
		    case 4:	return multiItemProbability(hold, 47, 97);		//research assistant
			default: return 0;
		}
	}
	
	/**
	 * This method handles the assigning of a Character object's race identity value.
	 * 
	 * @param rank - int value describing their position in the institute; it affects likelihood of certain outcomes.
	 * @param rand - Random object provided to save the time of generating a new one very often.
	 * @return - Returns an int value defining the sex and gender identity values.
	 */
	
	private int randomizeRace(int rank, Random rand){
		int hold = rand.nextInt(100);
		switch(rank){
		    case 0: return multiItemProbability(hold, 86, 90, 94, 96);		//president
		    case 1: return multiItemProbability(hold, 80, 84, 88, 98);		//dean
		    case 2: return multiItemProbability(hold, 80, 84, 88, 98);		//department head
		    case 3:	return multiItemProbability(hold, 72, 75, 80, 98);  	//researcher
		    case 4:	return multiItemProbability(hold, 47, 49, 61, 98);		//research assistant
			default: return 0;
		}
	}
	
	/**
	 * This method handles the assigning of a Character object's sexuality identity value.
	 * 
	 * @param rank - int value describing their position in the institute; it affects likelihood of certain outcomes.
	 * @param rand - Random object provided to save the time of generating a new one very often.
	 * @return - Returns an int value defining the sex and gender identity values.
	 */
	
	private int randomizeSexuality(int rank, Random rand){
		int hold = rand.nextInt(100);
		switch(rank){
			default: return multiItemProbability(hold, 80, 88, 92, 94, 96);
		}
	}
	
	/**
	 * This method handles the assigning of a Character object's disability identity value.
	 * 
	 * @param rank - int value describing their position in the institute; it affects likelihood of certain outcomes.
	 * @param rand - Random object provided to save the time of generating a new one very often.
	 * @return - Returns an int value defining the sex and gender identity values.
	 */
	
	private int randomizeDisability(int rank, Random rand){
		int hold = rand.nextInt(100);
		switch(rank){
		    case 0: return multiItemProbability(hold, 74, 81, 84, 85, 91, 97);		//president
		    case 1: return multiItemProbability(hold, 90, 92, 93, 94, 96, 98);		//dean
		    case 2: return multiItemProbability(hold, 90, 92, 93, 94, 96, 98);		//department head
		    case 3:	return multiItemProbability(hold, 92, 93, 94, 95, 96, 97);  	//researcher
		    case 4:	return multiItemProbability(hold, 92, 93, 94, 95, 96, 97);		//research assistant
			default: return 0;
		}
	}
	
	/**
	 * This method handles the assigning of a Character object's socio-economic identity value.
	 *
	 * @param rank - int value describing their position in the institute; it affects likelihood of certain outcomes.
	 * @param rand - Random object provided to save the time of generating a new one very often.
	 * @return - Returns an int value defining the sex and gender identity values.
	 */
	
	private int randomizeEconomicSocial(int rank, Random rand){
		int hold = rand.nextInt(100);
		switch(rank){
		    case 0: return multiItemProbability(hold, 15, 44);		//president
		    case 1: return multiItemProbability(hold, 18, 47);		//dean
		    case 2: return multiItemProbability(hold, 20, 50);		//department head
		    case 3:	return multiItemProbability(hold, 23, 56);  	//researcher
		    case 4:	return multiItemProbability(hold, 25, 60);		//research assistant
			default: return 0;
		}
	}
	
	/**
	 * This method simplifies the process of calculating a result based on varying percentage
	 * likelihoods; it takes in a calculated random value and a list of values denoting the ranges
	 * for certain results to occur, and then iterates through that list to see which option was
	 * selected.
	 * 
	 * @param calc - int value denoting the result of a randomization to choose an identity value.
	 * @param chance - varargs ... array of int values describing the likelihood of different outcomes. 
	 * @return - Returns an int value describing the result of checking the calc value against the chance values.
	 */
	
	private int multiItemProbability(int calc, int ... chance){
		for(int i = 0; i < chance.length; i++){
			if(calc < chance[i]){
				return i;
			}
		}
		return chance.length-1;
	}
	
}
