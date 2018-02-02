package org.feup.lcom.logic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import org.feup.lcom.utilities.*;
import org.feup.lcom.audio.Audio;
import org.feup.lcom.gui.GameScreen;

/**
 * Classe responsavel pela criação do jogador, que estende a classe Sprite.
 * O jogador está sempre a correr e vão surgindo inimigos e obstáculos que tem de desviar, saltando ou deslizando.
 * A pontuaçao com que termina o jogo depende do tempo total de jogo e do número de moedas que apanhou.
 * Caso colida com algum inimigo ou obstáculo, o seu estado é mudado para Dead e termina o jogo.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Player extends Sprite{

    private World world;
    private Point position;
    private GameScreen screen;

    /* Audio */
    private Audio audio;

    /* Box2d */
    private BodyDef bodyDef;
    private Body body;
    private PolygonShape shape;
    private FixtureDef fdef;

    /* Variables */
    private boolean doubleJump;
    private boolean isSlidding;
    private boolean wasSlidding;
    private int numCoins;
    private boolean canNotJump;

    /* State */
    private org.feup.lcom.utilities.State currentState;
    private org.feup.lcom.utilities.State previousState;

    /* Animations */
    private AssetsManager assetManager;
    private Animation animJumping;
    private Animation animRunning;
    private Animation animSlidding;
    private Animation animDying;
    private Animation animFalling;
    private TextureRegion playerDead;

    private float stateTimer;

    /**
     * Construtor da classe.
     * Inicializa o mundo, screen e posicao inicial.
     * Inicializa as variáveis necessárias e define o corpo do jogador.
     * De seguida guarda as suas animações, que vai buscar ao AssetsManager, em variáveis.
     * @param gameWorld Mundo com gravidade onde o jogo corre.
     * @param position Posicao inicial da moeda.
     * @param screen Screen onde vai ser desenhada a moeda.
     */
    public Player(World gameWorld, Point position, GameScreen screen)
    {
        this.screen = screen;
        this.world = gameWorld;
        this.position = position;

        initVariables();

        definePlayer();

        loadPlayerAnimations();

        world.setContactListener(new org.feup.lcom.utilities.WorldContactListener());
    }

    /**
     * Método auxiliar ao construtor da classe.
     * Inicializa as variáveis (booleanos de estados, timer, número de moedas e estados) necessárias.
     */
    private void initVariables()
    {
        this.isSlidding = false;
        this.wasSlidding = false;
        this.doubleJump = false;
        this.canNotJump = false;
        this.numCoins = 0;
        this.stateTimer = 0;
        this.currentState = org.feup.lcom.utilities.State.RUNNING;
        this.previousState = org.feup.lcom.utilities.State.RUNNING;
    }

    /**
     * Definicao do jogador.
     * E construido um corpo que reage a forças, do tipo dinâmico, numa certa posicao do mundo.
     * São tambem adicionados os sensores ao corpo do jogador.
     */
    private void definePlayer()
    {
        bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        createNewFixture(org.feup.lcom.utilities.Constants.PLAYER_WIDTH, org.feup.lcom.utilities.Constants.PLAYER_HEIGHT);
    }

    /**
     * Método que permite guardar as várias animações possiveis do jogador em variáveis da classe e definir as dimensões da animação
     * inicial e a sua posição inicial.
     * Estas animações encontram-se previamente guardadas na classe AssetsManager.
     */
    private void loadPlayerAnimations()
    {
        animRunning = assetManager.getInstance().getPlayerRunning();

        animJumping = assetManager.getInstance().getPlayerJumping();

        animSlidding = assetManager.getInstance().getPlayerSlidding();

        animDying = assetManager.getInstance().getPlayerDying();

        animFalling = assetManager.getInstance().getPlayerFalling();

        playerDead = assetManager.getInstance().getPlayerDead();

        float playerAnimWidth = ((1387/10)/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        float playerAnimHeight = (109/ org.feup.lcom.utilities.Constants.PPM)/(2* org.feup.lcom.utilities.Constants.ASPECT_RATIO);
        Point playerAnimPos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_WIDTH,body.getPosition().y - org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_HEIGHT);
        setBounds(0,0,playerAnimWidth,playerAnimHeight);
        setPosition(playerAnimPos.getX(),playerAnimPos.getY());
    }

    /* ------------------------------------- Gets e Sets ------------------------------ */

    /**
     * Retorna o estado atual do jogador
     * @return Estado do jogador.
     */
    public org.feup.lcom.utilities.State getState() { return currentState; }

    /**
     * Permite alterar o estado atual do jogador
     * @param state Novo estado do jogador.
     */
    public void setState(org.feup.lcom.utilities.State state)
    {
        previousState = currentState;
        currentState = state;
    }

    /**
     * Retorna o corpo jogador
     * @return Corpo do jogador
     */
    public Body getBody() {return body;}

    /**
     * Retorna se o jogador está atualmente morto
     * @return Booleano que diz se o jogador está morto (verdadeiro) ou nao (falso)
     */
    public boolean isDead() { return (currentState == org.feup.lcom.utilities.State.DEAD);  }

    /**
     * Retorna se o jogador está atualmente a deslizar
     * @return Booleano que diz se o jogador está a deslizar (verdadeiro) ou nao (falso)
     */
    public boolean isSlidding() {return isSlidding;}

    /**
     * Permite alterar o booleano que diz que o jogador está a deslizar
     * @param state Novo estado do jogador (a deslizar ou não)
     */
    public void setSlidding(boolean state) {isSlidding = state;}

    /**
     * Permite incrementar o número de moedas apanhadas pelo jogador
     */
    public void incCoins() { numCoins++;}

    /**
     * Retorna o número de moedas apanhadas pelo jogador
     * @return Número de moedas
     */
    public int getNumCoinsCoins() {return numCoins;}

    /**
     * Retorna o tempo já percorrido no mapa pelo jogador
     * @return Tempo no qual que percorreu o mapa
     */
    public float getStateTimer() {
        return stateTimer;
    }

    /**
     * Permite alterar o booleano que diz que o jogador pode saltar ou não
     * @param bool Booleano que diz se jogador pode ou não saltar
     */
    public void canNotJump(boolean bool) {canNotJump = bool;}

    /**
     * Retorna o se o jogador pode fazer duplo salto ou não
     * @return Booleano que informa se jogador pode fazer double jump ou não
     */
    public boolean getDoubleJump() {
        return doubleJump;
    }

    /**
     * Método chamado quando o jogador entra em contacto com um inimigo e o mata, alterando o seu estado para Dead
     * @param e Inimigo que o jogador matou
     */
    public void killEnemy(Enemy e) {
        e.kill();
    }

    /* ------------------------------ Update Movimento e Animações ---------------------------- */

    /**
     * Metodo que atualiza o jogador em delta periodos de tempo.
     * Atualiza a animacao e a posicao do jogador no mundo
     * @param dt periodo de tempo.
     */
    public void update(float dt)
    {
        updateXPos();

        updateFallingState();

        updateAnimations();

        /* Updates current animation */
        setRegion(getFrame(dt));
    }

    /**
     * Metodo auxiliar que atualiza a posição no eixo dos X do jogador, mantendo esta sempre fixa
     */
    private void updateXPos()
    {
         /* Player is always in the same X position */
        if((body.getPosition().x != org.feup.lcom.utilities.Constants.PLAYER_POS.getX()) && !(currentState == org.feup.lcom.utilities.State.DEAD))
            body.setTransform(org.feup.lcom.utilities.Constants.PLAYER_POS.getX(),body.getPosition().y, 0f);
        if(currentState == org.feup.lcom.utilities.State.DEAD)
            body.setTransform(org.feup.lcom.utilities.Constants.PLAYER_POS.getX()-0.0001f,body.getPosition().y-0.0001f, 0f);
    }

    /**
     * Metodo auxiliar que atualiza o estado do jogador caso ele esteja a cair
     */
    private void updateFallingState()
    {
        if(body.getLinearVelocity().y < -0.01f && currentState == org.feup.lcom.utilities.State.RUNNING) {
            previousState = currentState;
            currentState = org.feup.lcom.utilities.State.FALLING;
        }
    }

    /**
     * Metodo auxiliar que atualiza a animação atual do jogador consoante o estado atual dele.
     * Atualiza a posição e dimensões das sprites consoante a animação a mostrar.
     * Pode estar a correr, deslizar, saltar, cair ou morto.
     */
    private void updateAnimations()
    {
        float playerAnimWidth = 0;
        float playerAnimHeight = 0;
        Point playerAnimPos = new Point (0,0);

        if(isSlidding()) {
            playerAnimWidth = ((1344 / 10) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimHeight = (91 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimPos = new Point(body.getPosition().x - org.feup.lcom.utilities.Constants.PLAYER_WIDTH-0.1f, body.getPosition().y - org.feup.lcom.utilities.Constants.PLAYER_HEIGHT+ 0.02f);
        }else if(currentState == org.feup.lcom.utilities.State.RUNNING) {
            playerAnimWidth = (1059 / 8 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimHeight = (115 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimPos = new Point(body.getPosition().x - org.feup.lcom.utilities.Constants.PLAYER_WIDTH, body.getPosition().y - org.feup.lcom.utilities.Constants.PLAYER_HEIGHT - 0.025f);
        }else if(currentState == org.feup.lcom.utilities.State.JUMPING) {
            playerAnimWidth = (1038 / 8 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimHeight =  (116 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimPos = new Point(body.getPosition().x - org.feup.lcom.utilities.Constants.PLAYER_WIDTH-0.05f, body.getPosition().y - org.feup.lcom.utilities.Constants.PLAYER_HEIGHT - 0.025f);
        }else if(currentState == org.feup.lcom.utilities.State.DEAD && wasSlidding) {
            playerAnimWidth = ((1387 / 10) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimHeight = (109 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimPos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.PLAYER_WIDTH-0.05f, body.getPosition().y - org.feup.lcom.utilities.Constants.PLAYER_HEIGHT +0.015f);
        }else if((currentState == org.feup.lcom.utilities.State.DEAD)) {
            playerAnimWidth = ((1387 / 10) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimHeight = (109 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimPos = new Point(body.getPosition().x - org.feup.lcom.utilities.Constants.PLAYER_WIDTH-0.05f, body.getPosition().y - org.feup.lcom.utilities.Constants.PLAYER_HEIGHT - 0.025f);
        }else if(currentState == org.feup.lcom.utilities.State.FALLING) {
            playerAnimWidth =  ((1056 / 10) / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimHeight = (116 / org.feup.lcom.utilities.Constants.PPM) / (2 * org.feup.lcom.utilities.Constants.ASPECT_RATIO);
            playerAnimPos = new Point (body.getPosition().x - org.feup.lcom.utilities.Constants.PLAYER_WIDTH-0.05f, body.getPosition().y - org.feup.lcom.utilities.Constants.PLAYER_HEIGHT - 0.025f);
        }
        setBounds(0, 0, playerAnimWidth, playerAnimHeight);
        setPosition(playerAnimPos.getX(), playerAnimPos.getY());
    }

    /**
     * Metodo auxiliar que retorna a frame da animação atual a mostrar naquele momento, para que estas sejam fluidas e
     * mostradas de forma gradual.
     * @param dt Perido de tempo.
     */
    public TextureRegion getFrame(float dt)
    {
        TextureRegion texture = new TextureRegion();

        switch(currentState) {
            case RUNNING:
                texture = animRunning.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                texture = animJumping.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                texture = animFalling.getKeyFrame(stateTimer,true);
                break;
            case DEAD:
                if(stateTimer >= 0.7f) {
                    texture = playerDead;
                }else
                    texture = animDying.getKeyFrame(stateTimer, true);
                break;
            default:
                break;
        }

        if(isSlidding())
            texture = animSlidding.getKeyFrame(stateTimer, true);

        if(currentState == previousState) {
            stateTimer += dt;
        }else
            stateTimer = 0;

        previousState = currentState;
        return texture;
    }

    /**
     * Metodo que é chamado sempre que o utilizar queira que o jogador salte.
     * O corpo do jogador é mudado caso esteja a deslizar (para ficar proporcionar á sprite correspondente) e é-lhe aplicada uma força
     * vertical ascendente 1 ou 2 vezes, caso o jogador queira saltar 1 vez ou duplo salto.
     */
    public void jump(){

        if(!canNotJump || isSlidding) {
            if (isSlidding) {
                this.getBody().destroyFixture(this.getBody().getFixtureList().first());
                this.getBody().destroyFixture(this.getBody().getFixtureList().first());
                this.getBody().destroyFixture(this.getBody().getFixtureList().first());

                createNewFixture(org.feup.lcom.utilities.Constants.PLAYER_WIDTH, org.feup.lcom.utilities.Constants.PLAYER_HEIGHT);

                if(audio.getInstance().getAudioOn())
                      audio.getInstance().playPlayerSlidingSound();

                currentState = org.feup.lcom.utilities.State.RUNNING;
                isSlidding = false;
                return;
            }

            if (currentState == org.feup.lcom.utilities.State.RUNNING) {
                body.applyForceToCenter(0.0f, 80, true);
                currentState = org.feup.lcom.utilities.State.JUMPING;
                previousState = org.feup.lcom.utilities.State.RUNNING;

                if(audio.getInstance().getAudioOn())
                      audio.getInstance().playPlayerJumpingSound();

                doubleJump = true;
            } else if (currentState == org.feup.lcom.utilities.State.JUMPING && doubleJump) {
                body.applyForceToCenter(0.0f, 80, true);
                previousState = org.feup.lcom.utilities.State.JUMPING;

                if(audio.getInstance().getAudioOn())
                     audio.getInstance().playPlayerJumpingSound();

                doubleJump = false;
            }
        }
    }

    /**
     * Metodo que é chamado sempre que o utilizar queira que o jogador deslize no chão.
     * O corpo do jogador é mudado caso esteja a correr ou a saltar (para ficar proporcionar á sprite correspondente).
     * No caso de estar a saltar é-lhe aplicado uma força vertical descendente para que o obrigue a voltar para o chão.
     */
    public void slide(){

        if(currentState == org.feup.lcom.utilities.State.JUMPING || currentState == org.feup.lcom.utilities.State.FALLING){
            body.applyForceToCenter(0.0f, -80, true);
            currentState = org.feup.lcom.utilities.State.RUNNING;
        }

        if(currentState == org.feup.lcom.utilities.State.RUNNING) {
            this.getBody().destroyFixture(this.getBody().getFixtureList().first());
            this.getBody().destroyFixture(this.getBody().getFixtureList().first());
            this.getBody().destroyFixture(this.getBody().getFixtureList().first());
            createNewFixture(org.feup.lcom.utilities.Constants.PLAYER_WIDTH, org.feup.lcom.utilities.Constants.PLAYER_HEIGHT-0.04f);

            if(audio.getInstance().getAudioOn())
                audio.getInstance().playPlayerSlidingSound();
        }
        isSlidding = true;
    }

    /**
     * Metodo que é chamado sempre que o jogador colida com um inimigo ou com um obstáculo,
     * alterando o seu estado atual para Dead.
     */
    public void dead() {

        if(audio.getInstance().getAudioOn())
               audio.getInstance().playPlayerDyingSound();

        if(isSlidding())
            wasSlidding = true;
        isSlidding = false;

        Filter filter = new Filter();
        filter.maskBits = org.feup.lcom.utilities.Constants.NOTHING_BIT;

        for (Fixture fixture : body.getFixtureList()) {
            //colocar o nothing bit em todas as fixtures exceto o chão
            if(fixture.getFilterData().categoryBits == org.feup.lcom.utilities.Constants.BIT_PLAYER) {
                fixture.setFilterData(filter);
            }
        }

        previousState = currentState;
        currentState = org.feup.lcom.utilities.State.DEAD;
    }

    /* -------------------------------------- Colliders --------------------------------------- */

    /**
     * Metodo auxiliar que adiciona o corpo do jogador e os sensores relativos ao seu corpo e á deteção do chão
     * @param width Largura do corpo do jogador
     * @param height Altura do corpo do jogador
     */
    private void createNewFixture (float width, float height)
    {
        // QUADRADO À VOLTA DO PLAYER
        definePlayerBody(width, height);

        // SENSOR COM TODOS OS ELEMENTOS MENOS O GROUND
        definePlayerBodySensor(width, height);

        // SENSOR COM O GROUND
        defineGroundSensor(width, height);
        shape.dispose();
    }

    /**
     * Metodo auxiliar que adiciona o corpo do jogador
     * @param width Largura do corpo do jogador
     * @param height Altura do corpo do jogador
     */
    private void definePlayerBody(float width, float height)
    {
        shape = new PolygonShape();
        shape.setAsBox(width, height);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 2.5f;
        fdef.friction = 0.25f;
        fdef.restitution = 0.0f;
        body.createFixture(fdef);
    }

    /**
     * Metodo auxiliar que adiciona o sensor que envolve o corpo do jogador
     * @param width Largura do corpo do jogador
     * @param height Altura do corpo do jogador
     */
    private void definePlayerBodySensor(float width, float height)
    {
        shape = new PolygonShape();
        shape.setAsBox(width, height);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_PLAYER;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_ENEMY | org.feup.lcom.utilities.Constants.BIT_PLATFORMS | org.feup.lcom.utilities.Constants.BIT_COIN | org.feup.lcom.utilities.Constants.BIT_ENEMY_HEAD | org.feup.lcom.utilities.Constants.BIT_SPIKES;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Metodo auxiliar que adiciona o sensor necessário para deteção de colisão entre o corpo do jogador e o chão
     * @param width Largura do corpo do jogador
     * @param height Altura do corpo do jogador
     */
    private void defineGroundSensor(float width, float height)
    {
        shape = new PolygonShape();
        shape.setAsBox(width, height);
        fdef.filter.categoryBits = org.feup.lcom.utilities.Constants.BIT_PLAYER_GROUND;
        fdef.filter.maskBits = org.feup.lcom.utilities.Constants.BIT_GROUND;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

}
