package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import java.util.List;

/**
 *
 * @author renan
 */
public class Ship extends FSprite{
    private final int porcentagemLateral = 8;
    
    public Animation<TextureRegion> shipAnim;
    
    private TextureRegion[] shipParado;
    private Animation<TextureRegion> shipParadoAnim;
    
    private TextureRegion[] shipMovendoEsquerda;
    private Animation<TextureRegion> shipMovendoEsquerdaAnim;
    
    private TextureRegion[] shipMovendoDireita;
    private Animation<TextureRegion> shipMovendoDireitaAnim;

    private boolean movendo;
    private boolean atirando;
    private boolean parado;
    
    private boolean movendoEsquerda;
    private boolean movendoDireita;
    
    private float timeTiroAnt;
    private float timeScore;
    
    public List<Missile> missiles;
    
    private final Rectangle collision;

    private int score;
    private int life;
    
    private final BitmapFont labelScore;
    private final BitmapFont labelLife;
    
    public Ship(int widthTela, int heightTela) {
        super(widthTela, heightTela);
        this.shipAnim = this.getShipParadoAnim();
               
        this.setX(this.widthTela/2-(this.widthTela*3/100));
        this.setY(this.heightTela-(this.heightTela*90/100));
        
        this.collision = new Rectangle(this.getX(),this.getY(),40,50);
        this.timeTiroAnt = Gdx.graphics.getDeltaTime();
        this.timeScore = this.timeTiroAnt;
        
        this.score = 0;
        this.life = 100;
        
        this.labelLife = new BitmapFont();
        this.labelLife.setColor(Color.GREEN);
        this.labelScore = new BitmapFont();
    }
    
    public boolean isMovendo() {
        return movendo;
    }

    public void setMovendo(boolean movendo) {
        this.movendo = movendo;
        
        if(this.movendo){
            this.atirando = false;
        } else {
            this.movendoDireita = false;
            this.movendoEsquerda = false;
        }
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }
    
    

    public boolean isAtirando() {
        return atirando;
    }

    public void setAtirando(boolean atirando) {
        this.atirando = false;
        if(atirando){
            this.atirando = this.permiteAtirar();
            missiles.add(this.atirar());
            this.setMovendo(false);
        }
    }

    public boolean isParado() {
        return parado;
    }

    public Rectangle getCollision() {
        return collision;
    }
    
    public void setParado(boolean parado) {
        this.parado = parado;
        if(this.parado){
            this.shipAnim = this.getShipParadoAnim();
            this.setMovendo(false);
            this.setAtirando(false);
        }
    }

    public boolean isMovendoEsquerda() {
        return movendoEsquerda;
    }

    public void setMovendoEsquerda(boolean movendoEsquerda) {
        this.setMovendo(movendoEsquerda);
        this.movendoEsquerda = movendoEsquerda;
        this.movendoDireita = this.movendoEsquerda ? false : this.movendoDireita;
        
        if(this.movendoEsquerda){
            this.shipAnim = this.getShipMovendoEsquerdaAnim();
        }
        
        float x = this.permiteMoverEsquerda() ? this.valorMovimento()*-1 : 0;
        if(x < 0){
            this.translateX(x);
        }
    }

    public boolean isMovendoDireita() {
        return movendoDireita;
    }

    public void setMovendoDireita(boolean movendoDireita) {
        this.setMovendo(movendoDireita);
        this.movendoDireita = movendoDireita;
        this.movendoEsquerda = this.movendoDireita ? false : this.movendoEsquerda;
        
        if(this.movendoDireita){
            this.shipAnim = this.getShipMovendoDireitaAnim();
        }
        
        float x = this.permiteMoverDireita() ? this.valorMovimento() : 0;
        if(x > 0){
            this.translateX(x);
        }   
    }

    void dispose() {
//        texture.dispose();
    }

    private int valorMovimento(){
        return 6;
    }
    
    private boolean permiteMover(){
        //implementar por tempo
        return true;
    }
    
    private boolean permiteMoverDireita(){
        return this.permiteMover() && this.getX() < (this.widthTela - (this.widthTela*this.porcentagemLateral/100));
    }
    
    private boolean permiteMoverEsquerda(){
        return this.permiteMover() && this.getX() > (this.widthTela - (this.widthTela*(100-this.porcentagemLateral)/100));
    }
    
    public boolean permiteAtirar(){
        if(time - timeTiroAnt > 0.5){
            timeTiroAnt = time;
            return true;
        }
        return false;
    }
    
    public final Animation<TextureRegion> getShipParadoAnim() {
        if(this.shipParadoAnim == null){
            this.shipParado = new TextureRegion[3];
            for(int i = 0; i < 3; i++){
                this.shipParado[i] = new TextureRegion( new Texture("ship/ship-centro00"+(i+1)+".png"));
            }
            this.shipParadoAnim = new Animation<TextureRegion>(this.velocidadeAnim(),this.shipParado);
        }
        return shipParadoAnim;
    }

    public Animation<TextureRegion> getShipMovendoEsquerdaAnim() {
        if(this.shipMovendoEsquerdaAnim == null){
            this.shipMovendoEsquerda = new TextureRegion[3];
            for(int i = 0; i < 3; i++){
                this.shipMovendoEsquerda[i] = new TextureRegion( new Texture("ship/ship-esquerda00"+(i+1)+".png"));
            }
            this.shipMovendoEsquerdaAnim = new Animation<TextureRegion>(this.velocidadeAnim(),this.shipMovendoEsquerda);
        }
        return shipMovendoEsquerdaAnim;
    }

    public Animation<TextureRegion> getShipMovendoDireitaAnim() {
        if(this.shipMovendoDireitaAnim == null){
            this.shipMovendoDireita = new TextureRegion[3];
            for(int i = 0; i < 3; i++){
                this.shipMovendoDireita[i] = new TextureRegion( new Texture("ship/ship-direita00"+(i+1)+".png"));
            }
            this.shipMovendoDireitaAnim = new Animation<TextureRegion>(this.velocidadeAnim(),this.shipMovendoDireita);
        }
        return shipMovendoDireitaAnim;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float delta) {
        this.time = delta;
        if(this.time - this.timeScore > 1){
            this.timeScore = this.time;
            this.incScore();
        }
        
        this.collision.set(this.getX(), this.getY(), this.collision.getWidth(), this.collision.getWidth());
        TextureRegion currentFrame = this.shipAnim.getKeyFrame(delta, true);
        batch.draw(currentFrame, this.getX(), this.getY());
        
        if(this.useShapeRenderer){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(this.collision.getX(), this.collision.getY(), this.collision.getWidth(), this.collision.getHeight());
            shapeRenderer.end();
        }
        
        if(this.life <= 0){
            this.setDestruido(true);
        } else {
            labelLife.draw(batch,Integer.toString(life),20,heightTela-20);
            labelScore.draw(batch,Integer.toString(score),widthTela -50, heightTela-20);
        }
    }
    
    public boolean collides(Meteor meteor){
        if (this.getX() < meteor.getX() + meteor.getWidth()) {
            if(Intersector.overlaps(meteor.getCollision(),this.collision)){
                return true;
            }
        }
        return false;
    }

    public Missile atirar() {
        this.atirando = false;
        return new Missile(this.widthTela,this.heightTela,
                this.collision.getX() + (this.collision.getWidth()/2),
                this.getY()+this.collision.getHeight());
    }

    public void decLife(Meteor collision){
        switch(collision.tag){
            case 1:
                this.life -= 10;
                break;
            case 2:
                this.life -= 20;
                break;
            case 3:
                this.life -= 50;
                break;
        }
        if(this.life > 70){
            this.labelLife.setColor(Color.GREEN);
        } else if (this.life > 50){
            this.labelLife.setColor(Color.YELLOW);
        } else if (this.life > 30){
            this.labelLife.setColor(Color.ORANGE);
        } else {
            this.labelLife.setColor(Color.RED);
        }
    }
    
    public void incScore(Meteor collision){
        switch(collision.tag){
            case 1:
                this.score += 25;
                break;
            case 2:
                this.score += 50;
                break;
            case 3:
                this.score += 100;
                break;
        }
    }
    
    public void incScore(){
        this.score += (this.score / 10) + 1;
    }
    
}
