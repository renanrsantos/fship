/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import java.util.List;
import java.util.Random;

/**
 *
 * @author renan
 */
public class Meteor extends FSprite{
    public int radius;
    public int tag;
    public float gravidade;
    TextureRegion meteor;
    
    private final Circle collision;
    
    public Meteor(int widthTela, int heightTela, int tag) {
        super(widthTela, heightTela);
        this.setTag(tag);
        Random rand = new Random();
        this.setX(rand.nextInt(widthTela));
        this.setY(heightTela - (heightTela*5/100));
        this.collision = new Circle(this.getX(), this.getY(), this.tag * this.radius);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("meteor/sound.wav"));
    }
    
    public final void setTag(int tag){
        this.tag = tag;
        this.meteor = new TextureRegion(new Texture("meteor/meteor"+tag+".png"));
        switch(this.tag){
            case 1:
                gravidade = 5f;
                radius = 13;
                break;
            case 2:
                gravidade = 4.5f;
                radius = 10;
                break;
            case 3:
                gravidade = 4f;
                radius = 7;
                break;
        }
    }
    
    public float getGravidade(){
        return gravidade;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float delta) {
        this.setY(this.getY() - this.getGravidade());
        this.collision.set(this.getX(), this.getY(), this.tag * this.radius);
        if (this.getY() <= 0){
            this.destruido = true;
        } else {
            batch.draw(this.meteor, this.getX(), this.getY());
            
            if(this.useShapeRenderer){
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.circle(this.collision.x, this.collision.y, this.collision.radius);
                shapeRenderer.end();                
            }
        }
    }

    public Circle getCollision() {
        return collision;
    }
    
    public boolean collides(Ship ship){
        float m = (this.collision.y - this.collision.radius - 10);
        float s = ship.getCollision().getY();
        if (m < s) {
            return Intersector.overlaps(this.collision,ship.getCollision());
        }
        return false;
    }
    
    public boolean collides(List<Missile> missiles){
        float m = (this.collision.y - this.collision.radius - 10);
        
        for (Missile missile : missiles) {
            float mi = missile.getCollision().getY();
            if (m < mi) {
                if(Intersector.overlaps(this.collision,missile.getCollision())){
                    missile.setDestruido(true);
                    return true;
                }
            }            
        }
        return false;
    }

    @Override
    public void setDestruido(boolean destruido) {
        super.setDestruido(destruido); 
        if(destruido){
            this.playSound(0.5f);
        }
    }
    
    
}
