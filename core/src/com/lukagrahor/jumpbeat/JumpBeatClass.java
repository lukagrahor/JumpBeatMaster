package com.lukagrahor.jumpbeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lukagrahor.jumpbeat.Screens.MenuScreen;

public class JumpBeatClass extends Game{
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 180;
	public static final float PPM = 100;//pixels per meter

	//to so biti za box2d filtre
	public static  final short NOTHING_BIT = 0;
	public static  final short GROUND_BIT = 1;
	public static  final short JUMPY_BIT = 2;
	public static  final short SPIKE_BIT = 4;
	public static  final short ORB_BIT = 8;
	public static  final short JUMPY_RIGHT_SIDE_BIT = 16;
	public static  final short CHECKPOINT_BIT = 32;
	public static  final short ENEMY_BIT = 64;
	public static  final short ENEMY_HEAD_BIT = 128;
	public static  final short FLOATING_BLOCK_BIT = 256;
	public static  final short JUMPY_HEAD_BIT = 512;
	public static  final short JUMPY_LEFT_SIDE_BIT = 1024;
	public static  final short TRAMPOLINE_BIT = 2048;
	public static  final short TRAMPOLINE_SURFACE_BIT = 4096;
	public static final short COIN_BIT=8192;
	public static final short CHEST_BIT=16384;


	public SpriteBatch batch;//je memory intensive zato se uporablja le ena
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this));
	}
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
