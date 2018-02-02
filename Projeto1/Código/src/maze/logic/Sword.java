package maze.logic;

/**
 * Classe que cria objetos do tipo Sword.
 * Uma espada e um objeto que pode ter o estado visivel, caso esteja visivel no labirinto ou invisivel caso o heroi ou o dragao estejam armados.
 * Uma espada fica sempre no mesmo ponto. Quando um heroi a apanha fica invisivel o resto do jogo.
 * No caso de um dragao ficar armado, a espada fica invisivel apenas no espaço de tempo em que o dragao esta sobreposto a espada.
 * @author Inês Gomes e Andreia Rodrigues
 */
public class Sword extends Elements{

	public enum StateS {VISIBLE, INVISIBLE};
	private StateS currentState;
	
	/**
	 * Construtor do classe Sword filha da classe Elements.
	 * Cria um Objeto do tipo Sword na ponto de coordenadas 'x', 'y' com o simbolo 'E'.
	 * @param x Coordenada inicial x.
	 * @param y Coordenada inicial y.
	 * @param s Symbolo pelo que o objeto e identificado.
	 */
	public Sword(int x, int y, char s){
		super(new Point(x,y), s);
		currentState = StateS.VISIBLE;
	}
	
	/**
	 * Metodo que muda o estado atual da espada para o estado invisivel.
	 */
	public void setSwordInvisible(){
		currentState = StateS.INVISIBLE;
	}
	
	/**
	 * Metodo que muda o estado atual da espada para o estado visivel.
	 */
	public void setSwordVisible(){
		currentState = StateS.VISIBLE;
	}
	
	/**
	 * Metodo que retorna verdadeiro ou falso de acordo com o estado da espada. 
	 * @return Verdadeiro se a espada se encontra visivel, falso caso contrario.
	 */
	public boolean isVisible(){
		return (currentState == StateS.VISIBLE);
	}
}
