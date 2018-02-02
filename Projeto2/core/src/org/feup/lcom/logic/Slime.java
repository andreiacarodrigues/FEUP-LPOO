package org.feup.lcom.logic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.*;
import org.feup.lcom.audio.Audio;
import org.feup.lcom.gui.GameScreen;

import java.util.Random;

/**
 * Classe responsavel pelos inimigos do tipo Slime que extende a classe Enemy.
 * Os inimigos são obstáculos que vão aparecendo com o decorrer do tempo, em intervalos predefinidos.
 * Se o player entrar em contacto com o sensor do corpo do inimigo, o seu estado é alterado para Dead e perde o jogo.
 * Se o player entrar em contacto com o sensor do topo do inimigo, mata o inimigo e neste caso é o estado do inimigo que é alterado para Dead.
 * Quando o jogo verifica que este elemento tem o estado como Dead, quer dizer que ou foi morto pelo player ou então
 * saiu fora dos limites do ecrã de jogo e então elimina-o.
 * O corpo dos inimigos é do tipo kinematic e tem uma velocidade constante definida internamente pelo programa.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Slime extends Enemy {

    private Audio audio;

    /* Definition of box2D */
    private Point position;
    private BodyDef bodyDef;
    private Body body;
    private PolygonShape shape;
    private FixtureDef fdef;

    /* State */
    private boolean isDead;

    /* Animations */
    private AssetsManager assetsManager;
    private Animation currentAnimation;
    private Animation animSlimePink;
    private Animation animSlimeBlue;
    private Animation animSlimeGreen;
    private Animation animLadybug;
    private float stateTimer;

    /* Utilities */
    private Random rand = new Random();

    /**
     * Construtor da classe.
     * Chama o construtor da classe de que é derivado e faz load das animações disponiveis para este tipo de inimigo a partir do assetManager.
     * Logo em seguida escolhe aleatóriamente a animação que escolher para mostrar no jogo.
     * @param gameWorld Mundo envolvente
     * @param position Posicao inicial
     * @param screen Ecra onde e desenhada
     * @param speed Velocidade a que se desloca o inimigo
     */
    public Slime(World gameWorld, Point position, GameScreen screen, float speed) {
        super(gameWorld, position, screen, "slime1", speed);
        rand = new Random();

        this.position = position;
        this.isDead = false;

        /* Animations */

        this.stateTimer = 0;

        loadAnimations();

        chooseAnimation();
    }

     /**
     * Metodo auxiliar ao construtor da classe que vai buscar as animações dos inimigos á classe AssetsManager onde estas estão guardadas
     */
    private void loadAnimations()
    {
        animSlimePink = assetsManager.getInstance().getSlimePink();

        animSlimeBlue = assetsManager.getInstance().getSlimeBlue();

        animSlimeGreen = assetsManager.getInstance().getSlimeGreen();

        animLadybug = assetsManager.getInstance().getLadybug();
    }

    /**
     * Metodo auxiliar ao construtor da classe que escolhe aleatoriamente qual a animação guardada que quer usar.
     */
    private void chooseAnimation()
    {
        int randomNumber = rand.nextInt(4);

        switch(randomNumber)
        {
            case 0:
                currentAnimation = animLadybug;
                break;
            case 1:
                currentAnimation = animSlimePink;
                break;
            case 2:
                currentAnimation = animSlimeBlue;
                break;
            case 3:
                currentAnimation = animSlimeGreen;
                break;
        }
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

        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.ENEMY_WIDTH, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT - 0.05f);
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
        shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-org.feup.lcom.utilities.Constants.ENEMY_WIDTH+0.02f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT-0.033f);
        vertice[1] = new Vector2(org.feup.lcom.utilities.Constants.ENEMY_WIDTH-0.02f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT-0.033f);
        vertice[2] = new Vector2(org.feup.lcom.utilities.Constants.ENEMY_WIDTH-0.02f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT-0.06f);
        vertice[3] = new Vector2(-org.feup.lcom.utilities.Constants.ENEMY_WIDTH+0.02f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT-0.06f);
        shape.set(vertice);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_ENEMY_HEAD;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Método auxiliar que define as caracteristicas do sensor do corpo do inimigo.
     * O player quando em contacto com este sensor altera o seu estado para Dead e perde o jogo.
     */
    private void defineEnemyBodySensor()
    {
        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.ENEMY_WIDTH-0.01f, org.feup.lcom.utilities.Constants.ENEMY_HEIGHT - 0.07f);
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

        float slimeAnimWidth =((90*0.8f) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        float slimeAnimHeight = ((55*0.8f) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        Point slimeAnimPos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.ENEMY_WIDTH, body.getPosition().y - org.feup.lcom.utilities.Constants.ENEMY_HEIGHT + 0.041f);

        setBounds(0, 0, slimeAnimWidth,slimeAnimHeight) ;
        setPosition(slimeAnimPos.getX(), slimeAnimPos.getY());

        TextureRegion texture = new TextureRegion();
        setRegion(loadAnimationChosen(texture));
    }

    /**
     * Método auxiliar que devolve a textureRegion da animaçao do inimigo escolhida aleatoriamente.
     * @return TextureRegion com a texture da animação escolhida
     */
    private TextureRegion loadAnimationChosen(TextureRegion texture)
    {
        if(currentAnimation == animLadybug)
            texture = animLadybug.getKeyFrame(stateTimer, true);
        if(currentAnimation == animSlimePink)
            texture = animSlimePink.getKeyFrame(stateTimer, true);
        if(currentAnimation == animSlimeBlue)
            texture = animSlimeBlue.getKeyFrame(stateTimer, true);
        if(currentAnimation == animSlimeGreen)
            texture = animSlimeGreen.getKeyFrame(stateTimer, true);

        return texture;
    }

    /**
     * Método da classe abstrata Enemy que dá uma ordem para mudar o estado do inimigo para Dead.
     */
    @Override
    public void kill(){

        if(audio.getInstance().getAudioOn())
            audio.getInstance().playEnemyDyingSound();
        isDead = true;
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
