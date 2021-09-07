package dz.geekbrains.libgdx.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.sprites.Background;
import dz.geekbrains.libgdx.sprites.Ship;

public class ForLesson3Screen extends BaseScreen {
    private Texture texShip;
    private Texture texBg;

    private Background bg;
    private Ship ship;

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public void show() {
        super.show();
        texShip = new Texture("Ship_.png");
        texBg = new Texture("Space.jpg");
        bg = new Background(texBg);
        ship = new Ship (texShip, 0.1f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update (delta);
        batch.begin();
        draw (delta);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        bg.resize(worldBounds);
        ship.resize(worldBounds);
    }

    private void update (float delta) {
        ship.update(delta);
    }

    private void draw (float delta) {
        bg.draw(batch);
        ship.draw(batch);
        //batch.draw(texShip, 0, 0, 0.3f, 0.3f);
    }

    @Override
    public void dispose() {
        super.dispose();
        texBg.dispose();
        texBg.dispose();
    }
}
