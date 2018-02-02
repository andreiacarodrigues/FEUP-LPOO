package org.feup.lcom.utilities;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe que permite criar um ponto com coordenadas x e y.
 * Esta classe extende a classe Vector2.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Point extends Vector2 {

    private float x;
    private float y;

    /**
     * Construtor da classe.
     * Permite criar um ponto com coordenadas x e y
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public Point(float x, float y)
    {
        super(x,y);
        this.x = x;
        this.y = y;
    }

    /**
     * Método que devolve a coordenada x do ponto
     * @return Coordenada x
     */
    public float getX() {return x;}

    /**
     * Método que devolve a coordenada y do ponto
     * @return Coordenada y
     */
    public float getY() {return y;}

    /**
     * Método que permite modificar a coordenada x do ponto
     * @param newX Coordenada x
     */
    public void setX(float newX) {x = newX;}

    /**
     * Método que permite modificar a coordenada y do ponto
     * @param newY Coordenada y
     */
    public void setY(float newY) {y = newY;}
}
