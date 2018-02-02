package org.feup.lcom.networking;

import org.feup.lcom.gui.MenuScreen;
import org.feup.lcom.utilities.Highscores;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.badlogic.gdx.Gdx.*;


/**
 * Classe principal com que o jogo é iniciado.
 * É inicializado o SpriteBatch onde o jogo vai ser desenhado, o width e a height do ecrã e as highscores.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class EndlessRunner extends Game {

    private Game game;
    public SpriteBatch batch;
    public int screenWidth;
    public int screenHeight;
    private FileHandle file;
    private Highscores highscores;

    /**
     * Construtor vazio para inicialização do jogo
     */
    public EndlessRunner() {}

    /**
     * Metodo da classe Game, onde é criado o jogo.
     * É inicializado o spriteBatch onde os elementos dos menus vão ser desenhados e é definido como screen o menu principal do jogo.
     * São inicializadas as highscores.
     */
    @Override
    public void create () {
        game = this;
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));

        screenWidth = app.getGraphics().getWidth();
        screenHeight = app.getGraphics().getHeight();

        highscores = new Highscores();

        //deserializa a classe Highscores
        try {
            readHighscores();
        } catch (IOException e) {
            e.printStackTrace();}
    }

    /**
     * Metodo que atualiza as melhores pontuações, guardadas previamente num ficheiro
     */
    public void updateHighscores(String name, int score){
        highscores.updateHighscores(name,score);
    }

    /**
     * Metodo que mostra as melhores pontuaçoes e retorna-as como uma string
     * @return String com as highscores
     */
    public String displayHighscores(){
        return highscores.toString();
    }

    /**
     * Metodo da classe Game que atualiza o ecrã
     */
    @Override
    public void render () {
        super.render();
    }

    /**
     * Metodo da classe Game que guarda as highscores quando o jogo é posto em pausa
     */
    public void pause(){
        super.pause();
        try {
            saveHighscores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo da classe Game que guarda as highscores quando o jogo é fechado
     */
    public void dispose(){
        super.dispose();
        try {
            saveHighscores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo usado para ler ficheiros serializados, ou seja "deserializar" a classe Highscores.
     * A classe é guardada em ficheiros local.
     * @throws IOException
     */
    private void readHighscores() throws IOException {

        if(Gdx.files.local("highscores.dat").exists()) {

            ObjectInputStream is = null;
            file = Gdx.files.local("highscores.dat");

            try {
                is = new ObjectInputStream(file.read());
                highscores = (Highscores) is.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null)
                    is.close();
            }
        }
    }

    /**
     * Metodo usado para serializar a classe Highscores.
     * Os Highscores são guardados em ficheiros local.
     * @throws IOException
     */
    public void saveHighscores() throws IOException {

        file = Gdx.files.local("highscores.dat");

        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(file.write(false));
            os.writeObject(highscores);
        }
        finally {
            if (os != null)
                os.close();
        }
    }
}
