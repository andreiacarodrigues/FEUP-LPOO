package org.feup.lcom.logic;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.*;
import org.feup.lcom.audio.Audio;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe responsavel pelos inimigos do tipo Spinner que extende a classe Enemy.
 * Os inimigos são obstáculos que vão aparecendo com o decorrer do tempo, em intervalos predefinidos.
 * Se o player entrar em contacto com o sensor do corpo do inimigo, o seu estado é alterado para Dead e perde o jogo.
 * Se o player entrar em contacto com o sensor do topo do inimigo, mata o inimigo e neste caso é o estado do inimigo que é alterado para Dead.
 * Quando o jogo verifica que este elemento tem o estado como Dead, quer dizer que ou foi morto pelo player ou então
 * saiu fora dos limites do ecrã de jogo e então elimina-o.
 * O corpo dos inimigos é do tipo kinematic e tem uma velocidade constante definida internamente pelo programa.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Spinner extends Enemy{

    private Audio audio;

    /* Definition of box2D */
    private Point position;
    private BodyDef bodyDef;
    private Body body;
    private CircleShape shape;
    private PolygonShape sensorShape;
    private FixtureDef fdef;

    /* State */
    private boolean isDead;

    /* Animations */
    private AssetsManager assetsManager;
    private Animation animSpinner;
    private float stateTimer;

    /**
     * Construtor da classe.
     * Chama o construtor da classe de que é derivado e faz load da animação do spinner a partir do assetManager.
     * @param gameWorld Mundo envolvente
     * @param position Posicao inicial
     * @param screen Ecra onde e desenhada
     * @param speed Velocidade a que se desloca o inimigo
     */
    public Spinner(World gameWorld, Point position, GameScreen screen, float speed)
    {
        super(gameWorld, position, screen, "spinner", speed);

        this.position = position;
        this.isDead = false;

        /* Animations */
        this.stateTimer = 0;
        loadAnimation();
    }

    /**
     * Metodo auxiliar ao construtor da classe que vai buscar a animação do spinner á classe AssetsManager onde está guardada
     */
    private void loadAnimation()
    {
        animSpinner = assetsManager.getInstance().getSpinner();
    }

    /**
     * Método da classe abstrata Enemy que define o inimigo (sensores e caracteristicas do corpo)
     * @param position Posicao inicial
     * @param world Mundo envolvente
     * @param velocity Velocidade a que se desloca
     */
    @Override
    public void defineFeature(Point position, World world, Vector2 velocity) {

        defineEnemyBody(position, world, velocity);

        defineEnemyHeadSensor();

        defineEnemyBodySensor();

        shape.dispose();
    }

    /**
     * Método auxiliar que define as caracteristicas do corpo do inimigo.
     * @param position Posicao inicial.
     * @param world Mundo onde vai ser definido.
     * @param velocity Velocidade a que se move no mundo.
     */
    private void defineEnemyBody(Point position, World world, Vector2 velocity)
    {
        bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);

        shape = new CircleShape();
        shape.setRadius(org.feup.lcom.utilities.Constants.ENEMY_WIDTH);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 2.5f;
        fdef.friction = 0.25f;
        fdef.restitution = 0.5f;
        body.createFixture(fdef);
        body.setLinearVelocity(velocity);
    }

    /**
     * Método auxiliar que define as caracteristicas do sensor do topo do inimigo.
     * O player quando em contacto com este sensor mata o inimigo e altera o seu estado para Dead
     */
    private void defineEnemyHeadSensor()
    {
        sensorShape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-org.feup.lcom.utilities.Constants.ENEMY_WIDTH+0.06f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT+0.03f);
        vertice[1] = new Vector2(org.feup.lcom.utilities.Constants.ENEMY_WIDTH-0.06f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT+0.03f);
        vertice[2] = new Vector2(org.feup.lcom.utilities.Constants.ENEMY_WIDTH-0.06f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT-0.005f);
        vertice[3] = new Vector2(-org.feup.lcom.utilities.Constants.ENEMY_WIDTH+0.06f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT-0.005f);
        sensorShape.set(vertice);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_ENEMY_HEAD;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.shape = sensorShape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Método auxiliar que define as caracteristicas do sensor do corpo do inimigo.
     * O player quando em contacto com este sensor altera o seu estado para Dead e perde o jogo.
     */
    private void defineEnemyBodySensor()
    {
        shape = new CircleShape();
        shape.setRadius(org.feup.lcom.utilities.Constants.ENEMY_WIDTH-0.02f);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_ENEMY;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER | org.feup.lcom.utilities.Constants.BIT_GROUND;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Método da classe abstrata Enemy que atualiza os inimigos em delta periodos de tempo.
     * Atualiza a posicão do seu corpo e da sprite que o acompanha no mundo.
     * @param delta periodo de tempo.
     */
    @Override
    public void update(float delta){

        this.stateTimer += delta;

        position = new Point(body.getPosition().x, body.getPosition().y);
        if(position.getX() < (-org.feup.lcom.utilities.Constants.ASPECT_RATIO -(org.feup.lcom.utilities.Constants.ENEMY_WIDTH/2)))
            isDead = true;

        float spinnerAnimWidth =  ((100*0.7f) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        float spinnerAnimHeight = ((100*0.7f) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        Point spinnerAnimPos = new Point(body.getPosition().x - org.feup.lcom.utilities.Constants.ENEMY_WIDTH, body.getPosition().y - org.feup.lcom.utilities.Constants.ENEMY_HEIGHT+ 0.008f);

        setBounds(0, 0,spinnerAnimWidth, spinnerAnimHeight);
        setPosition(spinnerAnimPos.getX(),spinnerAnimPos.getY());

        TextureRegion texture = animSpinner.getKeyFrame(stateTimer, true);
        setRegion(texture);
    }

    /**
     * Método da classe abstrata Enemy que dá uma ordem para mudar o estado do inimigo para Dead.
     */
    @Override
    public void kill(){
        isDead = true;
        if(audio.getInstance().getAudioOn())
             audio.getInstance().playEnemyDyingSound();
    }

    /**
     * Método da classe abstrata Enemy que retorna Verdadeiro ou falso consoante o inimigo se encontra no estado morto ou nao.
     * @return Verdadeiro ou falso.
     */
    @Override
    public boolean isDead() {return isDead;}

    /**
     * Retorna o corpo criado.
     * @return Corpo do elemento.
     */
    @Override
    public Body getBody() {return body;}

    /**
     * Metodo que permite alterar a velocidade com que queremos que o inimigo se movimente no mundo.
     * @param gameSpeed Velocidade de movimento do inimigo
     */
    public void changeWorldVelocity(float gameSpeed){
        body.setLinearVelocity(new Vector2(gameSpeed,0));
    }

}
