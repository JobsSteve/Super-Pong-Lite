/*
 * 
 */
package com.headdetect.games.superpong;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Constants {

	
	// ==================================================================
	// Constants
	// ==================================================================
	public static final int CAMERA_WIDTH 						= 800;
	public static final int CAMERA_HEIGHT						= 480;
	
	public static final int HALF_CAMERA_WIDTH 					= CAMERA_WIDTH / 2;
	public static final int HALF_CAMERA_HEIGHT 					= CAMERA_HEIGHT / 2;
	
	public static final int WALL_HEIGHT 						= 32;
	public static final int WALL_WIDTH 							= CAMERA_WIDTH;
	
	public static final int HALF_WALL_HEIGHT 					= WALL_HEIGHT / 2;
	public static final int HALF_WALL_WIDTH 					= WALL_WIDTH / 2;
	
	public static final int PLAYER_PADDING 						= 5;
	public static final int WALL_PADDING 						= WALL_HEIGHT / 2;
	
	public static final int PLAYER_WIDTH 						= 50;
	public static final int PLAYER_HEIGHT 						= 180;
	
	public static final int HALF_PLAYER_HEIGHT 					= PLAYER_HEIGHT / 2;
	public static final int HALF_PLAYER_WIDTH 					= PLAYER_WIDTH / 2;
	
	public static final int MIN_PLAYER_X 						= 3;
	public static final int MAX_PLAYER_X 						= CAMERA_WIDTH - 3;
	
	
	public static final int MIN_PLAYER_Y 						= WALL_PADDING;
	public static final int MAX_PLAYER_Y 						= CAMERA_HEIGHT - WALL_PADDING;
	
	public static final int BALL_RAD 							= 25;
	
	public static final int HALF_BALL_RAD 						= BALL_RAD / 2;
	
	public static final int GUI_PADDING 						= 8 + WALL_PADDING;
	public static final int TEXT_DISTANCE_FROM_CENTER			= 40;
	
	public static final int CHECK_HEIGHT 						= 8;

	public static final FixtureDef BALL_FIXURE 					= PhysicsFactory.createFixtureDef(1, 1f, 0);
	public static final FixtureDef WALL_FIXTURE 				= PhysicsFactory.createFixtureDef(1, 1f, 0);
	public static final FixtureDef PLAYER_FIXTURE 				= PhysicsFactory.createFixtureDef(1, 1.03f, 0);
	
}
