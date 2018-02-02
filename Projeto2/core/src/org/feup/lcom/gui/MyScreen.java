package org.feup.lcom.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.feup.lcom.audio.Audio;
import org.feup.lcom.networking.EndlessRunner;
import org.feup.lcom.utilities.Constants;
import org.feup.lcom.utilities.Point;

public abstract class MyScreen implements Screen{

    /* Camara */
    private Viewport cameraPort;
    private Stage stage;
    private EndlessRunner game;
    private OrthographicCamera camera;

    /* UI */
    private TextureRegion background;
    private TextureAtlas atlas;

    /* Audio */
    private Audio audio;

    /**
     * Construtor da classe abstrata MyScreen.
     * Inicializa a camara, o atlas, background e processamento de inputs.
     * @param game
     */
    MyScreen(EndlessRunner game){
        this.game = game;

        setUpCamera();

        initAtlas();

        background = new TextureRegion(new Texture(Gdx.files.internal("menuBackground.png")));

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Metodo da classe abstracta MyScreen.
     * Inicializa a câmara do jogo
     * @return
     */
    protected void setUpCamera()
    {
        float fitViewPortWidth = 1920*0.7f;
        float fitViewPortHeight = 1200*0.7f;

        camera = new OrthographicCamera(2f * Constants.ASPECT_RATIO, 2f);
        cameraPort = new FitViewport(fitViewPortWidth, fitViewPortHeight, camera);
        stage = new Stage(cameraPort, game.batch);
    }

    /**
     * Metodo da classe abstracta MyScreen.
     * Inicializa o atlas para aceder ás imagens
     * @return
     */
    protected void initAtlas()
    {
        atlas = new TextureAtlas("ButtonsAndHeaders.txt");
    }

    /**
     * Metodo inerente a interface Screen.
     * Cria o cabecalho 'Pontuacoes', o painel sobre o qual ficarao os Highscores e um botao de voltar a tras bem como o seu Action Listener.
     * Tambem cria uma Label para o display do top10.
     */
    @Override
    public void show() {
        addUIElements(atlas, stage, game);
        addButtons(game, stage);
    }

    /**
     * Metodo abstrato a ser implementado nas classes derivadas.
     * Metodo que adiciona os elementos ao jogp.
     * @param atlas TextureAtlas que contem os elementos
     * @param stage
     * @param game
     */
    protected abstract void addUIElements(TextureAtlas atlas, Stage stage, EndlessRunner game);

    /**
     * Metodo abstrato a ser implementado nas classes derivadas.
     * Adiciona os botoes desse screen ao game.
     * @param game
     * @param stage
     */
    protected abstract void addButtons(EndlessRunner game, Stage stage);

     /**
     * Metodo auxiliar que preenche todos os requisitos para criar um novo botao.
     * @param region Nome da zona do atlas que contem a imagem do botao quando nao esta a ser premido
     * @param regionPressed Nome da zona do atlas que contem a imagem do botao quando esta a ser premido
     * @param x Coordenada x no ecra do botao
     * @param y Coordenada y no ecra do botao
     * @return Botao criado com as configuracoes mencionadas
     */
    protected TextButton createButton(String region, String regionPressed, float x, float y){
        TextButton button;

        Skin skin = new Skin();
        skin.addRegions(atlas);
        BitmapFont font = new BitmapFont(Gdx.files.internal("myFont2.fnt"),false);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable(region);
        style.down = skin.getDrawable(regionPressed);
        style.font = font;

        button = new TextButton("",style);
        button.setPosition(x, y);

        return button;
    }

    /**
     * Metodo inerente a interface 'Screen'
     * Atualizacao do ecra num periodo delta de tempo.
     * @param delta Perido de tempo a que o ecra e atualizado
     */
    @Override
    public void render(float delta) {
        Point backgroundPos = new Point(1920*0.7f,1200*0.7f);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, backgroundPos.getX(), backgroundPos.getY());
        stage.getBatch().end();

        stage.draw();
    }

    /**
     * Metodo da interface Screen, nao utilizado
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        cameraPort.update(width, height);
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
