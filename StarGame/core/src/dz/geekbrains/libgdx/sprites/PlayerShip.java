package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pools.BulletPool;

public class PlayerShip extends BaseShip {

    private static final float HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;
    private static final float SHOOT_DELAY = 0.2f;
    private static final int HP = 100;

    private static final float MAX_SKEW_SHIP = 20f;

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
    private float skewShip = 0f;
    private float increaseSkew = 1f;
    private enum Direction {
        LEFT,
        RIGHT,
        STOP
    }
    private Direction dir;

    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        bulletHeight = 0.01f;
        bulletDamage = 1;
        hp = HP;
        this.shipBulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        dir = Direction.STOP;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    public void update(float delta, int level) {
        if (!v.isZero()) {
            pos.mulAdd(v, delta);
        }

        switch (dir) {
            case LEFT: {
                skewShip += increaseSkew * 3;
                if (skewShip > MAX_SKEW_SHIP) {
                    skewShip = MAX_SKEW_SHIP;
                }
            } break;
            case RIGHT: {
                skewShip -= increaseSkew * 3;
                if (skewShip < -MAX_SKEW_SHIP) {
                    skewShip = -MAX_SKEW_SHIP;
                }
            } break;
            case STOP: {
                if (skewShip > 0.1f) {
                    skewShip -= increaseSkew;
                } else if (skewShip < -0.1f) {
                    skewShip += increaseSkew;
                }
            } break;
        }

        setAngle(skewShip);

        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }

        if (autoShootOn) {
            shoot(delta, level);
        }

        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }



//        if (getLeft() > worldBounds.getRight()) {
//            setRight(worldBounds.getLeft());
//        }
//        if (getRight() < worldBounds.getLeft()) {
//            setLeft(worldBounds.getRight());
//        }
    }

    private void shoot(float delta, int level) {
        if (shootDelay > SHOOT_DELAY) {
            shoot ();
            shootDelay = 0.0f;
        } else {
            shootDelay += delta * (.2f + level / 4f);
        }
    }

    private void setHP (int hp) {
        this.hp = hp;
    }

    public int getHP () {
        return hp;
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

    public boolean isCollision(Rect rect) {
        return !(
                rect.getRight() < getLeft()
                        || rect.getLeft() > getRight()
                        || rect.getBottom() > pos.y
                        || rect.getTop() < getBottom()
        );
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
            case Input.Keys.W:
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
        dir = Direction.RIGHT;
    }

    private void moveLeft() {
        v.set(v0).rotateDeg(180);
        dir = Direction.LEFT;
    }

    private void stop() {
        v.setZero();
        dir = Direction.STOP;
    }

    protected void shoot() {
        shipBulletSound.play(0.1f);
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x - skewShip / 600f, pos.y + getHalfHeight() * 0.8f);
        bulletV.setAngleDeg(90 + skewShip * 2);
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
    }

    @Override
    public void flushDestroy() {
        super.flushDestroy();
        stop();
        pressedLeft = false;
        pressedRight = false;
        setHP(HP);
        pos.x = 0f;
        skewShip = 0f;
    }

    @Override
    public void destroy(boolean isExploded) {
        super.destroy(isExploded);
    }

    public void dispose() {
        shipBulletSound.dispose();
    }
}
