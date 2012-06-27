package com.headdetect.games.superpong.Render;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.badlogic.gdx.math.Vector2;

public class StarRenderer {
	public static int NUM_STARS = 50;
	private static Random rng = new Random();
	private static final Paint p = new Paint();

	static {
		p.setColor(Color.parseColor("#e58bff"));
	}
	
	
	private class Star {
		public float x;
		public float y;
		public float speed;
		public boolean dead;

		public Star(float x, float y, float speed) {
			this.x = x;
			this.y = y;
			this.speed = speed;
		}
	}

	private float angle;
	private ArrayList<Star> stars;

	public StarRenderer() {
		stars = new ArrayList<Star>();
		
		for(int i = 0; i < NUM_STARS; i++){
			stars.add(new Star(rng.nextInt(800), rng.nextInt(480), rng.nextFloat() * 2 + 1));
		}
	}


	public void update(Canvas c) {
		// angle += rng.nextFloat() * 0.0001;
		if (stars.size() < NUM_STARS) {
			stars.add(new Star(rng.nextInt(100) * -1, rng.nextInt(c.getHeight()), rng.nextFloat() * 2 + 1));
		}
		for (int i = 0; i < stars.size(); i++) {
			Star s = stars.get(i);
			if (s.x > c.getWidth() || s.y > c.getHeight()) {
				s.dead = true;
			}
			com.badlogic.gdx.math.Vector2 mv = Vector2.fromAngle(angle).mul(s.speed);
			s.x += mv.x;
			s.y += mv.y;
			if (s.dead){
				stars.remove(i--);
				continue;
			}
			
			c.drawPoint(s.x, s.y, p);
			c.drawPoint(s.x - 1, s.y, p);
			c.drawPoint(s.x, s.y - 1, p);
			c.drawPoint(s.x - 1, s.y - 1, p);
		}
	}



}
