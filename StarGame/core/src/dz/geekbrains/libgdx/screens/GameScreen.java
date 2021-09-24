package dz.geekbrains.libgdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import dz.geekbrains.libgdx.buttons.NewGameButton;
import dz.geekbrains.libgdx.fonts.BaseFont;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pools.BulletPool;
import dz.geekbrains.libgdx.pools.EnemyPool;
import dz.geekbrains.libgdx.pools.ExplosionPool;
import dz.geekbrains.libgdx.sprites.*;
import dz.geekbrains.libgdx.utils.EnemyEmitter;

import java.util.List;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 64;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private static final float MARGIN = 0.01f;

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

    private BaseFont font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;
    private int frags;

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

        font = new BaseFont("font/font.fnt", "font/font.png");
        font.setSize(0.02f);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();


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
        newGame = new NewGameButton(atlas.findRegion("button_new_game"), ship, this);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        backgroundMusic.setLooping(true);
        try {
            backgroundMusic.play();
        } catch (GdxRuntimeException e) {
            //System.out.println("С третьего раза музыка пропадает");
        }
        ship.setExplosionPool (explosionPool);
    }

    public void deleteFrags () {
        frags = 0;
        enemyEmitter.flushLevel();
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
        printInfo();
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
                        if (enemyShip.isDestroyed()) {
                            frags++;
                        }
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
            ship.update(delta, enemyEmitter.getLevel());
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);

            enemyEmitter.generate(delta, frags);
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

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        sbHP.setLength(0);
        font.draw(batch, sbHP.append(HP).append(ship.getHP()), worldBounds.pos.x, worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - MARGIN, worldBounds.getTop() - MARGIN, Align.right);
    }
}
