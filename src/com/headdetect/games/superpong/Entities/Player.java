/*
 * 
 */
package com.headdetect.games.superpong.Entities;

import static com.headdetect.games.superpong.Constants.HALF_CAMERA_HEIGHT;
import static com.headdetect.games.superpong.Constants.HALF_PLAYER_HEIGHT;
import static com.headdetect.games.superpong.Constants.HALF_PLAYER_WIDTH;
import static com.headdetect.games.superpong.Constants.MAX_PLAYER_X;
import static com.headdetect.games.superpong.Constants.MIN_PLAYER_X;
import static com.headdetect.games.superpong.Constants.PLAYER_FIXTURE;
import static com.headdetect.games.superpong.Constants.PLAYER_HEIGHT;
import static com.headdetect.games.superpong.Constants.PLAYER_WIDTH;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.headdetect.games.superpong.EntityStats;
import com.headdetect.games.superpong.Activities.IGameInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class Player.
 */
public class Player extends AnimatedSprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static BuildableBitmapTextureAtlas mAtlas;

	private static TiledTextureRegion mRegion;

	private static IGameInterface gameInterface;

	private String playerName;

	private EntityStats entityStats;

	private Body mBody;
	
	private float currentPathToY;
	
	/* We could use getSettings().getStepHeight() */
	private float stepHeight;
	

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Instantiates a new player.
	 * 
	 * @param name
	 *            the name
	 * @param gameInterface
	 *            the game interface
	 * @param isOnLeft
	 *            is on left of screen
	 */
	private Player(String name, boolean isOnLeft) {
		super(isOnLeft ? MIN_PLAYER_X : MAX_PLAYER_X - PLAYER_WIDTH, HALF_CAMERA_HEIGHT - HALF_PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT, mRegion,
				gameInterface.getVertexBufferObjectManager());
		this.stepHeight = gameInterface.getSettings().getStepHeight();
		this.playerName = name;
		this.entityStats = new EntityStats();
		this.mBody = PhysicsFactory.createBoxBody(gameInterface.getPhysicsWorld(), isOnLeft ? getX() + HALF_PLAYER_WIDTH : getX(), getY(),
				HALF_PLAYER_WIDTH, PLAYER_HEIGHT, BodyType.StaticBody, PLAYER_FIXTURE);
		gameInterface.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this, mBody, true, true));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Sets the glowing.
	 * 
	 * @param glow
	 *            the new glowing
	 */
	public void setGlowing(boolean glow) {

		if (!glow) {
			gameInterface.lag(new Runnable() {

				@Override
				public void run() {
					setCurrentTileIndex(0);
				}
			}, .1f);
			return;
		}
		setCurrentTileIndex(1);
	}

	/**
	 * Gets the stats.
	 * 
	 * @return the stats
	 */
	public EntityStats getStats() {
		return entityStats;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return playerName;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.playerName = name;
	}

	/**
	 * Gets the body.
	 * 
	 * @return the body
	 */
	public Body getBody() {
		return mBody;
	}

	/**
	 * Sets the body.
	 * 
	 * @param mBody
	 *            the new body
	 */
	public void setBody(Body mBody) {
		this.mBody = mBody;
	}
	

	public void setPath(float posY) {
		currentPathToY = posY;
	}
	

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static Player create(String name, boolean isOnLeft) {
		if (gameInterface == null)
			throw new NullPointerException("Player.prepare() must be called first");

		return new Player(name, isOnLeft);
	}

	public static Player create(boolean isOnLeft) {
		return create("Player 1", isOnLeft);
	}

	/**
	 * Transforms the players position.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void transform(float x, float y) {
		final Vector2 mVec = mBody.getTransform().getPosition();
		mVec.set(mVec.x, (y + getHeight() / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		mBody.setTransform(mVec, 0);
	}

	

	/**
	 * Load the player textures.
	 * 
	 * @param mAct
	 *            the game interface
	 */
	public static void prepare(IGameInterface mAct) {
		mAtlas = new BuildableBitmapTextureAtlas(mAct.getTextureManager(), 256, 512, TextureOptions.NEAREST);
		mRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlas, mAct.getInstance(), "paddle.png", 2, 1);
		gameInterface = mAct;
		try {
			mAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			mAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}

	}
	
	public void onOwnUpdate(){
		final float nowY = getY();
		
		if(nowY == currentPathToY)
			return;
		
		if(nowY < currentPathToY){
			transform(getX(), nowY + stepHeight);
		}
		else{
			transform(getX(), nowY - stepHeight);
		}
	}

	


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
