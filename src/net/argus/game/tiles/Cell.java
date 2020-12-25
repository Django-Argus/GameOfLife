package net.argus.game.tiles;

import java.awt.Color;
import java.awt.Dimension;

import net.argus.game.Display;

public class Cell extends Tiles {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8145689965488922920L;

	public Cell(int x, int y) {
		super(x, y);
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(width, height));
		
		Display.getContentPane().add(this);
	}

	@Override
	public void render() {
		this.setBounds(x, y, width, height);
	}

}
