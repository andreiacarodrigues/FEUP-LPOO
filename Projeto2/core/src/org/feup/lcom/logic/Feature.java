package org.feup.lcom.logic;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.feup.lcom.utilities.*;

/**
 * Esta interface partilha contem as varias caracteristicas inerentes as classes Coin, Enemy e Element.
 * Todas as classes devem ter um metodo para definir as suas caracteristicas, um que retorna o corpo,outro que faz a atualizacao, e ainda outro que permite alterar a sua velocidade.
 */
public interface Feature {

    /**
     * Define as caracteristicas (sensores e caracteristicas do corpo)
     * @param position Posicao inicial
     * @param world Mundo envolvente
     * @param velocity Velocidade a que se desloca
     */
    public void defineFeature(Point position, World world, Vector2 velocity);

    /**
     * Retorna o corpo criado.
     * @return Corpo do elemento.
     */
    public Body getBody();

    /**
     * Muda a velocidade do corpo no mundo.
     * @param gameSpeed Nova velocidade.
     */
    public void changeWorldVelocity(float gameSpeed);

    /**
     * Atualizacao do corpo do elemento.
     * @param delta Periodo de tempo.
     */
    public void update(float delta);
}
