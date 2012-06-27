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

import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import com.headdetect.games.superpong.Editables;

import android.content.res.AssetManager;

// TODO: Auto-generated Javadoc
/**
 * The Interface IGameInterface.
 */
public interface IGameInterface {

	/**
	 * Gets the texture.
	 *
	 * @param loc the loc
	 * @return the texture
	 */
	public IInputStreamOpener getTexture(final String loc);

	/**
	 * Lag.
	 *
	 * @param runnable the runnable
	 * @param time the time
	 */
	public void lag(final Runnable runnable, float time);

	/**
	 * Make glowsplosion.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void makeGlowsplosion(float x, float y);

	/**
	 * Gets the vertex buffer object manager.
	 *
	 * @return the vertex buffer object manager
	 */
	public VertexBufferObjectManager getVertexBufferObjectManager();

	/**
	 * Gets the texture manager.
	 *
	 * @return the texture manager
	 */
	public TextureManager getTextureManager();

	/**
	 * Gets the physics world.
	 *
	 * @return the physics world
	 */
	public PhysicsWorld getPhysicsWorld();

	/**
	 * Gets the single instance of IGameInterface.
	 *
	 * @return single instance of IGameInterface
	 */
	public BaseGameActivity getInstance();

	/**
	 * Removes the entity.
	 *
	 * @param mSprite the m sprite
	 */
	public void removeEntity(IEntity mSprite);

	
	/**
	 * Gets the assets.
	 *
	 * @return the assets
	 */
	public AssetManager getAssets();
	

	public Editables getSettings();
}
