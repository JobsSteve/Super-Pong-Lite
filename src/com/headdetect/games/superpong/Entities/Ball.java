/*
 * 
 */
package com.headdetect.games.superpong.Entities;

import java.io.IOException;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.headdetect.games.superpong.Constants;
import com.headdetect.games.superpong.EntityStats;
import com.headdetect.games.superpong.Activities.IGameInterface;

import static com.headdetect.games.superpong.Constants.*;



public class Ball extends Sprite {

	private static ITexture mTexture;
	private static ITextureRegion mRegion;
	private static IGameInterface gameInterface;

	private EntityStats mStats;
	private Body mBody;

	private Ball(Vector2 pos) {
		super(pos.x, pos.y, BALL_RAD, BALL_RAD, mRegion, gameInterface.getVertexBufferObjectManager());

		mStats = new EntityStats();
		mBody = PhysicsFactory.createCircleBody(gameInterface.getPhysicsWorld(), getX(), getY(), BALL_RAD, BodyType.DynamicBody, Constants.BALL_FIXURE);
		mBody.setBullet(true);
		mBody.setAwake(true);
		gameInterface.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this, mBody, true, false));
	}

	public Body getBody() {
		return mBody;
	}

	public EntityStats getStats() {
		return mStats;
	}

	public void resetPos(){
		mBody.setLinearVelocity(new Vector2());
		mBody.resetMassData();
		transform(HALF_CAMERA_WIDTH - BALL_RAD / 2, HALF_CAMERA_HEIGHT - BALL_RAD / 2);
	}
	
	/**
	 * Transforms the balls position.
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


	public static Ball create(Vector2 pos){
		if(gameInterface == null)
			throw new NullPointerException("You must call Ball.prepare() before creating");
		
		return new Ball(pos);
	}
	
	public static void prepare(IGameInterface handle) {
		try {
			mTexture = new BitmapTexture(handle.getTextureManager(), handle.getTexture("other/ball.png"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		mRegion = TextureRegionFactory.extractFromTexture(mTexture);
		mTexture.load();
		
		gameInterface = handle;

	}

}
