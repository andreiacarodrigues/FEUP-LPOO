package maze.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import maze.logic.Maze;

public class MazePanel extends JPanel {

	private BufferedImage heroArmed;
	private BufferedImage heroUnarmed;
	private BufferedImage sword;
	private BufferedImage wall;
	private BufferedImage exitClose;
	private BufferedImage exitOpen;
	private BufferedImage floor;
	private BufferedImage dragonAwake;
	private BufferedImage dragonAsleep;
	private BufferedImage dragonAwakeArmed;
	private BufferedImage dragonAsleepArmed;

	private Maze maze;

	/**
	 * Create the panel.
	 */
	public MazePanel(Maze maze) throws IOException{
		this.maze = maze;
		try {
			heroArmed = ImageIO.read(new File("src\\maze\\resources\\heroArmed.jpg"));
			heroUnarmed = ImageIO.read(new File("src\\maze\\resources\\heroUnarmed.jpg"));
			sword = ImageIO.read(new File("src\\maze\\resources\\sword.jpg"));
			wall = ImageIO.read(new File("src\\maze\\resources\\wall.jpg"));
			exitClose = ImageIO.read(new File("src\\maze\\resources\\exitClose.jpg"));
			exitOpen = ImageIO.read(new File("src\\maze\\resources\\exitOpen.jpg"));
			floor = ImageIO.read(new File("src\\maze\\resources\\floor.jpg"));
			dragonAwake = ImageIO.read(new File("src\\maze\\resources\\dragonAwake.jpg"));
			dragonAsleep = ImageIO.read(new File("src\\maze\\resources\\dragonAsleep.jpg"));
			dragonAwakeArmed = ImageIO.read(new File("src\\maze\\resources\\dragonAwakeArmed.jpg"));	//
			dragonAsleepArmed = ImageIO.read(new File("src\\maze\\resources\\dragonAsleepArmed.jpg")); //
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		boolean isOpen = true;
		int exiti = 0;
		int exitj = 0;

		for(int i = 0; i < maze.getLength(); i++){
			for(int j = 0; j < maze.getLength(); j++){
				switch (maze.getSymbol(j, i)) {
				case 'x':
					g.drawImage(wall, i*wall.getHeight(), j*wall.getWidth(), null); //parede
					break;
				case 'H':
					g.drawImage(heroUnarmed, i*heroUnarmed.getHeight(), j*heroUnarmed.getWidth(), null); //heroi desarmado
					break;
				case 'A':
					g.drawImage(heroArmed,i*heroArmed.getHeight(), j*heroArmed.getWidth(), null); //heroi armado
					break;
				case ' ':
					g.drawImage(floor, i*floor.getHeight(), j*floor.getWidth(), null); //chao
					break;
				case 'D':
					isOpen = false;
					g.drawImage(dragonAwake, i*dragonAwake.getHeight(), j*dragonAwake.getWidth(), null); //dragao acordado
					break;
				case 'd':
					isOpen = false;
					g.drawImage(dragonAsleep,i*dragonAsleep.getHeight(), j*dragonAsleep.getWidth(), null); //dragao a dormir
					break;
				case 'F':
					isOpen = false;
					g.drawImage(dragonAwakeArmed,i*dragonAwakeArmed.getHeight(), j*dragonAwakeArmed.getWidth(), null); //dragao acordado em cima da espada
					break;
				case 'f':
					isOpen = false;
					g.drawImage(dragonAsleepArmed, i*dragonAsleepArmed.getHeight(), j*dragonAsleepArmed.getWidth(), null); //heroi a dormit em cima da espada
					break;
				case 'E':
					g.drawImage(sword, i*sword.getHeight(), j*sword.getWidth(), null); //espada
					break;
				case 'S':
					exiti = i;
					exitj = j;
					break;
				default:
					break;
				}
			}
		}
		//so desenha a porta no fim para ter a certeza que nao ha nenhum dragao no labirinto 
		//se as coordenadas sao zero e porque nao encontrou a saida
		if(!(exiti == 0 && exitj == 0)){
			if(isOpen)
				g.drawImage(exitOpen,exiti*exitOpen.getHeight(), exitj*exitOpen.getWidth(), null); //saida
			else
				g.drawImage(exitClose,exiti*exitClose.getHeight(), exitj*exitClose.getWidth(), null); //saida
		}
	}
}
