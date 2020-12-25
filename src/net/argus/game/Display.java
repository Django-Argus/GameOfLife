package net.argus.game;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Display {
	
	private static JFrame fen;
	
	public static void createDisplay() {
		fen = new JFrame();
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void setTitle(String title) {
		fen.setTitle(title);
	}
	
	public static void setSize(int width, int height) {
		fen.setSize(width, height);
	}
	
	public static int getWidth() {
		return fen.getWidth();
	}
	
	public static int getHeight() {
		return fen.getHeight();
	}
	
	public static void setVisible(boolean visible) {
		fen.setVisible(visible);
	}
	
	public static void setResizable(boolean resizable) {
		fen.setResizable(resizable);
	}
	
	public static void add(JComponent comp) {
		fen.add(comp);
	}
	
	public static void addKeyListener(KeyListener l) {
		fen.addKeyListener(l);
	}
	
	public static void setEnable(boolean enable) {
		fen.setEnabled(enable);
	}
	
	public static void addMouseListener(MouseListener l) {
		fen.addMouseListener(l);
	}
	
	public static void setContentPane(Container contentPane) {
		fen.setContentPane(contentPane);
	}
	
	public static Container getContentPane() {
		return fen.getContentPane();
	}
	
	public static void removeAll() {
		fen.removeAll();
	}
	
	public static void remove(Component comp) {
		fen.remove(comp);
	}
}
