package gui.widgets;

import javax.swing.JComboBox;

import gui.widgets.style.CustomColors;
import gui.widgets.style.CustomFonts;

public class ComboBox<E> extends JComboBox<E> {

	public ComboBox() {
		this.setForeground(CustomColors.TEXT_COLOR);
		this.setBackground(CustomColors.BACKGROUND_WHITE);
		this.setFont(CustomFonts.LABEL_FONT);
	}
}
