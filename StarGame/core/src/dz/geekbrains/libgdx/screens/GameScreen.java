package dz.geekbrains.libgdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import dz.geekbrains.libgdx.buttons.NewGameButton;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pool.BulletPool;
import dz.geekbrains.libgdx.pool.EnemyPool;
import dz.geekbrains.libgdx.pool.ExplosionPool;
import dz.geekbrains.libgdx.sprites.*;
import dz.geekbrains.libgdx.utils.EnemyEmitter;

import java.util.List;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;

    private Texture bg;
    private TextureAtlas atlas;
    private BaseSprite gameOver;
    private NewGameButton newGame;

    private Background background;
    private Star[] stars;
    private PlayerShip ship;
    private BulletPool bulletPool;

    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;
    private ExplosionPool explosionPool;

    private final Game game;
    private Music backgroundMusic;

    public GameScreen (Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("Space.jpg");
        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        gameOver = new BaseSprite(atlas.findRegion("message_game_over"));
        gameOver.setHeightProportion(0.07f);
        gameOver.setTop(0.1f);


        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }


        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas);
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds);

        ship = new PlayerShip(atlas, bulletPool);
        newGame = new NewGameButton(atlas.findRegion("button_new_game"), ship);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        backgroundMusic.setLooping(true);
        try {
            backgroundMusic.play();
        } catch (GdxRuntimeException e) {
            System.out.println("С третьего раза музыка пропадает");
        }
        ship.setExplosionPool (explosionPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        if (!ship.isDestroyed()) {
            checkCollisions();
        }
        freeAllDestroyed();

        batch.begin();
        draw();
        batch.end();
    }

    private void checkCollisions() {
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList) {
            float minDst = enemyShip.getHalfWidth() + ship.getHalfWidth();
            if (ship.pos.dst(enemyShip.pos) < minDst) {
                enemyShip.destroy(true);
                ship.damage(enemyShip.getBulletDamage() * 2);
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() != ship) {
                if (ship.isCollision(bullet)) {
                    ship.damage(bullet.getDamage());
                    bullet.destroy(true);
                }
            } else {
                for (EnemyShip enemyShip : enemyShipList) {
                    if (enemyShip.isCollision(bullet)) {
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroy(true);
                    }
                }
            }
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        ship.resize(worldBounds);

        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
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
        System.out.println("Dispose GameScreen");
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (!ship.isDestroyed()) {
            ship.touchDown(touch, pointer, button);
        } else {
            newGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (!ship.isDestroyed()) {
            ship.touchUp(touch, pointer, button);
        } else {
            newGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }

        if (!ship.isDestroyed()) {
            ship.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!ship.isDestroyed()) {
            ship.keyUp(keycode);
        }
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }

        if (!ship.isDestroyed()) {
            ship.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);

            enemyEmitter.generate(delta);
        }

        explosionPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }

        if (ship.isDestroyed() && explosionPool.getActiveObjects().size() == 0) {
            //Метод не сработал из-за исключения в 'Music'.play () на третьей попытке
            //game.setScreen(new MenuScreen(game, true));

            //Поэтому показываю кнопки в этом же Screen'e:
            gameOver.draw(batch);
            newGame.draw(batch);
            enemyPool.restart();
            bulletPool.restart();
        }

        if (!ship.isDestroyed()) {
            ship.draw(batch);
            bulletPool.drawActiveSprites(batch);

            enemyPool.drawActiveSprites(batch);
        }

        explosionPool.drawActiveSprites(batch);
    }
}
