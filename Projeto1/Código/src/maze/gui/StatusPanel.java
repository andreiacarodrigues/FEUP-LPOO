package maze.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {

	private BufferedImage hero; //1
	private BufferedImage dragon; //-1
	private BufferedImage end; //2
	private int status = 1;
	
	public StatusPanel() throws IOException {
		try {
			dragon = ImageIO.read(new File("src\\maze\\resources\\dragon.png"));
			hero = ImageIO.read(new File("src\\maze\\resources\\hero.png"));
			end = ImageIO.read(new File("src\\maze\\resources\\saida.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(status == 1)
			g.drawImage(hero, 0, 0, null);
		if(status == -1)
			g.drawImage(dragon, 0, 0, null);
		if(status == 2)
			g.drawImage(end, 0, 0, null);
	}
	
	public void changeStatus(int x)
	{
		status = x;
		repaint();
		return;
	}
}
