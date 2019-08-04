package entities;
import java.awt.Color;

public class Pixel {

	private int red;
	private int green;
	private int blue;
	private int alpha;
	
	public Pixel(int r, int g, int b){
		red = r >= 0 && r < 256 ? r : 0;
		green = g >= 0 && g < 256 ? g : 0;
		blue = b >= 0 && b < 256 ? b : 0;
		alpha = 255;
	}
	
	public Pixel(int r, int g, int b, int a){
		setRed(r);
		setGreen(g);
		setBlue(b);
		alpha = a >= 0 && a < 256 ? a : 255;
	}
	
	public Pixel(Color in){
		setRed(in.getRed());
		setGreen(in.getGreen());
		setBlue(in.getBlue());
		setAlpha(in.getAlpha());
	}
	
	public Pixel(Pixel in){
		setRed(in.getRed());
		setGreen(in.getGreen());
		setBlue(in.getBlue());
		setAlpha(in.getAlpha());
	}
	
	public Color getColor(){
		return new Color(red, green, blue, alpha);
	}
	
	public int getRed(){
		return red;
	}
	
	public int getGreen(){
		return green;
	}
	
	public int getBlue(){
		return blue;
	}
	
	public int getAlpha(){
		return alpha;
	}
	
	public void setRed(int r){
		red = r >= 0 && r < 256 ? r : 0;
	}
	
	public void setGreen(int g){
		green = g >= 0 && g < 256 ? g : 0;
	}
	
	public void setBlue(int b){
		blue = b >= 0 && b < 256 ? b : 0;
	}
	
	public void setAlpha(int a){
		alpha = a >= 0 && a < 256 ? a : 255;
	}
}
