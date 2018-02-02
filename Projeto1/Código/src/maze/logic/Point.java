package maze.logic;

/**
 * Classe que cria um ponto com coordenadas x e y.
 * @author In�s Gomes e Andreia Rodrigues
 */

public class Point implements Comparable<Point>{

	private int x;
	private int y;
	
	/**
	 * Construtor da classe
	 * Cria ponto de coordenadas x e y.
	 * @param x Coordenada x. 
	 * @param y Coordenada y. 
	 */
	
	public Point(int xPos, int yPos) {
		setX(xPos);
		setY(yPos);
	}
	
	/**
	 * Este m�todo permite comparar dois pontos. Retorna 0 caso sejam iguais ou 1 caso contr�rio.
	 * @return Inteiro que diz se coordenadas dos pontos s�o iguais ou n�o
	 */
	
	public int compareTo(Point obj){
		if((obj.getX() == x ) && (obj.getY() == y))
			return 0;
		else 
			return 1;
	}
	
	/**
	 * Este m�todo retorna a coordenada x do ponto
	 * @return Coordenada x
	 */
	
	public int getX() {
		return x;
	}

	/**
	 * Este m�todo altera a coordenada x do ponto
	 * @param Nova coordenada x
	 */
	
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Este m�todo retorna a coordenada y do ponto
	 * @return Coordenada y
	 */
	
	public int getY() {
		return y;
	}

	/**
	 * Este m�todo altera a coordenada y do ponto
	 * @param Nova coordenada y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Este m�todo permite comparar dois pontos. Retorna true caso sejam iguais ou falso caso contr�rio.
	 * @return Booleano que diz se coordenadas dos pontos s�o iguais ou n�o
	 */
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Point){
			if((((Point) obj).getX() == x ) && (((Point) obj).getY() == y))
				return true;
			else 
				return false;
		}
		else
			return false;
	}
}
