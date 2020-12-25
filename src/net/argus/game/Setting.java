package net.argus.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.argus.file.Properties;
import net.argus.util.ArrayManager;

public class Setting extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7953568917870001276L;
	
	private Simulation simu;
	
	private static final Integer[] defaultCreateSettings = new Integer[] {3};
	private static final Integer[] defaultRemoveSettings = new Integer[] {1, 4, 5, 6, 7, 8};
	
	private JCheckBox[] createBoxs;
	private JCheckBox[] removeBoxs;
			
	public Setting(Simulation simu) {
		super();
		this.simu = simu;
	}
	
	public void showSettings() {
		JDialog dial = new JDialog();
		dial.setTitle("Settings");
		dial.setSize(200, 270);
		dial.setLocationRelativeTo(Display.getContentPane());
		
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		dial.setContentPane(pan);

		JPanel cre = new JPanel();
		cre.setLayout(new BoxLayout(cre, BoxLayout.Y_AXIS));
		pan.add(BorderLayout.WEST, cre);
		
		JLabel titleCre = new JLabel("Création");
		cre.add(titleCre);
		
		createBoxs = new JCheckBox[8];
		List<Integer> creates = simu.getCreates();
		
		for(int i = 0; i < createBoxs.length; i++) {
			createBoxs[i] = new JCheckBox(Integer.toString(i+1));
			createBoxs[i].setFocusable(false);
			createBoxs[i].setSelected(creates.contains(i+1));
			cre.add(createBoxs[i]);
		}
		
		
		JPanel rem = new JPanel();
		rem.setLayout(new BoxLayout(rem, BoxLayout.Y_AXIS));
		pan.add(BorderLayout.EAST, rem);
		
		JLabel titleRem = new JLabel("Suppression");
		rem.add(titleRem);
		
		removeBoxs = new JCheckBox[8];
		List<Integer> removes = simu.getRemoves();
		
		for(int i = 0; i < removeBoxs.length; i++) {
			removeBoxs[i] = new JCheckBox(Integer.toString(i+1));
			removeBoxs[i].setFocusable(false);
			removeBoxs[i].setSelected(removes.contains(i+1));
			rem.add(removeBoxs[i]);
		}
		
		JPanel south = new JPanel();
		pan.add(BorderLayout.SOUTH, south);
		
		JButton def = new JButton("par defaut");
		south.add(def);
		
		JButton ok = new JButton("OK");
		south.add(ok);
		
		def.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < createBoxs.length; i++) {
					if(ArrayManager.content(defaultCreateSettings, i+1))
						createBoxs[i].setSelected(true);
					else
						createBoxs[i].setSelected(false);
				}
				
				for(int i = 0; i < removeBoxs.length; i++) {
					if(ArrayManager.content(defaultRemoveSettings, i+1))
						removeBoxs[i].setSelected(true);
					else
						removeBoxs[i].setSelected(false);
				}
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creates.clear();
				for(int i = 0; i < createBoxs.length; i++)
					if(createBoxs[i].isSelected())
						creates.add(i+1);
					
				removes.clear();
				for(int i = 0; i < removeBoxs.length; i++)
					if(removeBoxs[i].isSelected())
						removes.add(i+1);
				
				simu.setCreates(creates);
				simu.setRemoves(removes);
				
				
				Properties config = simu.getConfig();
				
				String text = "";
				for(int i : creates)
					text += Integer.toString(i) + ":";
				
				try {config.setKey("create", text);}
				catch(IOException e1) {e1.printStackTrace();}
				
				text = "";
				for(int i : removes)
					text += Integer.toString(i) + ":";
				
				try {config.setKey("remove", text);}
				catch(IOException e1) {e1.printStackTrace();}
				
				dial.setVisible(false);
				
			}
		});
		
		dial.setVisible(true);
	}
	
}
