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
    List<Meteor> meteors;     //Cria uma lista para os meteoros
    List<Missile> missiles;   //Cria uma lista para os misseis

    Ship p1;
    Sprite background;
    Texture backgroundTexture;

    InputProcessor key;

    @Override
    public void create () {
        width = Gdx.graphics.getWidth();  
        height = Gdx.graphics.getHeight();
        p1 = new Ship(width, height);

        backgroundTexture = new Texture("background.jpg"); //Chama a imagem background.jpg para preencher o fundo do cenário
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat); //Define que esta imagem ira sempre se repetir
        background = new Sprite(backgroundTexture);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();


        time = 0f;
        float deltaTime = Gdx.graphics.getDeltaTime();
        timeAntMeteor = new float[] {deltaTime,deltaTime,deltaTime};


        meteors = new ArrayList<Meteor>();              //Array com a lista de meteoros
        missiles = new ArrayList<Missile>();            //Array com a lista de misseis

        p1.missiles = missiles;                         //Chama os misseis(Tiros) da nave
    }

    @Override
    public void render () {                             //Metódo para rendenizar os elementos na tela
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        time += Gdx.graphics.getDeltaTime();

        batch.begin();                                  //Inicia o "jogo"
        drawBackground();                               //Desenha o background

        if(!p1.isDestruido()){                          //Verifica se o "avião" esta destruido
            for(int i=1; i <= 3; i++){                  //Instancia o meteoro que sera desenhado na tela
                instanciaMeteor(i);
            }
            setEstadoJogador();                         //Define estado do jogador(Posição)
            drawShip();                                 //Desenha o avião
            drawMeteors();                              //Desenha os meteoros
            drawMissiles();                             //Desenha os misseis
        }
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        p1.dispose();
    }

    public int getVelocidade(){
        return 3;                                       //Variavel da velocidade do background
    }

    public void drawBackground(){                       //Neste método ira definir a velocidade que o background ira "rodar"
        batch.draw(backgroundTexture,0,0,0,srcy,width,height);
        srcy -= getVelocidade();
    }

    public void setEstadoJogador(){                     //Máquina de estados do jogador, neste metodo ira "escutar" as teclas pressionadas e então atribuir movimentos ao jogados, tanto para ele ir a frente, para trás, para direita ou esquerda.
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

    public void instanciaMeteor(int id){                //Lógica para instaciar meteoros
        switch(id){
            case 1:
                if(time - timeAntMeteor[0] > 3){        //Verifica o tempo para estar adicionando um novo meteoro na tela, caso ele for maior que 3, então ira adicionar um novo elemento a tela
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

    public void drawShip(){                             //Desenha a nave na tela
        p1.draw(batch, shapeRenderer, time);
    }

    public void drawMissiles(){                         //Lógica para desenhar os misseis na tela
        Iterator<Missile> itr = missiles.iterator();    //Inicia uma nova lista (objeto) de misseis
        while(itr.hasNext()) {
            Missile m = itr.next();
            m.draw(batch, shapeRenderer, time);         //Desenha um novo missel na tela
            if(m.isDestruido()){                        //Verifica se o missel foi destruido, caso sim ira remover da tela
                itr.remove();
            }
        }

    }

    public void drawMeteors(){                            //Metódo para desenhar os meteoros na tela
        Iterator<Meteor> itr = meteors.iterator();        //Inicia uma nova lista(objeto) de meteoros
        while(itr.hasNext()) {                            //Lógica para enquanto não bater nos meteoros gerar outros meteoros
            Meteor m = itr.next();                        //Inicializa outro meteoro
            m.draw(batch, shapeRenderer, time);           //Desenha o meteoro na tela

            boolean collMissiles = m.collides(missiles);  //Variavel booleana para verificar se o "missel" bateu no meteoro
            boolean collShip = m.collides(p1);            //Variavel booleana para verificar se o "avião" bateu no meteoro
            if(collMissiles || collShip){                 //Verifica se houve colisão para entrar no metódo
                m.setDestruido(true);                     //Define que o meteoro foi destruido
                p1.incScore(m);                           //Define Score
                if(collShip){                             //Lógica para diminuir a vida caso o avião bata no meteoro
                    p1.decLife(m);
                }
            }

            if(m.isDestruido()){                          //Caso o meteoro for destruido ira remover ele da "tela"
                itr.remove();
            }
        }
    }
}
