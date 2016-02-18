package com.pong.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pong extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private Controller controller;
	private Client client;
	private Vector3 touchPos;
	private Rectangle ball;
	private ConcurrentLinkedQueue<Rectangle> gameQue;
	private Login login;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1080, 1920);
		touchPos = new Vector3();

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		gameQue = new ConcurrentLinkedQueue<Rectangle>();

		client = new Client();
		client.start();

		try {
			client.connect(5000, "192.168.1.83", Network.portTCP, Network.portUDP);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Network.register(client);

		client.addListener(new Listener.ThreadedListener(new Listener() {
			public void connected(Connection connection) {
				System.out.println("CONNECTED");
			}

			public void received(Connection connection, Object object) {
				if (object instanceof Rectangle) {
					ball = (Rectangle) object;

					//gameQue.add(ball);
					System.out.println(ball);
				}

				if (object instanceof Login) {
					login = (Login) object;
					System.out.println("Server Assigned ID: " + login.getId());
				}
			}

			public void disconnected(Connection connection) {
				System.exit(0);
			}
		}));

		touchPos = new Vector3();
		controller = new Controller(1080, 1920);
		login = new Login();
		login.setId((long) (Math.random() * 1000000000));
		System.out.println((long) (Math.random() * 1000000000));
		login.setName("Bryan " + Math.random() * 1000);
		client.sendTCP(login);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(Color.WHITE);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// draw stuff here
		Rectangle paddle = controller.getPaddle();
		shapeRenderer.rect(paddle.x, paddle.y, paddle.width, paddle.height);

		Rectangle ballRender = gameQue.poll();
		if (ballRender != null) {
			shapeRenderer.rect(ballRender.x, ballRender.y, ballRender.width, ballRender.height);
		}

		//shapeRenderer.setColor(Color.YELLOW);
		//Iterator<Rectangle> itter = gameQue.iterator();
		//while(itter.hasNext()) {
		//	Rectangle prev = itter.next();
		//	shapeRenderer.rect(prev.x, prev.y, prev.width, prev.height);
		//}
		shapeRenderer.setColor(Color.RED);
		if (ball != null) {
			shapeRenderer.rect(ball.x, ball.y, ball.width, ball.height);
		}

		shapeRenderer.end();

		if (Gdx.input.isTouched()) {
			for (int i = 0; i < 1; i++) {
				Login login = new Login();
				login.setId((long) (Math.random() * 1000000000));
				System.out.println((long) (Math.random() * 1000000000));
				login.setName("Bryan " + Math.random() * 1000);
				client.sendTCP(login);
			}
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			controller.movePaddle(touchPos.x, touchPos.y);
			client.sendUDP(controller.getPaddle());
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.position.set(width / 2, height / 2, 0);
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}