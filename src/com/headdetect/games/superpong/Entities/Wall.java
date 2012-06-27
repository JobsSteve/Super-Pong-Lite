/*
 * 
 */
package com.headdetect.games.superpong.Entities;

import static com.headdetect.games.superpong.Constants.CAMERA_HEIGHT;
import static com.headdetect.games.superpong.Constants.WALL_FIXTURE;
import static com.headdetect.games.superpong.Constants.WALL_HEIGHT;
import static com.headdetect.games.superpong.Constants.WALL_WIDTH;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
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
import com.headdetect.games.superpong.Activities.IGameInterface;

public class Wall extends AnimatedSprite {

	private static TiledTextureRegion mRegion;
	private static BuildableBitmapTextureAtlas mAtlas;
	private static IGameInterface gameInterface;

	private Body mBody;
	private IAreaShape mBodyShape;

	private Wall(boolean isOnBottom) {
		super(0, isOnBottom ? CAMERA_HEIGHT - WALL_HEIGHT : 0, WALL_WIDTH, WALL_HEIGHT, mRegion, gameInterface.getVertexBufferObjectManager());
		mBodyShape = new Rectangle(0, isOnBottom ? CAMERA_HEIGHT - 1 : 1, WALL_WIDTH, 1, gameInterface.getVertexBufferObjectManager());
		setBody(PhysicsFactory.createBoxBody(gameInterface.getPhysicsWorld(), mBodyShape, BodyType.StaticBody, WALL_FIXTURE));
	}

	public static Wall create(boolean isOnBottom) {
		if (gameInterface == null)
			throw new NullPointerException("Wall.prepare() must be called first");

		Wall w = new Wall(isOnBottom);
		w.transform(w.mBodyShape.getX(), w.mBodyShape.getY(), isOnBottom ? 0 : 180);
		return w;
	}

	public void transform(float x, float y, float rot) {
		final Vector2 mVec = mBody.getTransform().getPosition();
		mVec.set(mVec.x, (y + getHeight() / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		setRotation(rot);
		mBody.setTransform(mVec, (float) (rot * (Math.PI / 180)));
	}

	public void setGlowing() {
		this.animate(35, false, null);
	}

	public Body getBody() {
		return mBody;
	}

	public void setBody(Body mBody) {
		this.mBody = mBody;
	}

	public static void prepare(IGameInterface handle) {

		mAtlas = new BuildableBitmapTextureAtlas(handle.getTextureManager(), 1024, 320, TextureOptions.NEAREST);
		mRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlas, handle.getInstance(), "wall.png", 1, 5);
		gameInterface = handle;

		try {
			mAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			mAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}

	}

}
