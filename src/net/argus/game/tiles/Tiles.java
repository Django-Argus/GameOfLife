package net.argus.game.tiles;

import javax.swing.JPanel;

public abstract class Tiles extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3032282677771405251L;

	protected int x, y;
	
	protected int width = 10, height = width;
	
	public Tiles(int x, int y) {
		this.x = x * width;
		this.y = y * height;
	}
	
	public abstract void render();
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}

}
