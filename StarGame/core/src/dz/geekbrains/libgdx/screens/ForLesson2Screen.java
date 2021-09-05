package dz.geekbrains.libgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ForLesson2Screen extends BaseScreen {
    private SpriteBatch spriteBatch;
    private Texture texShip;
    private Vector2 posShip; //Позиция корабля
    private Vector2 velShip; //Вектор скорости
    private Vector2 posTarget; //Место, куда надо лететь
    private Vector2 posTouch; //Место тыка мышью
    private Vector2 posDist; //Линия полёта (будем измерять её длину)
    private boolean tempBool = false;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        posTarget.set (screenX, Gdx.graphics.getHeight() - screenY);
        System.out.println("posTarget (" + posTarget.x + ", " + posTarget.y + ")");
        posTouch.set (posTarget);
        System.out.println("posShip (" + posShip.x + ", " + posShip.y + ")");
        posTouch.sub (posShip);
        System.out.println("posTouch (" + posTouch.x + ", " + posTouch.y + ")");
        velShip.set (posTouch);
        System.out.println("velShip (" + velShip.x + ", " + velShip.y + ")");
        velShip.nor();
        System.out.println("velShip [nor] (" + velShip.x + ", " + velShip.y + ")");
        //Любое перемещение разбивается на 100 частей
        //Большое расстояние летим быстрее, маленькое - медленнее
        velShip.scl (posTouch.len() / 100);
        System.out.println("velShip [scl] (" + velShip.x + ", " + velShip.y + ")");
        tempBool = true;
        return false;
    }

    @Override
    public void show() {
        super.show();
        spriteBatch = new SpriteBatch();
        texShip = new Texture("Ship_.png");
        posShip = new Vector2(50, 50);
        velShip = new Vector2(0, 0);
        posTarget = new Vector2(0, 0);
        posTouch = new Vector2(0, 0);
        posDist = new Vector2(0, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        //Находим вектор между кораблём и целевой точкой
        posDist.set(posTarget);
        posDist.sub(posShip);

        if (posDist.len2 () < 0.1f) { //Если квадрат длины меньше 0.1, то останавливаем корабль
            velShip.set (0, 0);
            if (tempBool) {
                System.out.println("posTarget (" + posTarget.x + ", " + posTarget.y + ")");
                System.out.println("posShip (" + posShip.x + ", " + posShip.y + ")");
                tempBool = false;
            }
        }
        posShip.add (velShip);

        spriteBatch.begin();
        spriteBatch.draw(texShip, posShip.x, posShip.y);
        spriteBatch.end();
    }


}
