package org.feup.lcom.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;

import org.feup.lcom.utilities.AssetsManager;
import org.feup.lcom.audio.Audio;
import org.feup.lcom.gui.GameScreen;
import org.feup.lcom.networking.EndlessRunner;
import org.feup.lcom.utilities.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe responsavel pela criacao do jogo. Contem os varios elementos do jogo. Recebe e processa os inputs do jogador.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Game implements InputProcessor{

    private EndlessRunner game;
    private World world;
    private GameScreen screen;

    private int gameTime;
    private int newScore;

    /* Audio */
    private Audio audio;
    private AssetsManager assetsManager;

    private Player player;

    /* Animação */
    private Background background;
    private ArrayList<Element> elements = new ArrayList<Element>();
    private ArrayList<Ground> ground = new ArrayList<Ground>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Coin> coins = new ArrayList<Coin>();

    /* Spawn Enemys */
    private float currTime = 0;
    private float myTime = 0;
    private boolean spawn;
    private float timeSpawn;
    private float gameSpeed;

    private int controlo1;
    private int controlo2;

    /* Utilities */
    private Random rand = new Random();

    /**
     * Construtor da classe. Recebe como parametros o mundo, a MainActivity e o ecra onde e desenhado.
     * Instancia o som, para existir diferente musicas consoante os movimentos e concretizacoes do player.
     * Inicializa os diversos elementos: jogo, ecra, mundo, speed, tempo de spawn...
     * De seguida constroi o mundo e permite os inputs no ecra.
     * @param gameWorld Mundo de fisicas.
     * @param mainGame Jogo EndlessRunner.
     * @param screen Ecra
     */
    public Game(World gameWorld, EndlessRunner mainGame, GameScreen screen)
   {
       if(audio.getInstance().getAudioOn())
         audio.getInstance().playBackgroundMusic();

       game = mainGame;
       world = gameWorld;
       this.screen = screen;
       spawn = true;
       newScore = 0;
       gameSpeed = org.feup.lcom.utilities.Constants.GAME_INITIAL_SPEED.getX();
       timeSpawn = org.feup.lcom.utilities.Constants.GAME_INITIAL_SPAWN;
       controlo1 = 0;
       controlo2 = 0;

       buildWorld();

       Gdx.input.setInputProcessor(this);
   }

    /**
     * Constroi o mundo. Comeca por adicionar o background, de seguida o chao e o jogador.
     */
    private void buildWorld()
    {
        /* Adicionar background */
        background = new Background();

        /* Adiciona plataformas - chao*/
        Ground g;
        for(int i = 0; i <16; i++) {
            g = new Ground(world, new Point(-org.feup.lcom.utilities.Constants.X_MAX + org.feup.lcom.utilities.Constants.GROUND_SIZE + (2 * i * org.feup.lcom.utilities.Constants.GROUND_SIZE), - org.feup.lcom.utilities.Constants.Y_MAX + 2 * org.feup.lcom.utilities.Constants.GROUND_SIZE), screen);
            ground.add(g);
        }

        /* Adiciona player */
        player = new Player(world, Constants.PLAYER_POS, screen);

    }

    /**
     * Atualizacao do estado do jogo.
     * Comeca por atualizar o tempo. De seguida atualiza o jogador e background e chao.
     * Depois remove os elementos que entretanto ja nao pertencem a cena e gera novos de acordo com os novos tempos.
     * @param delta Perido de tempo.
     */
    public void update(float delta)
    {
        if(myTime >= 1) {
            gameTime++;
            myTime = 0;
        }
        else
            myTime += delta;

        /* Update Player */
        player.update(delta);

        /* update background */
        background.update(delta);

        /* Update Ground */
        for(Ground g : ground)
            g.update(delta);

        /* Update Elements */
        removeElements(delta);

        /* atualiza os tempos de spawn a cada 10s, a comecar nos 5s (timeSpawn diminui) */
        if((gameTime-5)%10 == 0 && gameTime >= 5 && timeSpawn >= 0.8f && controlo1 != gameTime) {
            controlo1 = gameTime;
            timeSpawn += org.feup.lcom.utilities.Constants.INCREMENT;
        }

        /* atualiza a velocidade dos elementos a cada 10s (speed aumenta) */
        if(gameTime%10 == 0 && gameTime != 0 && gameSpeed >= -1.8f && controlo2 != gameTime){
            controlo2 = gameTime;

            gameSpeed += org.feup.lcom.utilities.Constants.INCREMENT;

            background.changeWorldVelocity(org.feup.lcom.utilities.Constants.INCREMENT);
            for(Element p : elements)
                p.changeWorldVelocity(gameSpeed);
            for(Enemy e: enemies)
                e.changeWorldVelocity(gameSpeed);
            for(Coin c : coins)
                c.changeWorldVelocity(gameSpeed);
        }

        /* Spawn um elemento a cada timeSpawn sec */
        if(spawn)
            spawnElements(delta);
    }

    /**
     * Metodo que verifica o fim do jogo. Em caso de se verificar o fim do jogo, o audio e o mundo para, bem como o spawn de elementos.
     * Depois espera-se o tempo suficiente para o jogador fazer a sua animacao, calcula-se o score efetuado.
     * @return Verdadeiro ou falso consoante o jogo tenha acabado ou nao (player morre)
     */
    public boolean gameOver()
    {
        boolean done = false;

        if(player.isDead()){
            if(audio.getInstance().getAudioOn())
                 audio.getInstance().stopBackgroundMusic();

            background.changeWorldVelocity(0);
            for(Element p : elements)
                p.changeWorldVelocity(0);
            for(Enemy e: enemies)
                e.changeWorldVelocity(0);
            for(Coin c : coins)
                c.changeWorldVelocity(0);
            spawn = false;
             if(player.getStateTimer() > 2) {
                 done = true;
             }
        }
        if(done){
            newScore = 5*player.getNumCoinsCoins() + 10*gameTime;
            return true;
        }
        return false;
    }

    /**
     * Metodo que verifica se algum elemento do jogo deixou de existir, e destroi-lo.
     * @param delta Periodo de tempo.
     */
    private void removeElements(float delta)
    {
        //verificacao de inimigos
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            e.update(delta);
            if(e.isDead()) {
                world.destroyBody(e.getBody());
                enemies.remove(i);
            }
        }

        //verificacao de elementos
        for(int j = 0; j < elements.size(); j++)
        {
            Element p = elements.get(j);
            p.update(delta);
            if(p.isOutsideBoundaries()) {
                world.destroyBody(p.getBody());
                elements.remove(j);
            }
        }

        //verificacao de moedas
        for(int k = 0; k < coins.size(); k++)
        {
            Coin c = coins.get(k);
            c.update(delta);
            if(!c.isVisible()) {
                world.destroyBody(c.getBody());
                coins.remove(k);
            }
        }
    }

    /**
     * Metodo que gera aleatoriamente os elementos que aparecem no mundo (inimigos moedas e elementos)
     * @param delta Periodo de tempo.
     */
    private void spawnElements(float delta)
    {
      int randomNumber = rand.nextInt(11);

        if(currTime>=timeSpawn){
            switch(randomNumber)
            {
                case 0:
                    enemies.add(new Slime(world, org.feup.lcom.utilities.Constants.SLIME_ENEMY_POS, screen, gameSpeed));
                    break;
                case 1:
                    enemies.add(new Spinner(world, org.feup.lcom.utilities.Constants.SPINNER_ENEMY_POS, screen, gameSpeed));
                    break;
                case 2:
                   elements.add(new Spikes(world, org.feup.lcom.utilities.Constants.SPIKES_POS, screen, gameSpeed));
                    break;
                case 3:
                    elements.add(new Platform(world, org.feup.lcom.utilities.Constants.PLATFORM_POS,screen, gameSpeed));
                    enemies.add(new Slime(world, org.feup.lcom.utilities.Constants.SLIME_TOP_PLATFORM_ENEMY_POS, screen, gameSpeed));
                    break;
                case 4:
                    elements.add(new PlatformWithSpikes(world, org.feup.lcom.utilities.Constants.PLATFORMWSPIKES_POS, screen, gameSpeed));
                    break;
                case 5:
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN_POS, screen, gameSpeed));
                    break;
                case 6:
                    elements.add(new Platform(world, org.feup.lcom.utilities.Constants.PLATFORM_POS,screen, gameSpeed));
                    enemies.add(new Slime(world, org.feup.lcom.utilities.Constants.SLIME_BOTTOM_PLATFORM_ENEMY_POS, screen, gameSpeed));
                    break;
                case 7:
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN_POS, screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN2_POS, screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN3_POS, screen, gameSpeed));
                    break;
                case 8:
                    elements.add(new Platform(world, org.feup.lcom.utilities.Constants.PLATFORM_POS,screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN_TOP_PLATFORM_POS, screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN2_TOP_PLATFORM_POS, screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN3_TOP_PLATFORM_POS, screen, gameSpeed));
                    break;
                case 9:
                    elements.add(new Platform(world, org.feup.lcom.utilities.Constants.PLATFORM_POS,screen, gameSpeed));
                    elements.add(new Spikes(world, org.feup.lcom.utilities.Constants.SPIKES_PLATFORM_POS, screen, gameSpeed));
                    break;
                case 10:
                    elements.add(new Platform(world, org.feup.lcom.utilities.Constants.PLATFORM_POS,screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN_BOTTOM_PLATFORM_POS, screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN2_BOTTOM_PLATFORM_POS, screen, gameSpeed));
                    coins.add(new Coin(world, org.feup.lcom.utilities.Constants.COIN3_BOTTOM_PLATFORM_POS, screen, gameSpeed));
                    break;
            }
            currTime = 0;
        }
        else
            currTime += delta;
    }

    /**
     * Retorna o tempo do jogo.
     * @return gameTime
     */
    public int getGameTime() { return gameTime; }

    /**
     * Retorna o numero de moedas apanhadas pelo jogador.
     * @return Numero de moedas.
     */
    public int getScore() { return player.getNumCoinsCoins(); }

    /**
     * Retorna o player.
     * @return Player.
     */
    public Player getPlayer() {return player;}

    /**
     * Retorna o array do objeto "Ground" criado.
     * @return array list do objeto "Ground".
     */
    public ArrayList<Ground> getGround() { return ground;}

    /**
     * Retorna o background.
     * @return Background.
     */
    public Background getBackground(){ return background;}

    /**
     * Retorna o array de inimigos atual.
     * @return Array list do Objeto "Enemy"
     */
    public ArrayList<Enemy> getEnemies(){ return enemies;}

    /**
     * Retorna o array de elementos atual.
     * @return Array list do Objeto "Element"
     */
    public ArrayList<Element> getElements() {return elements; }

    /**
     * Retorna o array de moedas atual.
     * @return Array list do Objeto "Coin"
     */
    public ArrayList<Coin> getCoins(){return coins;}

    //-------------------------------------------- Input ------------------------------------------- //

    /**
     * Metodo invocado quando deteta um toque no ecra. Neste caso o jogador vai saltar.
     * @param screenX Posicao x no ecra do toque.
     * @param screenY Posicao y no ecra do toque.
     * @param pointer
     * @param button
     * @return True quando clicado.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!player.isDead())
            player.jump();
        return true;
    }


    /**
     * Metodo invocado quando deteta um delize no ecra. Neste caso o jogador vai deslizar.
     * Restricao para apenas detetar movimentos para baixo, ou seja, deltaY positivo e maior que 30 para nao confundir com saltos.
     * @param screenX Posicao x no ecra do toque.
     * @param screenY Posicao y no ecra do toque.
     * @param pointer
     * @return True quando ha um deslize
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Gdx.input.getDeltaY() > 30) {
            if (!player.isDead())
                player.slide();
        }
        return true;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @param keycode
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @param keycode
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @param character
     * @return
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @param screenX
     * @param screenY
     * @return
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @param amount
     * @return
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Metodo da interface InputProcessor nao usado.
     * @return
     */
    public int getNewScore() {
        return newScore;
    }
}
