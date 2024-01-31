package com.lukagrahor.jumpbeat.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lukagrahor.jumpbeat.Items.InteractiveTileObject;
import com.lukagrahor.jumpbeat.Items.StaticItems;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Sprites.Checkpoint;
import com.lukagrahor.jumpbeat.Sprites.Chest;
import com.lukagrahor.jumpbeat.Sprites.Coin;
import com.lukagrahor.jumpbeat.Sprites.Enemy;
import com.lukagrahor.jumpbeat.Sprites.Hunter;
import com.lukagrahor.jumpbeat.Sprites.StaticOrb;
import com.lukagrahor.jumpbeat.Sprites.Trampoline;

public class WorldContactListener implements ContactListener { //To je poklicano ko se 2 stvari v box2d dotikajo

    @Override
    public void beginContact(Contact contact) {//Ob zacetku dotika
        // Dolocimo kateri fixture bomo zaznavali
        Fixture fixA=contact.getFixtureA();
        Fixture fixB=contact.getFixtureB();
        if(fixA.getUserData()=="noge"||fixA.getUserData()=="noge"){//Najprej ne vemo če se dotikamo z glavo drugega objekta
            Fixture noge = fixA.getUserData()=="noge" ? fixA : fixB;
            Fixture object1 = noge == fixA ? fixB : fixA;//objekt katerega glava igralca dotika
            if(object1.getUserData()!=null&& InteractiveTileObject.class.isAssignableFrom(object1.getUserData().getClass())){//preveri če je objekt interactiveTileObject
                ((InteractiveTileObject) object1.getUserData()).onFeetHit();
            }
        }

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            //ko se igralec dotika glave nasprotnika
            case JumpBeatClass.ENEMY_HEAD_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case JumpBeatClass.ENEMY_BIT | JumpBeatClass.FLOATING_BLOCK_BIT:
            case JumpBeatClass.ENEMY_BIT | JumpBeatClass.SPIKE_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case JumpBeatClass.JUMPY_BIT | JumpBeatClass.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Hunter) fixA.getUserData()).hit();
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                else {
                    ((Hunter) fixB.getUserData()).hit();
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                }
                break;
            case JumpBeatClass.JUMPY_BIT | JumpBeatClass.SPIKE_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Hunter) fixA.getUserData()).hit();
                }
                else {
                    ((Hunter) fixB.getUserData()).hit();
                }
                break;
            case JumpBeatClass.JUMPY_BIT | JumpBeatClass.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Hunter) fixA.getUserData()).groundHit();
                }
                else {
                    ((Hunter) fixB.getUserData()).groundHit();
                }
                break;
            case JumpBeatClass.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case JumpBeatClass.ORB_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.ORB_BIT) {
                    ((StaticItems) fixA.getUserData()).hit();
                    ((StaticOrb) fixA.getUserData()).use();
                }
                else {
                    ((StaticItems) fixB.getUserData()).hit();
                    ((StaticOrb) fixB.getUserData()).use();
                }
                break;
            case JumpBeatClass.TRAMPOLINE_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Hunter) fixA.getUserData()).hit();
                    ((Trampoline) fixB.getUserData()).hit();
                }
                else {
                    ((Hunter) fixB.getUserData()).hit();
                    ((Trampoline) fixA.getUserData()).hit();
                }
                break;
            case JumpBeatClass.TRAMPOLINE_SURFACE_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Trampoline) fixB.getUserData()).hit();
                }
                else {
                    ((Trampoline) fixA.getUserData()).hit();
                }
                break;
            case JumpBeatClass.JUMPY_HEAD_BIT|JumpBeatClass.FLOATING_BLOCK_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_HEAD_BIT) {
                    ((Hunter) fixA.getUserData()).headHit();
                }
                else {
                    ((Hunter) fixB.getUserData()).headHit();
                }
                break;
            case JumpBeatClass.JUMPY_RIGHT_SIDE_BIT|JumpBeatClass.FLOATING_BLOCK_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_RIGHT_SIDE_BIT) {
                    ((Hunter) fixA.getUserData()).rightSideHit();
                }
                else {
                    ((Hunter) fixB.getUserData()).rightSideHit();
                }
                break;
            case JumpBeatClass.JUMPY_LEFT_SIDE_BIT|JumpBeatClass.FLOATING_BLOCK_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_LEFT_SIDE_BIT) {
                    //Gdx.app.log("floating block","left side hit");
                    ((Hunter) fixA.getUserData()).leftSideHit();
                }
                else {
                    //Gdx.app.log("floating block","right side hit");
                    ((Hunter) fixB.getUserData()).leftSideHit();
                }
                break;
            case JumpBeatClass.COIN_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Coin) fixB.getUserData()).hit();
                }
                else {
                    ((Coin) fixA.getUserData()).hit();
                }
                break;
            case JumpBeatClass.CHEST_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Chest) fixB.getUserData()).hit();
                }
                else {
                    ((Chest) fixA.getUserData()).hit();
                }
                break;
            case JumpBeatClass.CHECKPOINT_BIT|JumpBeatClass.JUMPY_BIT:
                if(fixA.getFilterData().categoryBits == JumpBeatClass.JUMPY_BIT) {
                    ((Checkpoint) fixB.getUserData()).hit();
                    ((Checkpoint) fixB.getUserData()).position();
                }
                else {
                    ((Checkpoint) fixA.getUserData()).hit();
                    ((Checkpoint) fixA.getUserData()).position();
                }
                break;
        }
    }
    @Override
    public void endContact(Contact contact) {//Ko se ne dotikajo vec

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {//rezultati dotika

    }
}
