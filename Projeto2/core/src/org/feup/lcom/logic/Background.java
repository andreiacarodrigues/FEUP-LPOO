package org.feup.lcom.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import org.feup.lcom.utilities.Constants;

/**
 * Classe Responsavel pelo Background do jogo que extende da classe Sprite da biblioteca LibGdx.
 * Cria um background com velocidade uma velocidade que é incrementada ao longo do jogo.
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Background extends Sprite {

    private final TextureRegion textureRegion;
    public Rectangle textureRegionBounds1;
    public Rectangle textureRegionBounds2;

    private double speed = 0.6f; //inicial

    /**
     * Construtor da classe que inicializa dois retangulos do tamanho do background.
     * Estes retangulos vão desde antes do ecrã até ao centro, e outro desde do centro até fora do ecrã.
     * Os retangulos vao se movimentando sucessivamente para a esquerda até que um fica a ocupar totalemente o ecrã.
     * Nesse momento o outro retângulo passa a ocupar a posicao a direita do primeiro.
     */
    public Background() {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(Constants.BACKGROUND)));
        textureRegionBounds1 = new Rectangle(-2*Constants.ASPECT_RATIO, -1, 2*Constants.ASPECT_RATIO, 2);
        textureRegionBounds2 = new Rectangle(0, -1, 2* Constants.ASPECT_RATIO, 2);
    }

    /**
     * Atualiza a imagem a cada delta tempo.
     * Se a margem esquerda foi atingida devemos recolocar os retangulos, se não atualizamos o x dos mesmos (movimento para a esquerda)
     * @param delta
     */
    public void update(float delta) {
        if (leftBoundsReached(delta))
            resetBounds();
        else
            updateXBounds(-delta);
    }

    /**
     * Metodo que verifica se o segundo retangulo já atingiu a margem esquerda do ecrã.
     * @param delta E atualizado de delta em delta time. O delta é multiplicado ao speed para saber o deslocamento.
     * @return Verdadeiro ou falso consoante chegou ou nao a margem esquerda.
     */
    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) <= -Constants.ASPECT_RATIO;
    }

    /**
     * Atualiza a posicao dos 2 retangulos de acordo com o tempo delta e velocidade speed do background.
     * @param delta Intervalo de tempo passado.
     */
    private void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
        textureRegionBounds2.x += delta * speed;
    }

    /**
     * Muda as posicoes dos retangulos: o primeiro retangulo passa para a posicao do segundo e o segundo para fora do ecra a direita.
     * Este metodo permite garantir a continuidade da imagem.
     */
    private void resetBounds() {
        textureRegionBounds1 = textureRegionBounds2;
        textureRegionBounds2 = new Rectangle(Constants.ASPECT_RATIO, -1, 2*Constants.ASPECT_RATIO, 2);
    }

    /**
     * Metodo inerente a classe Sprite.
     * Desenha os retangulos nas posicoes indicadas.
     * @param batch
     */
    @Override
    public void draw(Batch batch) {
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, 2*Constants.ASPECT_RATIO, 2);
        batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, 2*Constants.ASPECT_RATIO, 2);
    }

    /**
     * Metodo que altera a velocidade do background exteriormente.
     * Se o i for zero significa que é para parar o mundo.
     * Se o i for diferente de zero, devemos incrementar esse valor ao speed atual.
     * @param i
     */
    public void changeWorldVelocity(float i) {
        if(i == 0)
            this.speed = 0;
        else
            this.speed -= i;
    }
}