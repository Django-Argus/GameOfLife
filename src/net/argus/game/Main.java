package net.argus.game;

import java.awt.Dimension;

import javax.swing.UIManager;

import net.argus.gui.Look;
import net.argus.system.InitializedSystem;
import net.argus.system.UserSystem;

public class Main {
	
	private Simulation simu;
	
	private boolean tick, render;
	private boolean running;
	
	private static double speed = 10;
	
	public Main() {
		Display.createDisplay();
		Display.setSize(920, 944);
		Display.setResizable(false);
		Display.setVisible(true);
		simu = new Simulation(new Dimension(90, 90));
	}
	
	public void start() {
		this.running = true;
		loop();
	}
	
	public void stop() {
		this.running = false;
	}
	
	@SuppressWarnings("unused")
	public void loop() {
		simu.init();
		
		long timer = System.currentTimeMillis();
		
		long before = System.nanoTime();
		double elapsed = 0;
		
		int ticks = 0;
		int frames = 0;
		
		while(running) {
			double nanoSeconds = 1000000000.0 / speed;
			tick = false;
			render = false;
			
			long now = System.nanoTime();
			elapsed = now - before;
			
			if(elapsed > nanoSeconds) {
				before += nanoSeconds;
				tick = true;
				ticks++;
			}else {
				render = true;
				frames++;
			}
			
			if(tick) update();
			if(render) render();
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				ticks = 0;
				frames = 0;
			}
		}
	}
	
	public void update() {
		simu.update();
	}
	
	public void render() {
		simu.render();
	}
	
	public static void setSpeed(double speed) {
		Main.speed += speed;
		if(Main.speed < 0.0) Main.speed = 0;
		if(Main.speed > 50.0) Main.speed = 50.0;
	}
	
	public static double getSpeed() {return speed;}
	
	public static void main(String[] args) {
		InitializedSystem.initSystem(args, UserSystem.getDefaultInitializedSystemManager());
		Look.chageLook(UIManager.getSystemLookAndFeelClassName());
		Main main = new Main();
		main.start();
		
	}

}
