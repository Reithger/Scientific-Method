package entities;

/**
 * This class is intended to assist in inclusion of art assets into the program without
 * their being explicitly parts of the map already. Not really used yet.
 * 
 * This class is a part of the entities package.
 * 
 * @author Reithger
 *
 */

public class Decoration extends Entity{

//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for a Decoration object that takes in a coordinate location to place an image
	 * as defined by the provided file path.
	 * 
	 * @param x - int value describing the x coordinate position of this Decoration object.
	 * @param y - int value describing the y coordinate position of this Decoration object.
	 * @param imageLoc - String object describing the file path to the image associated to this Decoration object.
	 */
	
	public Decoration(int x, int y, String imageLoc){
		super(x, y, imageLoc);
	}
	
}
