package com.pong.game;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    static public final int portTCP = 54533;
    static public final int portUDP = 54777;

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Rectangle.class);
        kryo.register(State.class);
        kryo.register(Login.class);
    }

    static public class State {
        public Rectangle player1;
        public Rectangle player2;
        public Rectangle ball;
        public long frame;
        public long id;
    }

}
