package org.feup.lcom.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Classe responsavel por carregar todos os ficheiros de audio utilizados no jogo
 * Esta classe é um singleton, um padrão de desenho onde só precisamos de uma instancia desta classe, evitando
 * o load destes ficheiros mais do que uma vez por execução do código
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Audio {
    private static Audio ourInstance = new Audio();
    private Sound coinSound;
    private Sound playerDieSound;
    private Sound enemyDieSound;
    private Sound playerJump;
    private Sound playerSlide;
    private Sound button;
    private Music backgroundMusic;

    private boolean audioOn;

    /**
     * Metodo que retorna a única instancia desta classe
     * @return Instancia única desta classe
     */
    public static Audio getInstance() {
        return ourInstance;
    }

    /**
     * Construtor da classe.
     * Carrega todos os ficheiros de audio para variáveis, não sendo chamado mais que uma vez no decorrer do programa.
     */
    private Audio() {
        loadSounds();
    }

    /**
     * Método auxiliar que carrega os ficheiros de audio, utilizado no construtor
     */
    private void loadSounds() {
        coinSound = Gdx.audio.newSound(Gdx.files.internal("audio/Coins.wav"));
        playerDieSound = Gdx.audio.newSound(Gdx.files.internal("audio/Dies.wav"));
        enemyDieSound = Gdx.audio.newSound(Gdx.files.internal("audio/Enemy.mp3"));
        playerJump = Gdx.audio.newSound(Gdx.files.internal("audio/Jump.wav"));
        playerSlide = Gdx.audio.newSound(Gdx.files.internal("audio/Slide.wav"));
        button = Gdx.audio.newSound(Gdx.files.internal("audio/Bip.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/BackgroundMusic.mp3"));
        audioOn = true;
    }

    /**
     * Método que permite tocar o som de apanhar uma moeda uma vez.
     */
    public void playCoinSound() {
        coinSound.play(1.0f);
    }

    /**
     * Método que permite tocar o som da personagem morrer uma vez.
     */
    public void playPlayerDyingSound() {
        playerDieSound.play(1.0f);
    }

    /**
     * Método que permite tocar o som do inimigo morrer uma vez.
     */
    public void playEnemyDyingSound() {
        enemyDieSound.play(1.0f);
    }

    /**
     * Método que permite tocar o som da personagem saltar uma vez.
     */
    public void playPlayerJumpingSound() {
        playerJump.play(1.0f);
    }

    /**
     * Método que permite tocar o som da personagem deslizar uma vez.
     */
    public void playPlayerSlidingSound() {
        playerSlide.play(1.0f);
    }

    /**
     * Método que permite tocar o som de clicar num botão uma vez.
     */
    public void playButtonSound() {
        button.play(1.0f);
    }


    /**
     * Método que permite tocar a música de fundo do jogo continuamente até ao jogador perder.
     * É chamada cada vez que é começado um novo jogo
     */
    public void playBackgroundMusic() {
        backgroundMusic.play();
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.setLooping(true);
    }

    /**
     * Método que permite parar de tocar a música de fundo do jogo-
     * É chamada quando o jogador perde.
     */
    public void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    /**
     * Método que permite saber se o audio está ligado
     */
    public boolean getAudioOn() {return audioOn;}

    /**
     * Método que permite alterar se o audio está ligado ou desligado.
     * Este valor só pode ser alterado no menu, no botão do volume.
     */
    public void setAudioOn(boolean bool) {audioOn = bool;}

    /**
     * Metodo que é chamado quando o jogo termina.
     */
    public void dispose()
    {
        coinSound.dispose();
        playerDieSound.dispose();
        enemyDieSound.dispose();
        playerJump.dispose();
        playerSlide.dispose();
        backgroundMusic.dispose();
    }
}
