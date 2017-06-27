/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author 38816
 */
public class Missile extends FSprite{

    private final float velocidade = 5; //Seta velocidade do Missil
    
    public Animation<TextureRegion> missileAnim; //Textura do Missil
    private final TextureRegion[] missile;
    
    private final Rectangle collision; //Retangulo de colisão
    
    public Missile(int widthTela, int heightTela, float x, float y) { //Seta a posição do missil na tela
        super(widthTela, heightTela);
        this.setX(x);   //posicao do missivel no eixo X
        this.setY(y);   //posicao do missivel no eixo Y
        
        this.missile = new TextureRegion[3];
        for(int i = 0; i < 3; i++){
            this.missile[i] = new TextureRegion( new Texture("missile/missile00"+(i+1)+".png"));   //Imagem do missil
        }
        this.missileAnim = new Animation<TextureRegion>(this.velocidadeAnim(),this.missile);
        
        this.collision = new Rectangle(this.getX(),this.getY(),5,15);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("missile/sound.wav"));   //Som do missil
        
        this.playSound(1);
    }

    public Rectangle getCollision() {
        return collision;
    }
    
    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float delta) {
        this.setY(this.getY() + this.velocidade);
        this.collision.set(this.getX(), this.getY(), this.collision.getWidth(),this.collision.getHeight());
        if (this.getY() >= this.heightTela){
            this.setDestruido(true);
        } else {
            TextureRegion currentFrame = this.missileAnim.getKeyFrame(delta, true);
            batch.draw(currentFrame, this.getX(), this.getY());
            
            if(this.useShapeRenderer){
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(this.collision.getX(), this.collision.getY(), this.collision.getWidth(), this.collision.getHeight());
                shapeRenderer.end();
            }

        }
    }
    
}
