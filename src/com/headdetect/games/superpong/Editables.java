package com.headdetect.games.superpong;

import java.io.Serializable;

public class Editables implements Serializable {
	
	//TODO: change colors
	//TODO: change bg
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	/*Pointless numba?*/
	private static final long serialVersionUID = 5728183060777345551L;

	private float stepHeight;
	
	private boolean enableParticles;
	
	private int difficulty;
	
	private int musicVol;
	
	private int soundVol;
	
	private boolean enableVibration;
	
	private int maxNumberOfBalls;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Editables() {
		setStepHeight(2.5f);
		setEnableParticles(false);
		setDifficulty(1);
		setMusicVolume(100);
		setSoundVolume(100);
		setEnableVibration(true);
		setMaxNumberOfBalls(2);
	}



	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getDifficulty() {
		return difficulty;
	}


	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}


	public boolean isEnableParticles() {
		return enableParticles;
	}


	public void setEnableParticles(boolean enableParticles) {
		this.enableParticles = enableParticles;
	}


	public float getStepHeight() {
		return stepHeight;
	}


	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}



	public int getMusicVolume() {
		return musicVol;
	}



	public void setMusicVolume(int musicVol) {
		this.musicVol = musicVol;
	}



	public int getSoundVolume() {
		return soundVol;
	}



	public void setSoundVolume(int soundVol) {
		this.soundVol = soundVol;
	}



	public boolean isEnableVibration() {
		return enableVibration;
	}



	public void setEnableVibration(boolean enableVibration) {
		this.enableVibration = enableVibration;
	}



	public int getMaxNumberOfBalls() {
		return maxNumberOfBalls;
	}



	public void setMaxNumberOfBalls(int maxNumberOfBalls) {
		this.maxNumberOfBalls = maxNumberOfBalls;
	}

	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
