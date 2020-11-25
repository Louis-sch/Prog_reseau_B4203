package gui.widgets;

import gui.widgets.style.CustomColors;
import gui.widgets.style.CustomFonts;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import java.awt.*;

/**
 * TextField customis�.
 * @author aleconte
 * */
public class TextField extends JFormattedTextField {
	
	/**
	 * Application d'un nouveau style au textfield.
	 * */
	public TextField() {
		this.setForeground(Color.BLACK);
		this.setBackground(CustomColors.BACKGROUND_WHITE);
		this.setFont(CustomFonts.LABEL_FONT);
		this.setBorder(BorderFactory.createLineBorder(CustomColors.BORDER_COLOR));
	}
}
