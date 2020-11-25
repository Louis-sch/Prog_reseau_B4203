package gui.widgets;

import gui.widgets.style.CustomColors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainPanel extends JLayeredPane {
	private BufferedImage image;
	/**
	 * Application d'un nouveau style au panel.
	 * */
	public MainPanel() throws IOException {

		this.setPreferredSize(new Dimension(202,402));
		image = ImageIO.read(new File("src/gui/widgets/Iphone.png"));
		setOpaque(false);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}
