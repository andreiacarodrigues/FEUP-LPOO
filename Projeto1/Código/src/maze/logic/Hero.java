package maze.logic;
import java.util.ArrayList;

import maze.logic.Point;

/**
 * Classe que constroi um Objeto do tipo Hero.
 * O heroi pode mover-se ao longo do caminho do labirinto atrav�s de comandos dados pelo 
 * utilizador, com o objetivo de encontrar a saida.
 * Este pode apanhar uma espada que lhe permite matar um ou mais drag�es presentes no labirinto,
 * condi��o unica e necessaria para que ele possa encontrar a saida e ganhar o jogo. 
 * O heroi � morto caso n�o esteja armado e esteja numa posi��o adjacente a um drag�o. 
 * Existe um boleano chamado 'wall' que indica se o heroi efetivamente se mexeu ou apenas encontrou uma parede.
 * Caso seja true, o dragao nao se movimenta na sua vez.
 * @author In�s Gomes e Andreia Rodrigues
 */

public class Hero extends Elements{

	public enum State {UNARMED, ARMED, DEAD};
	private State currentState;
	private boolean wall = false;

	/**
	 * Construtor da classe
	 * Cria um her�i com a posi�ao inicial (x,y) e o simbolo que o identifica recebidos como par�metros. 
	 * Inicializa o estado atual do her�i como desarmado.
	 * @param x Coordenada x em que o heroi est� posicionado. 
	 * @param y Coordenada y em que o heroi est� posicionado. 
	 * @param s Simbolo usado para identificar o heroi no labirinto. 
	 */
	public Hero(int x, int y, char s){
		super(new Point(x,y), s);
		currentState = State.UNARMED;
	}

	/**
	 * Metodo que retorna o boleano que indica se o heroi bateu contra uma parede.
	 * @return Verdadeiro se o boleano se verificar e falso caso contr�rio.
	 */
	public boolean getWall(){
		return wall;
	}
	 
	/**
	 * Este m�todo retorna true caso o heroi esteja armado (j� tenha apanhado a espada)
	 * ou false caso n�o esteja.
	 * @return Booleano que identifica se est� armado ou n�o.
	 */
	public boolean isArmed()			
	{ return (currentState == State.ARMED);}

	/**
	 * Este m�todo retorna true caso o heroi esteja morto (tenha entrado em contacto com um
	 * dragao, desarmado) ou false caso n�o esteja.
	 * @return Booleano que identifica se est� morto ou n�o.
	 */
	public boolean isDead()				
	{ return (currentState == State.DEAD); }

	/**
	 * Este m�todo permite alterar o estado do heroi (n�o armado, armado ou morto).
	 * @param s Novo estado atual do heroi.
	 */
	public void setHeroState(State s){
		currentState = s;
	}

	/**
	 * Este m�todo permite aceder ao estado do heroi (n�o armado, armado ou morto).
	 * @return Estado actual do heroi.
	 */
	public State getHeroState(){
		return currentState;
	}

	/**
	 * Este m�todo permite alterar o estado atual do heroi para "armado".
	 * � utilizado quando o her�i apanha a espada.
	 */
	public void setArmed(){
		currentState = State.ARMED;
		setSymbol('A');
	}

	/**
	 * Este m�todo permite alterar o estado atual do heroi para "morto".
	 * � utilizado quando o her�i � apanhado desarmado e numa posi��o adjacente a um drag�o.
	 */
	public void killHero(){ 
		setHeroState(State.DEAD);
	}

	/**
	 * M�todo que � chamado sempre que o utilizador pretende mover o her�i.
	 * Recebe o comando introduzido, o labirinto, a cole�ao com todo os drag�es e a espada, 
	 * para que sejam atualizados se necess�rio na fun��o auxiliar "moveAuxiliar".
	 * @param command Comando introduzido pelo utilizador.
	 * @param maze Labirinto onde vamos mover o heroi
	 * @param dragons Cole��o do tipo Dragon
	 * @param sword Espada presente no labirinto
	 */
	public boolean move(String command, Maze maze, ArrayList<Dragon> dragons, Sword sword)
	{
		Point p = new Point(getXpos(), getYpos());

		if(command.equals("c"))
			p = new Point(getXpos()-1, getYpos());
		else if(command.equals("b"))
			p = new Point(getXpos()+1,getYpos());
		else if(command.equals("d"))
			p = new Point(getXpos(), getYpos()+1);
		else if(command.equals("e"))
			p = new Point(getXpos(), getYpos()-1);

		return moveAuxiliar(p, maze, dragons, sword);
	}

	/**
	 * M�todo que � chamado pela fun��o move onde vai ser aplicado o movimento de acordo com
	 * a posi��o atual do heroi e a posi��o para onde o utilizador o quer mover.
	 * � nesta fun��o que s�o feitos todos os testes �s varias situa��es que podem ocorrer
	 * aquanto da sua movimenta��o, tendo sempre em conta o simbolo que se encontra na posi��o
	 * para onde o heroi se quer mover. Algumas situa��es que s�o tratadas nesta fun��o s�o
	 * por ex. o heroi ir contra uma parede, apanhar a espada ou ser morto por um dragao.
	 * @param p Ponto para onde o heroi se quer mover.
	 * @param labirinto Labirinto onde vamos mover o heroi
	 * @param dragons Cole��o do tipo Dragon
	 * @param sword Espada presente no labirinto
	 */
	private boolean moveAuxiliar(Point p, Maze maze, ArrayList<Dragon> dragons, Sword sword)	//p - novo ponto
	{	
		boolean running = true;

		if(maze.compareElement(p, 'x')){ // encontra um muro
			wall = true;
			return running;
		}
		else
			wall = false;

		if(maze.compareElement(p, sword.getSymbol())) // encontra uma espada
		{
			setPoint(p);
			setSymbol('A');
			setHeroState(State.ARMED);
			sword.setSwordInvisible();
		}
		if(maze.compareElement(p, 'S') && allDragonsDead(dragons)){ //saida
			running = false;
			setSymbol(' ');
		}
		if(maze.compareElement(p, ' '))// espa�o livre
			setPoint(p);	

		return running;
	}


	/**
	 * M�todo que verifica se existe um drag�o adjacente ao heroi.
	 * Se existir um drag�o adjacente e naquele momento o heroi estiver armado, o drag�o � 
	 * morto. Caso contrario, se o heroi n�o estiver armado, morre e o seu estado atual � 
	 * atualizado.
	 * Retorna true se houver um drag�o adjacente ao heroi e false caso n�o haja.
	 * @param m Labirinto onde vamos mover o heroi
	 * @param d Drag�o a analizar
	 * @param s Espada presente no labirinto
	 * @return Boleano que me diz se existe um drag�o adjacente ao heroi ou n�o
	 */
	public boolean dragonAdjacent(Maze m, Dragon d, Sword s){
		if(d.heroAdjacent(this)){
			if(isArmed())
				d.killDragon();
			else
				killHero();
			return true;
		}else	
			return false;
	}

	/**
	 * M�todo que percorre a cole��o de drag�es e verifica se est�o mortos.
	 * @param dragons Cole��o do tipo Dragon
	 * @return Boleano consoante se verifica, ou n�o, que todos os drag�es da cole��o est�o mortos.
	 */
	public boolean allDragonsDead(ArrayList<Dragon> dragons){
		for(int i = 0; i < dragons.size(); i++)
			if(!dragons.get(i).isDead())
				return false;
		return true;
	}
}
