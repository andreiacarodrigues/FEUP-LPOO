package org.feup.lcom.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

import org.feup.lcom.audio.Audio;
import org.feup.lcom.networking.EndlessRunner;
import org.feup.lcom.utilities.Point;

/**
 * Classe responsavel por apresentar o ecra das informacoes.
 * Este ecra implementa a interface 'Screen' do Libgdx.
 * No ecra e possivel observar-se um cabecalho com o texto 'Informacoes', bem como um painel com as respetivas informacoes.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class InfoScreen extends MyScreen{

    /* UI */
    private TextButton back;
    private BitmapFont font;

    /* Audio */
    private Audio audio;

    /* Files */
    private FileHandle file;

    /**
     * Construtor da classe.
     * Recebe como parametro o jogo. As inicializacoes sao feitas na classe MyScreen atraves da chamada super().
     * Inicializa a font usada para escrever as informaceos e a abertura do ficheiro internal, "Regras.txt".
     * @param game Jogo EndlessRunner
     */
    public InfoScreen(EndlessRunner game) {
        super(game);

        font = new BitmapFont(Gdx.files.internal("myFont2.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(0.7f,0.7f);

        file = Gdx.files.internal("Regras.txt");
    }

    /**
     * Metodo auxiliar que cria os elementos visuais do ecrã ( cabeçalho 'Informacoes', painel e label com as informacoes )
     * @param atlas TextureAtlas que contem os elementos
     * @param stage
     * @param game
     */
    protected void addUIElements(TextureAtlas atlas, Stage stage, EndlessRunner game)
    {
        //adicionar painel
        TextureRegion panel = new TextureRegion(atlas.findRegion("panel"),0,0, 852, 690);
        Image displayPanel = new Image(panel);
        Point panelPos = new Point(stage.getWidth()/2-panel.getRegionWidth()/2, stage.getHeight()/2-panel.getRegionHeight()/2-70);
        displayPanel.setPosition(panelPos.getX(),panelPos.getY());
        stage.addActor(displayPanel);

        //adicionar titulo
        TextureRegion title = new TextureRegion(atlas.findRegion("informacoes"),0,0, 677, 173);
        Image imageTitle = new Image(title);
        Point imageTitlePos = new Point(stage.getWidth()/2-title.getRegionWidth()/2,stage.getHeight()/2+230);
        imageTitle.setPosition(imageTitlePos.getX(),imageTitlePos.getY());
        stage.addActor(imageTitle);

        //texto lido do ficheiro de texto "Regras.txt"
        String text = file.readString();
        Label displayScore = new Label(text, new Label.LabelStyle(font, Color.GOLDENROD));
        Point scorePos = new Point(stage.getWidth()/2-panel.getRegionWidth()/2+40, stage.getHeight()/2-370);
        displayScore.setPosition(scorePos.getX(),scorePos.getY());
        stage.addActor(displayScore);
    }

    /**
     * Cria o botao back e respetivo listener.
     * @param game
     * @param stage
     */
    protected void addButtons(final EndlessRunner game, Stage stage)
    {
        //back button
        back = createButton("back", "backPressed", stage.getWidth()/2+400,stage.getHeight()/2-400);
        back.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(audio.getInstance().getAudioOn())
                    audio.getInstance().playButtonSound();
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        stage.addActor(back);
    }

    /**
     * Metodo da interface Screen.
     * E chamado quando se muda de ecra.
     * Limpa as funcoes dos botoes.
     */
    @Override
    public void dispose() {
        back.clear();
    }
}
