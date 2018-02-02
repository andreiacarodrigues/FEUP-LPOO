package maze.logic;

import java.util.Random;

/**
 * Classe que constroi um Objeto do tipo Dragao.
 * Um dragao e morto se estiver adjacente a um heroi e o mesmo estiver armado.
 * No caso de o heroi estar desarmado, o dragao mata o heroi.
 * @author Inês Gomes e Andreia Rodrigues
 */
public class Dragon extends Elements{

	public enum Direction {UP, DOWN, RIGHT, LEFT};
	public enum StateD {AWAKE, ASLEEP, MOVE, ARMED, ASLEEP_ARMED, DEAD};
	private StateD currentState;

	/**
	 * Construtor da classe Dragon filha da classe Elements.
	 * Recebe como parâmetro a posição onde se encontra e o simbolo porque e representado.
	 * @param x Coordenada x da posição do dragao no tabuleiro.
	 * @param y Coordenada y da posicao do dragao no tabuleiro.
	 * @param s Simbolo pelo que e representado o dragao no tabuleiro.
	 */
	public Dragon(int x, int y, char s){
		super(new Point(x,y), s);
		currentState = StateD.AWAKE;
	}
	
	/**
	 * Metodo que modifica o estado de um dragao dentro da enumeracao 'StateD'.
	 * @param state Estado para o qual vamos mudar.
	 */
	public void setDragonState(StateD state){
		currentState = state;
	}

	/**
	 * Metodo que nos retorna um boleano consoante o estado do dragao e morto, ou nao.
	 * @return Verdadeiro se o dragao esta morto, falso se contrario.
	 */
	public boolean isDead(){ 
		return (currentState == StateD.DEAD);
	}

	/**
	 * Metodo que nos retorna um boleano consoante o estado do dragao e a dormir ou acordado.
	 * @return Verdadeiro se o dragao estava a dormir (armado ou desarmado) e falso nos restantes estados.
	 */
	public boolean isAsleep(){
		return (currentState == StateD.ASLEEP || currentState == StateD.ASLEEP_ARMED);
	}

	/**
	 * Metodo apenas usado para testes unitários.
	 * Caso um dragao esteja acordado e colocado a dormir tendo em conta se anteriormente estava ou nao armado.
	 */
	public void setAsleep(){
		if(currentState == StateD.AWAKE){
			setDragonState(StateD.ASLEEP);
			setSymbol('d');
		}
		else if(currentState == StateD.ARMED)
			setDragonState(StateD.ASLEEP_ARMED);
			setSymbol('f');
	}
	
	/**
	 * Metodo que coloca o estado do dragao como 'morto'.
	 */
	public void killDragon(){ 
		setDragonState(StateD.DEAD);
	}

	/**
	 * Metodo usado para tomar uma decisao acerca da proxima jogada do dragao.
	 * Apos o movimento do heroi, caso o modo de jogo escolhido seja medio ou dificil, o dragao toma uma decisao aleatoria.
	 * As 3 opcoes possiveis sao as 3 primeiras da enumeracao 'options' (acordar, adormecer ou movimetar-se).
	 * Caso, por exemplo, a opcao aleatorio seja acordar e o dragao ja esteja acordado, o dragao nao sofre qualquer alteracao pelo que existe 1 terco de hipotese de permanecer inalterado, acordar/adormecer e de se movimentar.
	 * Apos o dragao se movimentar e verificado se ficou adjacente ao dragao. Nesse caso e tomada uma decisao de acordo com o estado do heroi (armado ou desarmado).
	 * Para cada opcao do dragao e verificado o estado atual e o anterior de modo a alterar para o simbolo correto.
	 * @param h Objeto do tipo Hero
	 * @param m Objeto do tipo Maze (labirinto em causa)
	 * @param s Objeto do tipo Sword
	 */
	public void randomOption(Hero h, Maze m, Sword s){
		Random random = new Random();
		StateD option = StateD.values()[(random.nextInt(3))];  // retorna qual a opção 

		if(h.getWall()) //um dragao nao se mexe enquanto o heroi nao se mexer
			return;
		
		if(option == StateD.ASLEEP && currentState == StateD.AWAKE){
			setDragonState(StateD.ASLEEP);
			setSymbol('d');
		}
		else if(option == StateD.ASLEEP && currentState == StateD.ARMED){
			setDragonState(StateD.ASLEEP_ARMED);
			setSymbol('f');
		}
		else if(option == StateD.AWAKE && currentState == StateD.ASLEEP){
			setDragonState(StateD.AWAKE);
			setSymbol('D');
		}
		else if(option == StateD.AWAKE && currentState == StateD.ASLEEP_ARMED){
			setDragonState(StateD.ARMED);
			setSymbol('F');
			s.setSwordInvisible();
		}
		else if(option == StateD.MOVE && (currentState == StateD.AWAKE || currentState == StateD.ARMED))
			move(h, m, s);
	
		if(heroAdjacent(h)){
			if(h.isArmed())
				setDragonState(StateD.DEAD);
			else if(!isAsleep())
				h.killHero();
		}
	}

	/**
	 * Metodo que movimenta o dragao.
	 * O dragao e movido aleatoriamente para cima, baixo, direita ou esquerda.
	 * Se a posicao para a qual se iria mexer tiver, por exemplo uma parede, o dragao permanece no lugar. 
	 * @param h Objeto do tipo Hero.
	 * @param m Objeto do tipo Maze.
	 * @param s Objeto do tipo Sword.
	 */
	public void move(Hero h, Maze m, Sword s)
	{
		if(h.getWall()) //o dragao só se move se o heroi se mover
			return;
		
		Random random = new Random();
		Direction direction = Direction.values()[(random.nextInt(4))];  // retorna qual a direção para onde ele vai
		Point p = new Point(getXpos(), getYpos());

		if(direction == Direction.UP)
			p = new Point(getXpos()-1, getYpos());
		else if(direction == Direction.DOWN)
			p = new Point(getXpos()+1,getYpos());
		else if(direction == Direction.RIGHT)
			p = new Point(getXpos(), getYpos()+1);
		else if(direction == Direction.LEFT)
			p = new Point(getXpos(), getYpos()-1);

		moveAuxiliar(p, h, m, s);
	}

	/**
	 * Metodo que verifica se o dragao e o heroi estao em posicoes adjacentes.
	 * @param hero Objeto do tipo Hero.
	 * @return Verdadeiro se estiverem adjacentes, falso se o contrario.
	 */
	public boolean heroAdjacent(Hero hero)
	{
		int diffX = getXpos() - hero.getXpos();
		int diffY = getYpos() - hero.getYpos();

		diffX = Math.abs(diffX);
		diffY = Math.abs(diffY);

		boolean adj = false;

		if((diffX == 0) && (diffY == 1) || (diffX == 1) && (diffY == 0))
			adj = true;
		else if((diffX == 0) && (diffY == 0)) //pode acontecer o dragao ficar em cima do dragao -> deve mata-lo da mesma maneira
			adj = true;
		
		if(adj == true){
			if(hero.isArmed())
				setDragonState(StateD.DEAD);
			else if(!hero.isArmed() && !isAsleep())
				hero.killHero();	
		}
		return adj;
	}

	/**
	 * Metodo auxiliar ao move que faz as verificacoes com a espadas e celulas livres.
	 * @param p Ponto onde se faz as verificacoes.
	 * @param hero Objeto do tipo Hero.
	 * @param labirinto Objeto do tipo Maze.
	 * @param espada Objeto do tipo Sword.
	 */
	private void moveAuxiliar(Point p, Hero h, Maze m, Sword s)
	{	
		if(m.compareElement(p, s.getSymbol())) // encontra uma espada
		{
			setPoint(p);
			setSymbol('F');
			s.setSwordInvisible();
			setDragonState(StateD.ARMED);
		}

		if(m.compareElement(p, ' ')) // espaço livre
		{
			setPoint(p);
			if(currentState == StateD.ARMED)
			{
				setSymbol('D');
				setDragonState(StateD.AWAKE);
				s.setSwordVisible();
			}
		}	
		heroAdjacent(h);
	}
}
