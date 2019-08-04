package mechanics;

import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class InteractFrame extends JPanel{
	
	private static final int screenWidth = 1260;
	private static final int screenHeight = 860;
	
	private static ArrayList<Integer[]> eventSquare;
	private static clickComponent mouseEvent;
	private static keyComponent keyPress;
	
	public InteractFrame(){
		super();
		eventSquare = new ArrayList<Integer[]>();
		mouseEvent = new clickComponent();
		keyPress = new keyComponent();
		mouseEvent.setEventSquare(eventSquare);
		mouseEvent.setParentFrame(this);
		keyPress.setParentFrame(this);
		addMouseListener((MouseListener)mouseEvent);
		addKeyListener((KeyListener)keyPress);
		setSize(screenWidth, screenHeight);
		setVisible(true);
	}
	
	public void clickEvent(){
		System.out.println("Overwrite this method");
	}
	
	public void keyEvent(){
		System.out.println("Overwrite this method");
	}
	
	public clickComponent getClickComponent(){
		return mouseEvent;
	}
	
	public keyComponent getKeyComponent(){
		return keyPress;
	}
	
	public static Image retrieveImage(String path) {
		try {
			return ImageIO.read(InteractFrame.class.getResource(path));
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addPic(int x, int y, String path, Graphics g){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = retrieveImage(path);
		while(!tk.prepareImage(img, -1, -1, null)){	}
		g.drawImage(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, null);
	}
	
	public void addPicScaled(int x, int y, String path, Graphics g, int scale){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = retrieveImage(path);
		img = img.getScaledInstance(img.getWidth(null) * scale, img.getHeight(null) * scale, Image.SCALE_DEFAULT);
		while(!tk.prepareImage(img, -1, -1, null)){	}
		g.drawImage(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, null);
	}
	
	public void addClickPic(int x, int y, String path, Graphics g, int key){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = retrieveImage(path);
		while(!tk.prepareImage(img, -1, -1, null)){}
		g.drawImage(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, null);
		addButton(x, y, img.getWidth(null), img.getHeight(null), "", new Color(0,0,0,0), g, key);
	}
	
	public void addClickPicScaled(int x, int y, String path, Graphics g, int key, int scale){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = retrieveImage(path);
		img = img.getScaledInstance(img.getWidth(null) * scale, img.getHeight(null) * scale, Image.SCALE_DEFAULT);
		while(!tk.prepareImage(img, -1, -1, null)){}
		g.drawImage(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, null);
		addButton(x, y, img.getWidth(null), img.getHeight(null), "", new Color(0,0,0,0), g, key);
	}

	public void addText(int x, int y, String phrase, Graphics g){
		JLabel label = new JLabel(phrase);
		label.setFont(g.getFont());
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString(phrase, x - (int)label.getPreferredSize().getWidth()/2, y - (int)label.getPreferredSize().getHeight()/2 - metrics.getAscent());
	}
	
	public void addOwnText(int x, int y, String phrase, Graphics g, int scale){
		for(int i = 0; i < phrase.length(); i++){
			String address = "/Letters/";
			if("0123456789".indexOf(phrase.charAt(i)) != -1){
				address += "Digits/" + phrase.charAt(i) + ".png";
			}
			else if(phrase.charAt(i) == '!'){
				address += "SpecialCharacters/" + phrase.charAt(i) + ".png";
			}
			else if(phrase.charAt(i) == ':'){
				address += "SpecialCharacters/colon.png";
			}
			else if(phrase.charAt(i) == '?'){
				address += "SpecialCharacters/question_mark.png";
			}
			else if(phrase.charAt(i) == '.'){
				address += "SpecialCharacters/period.png";
			}
			else if(phrase.charAt(i) == '/'){
				address += "SpecialCharacters/forward_slash.png";
			}
			else if(phrase.charAt(i) == '-'){
				address += "SpecialCharacters/dash.png";
			}
			else if(phrase.charAt(i) == '('){
				address += "SpecialCharacters/open_paren.png";
			}
			else if(phrase.charAt(i) == ')'){
				address += "SpecialCharacters/close_paren.png";
			}
			else if(phrase.charAt(i) == '+'){
				address += "SpecialCharacters/plus.png";
			}
			else{
				address += "Alphabet/" + (phrase.charAt(i)+"").toLowerCase() + ".png";
			}
			if(phrase.charAt(i) != ' ')
			  addPicScaled(x + (i - phrase.length()/2) * scale * 6 + (phrase.length()%2 == 0 ? scale * 3 : 0), y, address, g, scale);
		}
	}

	public void addButton(int x, int y, int wid, int hei, String title, Color col, Graphics g, int key){
		Integer[] corners = new Integer[5];
		Color maintain = g.getColor();
		corners[0] = x - wid/2;
		corners[1] = y - hei/2;
		corners[2] = x + wid/2;
		corners[3] = y + hei/2;
		corners[4] = key;
		mouseEvent.addClickRegion(corners);
		g.setColor(col);
		g.fillRect(x - wid/2, y - hei/2, wid, hei);
		g.setColor(maintain);
		addText(x , y + hei, title, g);
		mouseEvent.setEventSquare(eventSquare);
	}
	
	public void addShadedRegion(int x, int y, int wid, int hei, Color col, Graphics g){
		Color maintain = g.getColor();
		g.setColor(col);
		g.fillRect(x, y, wid, hei);
		g.setColor(maintain);
	}
	
	public void addTextField(int x, int y, int wid, int hei, Graphics g, int key){
		//Add button so if clicked, changes state of KeyInput to record and save what is submitted and display as user types each letter.
		//Clicking anywhere else should de-select
	}
}
