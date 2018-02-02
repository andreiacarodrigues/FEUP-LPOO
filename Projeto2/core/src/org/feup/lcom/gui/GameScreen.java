package org.feup.lcom.gui;

import org.feup.lcom.audio.Audio;
import org.feup.lcom.logic.Coin;
import org.feup.lcom.utilities.Constants;
import org.feup.lcom.logic.Element;
import org.feup.lcom.logic.Enemy;
import org.feup.lcom.logic.Game;
import org.feup.lcom.logic.Ground;
import org.feup.lcom.networking.EndlessRunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Classe responsavel por fazer display do jogo em si. Esta classe implementa a interface Screen.
 * Apresenta no canto superior esquerdo a contagem do tempo, e no canto superior direito a contagem das moedas apanhadas.
 * O mundo e constituido pelo jogador e pelas plataformas, inimigos e moedas que se movem na direcao do mesmo.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class GameScreen implements Screen {

    private EndlessRunner game;
    private Game gameMap;
    private World gameWorld;

    /* Camara */
    private OrthographicCamera camera;
    private Viewport cameraPort;

    TextureAtlas elementsAtlas;
    TextureAtlas enemiesAtlas;
    /* UI */
    private HUD hud;

    /* Audio */
    private Audio audio;

    private float currTime;

    /**
     * Construtor da classe GameScreen. Recebe como parametro o jogo EndlessRunner.
     * Inicializa a camara, o tempo, os sprites, a gravidade, os elementos HUD (contagem do tempo e das moedas).
     * @param game
     */
    public GameScreen(EndlessRunner game) {
        this.game = game;
        currTime = 0;
        setUpCamera();
        gameWorld = new World(new Vector2(0,Constants.GRAVITY), true);
        elementsAtlas = new TextureAtlas("Elements.txt");
        enemiesAtlas = new TextureAtlas("Enemies.txt");
    }

    /**
     * Inicializa a câmara do jogo
     */
    protected void setUpCamera()
    {
        float fitViewPortWidth = game.screenWidth/Constants.PPM;
        float fitViewPortHeight = game.screenHeight/Constants.PPM;

        camera = new OrthographicCamera(2f * Constants.ASPECT_RATIO, 2f);
        cameraPort = new FitViewport(fitViewPortWidth, fitViewPortHeight, camera);
        camera.position.set(cameraPort.getWorldWidth()/ 2f, cameraPort.getWorldHeight()/ 2f, 0f);
    }

    /**
     * Metodo inerente a interface Screen.
     * Inicializa o jogo e a HUD.
     * Mostra no ecra os varios elementos.
     */
    @Override
    public void show() {
        gameMap = new Game(gameWorld, game,this);
        hud = new HUD(game.batch, gameMap, this);
    }

    /**
     * Metodo inerente a interface 'Screen'
     * Atualizacao do ecra num periodo delta de tempo.
     * @param delta Perido de tempo a que o ecra e atualizado
     */
    @Override
    public void render(float delta) {
        //limpa o ecra para desenhar por cima
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        hud.update();
        gameMap.update(delta);

        //inicio do desenho de todos os elemntos
        game.batch.begin();

        draw();

        // fim do desenho dos elementos
        game.batch.end();

        //desenho dos elementos hud
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //atualizacao do mundo
        gameWorld.step(Constants.TIMESTEP, Constants.VELOCITY, Constants.POSITION);

        //verificacao de fim de jogo
        if(currTime > 0.1f)
        {
           if(gameMap.gameOver()) {
               //mudanca de screen
               game.setScreen(new GameOverScreen(game, gameMap.getNewScore()));
               dispose();
           }
            else
                currTime = 0;
        }
        else
            currTime += delta;
    }

    /**
     * Metodo que desenha todas as sprites necessárias no ecrã naquele momento
     */
    private void draw()
    {
        gameMap.getBackground().draw(game.batch);

        ArrayList<Ground> ground = gameMap.getGround();
        for(Ground g : ground)
            g.draw(game.batch);

        ArrayList<Element> elements = gameMap.getElements();
        for(Element p : elements)
            p.draw(game.batch);

        ArrayList<Enemy> enemies = gameMap.getEnemies();
        for(Enemy e : enemies)
            e.draw(game.batch);

        ArrayList<Coin> coins = gameMap.getCoins();
        for(Coin c : coins)
            c.draw(game.batch);

        gameMap.getPlayer().draw(game.batch);
    }

    /**
     * Metodo que retorna o atlas dos elementos.
     * @return TextureAtlas com as sprites dos diversos elementos.
     */
    public TextureAtlas getElementsAtlas(){return elementsAtlas;}

    /**
     * Metodo que retorna o atlas dos inimigos.
     * @return TextureAtlas com as sprites dos inimigos.
     */
    public TextureAtlas getEnemiesAtlas(){return enemiesAtlas;}

    /**
     * Metodo da interface Screen.
     * E chamado quando se muda de ecra.
     */
    @Override
    public void dispose() {
        gameWorld.dispose();
    }

    /**
     * Metodo da interface Screen, nao utilizado
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Metodo da interface Screen, nao utilizado
     */
    @Override
    public void pause() {

    }

    /**
     * Metodo da interface Screen, nao utilizado
     */
    @Override
    public void resume() {

    }

    /**
     * Metodo da interface Screen, nao utilizado
     */
    @Override
    public void hide() {

    }
}
