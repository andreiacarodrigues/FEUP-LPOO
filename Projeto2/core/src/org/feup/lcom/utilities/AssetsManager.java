package org.feup.lcom.utilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Classe responsavel por fazer load de todas as imagens presentes no jogo
 * @author Ines Gomes e Andreia Rodrigues
 */
public class AssetsManager{

    private static AssetsManager ourInstance = new AssetsManager();

    public static AssetsManager getInstance() {
        return ourInstance;
    }

    /* Atlas */
    private TextureAtlas playerAtlas;
    private TextureAtlas elementsAtlas;
    private TextureAtlas enemiesAtlas;

    /* Player */
    private Animation playerJumping;
    private Animation playerRunning;
    private Animation playerSlidding;
    private Animation playerDying;
    private Animation playerFalling;
    private TextureRegion playerDead;

    /* Coin */
    private Animation coin;

    /* Enemys */
    private Animation slimePink;
    private Animation slimeBlue;
    private Animation slimeGreen;
    private Animation ladybug;
    private TextureRegion spikes;
    private Animation spinner;

    /* Ground and Platforms */
    private TextureRegion ground;
    private TextureRegion platform;
    private TextureRegion platformWSpikes;

    /**
     * Construtor da classe.
     * Inicializa os atlas e faz load de todas as sprites do jogo
     */
    private AssetsManager() {
        iniAtlas();
        loadPlayerAnimations();
        loadCoinAnimation();
        loadSlimeAnimations();
        loadSpikeSprite();
        loadGroundSprite();
        loadPlatformsSprites();
        loadSpinnerAnimation();
    }

    /**
     * Metodo que inicializa o atlas para aceder ás imagens
     */
    private void iniAtlas()
    {
        playerAtlas = new TextureAtlas("Player.txt");
        elementsAtlas = new TextureAtlas("Elements.txt");
        enemiesAtlas = new TextureAtlas("Enemies.txt");
    }

    /**
     * Metodo que vai adicionar a um vetor todas as frames de cada estado do player, presentes na sua sprite sheet.
     * É depois criada uma animação com estas frames, para cada um dos estados do player
     */
    private void loadPlayerAnimations()
    {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        /* Running Animation */
        for(int i = 2; i < 9; i++) {
            frames.add(new TextureRegion(playerAtlas.findRegion("Run").getTexture(),(i-1)*1120/8, 441, 1059/8, 115));
        }
        playerRunning = new Animation(0.08f, frames);
        frames.clear();

        /* Jumping Animation */
        for(int i = 1; i < 9; i++) {
            frames.add(new TextureRegion(playerAtlas.findRegion("Run").getTexture(),(i-1)*1120/8-30, 323, 1038/8, 116));
        }
        playerJumping = new Animation(0.08f, frames);
        frames.clear();

        /* Slidding Animation */
        for(int i = 1; i < 10; i++) {
            frames.add(new TextureRegion(playerAtlas.findRegion("Run").getTexture(),(i-1)*1120/8-30,112, 1344/10, 91));
        }
        playerSlidding = new Animation(0.08f, frames);
        frames.clear();

        /* Dying Animation */
        for(int i = 1; i < 10; i++) {
            if(i == 1)
                frames.add(new TextureRegion(playerAtlas.findRegion("Run").getTexture(),(i-1)*1120/8-30,1, 1387/10, 109));
            else
                frames.add(new TextureRegion(playerAtlas.findRegion("Run").getTexture(),(i-1)*1120/8-5,1, 1387/10, 109));
        }
        playerDying = new Animation(0.08f, frames);
        frames.clear();

        /* Falling Animation */
        for(int i = 1; i < 10; i++) {
            frames.add(new TextureRegion(playerAtlas.findRegion("Run").getTexture(),(i-1)*1120/8-5,205, 1056/10, 116));
        }
        playerFalling = new Animation(0.08f, frames);
        frames.clear();

        /* Dying Animation */
        playerDead = new TextureRegion(playerAtlas.findRegion("dying"),1387-1387/10,0, 1387/10, 109);

    }

    /**
     * Metodo que vai adicionar a um vetor todas as frames da animação da moeda, presentes na sua sprite sheet.
     * É depois criada uma animação com estas frames.
     */
    private void loadCoinAnimation()
    {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(elementsAtlas.findRegion("CoinSpin").getTexture(),145+i*50, 287, 50, 50));

        coin = new Animation(0.09f, frames);
        frames.clear();
    }

    /**
     * Metodo que vai adicionar a um vetor todas as frames da animação dos slimes, presentes na sua sprite sheet.
     * É depois criada uma animação com estas frames para cada slime.
     */
    private void loadSlimeAnimations()
    {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),203, 61, 90, 55));
        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),203+90, 61, 90, 55));

        slimePink = new Animation(0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),385, 1, 90, 55));
        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),385+90, 1, 90, 55));

        slimeBlue = new Animation(0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),567, 1, 90, 55));
        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),567+90, 1, 90, 55));

        slimeGreen = new Animation(0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),385, 58, 90, 54));
        frames.add(new TextureRegion(enemiesAtlas.findRegion("slime1").getTexture(),385+90, 58, 90, 54));

        ladybug = new Animation(0.2f, frames);
        frames.clear();
    }

    /**
     * Metodo que guarda a sprite dos spikes numa variavel.
     */
    private void loadSpikeSprite()
    {
        spikes = new TextureRegion(elementsAtlas.findRegion("spikes"),0,0, 140, 70);
    }

    /**
     * Metodo que guarda a sprite do chão numa variavel.
     */
    private void loadGroundSprite()
    {
        ground = new TextureRegion(elementsAtlas.findRegion("Ground"),0, 0,70,140);
    }

    /**
     * Metodo que guarda a sprite das plataformas em variaveis.
     */
    private void loadPlatformsSprites()
    {
        platform = new TextureRegion(elementsAtlas.findRegion("Platform"),0,0, 210, 40);
        platformWSpikes = new TextureRegion(elementsAtlas.findRegion("PlatformWSpikes"),0,0, 70, 420);
    }

    /**
     * Metodo que vai adicionar a um vetor todas as frames da animação do spinner, presentes na sua sprite sheet.
     * É depois criada uma animação com estas frames
     */
    private void loadSpinnerAnimation()
    {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(enemiesAtlas.findRegion("spinner").getTexture(),1, 1, 100, 100));
        frames.add(new TextureRegion(enemiesAtlas.findRegion("spinner").getTexture(),1+100, 1, 100, 100));

        spinner = new Animation(0.1f, frames);
        frames.clear();
    }

    /**
     * Metodo que retorna o atlas do jogador.
     * @return TextureAtlas com as sprites da personagem principal.
     */
    public TextureAtlas getPlayerAtlas() { return playerAtlas; }

    /**
     * Metodo que retorna o atlas dos elementos.
     * @return TextureAtlas com as sprites dos diversos elementos.
     */
    public TextureAtlas getElementsAtlas() { return elementsAtlas; }

    /**
     * Metodo que retorna o atlas dos inimigos.
     * @return TextureAtlas com as sprites dos inimigos.
     */
    public TextureAtlas getEnemiesAtlas() { return enemiesAtlas; }

    /**
     * Metodo que retorna animação do player a saltar.
     * @return Animação do player a saltar.
     */
    public Animation getPlayerJumping() {
        return playerJumping;
    }

    /**
     * Metodo que retorna animação do player a correr.
     * @return Animação do player a correr.
     */
    public Animation getPlayerRunning() {
        return playerRunning;
    }

    /**
     * Metodo que retorna animação do player a deslizar.
     * @return Animação do player a deslizar.
     */
    public Animation getPlayerSlidding() {
        return playerSlidding;
    }

    /**
     * Metodo que retorna animação do player a morrer.
     * @return Animação do player a morrer.
     */
    public Animation getPlayerDying() {
        return playerDying;
    }

    /**
     * Metodo que retorna animação do player a cair.
     * @return Animação do player a cair.
     */
    public Animation getPlayerFalling() {
        return playerFalling;
    }

    /**
     * Metodo que retorna sprite do player a morrer.
     * @return TextureRegion do player a morrer.
     */
    public TextureRegion getPlayerDead() {
        return playerDead;
    }

    /**
     * Metodo que retorna animação da moeda.
     * @return Animação da moeda a rodar.
     */
    public Animation getCoin() {
        return coin;
    }

    /**
     * Metodo que retorna animação do pinkSlime.
     * @return Animação do pinkSlime.
     */
    public Animation getSlimePink() {
        return slimePink;
    }

    /**
     * Metodo que retorna animação do blueSlime.
     * @return Animação do blueSlime.
     */
    public Animation getSlimeBlue() {
        return slimeBlue;
    }

    /**
     * Metodo que retorna animação do greenSlime.
     * @return Animação do greenSlime.
     */
    public Animation getSlimeGreen() {
        return slimeGreen;
    }

    /**
     * Metodo que retorna animação da ladyBug.
     * @return Animação da ladyBug.
     */
    public Animation getLadybug() {
        return ladybug;
    }

    /**
     * Metodo que retorna sprite dos spikes.
     * @return TextureRegion dos spikes.
     */
    public TextureRegion getSpikes() {
        return spikes;
    }

    /**
     * Metodo que retorna animação do spinner.
     * @return Animação do spinner a rodar.
     */
    public Animation getSpinner() {
        return spinner;
    }

    /**
     * Metodo que retorna sprite do chão.
     * @return TextureRegion do chão.
     */
    public TextureRegion getGround() {
        return ground;
    }

    /**
     * Metodo que retorna sprite das plataformas.
     * @return TextureRegion das plataformas.
     */
    public TextureRegion getPlatform() {
        return platform;
    }

    /**
     * Metodo que retorna sprite das plataformas com spikes.
     * @return TextureRegion  das plataformas com spikes
     */
    public TextureRegion getPlatformWSpikes() {
        return platformWSpikes;
    }
}
