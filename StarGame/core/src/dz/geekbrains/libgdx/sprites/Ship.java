package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;

public class Ship extends BaseSprite {
    private Vector2 posShip; //Позиция корабля
    private Vector2 velShip; //Вектор скорости
    private Vector2 posTarget; //Место, куда надо лететь
    private Vector2 posTouch; //Место тыка мышью
    private Vector2 posDist; //Линия полёта (будем измерять её длину)

    public Ship(TextureRegion region) {
        super(region);
        init ();
    }

    public Ship (Texture texture, float height) {
        super (new TextureRegion(texture));
        init ();
        setHeightProportion(height);

    }

    private void init () {
        posShip = new Vector2(0, 0);
        velShip = new Vector2(0, 0);
        posTarget = new Vector2(0, 0);
        posTouch = new Vector2(0, 0);
        posDist = new Vector2(0, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.pos.set(worldBounds.pos);
        setHeightProportion(getHeight());
    }

    @Override
    public void update(float delta) {
        //Находим вектор между кораблём и целевой точкой
        posDist.set(posTarget);
        posDist.sub(posShip);

        if (posDist.len2 () < 0.00001f) { //Если квадрат длины меньше 0.00001, то останавливаем корабль
            velShip.set (0, 0);
            posShip.set (posTarget);
        }
        posShip.add (velShip);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        posTarget.set (touch);
        posTouch.set (posTarget);
        posTouch.sub (posShip);
        velShip.set (posTouch);
        velShip.nor();

        //Любое перемещение разбивается на 30 частей
        //Большое расстояние летим быстрее, маленькое - медленнее
        velShip.scl (posTouch.len() / 30);
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        setLeft(posShip.x - getHalfWidth());
        setBottom(posShip.y - getHalfHeight());
        super.draw(batch);
    }
}
