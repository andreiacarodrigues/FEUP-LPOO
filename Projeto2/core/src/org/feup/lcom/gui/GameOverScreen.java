package org.feup.lcom.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import org.feup.lcom.audio.Audio;
import org.feup.lcom.networking.EndlessRunner;
import org.feup.lcom.utilities.Point;

/**
 * Classe responsavel por apresentar o ecra de 'GameOver', quando o jogador morre.
 * Este ecra implementa a interface 'Screen' do Libgdx.
 * No ecra e possivel observar-se um cabecalho com o texto 'GameOver', bem como o score do conseguido, e a possibilidade de digitar o nome do jogador.
 * Depois o jogador pode escolher recomecar o jogo ou voltar ao menu inicial.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class GameOverScreen extends MyScreen {

    /* Pontuação */
    private int score;

    /* UI */
    private TextButton reload, back;
    private BitmapFont font;
    private TextField textfield;

    /* Audio */
    private Audio audio;

    /**
     * Construtor da classe.
     * Recebe como parametros o jogo e score atingido. As inicializacoes sao feitas na classe MyScreen atraves da chamada super().
     * Inicializa a font usada para fazer display dos scores e nome do player.
     * @param game Jogo EndlessRunner
     * @param score Score atingido pelo jogador
     */
    public GameOverScreen(EndlessRunner game, int score) {
        super(game);
        this.score = score;

        font = new BitmapFont(Gdx.files.internal("myFont1.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(0.7f);
    }

    /**
     * Metodo auxiliar que cria os elementos visuais do ecrã ( cabeçalho 'GameOver', painel, score e nome )
     * @param atlas TextureAtlas que contem os elementos
     * @param stage
     * @param game
     */
    protected void addUIElements(TextureAtlas atlas, Stage stage, EndlessRunner game)
    {
        //Adicionar painel
        TextureRegion panel = new TextureRegion(atlas.findRegion("panel"),0,0, 852, 690);
        Image displayPanel = new Image(panel);
        Point panelPos = new Point(stage.getWidth()/2-340, stage.getHeight()/2-200);
        displayPanel.setPosition(panelPos.getX(),panelPos.getY());
        displayPanel.setScale(0.8f, 0.6f);
        stage.addActor(displayPanel);

        //Adicionar titulo
        TextureRegion title = new TextureRegion(atlas.findRegion("gameOver"),0,0, 677, 173);
        Image imageTitle = new Image(title);
        Point imagePos = new Point(stage.getWidth()/2-title.getRegionWidth()/2, stage.getHeight()/2+100);
        imageTitle.setPosition(imagePos.getX(),imagePos.getY());
        stage.addActor(imageTitle);

        //Display do score
        Label displayScore = new Label(String.format("Score : %05d", score), new Label.LabelStyle(font, Color.GOLDENROD));
        Point scorePos = new Point (stage.getWidth()/2-200, stage.getHeight()/2-100+35);
        displayScore.setPosition(scorePos.getX(),scorePos.getY());
        stage.addActor(displayScore);

        //Digitar o nome do vencedor
        Point textFieldPos = new Point (stage.getWidth()/2-200, stage.getHeight()/2+40);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.GOLDENROD;
        style.font = font;
        textfield = new TextField("playerName" ,style);
        textfield.setText("Name ");
        textfield.setPosition(textFieldPos.getX(),textFieldPos.getY());
        stage.addActor(textfield);
    }

    /**
     * Metodo abstrato da classe MyScreen.
     * Cria os botoes de reload e back com os respetivos action listeners
     * @param game
     * @param stage
     */
    protected void addButtons(final EndlessRunner game, Stage stage)
    {
        //reload button
        reload = createButton("reload", "reloadPressed", stage.getWidth()/2-175,stage.getHeight()/2-250);
        reload.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //atualiza os highscores
                game.updateHighscores(textfield.getText().toString(),score);

                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        stage.addActor(reload);

        //back button
        back = createButton("back", "backPressed", stage.getWidth()/2+25,stage.getHeight()/2-250);
        back.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //atualiza os highscores
                game.updateHighscores(textfield.getText().toString(),score);

                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        stage.addActor(back);
    }

    /**
     * Metodo da interface Screen.
     * E chamado quando se muda de ecra.
     */
    @Override
    public void dispose() {
        back.clear();
        reload.clear();
    }
}
