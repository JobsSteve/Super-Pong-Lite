/*
 * 
 */
package com.headdetect.games.superpong;

public class EntityStats {
	
	private int score;
	private boolean big;
	private boolean frozen;
	private boolean slowBall;
	
	
	public EntityStats(){
		score = 0;
		big = false;
		frozen = false;
		slowBall = false;
	}
	
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public boolean isSlowBall() {
		return slowBall;
	}

	public void setSlowBall(boolean slowBall) {
		this.slowBall = slowBall;
	}


	public boolean isBig() {
		return big;
	}


	public void setBig(boolean big) {
		this.big = big;
	}
	
	

}
