/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertEquals;

import org.feup.lcom.logic.Background;
import org.feup.lcom.logic.Coin;
import org.feup.lcom.logic.Enemy;
import org.feup.lcom.logic.Platform;
import org.feup.lcom.logic.Player;
import org.feup.lcom.logic.Slime;
import org.feup.lcom.logic.Spinner;
import org.feup.lcom.utilities.Constants;
import org.feup.lcom.utilities.State;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class UnitTestExample {

	@Test
	public void oneEqualsOne() {
		assertEquals(1, 1);
	}

    /* LOGICA DO PLAYER */

	@Test
	public void playerPicksCoin() {
		Player p = new Player(null, null, null);

		//se o player apanhar uma moeda, fica com uma moeda
		p.incCoins();

		assertEquals(1, p.getNumCoinsCoins());
	}

	@Test
	public void playerJumpOnce(){
		Player p = new Player(null, null, null);

		//se o player saltar, o seu estado passa para JUMPING e pode fazer doubleJump
		p.jump();

		assertEquals(p.getState(), State.JUMPING);
		assertEquals(p.getDoubleJump(), true);
	}

	@Test
	public void playerJumpTwice(){
		Player p = new Player(null, null, null);

		//se o player saltar 2 vezes, o seu estado passa para JUMPING e nao pode fazer doubleJump
		p.jump();
		p.jump();

		assertEquals(p.getState(), State.JUMPING);
		assertEquals(p.getDoubleJump(), false);
	}

	@Test
	public void playerJumpWhileSliding(){
		Player p = new Player(null, null, null);

		//se o player saltar enquanto deslizava apenas se levanta, ou seja, passa para o estado de RUNNING e isSliding passa a false
		p.jump();

		assertEquals(p.getState(), State.RUNNING);
		assertEquals(p.isSlidding(), false);
	}

	@Test
	public void playerSlide(){
		Player p = new Player(null, null, null);

		//se o player fizer slide, o booleano isSliding deve ficar a verdadeiro
		p.slide();

		assertEquals(p.isSlidding(), true);
	}

	@Test
	public void playerDies(){
		Player p = new Player(null, null, null);

		//se o player morrer, o seu estado passa a DEAD
		p.dead();

		assertEquals(p.getState(), State.DEAD);
	}

	@Test
	public void playerKill() {
		Player p = new Player(null, null, null);
		Enemy e = new Spinner(null, null, null, 0);

		//se o player matar um inimigo, o estado do player continua a ser RUNNING, e o inimigo passa a morto
		p.killEnemy(e);

		assertEquals(p.getState(), State.RUNNING);
		assertEquals(e.isDead(), true);
	}

    /* LOGICA DO BACKGROUND */

	@Test
	public void backgroundMoving() {
		Background b = new Background();

		//depois de um update, os retangulos devem se ter movido
		b.update(0.5f);

		assertEquals(b.textureRegionBounds1.x, -2*Constants.ASPECT_RATIO -0.6*0.5f);
		assertEquals(b.textureRegionBounds2.x, -0.6*0.5f);
	}


	@Test
	public void backgroundLeftBoundarie() {
		Background b = new Background();

		//depois de um update, os retangulos devem se ter movidos
		b.update(3);

		//como passa do ecra, é feito resetBoundaries
		assertEquals(b.textureRegionBounds2.x, Constants.ASPECT_RATIO);
	}

    /* LOGICA DA COIN */

	@Test
	public void coinVisibleWhenCreated() {
		Coin c = new Coin(null, new Point(0,0), null,0);
		assertEquals(c.isVisible(), true);

		c.update(2.1f);

		//2.1f segundos depois já está fora do ecra
		assertEquals(c.isVisible(), false);
	}

    /* LOGICA ELEMENTS */

	@Test
	public void platformChangeVelocity() {
		Platform p = new Platform(null, new Point(0,0), null,0);

		assertEquals(p.getSpeed(), Constants.GAME_INITIAL_SPEED.getX());

		p.changeWorldVelocity(0.5f);

		assertEquals(p.getSpeed(), 0.5f);
	}

    /* LOGICA ENEMY */

	@Test
	public void enemyCreatedAlive() {
		Enemy e = new Slime(null, new Point(0,0), null,0);
		assertEquals(e.isDead(), false);
	}

}
