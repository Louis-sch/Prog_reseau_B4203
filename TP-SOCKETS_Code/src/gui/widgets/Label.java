package gui.widgets;

import gui.widgets.style.CustomColors;
import gui.widgets.style.CustomFonts;

import javax.swing.JLabel;

public class Label extends JLabel {
	/**
	 * Application d'un nouveau style au label.
	 * @param text Texte du label.
	 * */
	public Label(String text) {
		super(text);
		this.setForeground(CustomColors.TEXT_COLOR);
		this.setBackground(CustomColors.BACKGROUND_FACEBOOK_BLUE);
		this.setFont(CustomFonts.LABEL_FONT);
	}
}
