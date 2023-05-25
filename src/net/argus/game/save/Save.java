package net.argus.game.save;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.argus.file.Properties;
import net.argus.game.Display;
import net.argus.game.tiles.Cell;
import net.argus.game.tiles.Tiles;
import net.argus.gui.DialogProgress;

public class Save extends Properties {

	public Save(String fileName, String extention, File path) {
		super(new File(path.getAbsolutePath() + "/" + fileName + "." + extention));
	}
	
	public void writeSave(List<Tiles> tiles) throws IOException {
		clear();
		for(int i = 0; i < tiles.size();i ++) {
			Tiles tile = tiles.get(i);
			super.addKey("tile" + i + ".coord", tile.getX() / tile.getWidth() + ":" + tile.getY() / tile.getHeight());
		}
	}
	
	public List<Tiles> openSave(){
		List<Tiles> tiles = new ArrayList<Tiles>();
		
		DialogProgress dp = new DialogProgress("Loading the pattern", Display.getContentPane());
		
		dp.showDialog(0, super.toArray().length, "tiles loaded");
		for(int i = 0; i < super.toArray().length; i++) {
			Point p = getPoint("tile" + i + ".coord");
			tiles.add(new Cell((int) p.getX(), (int) p.getY()));
			dp.setValue(i);
		}
		
		Display.getContentPane().setEnabled(true);
		dp.exit();
		return tiles;
	}

}
