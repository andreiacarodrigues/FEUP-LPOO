package org.feup.lcom.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.feup.lcom.utilities.AssetsManager;
import org.feup.lcom.logic.Game;


/**
 * Classe responsavel por apresentar os valores do tempo e score no ecrã durante o decorrer do jogo.
 * Esta classe implementa a classe 'Sprite' do Libgdx.
 * Estes valores vão sendo incrementados ao longo do jogo e serão usados para o cálculo da pontuação final.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class HUD extends Sprite implements Disposable{

    public Stage stage;
    private Viewport viewport;
    GameScreen screen;
    AssetsManager assetsManager;

    private Label timeLabel;
    private Label coinsLabel;
    BitmapFont font;

    private Game game;

    /**
     * Construtor da classe.
     * Recebe como parametro a spriteBach usada pelo jogo, onde vai desenhar os valores, o jogo e o screen onde os vai desenhar
     * Define uma nova camera, com dimensões maiores, onde vai desenhar o tempo e o numero de moedas no topo do ecrã.
     * Inicializa a font que vai ser utilizada para escrever esses valores.
     * @param sb SpriteBatch do jogo
     * @param game Jogo
     * @param screen Ecrã de jogo
     */
    public HUD(SpriteBatch sb, Game game, GameScreen screen){
        this.game = game;
        this.screen = screen;

        float fitViewportWidth = 1920*0.7f;
        float fitViewportHeight = 1200*0.7f;

        viewport = new FitViewport(fitViewportWidth, fitViewportHeight, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        font = new BitmapFont(Gdx.files.internal("myFont2.fnt"));
        font.setColor(Color.WHITE);

        addUIElements();
    }

    /**
     * Metodo auxiliar que cria os elementos visuais do ecrã (Labels onde vão ser escritos os valores de tempo e numero de moedas)
     * e os adiciona ao stage que vai ser desenhado no ecrã de jogo
     */
    private void addUIElements()
    {
        timeLabel = new Label(String.format("%04d", game.getGameTime()), new Label.LabelStyle(font, Color.WHITE));
        timeLabel.setPosition(225,750);

        TextureRegion time = new TextureRegion(assetsManager.getInstance().getElementsAtlas().findRegion("Time"),0,0, 280, 120);
        Image imageTime = new Image(time);
        imageTime.setPosition(50,720);

        stage.addActor(imageTime);
        stage.addActor(timeLabel);

        coinsLabel = new Label(String.format("%04d", game.getScore()), new Label.LabelStyle(font, Color.WHITE));
        coinsLabel.setPosition(1175,750);
        coinsLabel.setScale(0.5f);

        TextureRegion coins = new TextureRegion(assetsManager.getInstance().getElementsAtlas().findRegion("Coins"),0,0, 280, 120);
        Image imageCoins = new Image(coins);
        imageCoins.setPosition(1000,720);

        stage.addActor(imageCoins);
        stage.addActor(coinsLabel);
    }

    /**
     *  Metodo auxiliar que vai fazendo update dos valores no método render do ecrã de jogo e os desenha
     */
    public void update(){
        timeLabel.setText(String.format("%04d", game.getGameTime()));
        coinsLabel.setText(String.format("%04d", game.getScore()));
    }

    /**
     * Metodo inerente à interface Disposable
     * É chamado quando se muda do GameScreen para outro ecrã.
     */
    @Override
    public void dispose() { stage.dispose(); }
}