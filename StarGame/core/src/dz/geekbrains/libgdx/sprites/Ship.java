package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;

public class Ship extends BaseSprite {
    private final float HEIGHT = 0.15f;
    private final float PADDING = 0.01f;
    private final float VEL_SHIP = 0.003f;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private Vector2 posShip; //Позиция корабля
    private Vector2 velShip; //Вектор скорости
    private Vector2 posTarget; //Место, куда надо лететь
    private Vector2 posTouch; //Место тыка мышью
    private Vector2 posDist; //Линия полёта (будем измерять её длину)

    public Ship(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        init ();
    }

    private void init () {
        //Раскатываем двумерный массив в одномерный
        TextureRegion [][] tempRegion = this.regions[0].split(this.regions[0].getRegionWidth() / 2,
                this.regions[0].getRegionHeight());
        this.regions = new TextureRegion[tempRegion.length * tempRegion [0].length];
        for (int j = 0; j < tempRegion.length; j++) {
            for (int i = 0; i < tempRegion[0].length; i++) {
                this.regions[j * tempRegion [0].length + i] = tempRegion[j][i];
            }
        }

        //Создаем векторы
        posShip = new Vector2(0, 0);
        velShip = new Vector2(0, 0);
        posTarget = new Vector2(0, 0);
        posTouch = new Vector2(0, 0);
        posDist = new Vector2(0, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setLeft(0 - getHalfWidth());
        setBottom(worldBounds.getBottom() + PADDING);
        posShip.x = -getHalfWidth();
        posTarget.x = -getHalfWidth();
    }

    @Override
    public void update(float delta) {
        if (leftPressed) {
            if (Math.abs(worldBounds.getLeft() - posShip.x) > VEL_SHIP) {
                posTarget.x -= VEL_SHIP;
                velShip.x = -VEL_SHIP;
            } else {
                posTarget.x = worldBounds.getLeft();
                velShip.x = -Math.abs(posTarget.x - posShip.x);
            }
        }

        if (rightPressed) {
            if (Math.abs(worldBounds.getRight() - getRight()) > VEL_SHIP) {
                posTarget.x += VEL_SHIP;
                velShip.x = VEL_SHIP;
            } else {
                posTarget.x = Math.abs (worldBounds.getRight() - getWidth());
                System.out.println("worldBounds.getRight() = " + worldBounds.getRight() +
                        "getHalfWidth() = " + getHalfWidth());
                velShip.x = Math.abs(posTarget.x - posShip.x);
                System.out.println("posShip.x = " + posShip.x);
            }
        }

        if (Math.abs (posTarget.x - posShip.x) < 0.00001f) { //Если квадрат длины меньше 0.00001, то останавливаем корабль
            velShip.set (0, 0);
            posShip.x = posTarget.x;
            posShip.y = 0;
        }
        posShip.x+= velShip.x;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        posTarget.x = touch.x  - getHalfWidth();
        posTouch.set (posTarget);
        posTouch.sub (posShip);
        velShip.set (posTouch);
        velShip.nor();

        //Любое перемещение разбивается на 40 частей
        //Большое расстояние летим быстрее, маленькое - медленнее
        velShip.scl (posTouch.len() / 40);
        velShip.y = 0;
        System.out.println("velShip: " + velShip.x + "; " + velShip.y);
        return false;
    }

    @Override
    public boolean keyDown(int key) {
        //Input.Keys.LEFT;
        //Input.Keys.RIGHT;
        if (Input.Keys.LEFT == key) {
            leftPressed = true;
        }

        if (Input.Keys.RIGHT == key) {
            rightPressed = true;
        }

        System.out.println("posShip: " + posShip.x + "; " + posShip.y);
        System.out.println("left, right: " + getLeft() + "; " + getRight());
        return false;
    }

    @Override
    public boolean keyUp(int key) {
        if (Input.Keys.LEFT == key) {
            leftPressed = false;
        }
        if (Input.Keys.RIGHT == key) {
            rightPressed = false;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        //frame = 1;
        setLeft(posShip.x);
        super.draw(batch);
    }
}
