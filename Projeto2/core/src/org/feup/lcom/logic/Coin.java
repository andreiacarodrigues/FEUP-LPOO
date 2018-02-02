package org.feup.lcom.logic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.*;
import org.feup.lcom.audio.Audio;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe responsavel pelas moedas que extende a classe Sprite.
 * As moedas sao objetos que podem ou nao ser apanhados pelo player.
 * No caso de serem apanhadas o score do player aumenta.
 * Nao sofrem de gravidade e rodam em torno de si proprias.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Coin extends Sprite implements Feature{

    private Audio audio;
    private AssetsManager assetsManager;
    private World world;
    private Point position;
    private GameScreen screen;

    private BodyDef bodyDef;
    private Body body;
    private PolygonShape shape;
    private FixtureDef fdef;

    private boolean visible;
    private float speed;

    private Animation animCoin;
    private float stateTimer;

    /**
     * Construtor da classe.
     * Inicializa o mundo, screen e posicao inicial.
     * De seguida define o corpo e a animacao.
     * @param gameWorld Mundo com gravidade onde o jogo corre.
     * @param position Posicao inicial da moeda.
     * @param screen Screen onde vai ser desenhada a moeda.
     */
    public Coin(World gameWorld, Point position, GameScreen screen, float speed){

        //regiao do atlas onde se encontra a animacao.
        super(screen.getElementsAtlas().findRegion("CoinSpin"));

        this.world = gameWorld;
        this.screen = screen;
        this.position = position;
        visible = true;
        this.speed = speed;

        //construcao do elemento
        defineFeature(position,world,new Vector2(speed,0));

        /* Animation */
        this.stateTimer = 0;

        loadCoinAnimation();
    }

    /**
     * Definicao do elemento.
     * E construido um corpo que nao reage a forcas (do tipo kinematic) numa certa posicao e velocidade nesse mundo.
     * E ainda colocado um sensor na moeda que apena reage ao toque do player.
     */
    public void defineFeature(Point position, World world, Vector2 velocity) {

        bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);

        shape = new PolygonShape();
        shape.setAsBox(Constants.COIN_SIZE, Constants.COIN_SIZE);

        fdef = new FixtureDef();
        fdef.shape = shape;

        body.createFixture(fdef);
        body.setLinearVelocity(velocity);

        shape = new PolygonShape();
        shape.setAsBox(Constants.COIN_SIZE, Constants.COIN_SIZE);
        fdef.filter.categoryBits = Constants.BIT_COIN;
        fdef.filter.maskBits = Constants.BIT_PLAYER;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    /**
     * Metodo que vai buscar a animação da moeda á classe AssetsManager onde esta está guardada
     */
    private void loadCoinAnimation()
    {
        animCoin = assetsManager.getInstance().getCoin();
    }

    /**
     * Metodo que atualiza as moedas em delta periodos de tempo.
     * Atualiza a sua animacao e a posicao do player no mundo.
     * Tambem verifica se se a moeda passou o limite esquerdo do ecra
     * @param delta periodo de tempo.
     */
    public void update(float delta)
    {
        this.stateTimer += delta;

        //atualizacao da posicao
        position = new Point(body.getPosition().x, body.getPosition().y);
        if(position.getX() < (-Constants.ASPECT_RATIO -(Constants.COIN_SIZE)))
            visible = false;

        //atualizacao da animacao
        float animWidth = (50 / Constants.PPM) / (2 * Constants.ASPECT_RATIO);
        float animHeight = (50 / Constants.PPM) / (2 * Constants.ASPECT_RATIO);
        Point animPos = new Point(body.getPosition().x - Constants.COIN_SIZE,body.getPosition().y - Constants.COIN_SIZE);
        setBounds(0, 0, animWidth, animHeight);
        setPosition(animPos.getX(), animPos.getY());
        setRegion(animCoin.getKeyFrame(stateTimer, true));
    }

    /**
     * Retorna o corpo da moeda.
     * @return corpo da moeda.
     */
    public Body getBody() {return body;}

    /**
     * Retorna se a moeda esta visivel (Dentro do ecra ou nao apanhada)
     * @return Verdadeiro ou falso consoante esteja ou nao visivel
     */
    public boolean isVisible() {return visible;}

    /**
     * Altera a visibilidade da moeda.
     * @param visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        if(audio.getInstance().getAudioOn())
            audio.getInstance().playCoinSound();
    }

    /**
     * Altera a velocidade do jogo.
     * @param gameSpeed Nova velocidade.
     */
    public void changeWorldVelocity(float gameSpeed) {
        body.setLinearVelocity(new Vector2(gameSpeed,0));
    }
}
