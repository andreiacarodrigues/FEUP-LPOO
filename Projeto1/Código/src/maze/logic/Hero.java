package maze.logic;
import java.util.ArrayList;

import maze.logic.Point;

/**
 * Classe que constroi um Objeto do tipo Hero.
 * O heroi pode mover-se ao longo do caminho do labirinto através de comandos dados pelo 
 * utilizador, com o objetivo de encontrar a saida.
 * Este pode apanhar uma espada que lhe permite matar um ou mais dragões presentes no labirinto,
 * condição unica e necessaria para que ele possa encontrar a saida e ganhar o jogo. 
 * O heroi é morto caso não esteja armado e esteja numa posição adjacente a um dragão. 
 * Existe um boleano chamado 'wall' que indica se o heroi efetivamente se mexeu ou apenas encontrou uma parede.
 * Caso seja true, o dragao nao se movimenta na sua vez.
 * @author Inês Gomes e Andreia Rodrigues
 */

public class Hero extends Elements{

	public enum State {UNARMED, ARMED, DEAD};
	private State currentState;
	private boolean wall = false;

	/**
	 * Construtor da classe
	 * Cria um herói com a posiçao inicial (x,y) e o simbolo que o identifica recebidos como parâmetros. 
	 * Inicializa o estado atual do herói como desarmado.
	 * @param x Coordenada x em que o heroi está posicionado. 
	 * @param y Coordenada y em que o heroi está posicionado. 
	 * @param s Simbolo usado para identificar o heroi no labirinto. 
	 */
	public Hero(int x, int y, char s){
		super(new Point(x,y), s);
		currentState = State.UNARMED;
	}

	/**
	 * Metodo que retorna o boleano que indica se o heroi bateu contra uma parede.
	 * @return Verdadeiro se o boleano se verificar e falso caso contrário.
	 */
	public boolean getWall(){
		return wall;
	}
	 
	/**
	 * Este método retorna true caso o heroi esteja armado (já tenha apanhado a espada)
	 * ou false caso não esteja.
	 * @return Booleano que identifica se está armado ou não.
	 */
	public boolean isArmed()			
	{ return (currentState == State.ARMED);}

	/**
	 * Este método retorna true caso o heroi esteja morto (tenha entrado em contacto com um
	 * dragao, desarmado) ou false caso não esteja.
	 * @return Booleano que identifica se está morto ou não.
	 */
	public boolean isDead()				
	{ return (currentState == State.DEAD); }

	/**
	 * Este método permite alterar o estado do heroi (não armado, armado ou morto).
	 * @param s Novo estado atual do heroi.
	 */
	public void setHeroState(State s){
		currentState = s;
	}

	/**
	 * Este método permite aceder ao estado do heroi (não armado, armado ou morto).
	 * @return Estado actual do heroi.
	 */
	public State getHeroState(){
		return currentState;
	}

	/**
	 * Este método permite alterar o estado atual do heroi para "armado".
	 * É utilizado quando o herói apanha a espada.
	 */
	public void setArmed(){
		currentState = State.ARMED;
		setSymbol('A');
	}

	/**
	 * Este método permite alterar o estado atual do heroi para "morto".
	 * É utilizado quando o herói é apanhado desarmado e numa posição adjacente a um dragão.
	 */
	public void killHero(){ 
		setHeroState(State.DEAD);
	}

	/**
	 * Método que é chamado sempre que o utilizador pretende mover o herói.
	 * Recebe o comando introduzido, o labirinto, a coleçao com todo os dragões e a espada, 
	 * para que sejam atualizados se necessário na função auxiliar "moveAuxiliar".
	 * @param command Comando introduzido pelo utilizador.
	 * @param maze Labirinto onde vamos mover o heroi
	 * @param dragons Coleção do tipo Dragon
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
	 * Método que é chamado pela função move onde vai ser aplicado o movimento de acordo com
	 * a posição atual do heroi e a posição para onde o utilizador o quer mover.
	 * É nesta função que são feitos todos os testes ás varias situações que podem ocorrer
	 * aquanto da sua movimentação, tendo sempre em conta o simbolo que se encontra na posição
	 * para onde o heroi se quer mover. Algumas situações que são tratadas nesta função são
	 * por ex. o heroi ir contra uma parede, apanhar a espada ou ser morto por um dragao.
	 * @param p Ponto para onde o heroi se quer mover.
	 * @param labirinto Labirinto onde vamos mover o heroi
	 * @param dragons Coleção do tipo Dragon
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
		if(maze.compareElement(p, ' '))// espaço livre
			setPoint(p);	

		return running;
	}


	/**
	 * Método que verifica se existe um dragão adjacente ao heroi.
	 * Se existir um dragão adjacente e naquele momento o heroi estiver armado, o dragão é 
	 * morto. Caso contrario, se o heroi não estiver armado, morre e o seu estado atual é 
	 * atualizado.
	 * Retorna true se houver um dragão adjacente ao heroi e false caso não haja.
	 * @param m Labirinto onde vamos mover o heroi
	 * @param d Dragão a analizar
	 * @param s Espada presente no labirinto
	 * @return Boleano que me diz se existe um dragão adjacente ao heroi ou não
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
	 * Método que percorre a coleção de dragões e verifica se estão mortos.
	 * @param dragons Coleção do tipo Dragon
	 * @return Boleano consoante se verifica, ou não, que todos os dragões da coleção estão mortos.
	 */
	public boolean allDragonsDead(ArrayList<Dragon> dragons){
		for(int i = 0; i < dragons.size(); i++)
			if(!dragons.get(i).isDead())
				return false;
		return true;
	}
}
