package maze.test;

import static org.junit.Assert.*;
import org.junit.Test;
import maze.logic.*;

public class TestMazeWithStaticDragon {

	char [][] m1 = {{'X', 'X', 'X', 'X', 'X'},
			{'X', ' ', ' ', 'H', 'S'},
			{'X', ' ', 'X', ' ', 'X'},
			{'X', 'E', ' ', 'D', 'X'},
			{'X', 'X', 'X', 'X', 'X'}};

	//a
	@Test
	public void testMoveHeroToFreeCell() {
		Game g = new Game(m1, 0);
		assertEquals(new Point(1,3),g.getHero().getPoint());
		g.gameLogic("e");
		assertEquals(new Point(1,2),g.getHero().getPoint());
	}

	//b
	@Test
	public void testMoveHeroAgainstWall() {
		Game g = new Game(m1,0);
		assertEquals(new Point(1,3),g.getHero().getPoint());
		g.gameLogic("c");
		assertEquals(new Point(1,3),g.getHero().getPoint());
	}

	//c
	@Test
	public void testMoveHeroToSword(){
		Game g = new Game(m1,0);

		g.getHero().setPoint(new Point(2,1));
		g.gameLogic("b");
		assertEquals(new Point(3,1), g.getHero().getPoint()); //ver se está no ponto (3,1)
		assertEquals('A', g.getHero().getSymbol());	// ver se o símbolo é o A
	}

	//d
	@Test
	public void testMoveHeroAdjacentToDragon() {
		Game g = new Game(m1,0);

		assertEquals(new Point(1,3),g.getHero().getPoint());
		g.gameLogic("b"); // move heroi para ponto adjacente ao dragao
		assertEquals(new Point(2,3),g.getHero().getPoint());
		// vê se heroi está adjacente com dragao. se sim, hero.isDead() == true
		assertTrue(g.getHero().dragonAdjacent(g.getMaze(), g.getDragons().get(0), g.getSword())); 
		assertTrue(g.getHero().isDead());
	}

	//e
	@Test
	public void testHeroKillsDragon(){
		Game g = new Game(m1,0);

		//coloca o herói armado
		g.getSword().setSwordInvisible();
		g.getHero().setArmed();
		assertEquals('A', g.getHero().getSymbol());

		//coloca o heroi na posicao adjacente
		g.gameLogic("b");
		assertEquals(new Point(2,3), g.getHero().getPoint());	

		// verifica se está adjacente e mata se estiver
		assertTrue(g.getHero().dragonAdjacent(g.getMaze(), g.getDragons().get(0), g.getSword()));
		assertTrue(g.getDragons().get(0).isDead());
	}

	//f
	@Test
	public void testHeroExitSucessfully() {
		Game g = new Game(m1,0);

		assertEquals(new Point(1,3),g.getHero().getPoint());

		// move heroi para a posição da espada
		g.gameLogic("e");
		g.gameLogic("e");
		g.gameLogic("b");
		g.gameLogic("b");

		// verifica se heroi apanhou a espada
		assertEquals(g.getSword().getPoint(),g.getHero().getPoint());
		assertTrue(g.getHero().isArmed());

		// move heroi para posição adjacente ao dragao
		g.gameLogic("d");
		assertEquals(new Point(3,2),g.getHero().getPoint());

		// verifica se heroi mata o dragao
		assertTrue(g.getHero().dragonAdjacent(g.getMaze(), g.getDragons().get(0), g.getSword())); 
		assertTrue(g.getDragons().get(0).isDead());

		// faz update do labirinto para que o dragao desapareça
		g.getMaze().updateMaze(g.getDragons(), g.getHero(), g.getSword());
		assertTrue(g.getMaze().compareElement(new Point(3,3), ' '));

		// move o heroi em direção á saida
		g.gameLogic("d");
		g.gameLogic("c");
		g.gameLogic("c");
		g.gameLogic("d");

		// quando atinge a saida, verifica se o jogo acabou
		assertFalse(g.getRunning());
	}

	//g
	@Test
	public void testHeroExitUnsuccessfullyWithoutSword(){
		Game g = new Game(m1,0);

		//mexe para o lado e vê se ficou no mesmo sitio
		g.gameLogic("d");
		assertEquals(new Point(1,3), g.getHero().getPoint());		

		//vê se está desarmado
		assertFalse(g.getHero().isArmed());

		//vê se o booleano de estar a correr está true
		assertTrue(g.getRunning());
	}

	//h
	@Test
	public void testHeroExitUnsuccessfullyWithSword(){
		Game g = new Game(m1,0);

		// heroi move-se até á posição da espada
		g.gameLogic("e");
		g.gameLogic("e");
		g.gameLogic("b");
		g.gameLogic("b");
		assertEquals(g.getSword().getPoint(),g.getHero().getPoint());

		// coloca heroi na posição inicial
		g.getHero().setXpos(1);
		g.getHero().setYpos(3);
		assertEquals(new Point(1,3), g.getHero().getPoint());

		// vê se o heroi está armado
		assertTrue(g.getHero().isArmed());

		// move heroi para a saida
		assertTrue(g.gameLogic("d"));

		//vê se o booleano de estar a correr está true
		assertTrue(g.getRunning());
		
		g.gamePrintMaze();
	}

}
