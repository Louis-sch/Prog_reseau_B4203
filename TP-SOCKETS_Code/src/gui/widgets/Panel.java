package gui.widgets;

import gui.widgets.style.CustomColors;

import java.awt.Dimension;

import javax.swing.JPanel;

public class Panel extends JPanel {
	
	/**
	 * Application d'un nouveau style au panel.
	 * */
	public Panel() {
		this.setBackground(CustomColors.BACKGROUND_FACEBOOK_BLUE);
	}
	
	/**
	 * Application d'un nouveau style au panel.
	 * @param dim Dimension souhait�e pour le panel.
	 * */
	public Panel(Dimension dim) {
		this();
		this.setPreferredSize(dim);
	}
}
