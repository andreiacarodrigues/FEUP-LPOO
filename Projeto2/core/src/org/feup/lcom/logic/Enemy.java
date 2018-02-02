package org.feup.lcom.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.*;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe abstrata responsavel pela criacao dos inimigos do jogo (slimes e spinners)
 * Esta classe extende a classe Sprite.
 * Recebe como parametros o mundo, posicao do elemento, ecra onde vai ser desenhada, regiao do atlas onde se encontra a sua animacao e velocidade.
 * E constituida por varios metodos que devem ser implementados nas classes derivadas.
 * @author Ines Gomes e Andreia Rodrigues
 */
public abstract class Enemy extends Sprite implements Feature {

    private World world;
    private GameScreen screen;
    AssetsManager assetsManager;

    /**
     * Construtor da classe. Inicializa o mundo e o ecra. De seguida calcula o vetor da velocidade e define o inimigo de acordo com a sua classe.
     * Ativa o contactListener para este inimigo para saber que colisoes detetar.
     * @param gameWorld Mundo envolvente
     * @param position Posicao inicial
     * @param screen Ecra onde e desenhada
     * @param region Regiao do textureAtlas onde se encontra a animacao
     * @param speed Velocidade a que se desloca o inimigo
     */
    public Enemy(World gameWorld, Point position, GameScreen screen, String region, float speed)
    {
        super(screen.getEnemiesAtlas().findRegion(region));
        this.world = gameWorld;
        this.screen = screen;

        Vector2 velocity = new Vector2(speed,0);

        defineFeature(position, gameWorld, velocity);

        world.setContactListener(new org.feup.lcom.utilities.WorldContactListener());
    }

    /**
     * Ordem para mudar o estado do inimigo para Dead.
     */
    public abstract void kill();

    /**
     * Retorna Verdadeiro ou falso consoante o inimigo se encontra no estado morto ou nao.
     * @return Verdadeiro ou falso.
     */
    public abstract boolean isDead();

}
