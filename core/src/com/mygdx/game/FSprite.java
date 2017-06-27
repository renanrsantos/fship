/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author renan
 */
public abstract class FSprite extends Sprite{
    protected final boolean useShapeRenderer = true;
    
    public float time;
    protected int widthTela; //LArgura da tela
    protected int heightTela;  //altura da tela
    
    protected Sound sound;  //Som
    
    
    protected boolean destruido = false;  //Seta destruido como Falso

    public boolean isDestruido() { 
        return destruido;
    }

    public void setDestruido(boolean destruido) {
        this.destruido = destruido;
    }
    
    public FSprite(int widthTela, int heightTela){ //Recebe largura e altura da tela
        this.widthTela = widthTela;
        this.heightTela = heightTela;
    }
    
    public abstract void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float delta); //Metodo que desenha na tela
    
    public float velocidadeAnim(){ //Velocidade da animação
        return 0.1f;
    }
    
    public void playSound(float volume){ //Metodo que administra o volume do som
        if(this.sound != null){
            this.sound.play(volume);
        }
    }
}
