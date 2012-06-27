/*

﻿ *    Copyright 2012 Brayden (headdetect) Lopez
 *    
 *    Dual-licensed under the Educational Community License, Version 2.0 and
 *	the GNU General Public License Version 3 (the "Licenses"); you may
 *	not use this file except in compliance with the Licenses. You may
 *	obtain a copy of the Licenses at
 *
 *		http://www.opensource.org/licenses/ecl2.php
 *		http://www.gnu.org/licenses/gpl-3.0.html
 *
 *		Unless required by applicable law or agreed to in writing
 *	software distributed under the Licenses are distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the Licenses for the specific language governing
 *	permissions and limitations under the Licenses.
 * 
 */
package com.headdetect.games.superpong.Activities;

import static com.headdetect.games.superpong.Constants.CAMERA_HEIGHT;
import static com.headdetect.games.superpong.Constants.CAMERA_WIDTH;
import static com.headdetect.games.superpong.Constants.CHECK_HEIGHT;
import static com.headdetect.games.superpong.Constants.GUI_PADDING;
import static com.headdetect.games.superpong.Constants.HALF_BALL_RAD;
import static com.headdetect.games.superpong.Constants.HALF_CAMERA_HEIGHT;
import static com.headdetect.games.superpong.Constants.HALF_CAMERA_WIDTH;
import static com.headdetect.games.superpong.Constants.HALF_PLAYER_HEIGHT;
import static com.headdetect.games.superpong.Constants.MAX_PLAYER_Y;
import static com.headdetect.games.superpong.Constants.MIN_PLAYER_Y;
import static com.headdetect.games.superpong.Constants.TEXT_DISTANCE_FROM_CENTER;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import android.opengl.GLES20;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.headdetect.games.superpong.Editables;
import com.headdetect.games.superpong.Entities.Ball;
import com.headdetect.games.superpong.Entities.Letters;
import com.headdetect.games.superpong.Entities.Player;
import com.headdetect.games.superpong.Entities.Wall;

// TODO: Auto-generated Javadoc
/**
 * The Class SinglePlayerPongActivity.
 */
public class SinglePlayerPongActivity extends BaseGameActivity implements IGameInterface {

	// ===========================================================
	// Constants
	// ===========================================================

	// TODO: Use Android.r

	public static String pauseMessage = "Paused";
	public static String winMessageTopLine = "And the winner is...";

	// ===========================================================
	// Fields
	// ===========================================================

	// --------------
	// - Components
	// --------------
	private Scene mScene;
	private Scene mWinScene;
	private Scene mPauseScene;

	private PhysicsWorld mPhysics;
	private Editables settings;
	private int playerScore;
	private int computerScore;

	// ---------------------
	// - Textures & Regions
	// ---------------------
	private ITextureRegion mRegionParticleCircle;

	// -------------------
	// - Sprites
	// -------------------
	private Ball mBall;
	private Wall topWall;
	private Wall bottomWall;
	private Player mPlayer;
	private Player mComputer;
	private Text playerText;
	private Text computerText;

	// ------------------
	// - Final Overriding Fields
	// -----------------

	private final IUpdateHandler UpdateHandle = new IUpdateHandler() {

		@Override
		public void onUpdate(float pSecondsElapsed) {

			final float ballY = mBall.getY();
			final float ballX = mBall.getX();
			final float computerY = mComputer.getY();

			final float diff = ballY - computerY;

			if (diff >= CHECK_HEIGHT || diff <= -CHECK_HEIGHT) {
				mComputer.setPath(ballY - HALF_PLAYER_HEIGHT);
			}

			mComputer.onOwnUpdate();

			// TODO: dim/opaque text by calculating distance from text and ball.

			if (ballX < -20 || ballX > CAMERA_WIDTH + 20) {
				mBall.getBody().setLinearVelocity(0, 0);
				mBall.getBody().setTransform((CAMERA_WIDTH / 2) / 32, (CAMERA_HEIGHT / 2) / 32, 0f);

				if (ballX < -20)
					computerScore++;
				else
					playerScore++;

				playerText.setText(String.valueOf(playerScore));
				computerText.setText(String.valueOf(computerScore));

				if (computerScore == 7) {
					win(mComputer);
					return;
				} else if (playerScore == 7) {
					win(mPlayer);
					return;
				}

				lag(new Runnable() {
					@Override
					public void run() {
						Vector2 vec = Vector2.createRandom(7f, 13f, 7f, 10f);
						int dir = 1;
						if (SystemClock.uptimeMillis() % 2 == 0)
							dir = -1;
						mBall.getBody().setLinearVelocity(vec.x * dir, vec.y * dir);
					}
				}, 3);
			}
		}

		@Override
		public void reset() {
			mComputer.transform(mComputer.getX(), CAMERA_HEIGHT / 2 - mComputer.getHeight() / 2);
		}
	};

	private final ContactListener contactListener = new ContactListener() {

		@Override
		public void beginContact(Contact contact) {

			final Body bodyA = contact.getFixtureA().getBody();
			final Body bodyB = contact.getFixtureB().getBody();

			if (bodyA.equals(mBall.getBody()) || bodyB.equals(mBall.getBody())) {
				if (bodyA.equals(topWall.getBody()) || bodyB.equals(topWall.getBody())) {
					topWall.setGlowing();
				}

				if (bodyA.equals(bottomWall.getBody()) || bodyB.equals(bottomWall.getBody())) {
					bottomWall.setGlowing();
				}

				if (bodyA.equals(mPlayer.getBody()) || bodyB.equals(mPlayer.getBody())) {
					mPlayer.setGlowing(true);
					SinglePlayerPongActivity.this.makeGlowsplosion(mBall.getX(), mBall.getY());
				}

				if (bodyA.equals(mComputer.getBody()) || bodyB.equals(mComputer.getBody())) {
					mComputer.setGlowing(true);
					SinglePlayerPongActivity.this.makeGlowsplosion(mBall.getX(), mBall.getY());
				}
			}
		}

		@Override
		public void endContact(Contact contact) {
			mPlayer.setGlowing(false);
			mComputer.setGlowing(false);
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}
	};

	private final IOnSceneTouchListener sceneOnTouchListener = new IOnSceneTouchListener() {

		@Override
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent mEvent) {
			final float y = mEvent.getY();
			if (y - HALF_PLAYER_HEIGHT > MIN_PLAYER_Y && y + HALF_PLAYER_HEIGHT < MAX_PLAYER_Y)
				mPlayer.transform(mPlayer.getX(), y - HALF_PLAYER_HEIGHT);
			return true;
		}

	};

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void lag(final Runnable runnable, float time) {
		getEngine().registerUpdateHandler(new TimerHandler(time, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				runnable.run();
			}
		}));
	}

	@Override
	public PhysicsWorld getPhysicsWorld() {
		return mPhysics;
	}

	@Override
	public BaseGameActivity getInstance() {
		return this;
	}

	@Override
	public void removeEntity(IEntity mSprite) {
		if (mSprite == null)
			return;

		final EngineLock mLock = mEngine.getEngineLock();
		mLock.lock();
		try {
			mEngine.getScene().detachChild(mSprite);
			mSprite.dispose();
			mSprite = null;
		} catch (Exception e) {
		}
		mLock.unlock();

	}

	@Override
	public Editables getSettings() {
		return settings;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {

		settings = new Editables();

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), new Camera(0, 0,
				CAMERA_WIDTH, CAMERA_HEIGHT));
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback p) throws Exception {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("other/");

		Ball.prepare(this);
		Wall.prepare(this);
		Player.prepare(this);
		Letters.prepare(this);

		ITexture mTextureParticleCircle = new BitmapTexture(getTextureManager(), this.getTexture("particles/circle_glow.png"));
		mRegionParticleCircle = TextureRegionFactory.extractFromTexture(mTextureParticleCircle);
		mTextureParticleCircle.load();

		p.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback p) throws Exception {
		mEngine.registerUpdateHandler(new FPSLogger());
		if (MultiTouch.isSupported(this)) {
			mEngine.setTouchController(new MultiTouchController());
			if (!MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(this, "Warning!\nYour device might have problems to distinguish between separate fingers.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Sorry your device does not support MultiTouch!", Toast.LENGTH_LONG).show();
			finish();
		}

		mScene = new Scene();
		mScene.setBackground(new SpriteBackground(getBackground()));

		mPhysics = new FixedStepPhysicsWorld(50, 2, new Vector2(0, 0), false, 8, 8);
		mPhysics.setContactListener(contactListener);

		topWall = Wall.create(false);
		bottomWall = Wall.create(true);
		mBall = Ball.create(Vector2Pool.obtain(HALF_CAMERA_WIDTH - HALF_BALL_RAD, HALF_CAMERA_HEIGHT - HALF_BALL_RAD));
		mPlayer = Player.create("Player", true);
		mComputer = Player.create("Computer", false);
		playerText = Letters.getText(new Vector2(HALF_CAMERA_WIDTH - TEXT_DISTANCE_FROM_CENTER, GUI_PADDING), String.valueOf(playerScore),
				Letters.HORIZONTAL_ALIGN_CENTER);
		computerText = Letters.getText(new Vector2(HALF_CAMERA_WIDTH + TEXT_DISTANCE_FROM_CENTER, GUI_PADDING), String.valueOf(computerScore),
				Letters.HORIZONTAL_ALIGN_CENTER);

		mScene.registerUpdateHandler(mPhysics);
		mScene.registerUpdateHandler(UpdateHandle);
		mScene.setOnSceneTouchListener(sceneOnTouchListener);
		mScene.attachChild(mBall);
		mScene.attachChild(topWall);
		mScene.attachChild(bottomWall);
		mScene.attachChild(mPlayer);
		mScene.attachChild(mComputer);
		mScene.attachChild(playerText);
		mScene.attachChild(computerText);

		// -----------------------------------------
		// - Pause Scene
		// -----------------------------------------

		mPauseScene = new Scene();
		org.andengine.entity.primitive.Rectangle dimShape = new org.andengine.entity.primitive.Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,
				this.getVertexBufferObjectManager());
		dimShape.setColor(0f, 0f, 0f, .8f);
		Text pText = Letters.getText(new Vector2(HALF_CAMERA_WIDTH - Letters.measureString(pauseMessage) / 2, HALF_CAMERA_HEIGHT), pauseMessage,
				Letters.HORIZONTAL_ALIGN_CENTER);

		mPauseScene.attachChild(pText);
		mPauseScene.attachChild(dimShape);

		p.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback p) throws Exception {

		lag(new Runnable() {
			@Override
			public void run() {
				Vector2 vec = Vector2.createRandom(7f, 13f, 7f, 10f);
				int dir = 1;
				if (SystemClock.uptimeMillis() % 2 == 0)
					dir = -1;
				mBall.getBody().setLinearVelocity(vec.x * dir, vec.y * dir);
			}
		}, 3);

		p.onPopulateSceneFinished();
	}

	@Override
	public void makeGlowsplosion(float x, float y) {

		if (!getSettings().isEnableParticles())
			return;

		final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new CircleParticleEmitter(x, y, 30f), 10, 20, 30,
				this.mRegionParticleCircle, this.getVertexBufferObjectManager());
		particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-200, 200, -200, 200));
		particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-1.5f));
		particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(.8f, 1f, .8f));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(.2f, .8f));

		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 1, 0.01f, .2f));

		mEngine.getScene().attachChild(particleSystem);

		lag(new Runnable() {

			@Override
			public void run() {
				removeEntity(particleSystem);
			}
		}, 1f);
	}

	@Override
	public IInputStreamOpener getTexture(final String loc) {
		return new IInputStreamOpener() {
			@Override
			public InputStream open() throws IOException {
				return getAssets().open(loc);
			}
		};
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			pause();
			return true;
		}
		return super.onKeyDown(pKeyCode, pEvent);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private final Sprite getBackground() {
		try {
			final ITexture mTexture = new BitmapTexture(this.getTextureManager(), getTexture("other/bg.png"),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			final ITextureRegion mRegion = TextureRegionFactory.extractFromTexture(mTexture);
			mTexture.load();

			return new Sprite(-3, -3, CAMERA_WIDTH + 3, CAMERA_HEIGHT + 3, mRegion, this.getVertexBufferObjectManager());

		} catch (IOException e) {
			return null;
		}
	}

	private void win(Player p) {
		mBall.resetPos();
		// mScene.clearUpdateHandlers();

		mWinScene = new Scene();
		mWinScene.setBackgroundEnabled(false);

		Text firstLine = Letters.getText(new Vector2(HALF_CAMERA_WIDTH - Letters.measureString(winMessageTopLine) / 2, HALF_CAMERA_HEIGHT - 100),
				winMessageTopLine, Letters.HORIZONTAL_ALIGN_CENTER, .6f);
		Text secondLine = Letters.getText(new Vector2(HALF_CAMERA_WIDTH - Letters.measureString(p.getName()) / 2, HALF_CAMERA_HEIGHT), p.getName(),
				Letters.HORIZONTAL_ALIGN_CENTER, .6f);

		mWinScene.attachChild(firstLine);
		mWinScene.attachChild(secondLine);

		if (this.mEngine.isRunning()) {
			this.mScene.setChildScene(mWinScene);
			this.mEngine.stop();
		}

	}

	private void pause() {
		if (this.mEngine.isRunning()) {
			this.mScene.setChildScene(mPauseScene);
			this.mEngine.stop();
		} else {
			this.mScene.clearChildScene();
			this.mEngine.start();
		}

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}