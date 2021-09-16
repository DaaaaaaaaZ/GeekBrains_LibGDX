package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pool.BulletPool;
import dz.geekbrains.libgdx.pool.ExplosionPool;

public class EnemyShip extends BaseShip {


    public EnemyShip(BulletPool bulletPool, Rect worldBounds, ExplosionPool explosionPool) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.bulletV = new Vector2();
        this.bulletPos = new Vector2();
        if (this.explosionPool == null) {
            this.explosionPool = explosionPool;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.bulletPos.set(pos.x, pos.y - getHalfHeight());
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletV,
            int bulletDamage,
            float reloadInterval,
            Sound bulletSound,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(bulletV);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.bulletSound = bulletSound;
        setHeightProportion(height);
        this.hp = hp;
        v.set(v0);
    }
}
