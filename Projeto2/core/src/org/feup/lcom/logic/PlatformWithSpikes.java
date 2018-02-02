package org.feup.lcom.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.*;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe responsavel pela criação das plataformas verticais com spikes do jogo.
 * Esta classe extende a classe Element.
 * O estado do player é mudado para Dead se entrar em contacto com o sensor que envolve o corpo da plataforma.
 * As plataformas são corpos kinematic e é-lhes aplicada velocidade para que vão de encontro ao player.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class PlatformWithSpikes extends Element {

    private Point position;

    /* Box2d */
    private BodyDef bodyDef;
    private Body body;
    private PolygonShape shape;
    private FixtureDef fdef;

    /* Sprites */
    private TextureRegion platform;
    private AssetsManager assetsManager;

    private boolean outsideBoundaries;

    /**
     * Construtor da classe.
     * Inicializa o mundo, screen, posicao inicial e a velocidade da plataforma.
     * Vai buscar a sprite ao AssetsManager e define a sua posição e dimensões.
     * @param gameWorld Mundo com gravidade onde o jogo corre.
     * @param position Posicao inicial do chão.
     * @param screen Screen onde vai ser desenhado o chão.
     * @param speed Velocidade com que as plataformas se vão deslocar no jogo
     */
    public PlatformWithSpikes(World gameWorld, Point position, GameScreen screen, float speed)
    {
        super(gameWorld,position,screen,speed);
        this.position = position;
        outsideBoundaries = false;

        platform = assetsManager.getInstance().getPlatformWSpikes();
        loadPlatformSprite();
    }

    /**
     * Método auxiliar ao construtor da classe, onde são definidas as dimensões da sprite das plataformas e a posição em que vai
     * ser desenhada.
     */
    protected void loadPlatformSprite()
    {
        float platformSpriteWidth = (70/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        float platformSpriteHeight = (420/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        Point platformPos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH, body.getPosition().y - org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT);
        setBounds(0,0,platformSpriteWidth, platformSpriteHeight);
        setPosition(platformPos.getX(),platformPos.getY());
        setRegion(platform);
    }

    /**
     * Método da classe abstrata Element que define o elemento (sensores e caracteristicas do corpo)
     * @param world Mundo onde vai ser definido.
     * @param position Posicao inicial
     * @param velocity Velocidade a que se move no mundo
     */
    @Override
    public void defineFeature( Point position,World world, Vector2 velocity)
    {
        defineElementBody(world, position, velocity);

        defineElementBodySensor();

        definePlatformBottomSensor();

        shape.dispose();
    }

    /**
     * Método da classe abstrata Element que define as caracteristicas do corpo do elemento.
     * @param world Mundo onde vai ser definido.
     * @param position Posicao inicial.
     * @param velocity Velocidade a que se move no mundo.
     */
    @Override
    protected void defineElementBody(World world,Point position, Vector2 velocity)
    {
        bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);

        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH, org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT);

        fdef = new FixtureDef();
        fdef.shape = shape;

        body.createFixture(fdef);
        body.setLinearVelocity(velocity);
    }

    /**
     * Método da classe abstrata Element que define o sensor do corpo associado ao elemento.
     * Este sensor, quando em contacto com o player faz com que ele perca o jogo (altera o seu estado para Dead).
     */
    @Override
    protected void defineElementBodySensor()
    {
        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH, org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_PLATFORMS;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Método auxiliar que define o sensor dos spikes associado á plataforma.
     * Este sensor permite detetar se o player tocou na plataforma, na parte dos spikes e altera o seu estado para Dead quando em contacto com ele.
     */
    private void definePlatformBottomSensor()
    {
        shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH, -org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT+0.12f);
        vertice[1] = new Vector2(org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH, -org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT+0.12f);
        vertice[2] = new Vector2(org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH, -org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT+0.08f);
        vertice[3] = new Vector2(-org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH,-org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT+0.08f);
        shape.set(vertice);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_PLATFORMS;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Metodo que atualiza as plataformas em delta periodos de tempo.
     * Atualiza a posicão do seu corpo e da sprite que o acompanha no mundo.
     * @param dt periodo de tempo.
     */
    @Override
    public void update(float dt)
    {
        position = new Point(body.getPosition().x, body.getPosition().y);
        if(position.getX() < (-org.feup.lcom.utilities.Constants.ASPECT_RATIO -(org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH)))
            outsideBoundaries = true;

        setPosition(body.getPosition().x - org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH,body.getPosition().y - org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT);
    }

    /**
     * Metodo que permite alterar a velocidade com que queremos que a plataforma se movimente no mundo.
     * @param gameSpeed Velocidade de movimento da platafoma
     */
    public void changeWorldVelocity(float gameSpeed){
        body.setLinearVelocity(new Vector2(gameSpeed,0));
    }

    /**
     * Metodo que permite verificar se a plataforma já saiu fora dos limites do ecrã de jogo.
     * Isto permite que o objeto seja eliminado caso já nao seja necessário
     * @return booleano que permite saber se elemento saiu fora dos limites do ecrã
     */
    public boolean isOutsideBoundaries() {return outsideBoundaries;}

    /**
     * Retorna o corpo da chão.
     * @return corpo da chão.
     */
    public Body getBody() {return body;}
}
