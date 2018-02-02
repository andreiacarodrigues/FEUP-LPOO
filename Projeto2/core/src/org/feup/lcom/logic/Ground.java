package org.feup.lcom.logic;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.*;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe responsavel pelo chão do jogo.
 * Esta classe extende a classe Sprite.
 * O estado do player é mudado para Running se estiver em contacto com estes elementos do jogo.
 * O chão é o único corpo estático e o unico corpo a que não lhe é aplicada velocidade no jogo, para além do player.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class  Ground extends Sprite {

    private World world;
    private Point position;
    private AssetsManager assetsManager;

    /* Box2d*/
    private BodyDef bodyDef;
    private Body body;
    private PolygonShape shape;
    private FixtureDef fdef;

    /* Sprite */
    private TextureRegion ground;

    /**
     * Construtor da classe.
     * Inicializa o mundo, screen e posicao inicial.
     * Vai buscar a sprite ao AssetsManager.
     * De seguida define o corpo e atribui-lhe a sprite que guardou.
     * @param gameWorld Mundo com gravidade onde o jogo corre.
     * @param position Posicao inicial do chão.
     * @param screen Screen onde vai ser desenhado o chão.
     */
    public Ground(World gameWorld, Point position, GameScreen screen)
    {
        this.world = gameWorld;
        this.position = position;

        ground = assetsManager.getInstance().getGround();
        float groundWidth = (70/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        float groundHeight = (140/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        setBounds(0,0,groundWidth,groundHeight);
        setRegion(ground);

        defineGround();
    }

    /**
     * Definicao do elemento.
     * E construido um corpo estático numa certa posicao desse mundo e um sensor que o rodeia e que permite detetar se o player
     * está em contacto com este corpo ou nao.
     */
    private void defineGround()
    {
        defineGroundBody();

        defineGroundSensor();
    }

    /**
     * Método auxiliar á definição do elemento.
     * Adiciona um corpo estático ao elemento.
     */
    private void defineGroundBody()
    {
        bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.GROUND_SIZE, org.feup.lcom.utilities.Constants.GROUND_SIZE*2);

        fdef = new FixtureDef();
        fdef.shape = shape;

        body.createFixture(fdef);}

    /**
     * Método auxiliar á definição do elemento.
     * Adiciona um sensor que deteta colisão do chão com o player e os inimigos.
     */
    private void defineGroundSensor()
    {
        shape = new PolygonShape();
        shape.setAsBox(org.feup.lcom.utilities.Constants.GROUND_SIZE, org.feup.lcom.utilities.Constants.GROUND_SIZE*2);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_GROUND;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_ENEMY | org.feup.lcom.utilities.Constants.BIT_PLAYER_GROUND;
        fdef.shape = shape;
        fdef.isSensor = true;

        body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    /**
     * Metodo que atualiza o chão em delta periodos de tempo.
     * Atualiza a sua posicao no mundo.
     * @param dt periodo de tempo.
     */
    public void update(float dt)
    {
        Point groundPos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.GROUND_SIZE, body.getPosition().y - org.feup.lcom.utilities.Constants.GROUND_SIZE*2);
        setPosition(groundPos.getX(),groundPos.getY());
    }

    /**
     * Retorna o corpo da chão.
     * @return corpo da chão.
     */
    public Body getBody() {return body;}
}
