package org.feup.lcom.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.feup.lcom.utilities.*;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe abstrata responsavel pela criacao dos elementos do jogo (plataforms, platformsWithSpikes e spikes)
 * Esta classe extende a classe Sprite. Recebe como parametros o mundo, posicao do elemento, ecra onde vai ser desenhada e velocidade.
 * E constituida por varios metodos que devem ser implementados nas classes derivadas.
 * @author Ines Gomes e Andreia Rodrigues
 */
public abstract class Element extends Sprite implements Feature{

    private World world;
    private GameScreen screen;

    /**
     * Construtor da classe. Inicializa o mundo e o ecra. De seguida calcula o vetor da velocidade e define o elemento de acordo com a sua classe.
     * Ativa o contactListener para este elemento para saber que colisoes detetar.
     * @param gameWorld
     * @param position
     * @param screen
     * @param speed
     */
    public Element(World gameWorld, Point position, GameScreen screen, float speed){
        this.screen = screen;
        this.world = gameWorld;

        Vector2 velocity = new Vector2(speed,0);

        defineFeature(position, gameWorld, velocity);

        world.setContactListener(new org.feup.lcom.utilities.WorldContactListener());
    }

    /**
     * Define as caracteristicas do corpo do elemento.
     * @param world Mundo onde vai ser definido.
     * @param position Posicao inicial.
     * @param velocity Velocidade a que se move no mundo.
     */
    protected abstract void defineElementBody(World world, Point position, Vector2 velocity);

    /**
     * Define o sensor associado ao elemento.
     */
    protected abstract void defineElementBodySensor();

    /**
     * Verifica se o elemento se encontra dentro do ecra e retorna verdadeiro ou falso.
     * @return Verdadeiro se o elemento se encontra fora margem esquerda do ecra, falso em caso contrario.
     */
    public abstract boolean isOutsideBoundaries();
}
