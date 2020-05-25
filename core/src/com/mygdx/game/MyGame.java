package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGame implements ApplicationListener {
	private SpriteBatch batch;
	private Texture textura_jugador, textura_enemigo, textura_vida, textura_fondo;
	private Sprite sprite_fondo;
	private BitmapFont contador_puntos,contador_vidas,rotulo_puntuacion,game_over,nivel_pantalla;
	private int velocidad_nave = 200;
	private int altura;
	private Rectangle rectangulo_jugador, rectangulo_cpu_enemigo, rectangulo_vida;
	private long numero_enemigos,numero_vidas;
	private float velocidad_enemigo = 300.0f;
	private float velocidad_vida = 200.0f;
	public Music coger_vida,golpe_enemigo,fondo_escenario;
	int vidas = 3;
	int puntos = 0;
	int nivel = 0;
	float tamanio_fuente_puntuacion = 3.0f;
	float tamanio_fuente_over = 4.0f;

	@Override
	public void create () {
		rectangulo_jugador = new Rectangle();
		rectangulo_jugador.width = 128;
		rectangulo_jugador.height = 64;
		rectangulo_jugador.x = 150;
		rectangulo_jugador.y = 600;
		rectangulo_cpu_enemigo = new Rectangle();
		rectangulo_cpu_enemigo.width = 128;
		rectangulo_cpu_enemigo.height = 64;
		rectangulo_cpu_enemigo.x = 1280;
		rectangulo_cpu_enemigo.y = MathUtils.random(0,800 - 64);
		rectangulo_vida = new Rectangle();
		rectangulo_vida.width = 128;
		rectangulo_vida.height = 64;
		rectangulo_vida.x = 1280;
		rectangulo_vida.y = MathUtils.random(0,800 - 64);
		altura = Gdx.graphics.getHeight();
		contador_puntos = new BitmapFont();
		contador_vidas = new BitmapFont();
		rotulo_puntuacion = new BitmapFont();
		nivel_pantalla = new BitmapFont();
		game_over = new BitmapFont();
		batch = new SpriteBatch();
		textura_jugador = new Texture(Gdx.files.internal("data/nave.png"));
		textura_vida = new Texture(Gdx.files.internal("data/moneda.png"));
		textura_enemigo = new Texture(Gdx.files.internal("data/enemigo.png"));
		textura_fondo = new Texture(Gdx.files.internal("data/fondo.png"));
		coger_vida = Gdx.audio.newMusic(Gdx.files.internal("data/moneda.wav"));
		golpe_enemigo = Gdx.audio.newMusic(Gdx.files.internal("data/golpe_enemigo.wav"));
		fondo_escenario = Gdx.audio.newMusic(Gdx.files.internal("data/fondo_escenario.wav"));
		fondo_escenario.setLooping(true);
		fondo_escenario.play();
		int w;
		int h;
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		sprite_fondo = new Sprite(textura_fondo,0,0, w,h);
		sprite_fondo.setPosition(0,0);
	}

	@Override
	public void render () {
		pintarJuego();
		controlesJugador();
		colisionYPuntos();

		if(TimeUtils.nanoTime() - numero_enemigos > 10000000) {
			crearEnemigos();
		}
		if(TimeUtils.nanoTime() - numero_vidas > 10000000) {
			crearVida();
		}
	}


	@Override
	public void dispose () {
		batch.dispose();
		textura_jugador.dispose();
		textura_enemigo.dispose();
		textura_vida.dispose();
		textura_fondo.dispose();
	}

	private void colisionYPuntos() {
		if(rectangulo_jugador.overlaps(rectangulo_cpu_enemigo)) {
			vidas = vidas - 1;
			golpe_enemigo.play();
			rectangulo_cpu_enemigo.x = 1280;
			rectangulo_cpu_enemigo.y = MathUtils.random(0,800 - 64);
		}
		if(rectangulo_jugador.overlaps(rectangulo_vida)) {
			if((puntos == 10)) {
				vidas = vidas + 1;
				nivel = nivel + 1;
				velocidad_enemigo = 500.0f;
				velocidad_vida = 300.0f;

			}else if((puntos == 20)) {
				vidas = vidas + 1;
				nivel = nivel + 1;
				velocidad_enemigo = 800.0f;
				velocidad_vida = 400.0f;
			}else if((puntos == 30)) {
				vidas = vidas + 1;
				nivel = nivel + 1;
				velocidad_enemigo = 1000.0f;
				velocidad_vida = 500.0f;
			}else if((puntos == 40)) {
				vidas = vidas + 1;
				nivel = nivel + 1;
				velocidad_enemigo = 1200.0f;
				velocidad_vida = 600.0f;
			}
			puntos = puntos + 2;
			coger_vida.play();
			rectangulo_vida.x = 1280;
			rectangulo_vida.y = MathUtils.random(0,800);
		}
	}

	private void crearEnemigos() {
		float tiempo = Gdx.graphics.getDeltaTime();
		rectangulo_cpu_enemigo.x = rectangulo_cpu_enemigo.x - velocidad_enemigo * tiempo;
		if(rectangulo_cpu_enemigo.x < -128) {
			rectangulo_cpu_enemigo.x = 1280;
			rectangulo_cpu_enemigo.y = MathUtils.random(64, 800);
			numero_enemigos = TimeUtils.nanoTime();
		}
		if(rectangulo_cpu_enemigo.y > 800 - 64) {
			rectangulo_cpu_enemigo.y = 800 - 64;
		}

		if(rectangulo_cpu_enemigo.y < 0) {
			rectangulo_cpu_enemigo.y = 0;
		}
	}

	private void crearVida() {
		float tiempo = Gdx.graphics.getDeltaTime();
		rectangulo_vida.x = rectangulo_vida.x - velocidad_vida * tiempo;
		if(rectangulo_vida.x < -128) {
			rectangulo_vida.x = 1280;
			rectangulo_vida.y = MathUtils.random(64, 800 - 64);
			numero_enemigos = TimeUtils.nanoTime();
		}

		if(rectangulo_vida.y > 800 - 64) {
			rectangulo_vida.y = 800 - 64;
		}

		if(rectangulo_vida.y < 0) {
			rectangulo_vida.y = 0;
		}
	}

	private void controlesJugador() {
		boolean tocar_pantalla = Gdx.input.isTouched();
		float nave_x = rectangulo_jugador.getX();
		float nave_y = rectangulo_jugador.getY();
		float tiempo = Gdx.graphics.getDeltaTime();

		if(tocar_pantalla) {
			if(rectangulo_jugador.y > 800 - 64) {
				nave_y = 800 - 64;
			}else {
				nave_y = nave_y + velocidad_nave * tiempo;
			}
		}else {
			if(rectangulo_jugador.y < 0) {
				nave_y = 0;
			}else {
				nave_y = nave_y - velocidad_nave * tiempo;
			}
		}
		rectangulo_jugador.set(nave_x, nave_y, 256, 64);
	}

	private void pintarJuego() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite_fondo.draw(batch);

		if(vidas >= 0 && puntos < 60) {
			batch.draw(textura_jugador, rectangulo_jugador.x, rectangulo_jugador.y);
			batch.draw(textura_enemigo, rectangulo_cpu_enemigo.x, rectangulo_cpu_enemigo.y);
			batch.draw(textura_vida, rectangulo_vida.x, rectangulo_vida.y);
			nivel_pantalla.setColor(Color.GREEN);
			nivel_pantalla.getData().setScale(tamanio_fuente_puntuacion);
			nivel_pantalla.draw(batch, "Nivel: " + nivel, 900,altura -140);
			contador_vidas.setColor(Color.YELLOW);
			contador_vidas.getData().setScale(tamanio_fuente_puntuacion);
			contador_vidas.draw(batch, "Vidas restantes: " + vidas, 900, altura - 80);
			contador_puntos.setColor(Color.BLUE);
			contador_puntos.getData().setScale(tamanio_fuente_puntuacion);
			contador_puntos.draw(batch, "Puntuación: " + puntos, 900, altura - 20);

		}else if(puntos == 60) {
			batch.draw(textura_enemigo, rectangulo_cpu_enemigo.x, rectangulo_cpu_enemigo.y);
			batch.draw(textura_vida, rectangulo_vida.x, rectangulo_vida.y);
			game_over.setColor(Color.RED);
			game_over.getData().setScale(tamanio_fuente_over);
			game_over.draw(batch, "Final de la partida", 300,700);
			rotulo_puntuacion.setColor(Color.BLACK);
			rotulo_puntuacion.getData().setScale(tamanio_fuente_over);
			rotulo_puntuacion.draw(batch, "Puntuación final: " + ((vidas * 10) + puntos) + " ptos", 300,600);
		}else {
			batch.draw(textura_enemigo, rectangulo_cpu_enemigo.x, rectangulo_cpu_enemigo.y);
			batch.draw(textura_vida, rectangulo_vida.x, rectangulo_vida.y);
			game_over.setColor(Color.RED);
			game_over.getData().setScale(tamanio_fuente_over);
			game_over.draw(batch, "Final de la partida", 300,700);
			rotulo_puntuacion.setColor(Color.BLACK);
			rotulo_puntuacion.getData().setScale(tamanio_fuente_over);
			rotulo_puntuacion.draw(batch, "Puntuación final: " + puntos + " ptos", 300,600);
		}
		batch.end();
	}


	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

}
