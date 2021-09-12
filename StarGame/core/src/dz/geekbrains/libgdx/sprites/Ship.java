package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pool.BulletPool;

public class Ship extends BaseSprite {

    private final float HEIGHT = 0.15f;
    private final float BOTTOM_MARGIN = 0.05f;
    private final int INVALID_POINTER = -1;
    private final float SHOOT_DELAY = 0.2f;

    private final Vector2 v0 = new Vector2(0.5f, 0);
    private final Vector2 v = new Vector2();

    private final BulletPool bulletPool;
    private final TextureRegion bulletRegion;
    private final Vector2 bulletV;
    private final Vector2 bulletPos;
    private final float bulletHeight;
    private final int bulletDamage;

    private float shootDelay;
    private boolean autoShootOn = false;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private Rect worldBounds;
    
    private Sound shipBulletSound;

    public Ship(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        bulletHeight = 0.01f;
        bulletDamage = 1;
        this.shipBulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    @Override
    public void update(float delta) {
        if (!v.isZero()) {
            pos.mulAdd(v, delta);
        }

        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }

        if (autoShootOn) {
            shoot(delta);
        }

//        if (getLeft() > worldBounds.getRight()) {
//            setRight(worldBounds.getLeft());
//        }
//        if (getRight() < worldBounds.getLeft()) {
//            setLeft(worldBounds.getRight());
//        }
    }

    private void shoot(float delta) {
        if (shootDelay > SHOOT_DELAY) {
            shoot ();
            shootDelay = 0.0f;
        } else {
            shootDelay += delta;
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            startAndStopAutoShoot ();
        }

        if (button == Input.Buttons.LEFT) {
            if (touch.x < pos.x) {
                moveLeft();
            } else {
                moveRight();
            }
        }
        return false;
    }

    private void startAndStopAutoShoot() {
        if (autoShootOn) {
            autoShootOn = false;
            shootDelay = 0.0f;
        } else {
            autoShootOn = true;
        }
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                stop();
                break;
            /*case Input.Buttons.RIGHT:
                autoShootOn = false;
                break;*/
            }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                startAndStopAutoShoot ();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotateDeg(180);
    }

    private void stop() {
        v.setZero();
    }

    private void shoot() {
        shipBulletSound.play(0.03f);
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
    }

    public void dispose() {
        shipBulletSound.dispose();
    }
}
