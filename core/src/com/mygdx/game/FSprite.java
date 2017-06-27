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
    protected int widthTela;
    protected int heightTela;

    protected Sound sound;


    protected boolean destruido = false;

    public boolean isDestruido() {
        return destruido;
    }

    public void setDestruido(boolean destruido) {
        this.destruido = destruido;
    }

    public FSprite(int widthTela, int heightTela){
        this.widthTela = widthTela;
        this.heightTela = heightTela;
    }

    public abstract void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float delta);

    public float velocidadeAnim(){
        return 0.1f;
    }

    public void playSound(float volume){  //Define o som a ser executado durante o jogo
        if(this.sound != null){           //Verifica se o som não esta nulo
            this.sound.play(volume);      //Começa a executar o som com o volume definido atraves de uma variavel
        }
    }
}
