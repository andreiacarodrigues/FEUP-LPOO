package org.feup.lcom.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import org.feup.lcom.audio.Audio;
import org.feup.lcom.networking.EndlessRunner;
import org.feup.lcom.utilities.Point;

/**
 * Classe responsavel por apresentar o menu inicial do jogo. Implementa a interface Screen.
 * Apresenta um cabecalho com o nome do jogo (Endless Runner) e mais 5 botoes:
 * Botao de play que redireciona para o jogo;
 * Botao dos highscores que redireciona para a tabela do top 10 pontuacoes do jogo
 * Botao de informacoes com as regras, controlos e creditos do jogo
 * Botao de som/sem som que permite que o jogo tenha ou nao musica.
 * Botao exit para sair do jogo
 * @author Ines Gomes e Andreia Rodrigues
 */
public class MenuScreen extends MyScreen {

    /* UI */
    private TextButton playGame, highscores, info, sound, noSound, exit;

    /* Audio */
    private Audio audio;

    /**
     * Construtor da classe. Recebe como parametro o jogo EndlessRunner (Main Activity).
     * As inicializacoes sao todas feitas na class MyScreen através da chamada super().
     * @param game Jogo
     */
    public MenuScreen(EndlessRunner game) {
        super(game);
    }

    /**
     * Metodo auxiliar que adiciona os elementos, neste caso apenas o cabecalho 'Endless Runner'
     * @param atlas TextureAtlas que contem os elementos
     * @param stage
     * @param game
     */
    protected void addUIElements(TextureAtlas atlas, Stage stage, EndlessRunner game)
    {
        //adicionar titulo
        TextureRegion title = new TextureRegion(atlas.findRegion("titulo"),0,0, 677, 173);
        Image imageTitle = new Image(title);
        Point imageTitlePos = new Point(stage.getWidth()/2-title.getRegionWidth()/2, stage.getHeight()/2+150);
        imageTitle.setPosition(imageTitlePos.getX(), imageTitlePos.getY());
        stage.addActor(imageTitle);
    }

    /**
     * Metodo auxiliar que cria os botoes Play, highscores, informacoes,som e sem som e de saida bem como todos os actions listeners correspondentes.
     * @param game
     * @param stage
     */
    protected void addButtons(final EndlessRunner game, Stage stage)
    {
        Point playButtonPos = new Point(stage.getWidth()/2-90,stage.getHeight()/2);
        //botao play
        playGame = createButton("play","playPressed", playButtonPos.getX(),playButtonPos.getY());
        playGame.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        stage.addActor(playGame);

        Point highscoresButtonPos = new Point(stage.getWidth()/2-275,stage.getHeight()/2-200);
        //botao highscores
        highscores = createButton("scores", "scoresPressed", highscoresButtonPos.getX(),highscoresButtonPos.getY());
        highscores.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new HighscoreScreen(game));
                dispose();
            }
        });
        stage.addActor(highscores);

        //botao info
        Point infoButtonPos = new Point(stage.getWidth()/2-75,stage.getHeight()/2-200);
        info = createButton("info", "infoPressed", infoButtonPos.getX(),infoButtonPos.getY());
        info.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new InfoScreen(game));
                dispose();
            }
        });
        stage.addActor(info);

        //botao noSound
        Point soundButtonPos = new Point(stage.getWidth()/2+125,stage.getHeight()/2-200);
        noSound = createButton("noSound", "noSoundPressed", soundButtonPos.getX(),soundButtonPos.getY());
        noSound.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                audio.getInstance().setAudioOn(true);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                noSound.setVisible(false);
                noSound.setDisabled(true);
                sound.setVisible(true);
                sound.setDisabled(false);
            }
        });
        stage.addActor(noSound);

        //botao sound
        sound = createButton("sound", "soundPressed", soundButtonPos.getX(),soundButtonPos.getY());
        sound.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                audio.getInstance().setAudioOn(false);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                noSound.setVisible(true);
                noSound.setDisabled(false);
                sound.setVisible(false);
                sound.setDisabled(true);
            }
        });
        stage.addActor(sound);

        if(audio.getInstance().getAudioOn())
        {
            noSound.setVisible(false);
            noSound.setDisabled(true);
            sound.setVisible(true);
            sound.setDisabled(false);
        }
        else
        {
            noSound.setVisible(true);
            noSound.setDisabled(false);
            sound.setVisible(false);
            sound.setDisabled(true);
        }

        //botao exit
        Point exitButtonPos = new Point(stage.getWidth()-200,50);
        exit = createButton("Exit", "Exit", exitButtonPos.getX(),exitButtonPos.getY());
        exit.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Gdx.app.exit();
            }
        });
        stage.addActor(exit);
    }

    /**
     * Metodo da interface Screen.
     * E chamado quando se muda de ecra.
     * Limpa as funcoes dos botoes.
     */
    @Override
    public void dispose() {
        playGame.clear();
        sound.clear();
        noSound.clear();
        info.clear();
        highscores.clear();
    }
}
