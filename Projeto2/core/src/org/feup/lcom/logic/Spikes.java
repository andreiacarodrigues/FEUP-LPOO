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
 * Classe responsavel pelos elementos do tipo Spikes que extende a classe Element.
 * Estes elementos funcionam de maneira igual aos inimigos, mas estes nao podem ser mortos pelo player.
 * Quando o player entra em contacto com o sensor que envolve o corpo dos spikes, o seu estado é alterado para Dead e perde o jogo.
 * O corpo dos inimigos é do tipo kinematic e tem uma velocidade constante definida internamente pelo programa.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Spikes extends Element {

    private World world;
    private GameScreen screen;

    /* Definition of box2D */
    private Point position;
    private BodyDef bodyDef;
    private Body body;
    private PolygonShape shape;
    private FixtureDef fdef;

    /* State */
    private boolean outsideBoundaries;

    /* Animations */
    private AssetsManager assetsManager;
    private TextureRegion spikes;

    /**
     * Construtor da classe.
     * Inicializa o mundo, screen, posicao inicial e a velocidade da plataforma.
     * Vai buscar a sprite correspondente aos spikes ao AssetsManager e define a sua posição e dimensões.
     * @param gameWorld Mundo com gravidade onde o jogo corre.
     * @param position Posicao inicial do chão.
     * @param screen Screen onde vai ser desenhado o chão.
     * @param speed Velocidade com que os spikes se vão deslocar no jogo
     */
    public Spikes(World gameWorld, Point position, GameScreen screen,float speed)
    {
        super(gameWorld,position,screen, speed);

        this.position = position;
        outsideBoundaries = false;

        spikes = assetsManager.getInstance().getSpikes();
        loadSpikesSprite();
    }

    /**
     * Método auxiliar ao construtor da classe, onde são definidas as dimensões da sprite dos spikes e a posição em que vai
     * ser desenhada.
     */
    protected void loadSpikesSprite()
    {
        float spikesSpriteWidth =((140*0.45f)/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        float spikesSpriteHeight = ((70*0.45f)/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        Point spikesSpritePos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f+0.1f, body.getPosition().y - org.feup.lcom.utilities.Constants.GROUND_SIZE*0.45f+0.05f);

        setBounds(0,0,spikesSpriteWidth,spikesSpriteHeight);
        setPosition(spikesSpritePos.getX(),spikesSpritePos.getY());
        setRegion(spikes);
    }

    /**
     * Método da classe abstrata Element que define o elemento (sensores e caracteristicas do corpo)
     * @param world Mundo onde vai ser definido.
     * @param position Posicao inicial
     * @param velocity Velocidade a que se move no mundo
     */
    @Override
    public void defineFeature( Point position,World world, Vector2 velocity) {

        defineElementBody(world, position,velocity);

        defineElementBodySensor();

        defineSpikesHeadSensor();

        shape.dispose();
    }

    /**
     * Método da classe abstrata Element que define as caracteristicas do corpo do elemento.
     * @param world Mundo onde vai ser definido.
     * @param position Posicao inicial.
     * @param velocity Velocidade a que se move no mundo.
     */
    @Override
    protected void defineElementBody(World world, Point position, Vector2 velocity)
    {
        bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);

        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f, org.feup.lcom.utilities.Constants.GROUND_SIZE*0.45f);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 2.5f;
        fdef.friction = 0.25f;
        fdef.restitution = 0.5f;
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
        shape.setAsBox(org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f, org.feup.lcom.utilities.Constants.GROUND_SIZE*0.45f);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_SPIKES;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Método auxiliar que define o sensor do topo do corpo associado aos spikes.
     * Este sensor permite detetar se o player entrou em contacto com os spikes pela parte superior destes.
     * Este sensor, quando em contacto com o player faz com que ele perca o jogo (altera o seu estado para Dead).
     */
    private void defineSpikesHeadSensor()
    {
        shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f+0.01f, org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f-0.033f);
        vertice[1] = new Vector2(org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f-0.01f, org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f-0.033f);
        vertice[2] = new Vector2(org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f-0.01f, org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f-0.06f);
        vertice[3] = new Vector2(-org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f+0.01f, org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f-0.06f);
        shape.set(vertice);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_SPIKES;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Metodo que atualiza os spikes em delta periodos de tempo.
     * Atualiza a posicão do seu corpo e da sprite que o acompanha no mundo.
     * @param delta periodo de tempo.
     */
    @Override
    public void update(float delta){

        position = new Point(body.getPosition().x, body.getPosition().y);
        if(position.getX() < (-org.feup.lcom.utilities.Constants.ASPECT_RATIO -(org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f)))
            outsideBoundaries = true;

        Point spikesSpritePos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.GROUND_SIZE*2*0.45f, body.getPosition().y - org.feup.lcom.utilities.Constants.GROUND_SIZE*0.45f);
        setPosition(spikesSpritePos.getX(), spikesSpritePos.getY());
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
