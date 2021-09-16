package dz.geekbrains.libgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pool.BulletPool;
import dz.geekbrains.libgdx.pool.EnemyPool;
import dz.geekbrains.libgdx.pool.ExplosionPool;
import dz.geekbrains.libgdx.sprites.Background;
import dz.geekbrains.libgdx.sprites.PlayerShip;
import dz.geekbrains.libgdx.sprites.Star;
import dz.geekbrains.libgdx.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;

    private Texture bg;
    private TextureAtlas atlas;

    private Background background;
    private Star[] stars;
    private PlayerShip ship;
    private BulletPool bulletPool;

    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;
    private ExplosionPool explosionPool;


    private Music backgroundMusic;

    @Override
    public void show() {
        super.show();
        bg = new Texture("Space.jpg");
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }


        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas);
        enemyPool = new EnemyPool(bulletPool, worldBounds, explosionPool);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds);

        ship = new PlayerShip(atlas, bulletPool);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyed();
        batch.begin();
        draw();
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        ship.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        ship.dispose ();
        backgroundMusic.dispose();
        enemyEmitter.dispose();
        explosionPool.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        ship.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        ship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        ship.keyUp(keycode);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }

        ship.update(delta);
        bulletPool.updateActiveSprites(delta);

        enemyPool.updateActiveSprites(delta);

        explosionPool.updateActiveSprites(delta);

        enemyEmitter.generate(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        ship.draw(batch);
        bulletPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
    }
}
