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
    
    int width; // Variavel da LArgura
    int height;  // Variavel da Altura
    int srcy;
    float time;  // Variavel de Tempo
    float[] timeAntMeteor;  
    List<Meteor> meteors;  //Lista de Meteoros  
    List<Missile> missiles;  // Lista de Misseis
    
    Ship p1; // Define a nave (Player1)
    Sprite background;  //
    Texture backgroundTexture;

    InputProcessor key;
    
    @Override
    public void create () {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        p1 = new Ship(width, height);  //Cria uma nova nave com os tamanhos
        
        backgroundTexture = new Texture("background.jpg");  // Chama a Imagem do background
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat); //Imagem se repetira
        background = new Sprite(backgroundTexture);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        
        time = 0f;
        float deltaTime = Gdx.graphics.getDeltaTime();
        timeAntMeteor = new float[] {deltaTime,deltaTime,deltaTime};
        
        
        meteors = new ArrayList<Meteor>();  //Cria um array da lista de meteoros
        missiles = new ArrayList<Missile>();  //Cria um array da lista de Misseis
        
        p1.missiles = missiles; // Os misseis da Nave
    }

    @Override
    public void render () { 
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        time += Gdx.graphics.getDeltaTime();
        
        batch.begin();  // Inicia o jogo 
        drawBackground(); // Carrega o BackGround
            
        if(!p1.isDestruido()){ // Validação se a nave não foi destruida 
            for(int i=1; i <= 3; i++){
                instanciaMeteor(i); // Cria mais meteoros
            }
            setEstadoJogador();
            drawShip(); //Desenha o Avião
            drawMeteors();   //Desenha os meteoros
            drawMissiles();  //Desenha os misseis
        }
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose(); // encerra o jogo 
        p1.dispose();  // destroi a nave
    }
        
    public int getVelocidade(){
        return 3; // Seta a velocidade do background
    }
    
    public void drawBackground(){ //define como o background ira ser executado
        batch.draw(backgroundTexture,0,0,0,srcy,width,height);
        srcy -= getVelocidade();
    }
    
    public void setEstadoJogador(){   //Metodo que verifica as teclas que o Jogador esta apertando
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
    
    public void instanciaMeteor(int id){  // Metodo para instanciar os meteoros
        switch(id){
            case 1:
                if(time - timeAntMeteor[0] > 3){ // verifica o tempo do meteoro anterior
                    meteors.add(new Meteor(width, height,1)); //novo meteoro
                    timeAntMeteor[0] = time; //zera o tempo do meteoro anterior
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
        p1.draw(batch, shapeRenderer, time);   // Metodo que inicia e renderiza a nave
    }
    
    public void drawMissiles(){    
        Iterator<Missile> itr = missiles.iterator();
        while(itr.hasNext()) {
            Missile m = itr.next();
            m.draw(batch, shapeRenderer, time);            
            if(m.isDestruido()){
                itr.remove();  // Remove os Missies destruidos
            }
        }
    
    }
    
    public void drawMeteors(){   // Metodo que implementa os meteoros
        Iterator<Meteor> itr = meteors.iterator();
        while(itr.hasNext()) {
            Meteor m = itr.next();
            m.draw(batch, shapeRenderer, time);

            boolean collMissiles = m.collides(missiles);  //Variavel se o missil colidiu ou não
            boolean collShip = m.collides(p1); //Variavel se a nave colidiu no meteoro ou não
            if(collMissiles || collShip){ //
                m.setDestruido(true);//seta meteoro como destruido
                p1.incScore(m);
                if(collShip){ //Validação se a nave colidiu no meteoro
                    p1.decLife(m); //perde vida
                }
            }
            
            if(m.isDestruido()){ // se o meteoro for destruido
                itr.remove();  // remove os meteoros destruidos
            }
        }
    }
}
