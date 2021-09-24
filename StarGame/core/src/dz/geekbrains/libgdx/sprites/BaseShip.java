package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pool.BulletPool;
import dz.geekbrains.libgdx.pool.ExplosionPool;

import static com.badlogic.gdx.Graphics.GraphicsType.LWJGL;

public class BaseShip extends BaseSprite {
    protected static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected ExplosionPool explosionPool;

    private static Rect playerShip;
    protected final Vector2 v0 = new Vector2();
    protected final Vector2 v = new Vector2();

    protected Sound bulletSound;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int bulletDamage;

    protected float reloadTimer;
    protected float reloadInterval;

    protected Rect worldBounds;

    protected int hp;
    protected float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    public BaseShip() {
    }

    public BaseShip(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        if (this instanceof PlayerShip) {
            playerShip = this;
        }
    }

    @Override
    public void update(float delta) {
        if (pos.y < worldBounds.getTop()) {
            pos.mulAdd(v, delta);

            reloadTimer += delta;
            if (reloadTimer >= reloadInterval) {
                reloadTimer = 0f;
                shoot();
            }
        } else {
            pos.mulAdd(v, delta * 10);
        }

        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    public void damage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy(true);
        }
        frame = 1;
        damageAnimateTimer = 0f;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        bulletSound.play(0.1f);
    }
}
