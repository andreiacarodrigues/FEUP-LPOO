package maze.test;

import static org.junit.Assert.*;
import org.junit.Test;
import maze.logic.*;
import maze.logic.Dragon.StateD;
import maze.logic.Hero.State;

public class TestMazeWithMovingDragon {

	char [][] m1 = {{'x', 'x', 'x', 'x', 'x'},
					{'x', ' ', ' ', 'H', 'S'},
					{'x', ' ', 'x', ' ', 'x'},
					{'x', 'E', ' ', 'D', 'x'},
					{'x', 'x', 'x', 'x', 'x'}};

	@Test
	public void testHeroUnarmedWithSleepingDragon() {
		Game g = new Game(m1,1);

		//posi��o inicial do heroi
		assertEquals(new Point(1,3),g.getHero().getPoint());

		//1� - mover o heroi
		g.gameLogic("b");

		//2� - Caso n�o tenha ficado adormecido, obrigar o dragao a tal
		if(!g.getDragons().get(0).isAsleep()){
			g.getDragons().get(0).setPoint(new Point(3,3)); //colocar na posicao inicial (caso se tenha mexido)
			g.getDragons().get(0).setAsleep(); //colocar o dragao adormecido
			if(g.getHero().isDead()){	//caso o dragao se tenha mexido e morto o heroi
				g.getHero().setHeroState(State.UNARMED);
				assertFalse(g.getHero().isDead());
			}
		}

		//3� - Verificar que o dragao est� a dormir no ponto 3,3 e o heroi est� no ponto 2,3 desarmado
		assertEquals(new Point(3,3), g.getDragons().get(0).getPoint());
		assertTrue(g.getDragons().get(0).isAsleep());
		assertEquals(new Point(2,3), g.getHero().getPoint());
		assertEquals('H', g.getHero().getSymbol());

		// resultado - nenhum morre
		assertTrue(g.getDragons().get(0).heroAdjacent(g.getHero()));
		assertFalse(g.getDragons().get(0).isDead());
	}

	@Test
	public void testHeroArmedWithSleepingDragon(){
		Game g = new Game(m1,1);

		//posi��o inicial do heroi
		assertEquals(new Point(1,3),g.getHero().getPoint());

		//1� - colocar o heroi armado
		g.getSword().setSwordInvisible();
		g.getHero().setArmed();
		assertEquals('A', g.getHero().getSymbol());

		//2� - mover o heroi de modo a que fique no mesmo lugar
		g.gameLogic("c");
		assertEquals(new Point(1,3),g.getHero().getPoint());

		//3� - Caso n�o tenha ficado adormecido, obrigar o dragao a tal
		if(!g.getDragons().get(0).isAsleep()){
			if(g.getDragons().get(0).isDead())	//caso tenha morrido
				g.getDragons().get(0).setDragonState(StateD.AWAKE);
			g.getDragons().get(0).setPoint(new Point(3,3)); //colocar na posicao inicial (caso se tenha mexido)
			g.getDragons().get(0).setAsleep(); //colocar o dragao adormecido
		}

		//3� - Verificar que o dragao est� a dormir no ponto 3,3
		assertTrue(g.getDragons().get(0).isAsleep());
		assertEquals(new Point(3,3), g.getDragons().get(0).getPoint());

		//4� - mover em dire��o ao heroi
		g.gameLogic("b");

		// resultado - dragao morre (quer continue a dormir quer acorde - n�o tem hipotese de mexer)
		assertTrue(g.getDragons().get(0).isDead());
		assertFalse(g.getHero().isDead());
	}

	@Test
	public void testDragonMovingKillingUnarmedHero(){
		Game g = new Game(m1,2);

		//1� - coloca uma parede no ponto (3,2) -> o dragao s� pode mexer para cima
		Point p = new Point(3,2);
		m1[p.getX()][p.getY()] = 'x';
		assertTrue(g.getMaze().compareElement(p, 'x'));

		//3� - verificar que o heroi est� desarmado e o dragao no ponto 3,3
		assertEquals('H', g.getHero().getSymbol());
		assertEquals(new Point(3,3),g.getDragons().get(0).getPoint());

		//4� - esperar que o dragao ande para cima
		Point p1 = new Point(2,3); //dragao
		Point p2 = new Point(1,3); //heroi
		int i = 0;
		while(true){
			if(i%2== 0)
				g.gameLogic("e");
			else
				g.gameLogic("d");
			if(p1.equals(g.getDragons().get(0).getPoint()) && p2.equals(g.getHero().getPoint()))
				break;
			i++;
		}

		//5� - verifica que o dragao andou para cima e que apenas morreu o heroi
		assertEquals(p1, g.getDragons().get(0).getPoint());
		assertFalse(g.getDragons().get(0).isDead());
		assertTrue(g.getHero().isDead());
	}

	@Test
	public void testDragonMovingBeingKilledByHero(){
		Game g = new Game(m1,2);

		//1� - colocar uma x no ponto (3,2) para apenas se mexer para cima
		Point p = new Point(3,2);
		m1[p.getX()][p.getY()] = 'x';
		assertTrue(g.getMaze().compareElement(p, 'x'));

		//2� - colocar o heroi armado
		g.getSword().setSwordInvisible();
		g.getHero().setArmed();
		assertEquals('A', g.getHero().getSymbol());

		//3� - esperar que o dragao se mexa para cima -> o heroi anda para esquerda e direita
		Point p1 = new Point(2,3); //dragao
		Point p2 = new Point(1,3); //heroi
		int i = 0;
		while(true){
			if(i%2== 0)
				g.gameLogic("e");
			else
				g.gameLogic("d");
			if(p1.equals(g.getDragons().get(0).getPoint()) && p2.equals(g.getHero().getPoint()))
				break;
			i++;
		}
		
		//4� - verifica que o dragao andou para a cima e que morreu
		assertEquals(p1 , g.getDragons().get(0).getPoint());
		assertFalse(g.getHero().isDead());
		assertTrue(g.getDragons().get(0).isDead());
	}

	@Test
	public void testMovingDragonToSword(){
		Game g = new Game(m1,2);

		//1� - colocar o dragao na posicao (2,1)
		Point p = new Point(2,1);
		g.getDragons().get(0).setPoint(p);
		assertEquals(p, g.getDragons().get(0).getPoint());

		//2� - colocar 'x' na posicao (1,1) para a sua unica hipotese ser andar para baixo
		Point p1 = new Point(1,1);
		m1[p1.getX()][p1.getY()] = 'x';
		assertTrue(g.getMaze().compareElement(p1, 'x'));

		//3� - esperar que o dragao ande para baixo -> o heroi move-se para cima e para baixo
		Point p3 = new Point(3,1);
		int i = 0;
		while(true){
			if(i%2== 0)
				g.gameLogic("b");
			else
				g.gameLogic("c");
			if(p3.equals(g.getDragons().get(0).getPoint()))
				break;
			i++;
		}

		//4� - verificar que est� na posicao (3,1) com o s�mbolo 'F'
		assertEquals(new Point(3,1), g.getDragons().get(0).getPoint());
		assertEquals('F', g.getDragons().get(0).getSymbol());
	}

	@Test
	public void testMovingDragonFromSword(){
		Game g = new Game(m1,2);

		//1� colocar a espada invisivel e o dragao armado
		g.getSword().setSwordInvisible();
		g.getDragons().get(0).setDragonState(StateD.ARMED);
		g.getDragons().get(0).setSymbol('F');
		assertFalse(g.getSword().isVisible());

		//2� colocar o dragao no ponto (3,1)
		Point p = new Point(3,1);
		g.getDragons().get(0).setPoint(p);
		assertEquals(p, g.getDragons().get(0).getPoint());

		//3� colocar uma parede no ponto (2,1) para o dragao apenas se mexer para a direita
		Point p1 = new Point(2,1);
		m1[p1.getX()][p1.getY()] = 'x';
		assertTrue(g.getMaze().compareElement(p1, 'x'));

		//4� - esperar que o dragao se mova para a direita -> o heroi move-se para cima e para baixo
		Point p2 = new Point(3,2);
		int i = 0;
		while(true){
			if(i%2== 0)
				g.gameLogic("b");
			else
				g.gameLogic("c");
			if(p2.equals(g.getDragons().get(0).getPoint()))
				break;
			i++;
		}

		//6� verificar que no ponto (3,1) est� a espada, e que o dragao est� no ponto (3,2) com o simbolo 'D'
		assertTrue(g.getMaze().compareElement(p, 'E'));
		assertEquals(p2, g.getDragons().get(0).getPoint());
		assertEquals('D', g.getDragons().get(0).getSymbol());
	}

	@Test
	public void testDragonSleepingSword(){
		Game g = new Game(m1,1);

		//1� - colocar o dragao na posicao (2,1)
		Point p = new Point(2,1);
		g.getDragons().get(0).setPoint(p);
		assertEquals(p,g.getDragons().get(0).getPoint());

		//2� - colocar 'x' na posicao (1,1) e (3,2) para a sua unica hipotese ser andar para baixo 
		m1[1][1] = 'x';
		m1[3][2] = 'x';
		assertTrue(g.getMaze().compareElement(new Point(1,1), 'x'));
		assertTrue(g.getMaze().compareElement(new Point(3,2), 'x'));

		//3� - esperar que ande para baixo -> o her�i movimenta-se sucessivamente para a esquerda e para a direita
		Point p2 = new Point(3,1);
		int i = 1;
		do{
			if(i%2 == 0)
				g.gameLogic("c");
			else
				g.gameLogic("b");
			i++;
		}while(!p2.equals(g.getDragons().get(0).getPoint()));

		//4� - verificar que est� na posicao (3,1) com o s�mbolo 'F'
		assertEquals(p2, g.getDragons().get(0).getPoint());
		assertEquals('F', g.getDragons().get(0).getSymbol());
		
		//6� - esperar que o dragao adormeca
		i = 1;
		do{
			if(i%2 == 0)
				g.gameLogic("c");
			else
				g.gameLogic("b");
			i++;
		}while(!p2.equals(g.getDragons().get(0).getPoint()) || !g.getDragons().get(0).isAsleep());
		
		//6� - verificar que o simbolo � 'f' e esta a dormir
		assertTrue(g.getDragons().get(0).isAsleep());
		assertEquals('f', g.getDragons().get(0).getSymbol());
	}
}
