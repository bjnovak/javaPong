package com.pong.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Controller {

    private Rectangle paddle;
    private int width;
    private int height;

    public Controller(int width, int height) {
        this.width = width;
        this.height = height;
        paddle = new Rectangle(width/2 - 150, 100, 300, 30);
    }

    public void movePaddle(float touchX, float touchY) {
        if (Gdx.input.isTouched()) {
            paddle.x = touchX - paddle.width / 2;
            paddle.y = touchY - paddle.height / 2;

            if (paddle.x < 0) {
                paddle.x = 0;
            } else if (paddle.x + paddle.width > width) {
                paddle.x = width - paddle.width;
            }

            if (paddle.y < 100) {
                paddle.y = 100;
            } else if (paddle.y > height * 0.45) {
                paddle.y = height * (float) 0.45;
            }
        }
    }

    public Rectangle getPaddle() {
        return paddle;
    }

}
