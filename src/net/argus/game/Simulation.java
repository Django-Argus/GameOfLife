package net.argus.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import net.argus.file.Filter;
import net.argus.file.Properties;
import net.argus.game.save.Save;
import net.argus.game.tiles.Cell;
import net.argus.game.tiles.Tiles;

public class Simulation {
	
	private Properties config;
	
	private List<Tiles> tiles = new ArrayList<Tiles>();
	private JPanel pan;
	private Setting set;
	private boolean running;
	private int width, height;
	
	private List<Integer> creates = new ArrayList<Integer>();
	private List<Integer> removes = new ArrayList<Integer>();
	
	private int generation;
	
	private int button;
	
	public Simulation(Dimension d) {
		this.width = d.width;
		this.height = d.height;
		pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setLayout(null);
		Display.setContentPane(pan);
		
		JPanel sp = new JPanel();
		sp.setOpaque(false);
		sp.setBounds(0, 0, d.width * 10 + 15, d.height * 10 + 15);
		sp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		pan.add(sp);
		
		config = new Properties("config", "");
		
		int[] numCre = config.getMultiInteger("create");
		
		for(int num : numCre)
			creates.add(num);
		
		int[] numRem = config.getMultiInteger("remove");
		
		for(int num : numRem)
			removes.add(num);
		
		set = new Setting(this);
	}
	
	public void init() {
		pan.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {button = e.getButton(); click(e);}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		
		pan.addMouseWheelListener(new MouseWheelListener() {public void mouseWheelMoved(MouseWheelEvent e) {Main.setSpeed(-e.getWheelRotation());}});
		
		pan.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {}
			public void mouseDragged(MouseEvent e) {click(e);}
		});
		
		Display.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE)
					running = !running;
				
				if((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
					save();
				 
				if((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
					open(); 
				
				if((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
					removeAll();
				
				if((e.getKeyCode() == KeyEvent.VK_P) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
					set.showSettings();
				
			}
		});

	}
	
	public void update() {
		if(running) {
			List<Point> create = new ArrayList<Point>();
			List<Point> remove = new ArrayList<Point>();
			
			for(int x = 0, y = x; x <= width && y <= height; y += x>=width?1:0, x = x>=width?0:x+1) {
				if(!isTileExist(x, y) && creates.contains(countAdjacent(x, y)))
					create.add(new Point(x, y));
				
				if(isTileExist(x, y) && removes.contains(countAdjacent(x, y)))
					remove.add(new Point(x, y));
				
				if(isTileExist(x, y) && countAdjacent(x, y) == 0)
					remove.add(new Point(x, y));
				
			}
			
			for(Point p : create) {
				tiles.add(new Cell(p.x, p.y));
				//System.out.println("Create: " + p.x + "  "  + p.y);
			}
			
			for(Point p : remove) {
				remove(p.x, p.y);
				//System.out.println("Removed: " + p.x + "  "  + p.y);	
			}
			
			generation++;
			
		}
		Display.setTitle("Génération: " + generation + "  Running: " + running + "  Speed: " + Main.getSpeed());
	}
	
	public void render() {
		for(int i = 0; i < tiles.size(); i++) {
			Tiles tile = tiles.get(i);
			if(tile!=null) tile.render();
			Display.getContentPane().repaint();
		}
	}
	
	public int countAdjacent(int x, int y) {
		int count = 0;
		
		if(isTileExist(x - 1, y)) count++;
		if(isTileExist(x + 1, y)) count++;
		if(isTileExist(x, y - 1)) count++;
		if(isTileExist(x, y + 1)) count++;
		
		if(isTileExist(x - 1, y - 1)) count++;
		if(isTileExist(x - 1, y + 1)) count++;
		if(isTileExist(x + 1, y - 1)) count++;
		if(isTileExist(x + 1, y + 1)) count++;
		
		return count;
	}
	
	public void click(MouseEvent e) {
		if(!running)
			if(button == MouseEvent.BUTTON3 && !isTileExist(e.getPoint().x / 10, e.getPoint().y / 10)) 
				tiles.add(new Cell(e.getPoint().x / 10, e.getPoint().y / 10));
			else if(button == MouseEvent.BUTTON1)
				remove(e.getPoint().x / 10, e.getPoint().y / 10);
	}
	
	public void save() {
		running = false;
		JFileChooser choos = new JFileChooser(System.getProperty("user.dir"));
		
		choos.setFileFilter(new Filter(new String[] {"meta"}, "meta file"));
		
		choos.setMultiSelectionEnabled(false);
		int returnValue = choos.showOpenDialog(null);
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = choos.getSelectedFile();
			
			String path = selectedFile.getPath();
			String fileName = path.substring(path.lastIndexOf('\\') + 1);
			
			path = path.substring(0, path.lastIndexOf('\\'));
			
			selectedFile = new File(path);
			
			try{new Save(fileName, "meta", selectedFile).writeSave(tiles);}
			catch(IOException e) {e.printStackTrace();}
		}
	}
	
	public void open() {
		running = false;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				JFileChooser choos = new JFileChooser(System.getProperty("user.dir"));
				choos.setFileFilter(new Filter(new String[] {"meta"}, "meta file"));
				
				choos.setMultiSelectionEnabled(false);
				
				int returnValue = choos.showOpenDialog(null);
				
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = choos.getSelectedFile();
					
					String path = selectedFile.getPath();
					String fileName = path.substring(path.lastIndexOf('\\') + 1, path.lastIndexOf('.'));
					//String fileName = selectedFile.getName();
					
					path = path.substring(0, path.lastIndexOf('\\'));
					
					selectedFile = new File(path);
					
					removeAll();
					tiles = new Save(fileName, "meta", selectedFile).openSave();
				}
				
			}
		}).start();
		
	}
	
	public void removeAll() {
		while(tiles.size()>0) {
			remove(tiles.get(0));
		}
		
	}
	
	public void remove(Tiles tile) {
		tiles.remove(tile);
		Display.getContentPane().remove(tile);
	}
	
	public void remove(int x, int y) {
		for(int i = 0; i < tiles.size(); i++) {
			Tiles tile = tiles.get(i);
			if(tile.getX() / tile.getWidth() == x && tile.getY() / tile.getHeight() == y) {
				remove(tile);
			}
		}
		
	}
	
	public boolean isTileExist(int x, int y) {
		for(Tiles tile : tiles)
			if(tile.getX() / tile.getWidth() == x && tile.getY() / tile.getHeight() == y)
				return true;
		return false;
	}
	
	public void play() {running = true;}
	public void sleep() {running = false;}
	
	public List<Tiles> getTiles() {return tiles;}
	public Properties getConfig() {return config;}

	public List<Integer> getCreates() {return creates;}
	public List<Integer> getRemoves() {return removes;}
	
	public void setCreates(List<Integer> list) {this.creates = list;}
	public void setRemoves(List<Integer> list) {this.removes = list;}

}
