package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FShip extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    
    int width;
    int height;
    int srcy;
    float time;
    float[] timeAntMeteor;
    List<Meteor> meteors;
    List<Missile> missiles;
    
    Ship p1;
    Sprite background;
    Texture backgroundTexture;

    InputProcessor key;
    
    @Override
    public void create () {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        p1 = new Ship(width, height);
        
        backgroundTexture = new Texture("background.jpg");
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(backgroundTexture);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        
        time = 0f;
        float deltaTime = Gdx.graphics.getDeltaTime();
        timeAntMeteor = new float[] {deltaTime,deltaTime,deltaTime};
        
        
        meteors = new ArrayList<Meteor>();
        missiles = new ArrayList<Missile>();
        
        p1.missiles = missiles;
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        time += Gdx.graphics.getDeltaTime();
        
        batch.begin();
        drawBackground();
            
        if(!p1.isDestruido()){
            for(int i=1; i <= 3; i++){
                instanciaMeteor(i);
            }
            setEstadoJogador();
            drawShip();
            drawMeteors();   
            drawMissiles();
        }
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        p1.dispose();
    }
        
    public int getVelocidade(){
        return 3;
    }
    
    public void drawBackground(){
        batch.draw(backgroundTexture,0,0,0,srcy,width,height);
        srcy -= getVelocidade();
    }
    
    public void setEstadoJogador(){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || 
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                p1.setMovendoEsquerda(true);
            } 
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                p1.setMovendoDireita(true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                p1.setAtirando(p1.permiteAtirar());
            }            
        } else {
            p1.setParado(true);
        }
    }
    
    public void instanciaMeteor(int id){
        switch(id){
            case 1:
                if(time - timeAntMeteor[0] > 3){
                    meteors.add(new Meteor(width, height,1));
                    timeAntMeteor[0] = time;
                }
                break;
            case 2:
                if(time - timeAntMeteor[1] > 5){
                    meteors.add(new Meteor(width, height,2));
                    timeAntMeteor[1] = time;
                }
                break;
            case 3:
                if(time - timeAntMeteor[2] > 7){
                    meteors.add(new Meteor(width, height,3));
                    timeAntMeteor[2] = time;
                }
                break;
        }
    }
    
    public void drawShip(){
        p1.draw(batch, shapeRenderer, time);
    }
    
    public void drawMissiles(){
        Iterator<Missile> itr = missiles.iterator();
        while(itr.hasNext()) {
            Missile m = itr.next();
            m.draw(batch, shapeRenderer, time);            
            if(m.isDestruido()){
                itr.remove();
            }
        }
    
    }
    
    public void drawMeteors(){
        Iterator<Meteor> itr = meteors.iterator();
        while(itr.hasNext()) {
            Meteor m = itr.next();
            m.draw(batch, shapeRenderer, time);

            boolean collMissiles = m.collides(missiles);
            boolean collShip = m.collides(p1);
            if(collMissiles || collShip){
                m.setDestruido(true);
                p1.incScore(m);
                if(collShip){
                    p1.decLife(m);
                }
            }
            
            if(m.isDestruido()){
                itr.remove();
            }
        }
    }
}
