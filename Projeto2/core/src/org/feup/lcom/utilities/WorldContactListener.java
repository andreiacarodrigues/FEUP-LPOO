package org.feup.lcom.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.feup.lcom.logic.Coin;
import org.feup.lcom.logic.Enemy;
import org.feup.lcom.logic.Player;

/**
 * Classe responsavel pela deteção de colisões entre os vários elementos do jogo.
 * Esta classe implementa a interface ContactListener e funciona como Observer das interações entre os diferentes elementos
 * do programa.
 * Ela deteta as colisões entre as várias fixtures e age tendo estas em conta (ex. mudar o estado do Player quando entra em contacto com um inimigo)
 * @author Ines Gomes e Andreia Rodrigues
 */
public class WorldContactListener implements ContactListener {

    /**
     * Método da interface ContactListener que deteta o contacto entre dois objetos do jogo e os altera conforme o resultado que este
     * contacto tenha
     */
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //nao nos interesse qual é a fixture A ou B, desde que juntas sejam uma das combinações possiveis das colisões
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Constants.BIT_PLAYER | Constants.BIT_PLATFORMS:
                if (fixA.getFilterData().categoryBits == Constants.BIT_PLAYER)// o Player era a fixA
                    ((Player) fixA.getUserData()).dead();
                else
                    ((Player) fixB.getUserData()).dead();
                break;
            case Constants.BIT_PLAYER | Constants.BIT_COIN:
                if (fixA.getFilterData().categoryBits == Constants.BIT_PLAYER) {// o Player era a fixA
                    ((Player) fixA.getUserData()).incCoins();
                    ((Player) fixA.getUserData()).canNotJump(true);
                    ((Coin) fixB.getUserData()).setVisible(false);
                }else{
                    ((Player) fixB.getUserData()).incCoins();
                    ((Player) fixB.getUserData()).canNotJump(true);
                    ((Coin) fixA.getUserData()).setVisible(false);
                }
                break;
            case Constants.BIT_PLAYER_GROUND | Constants.BIT_GROUND:
                if (fixA.getFilterData().categoryBits == Constants.BIT_PLAYER_GROUND) {
                    if (((Player) fixA.getUserData()).isSlidding() || ((Player) fixA.getUserData()).isDead())
                        break;
                    ((Player) fixA.getUserData()).setState(State.RUNNING);
                    ((Player) fixA.getUserData()).canNotJump(false);
                }else{
                    if(((Player)fixB.getUserData()).isSlidding() || ((Player) fixB.getUserData()).isDead())
                        break;
                    ((Player)fixB.getUserData()).setState(State.RUNNING);
                    ((Player) fixB.getUserData()).canNotJump(false);
                }
                break;
            case Constants.BIT_PLAYER | Constants.BIT_ENEMY_HEAD:
                if (fixA.getFilterData().categoryBits == Constants.BIT_PLAYER) // o Enemy era a fixA
                    ((Player)fixA.getUserData()).killEnemy((Enemy)fixB.getUserData());
                else
                    ((Player)fixB.getUserData()).killEnemy((Enemy)fixA.getUserData());
                break;
            case Constants.BIT_PLAYER | Constants.BIT_ENEMY:
                if (fixA.getFilterData().categoryBits == Constants.BIT_PLAYER)// o Player era a fixA
                    ((Player) fixA.getUserData()).dead();
                else
                    ((Player) fixB.getUserData()).dead();
                break;
            case Constants.BIT_PLAYER | Constants.BIT_SPIKES:
                if (fixA.getFilterData().categoryBits == Constants.BIT_PLAYER)// o Player era a fixA
                    ((Player) fixA.getUserData()).dead();
                else
                    ((Player) fixB.getUserData()).dead();
                break;
            default:
                break;
        }
    }

    /**
     * Método da interface ContactListener, não utilizado
     */
    @Override
    public void endContact(Contact contact) {
    }

    /**
     * Método da interface ContactListener, não utilizado
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    /**
     * Método da interface ContactListener, não utilizado
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
