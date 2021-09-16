package dz.geekbrains.libgdx.pool;


import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.sprites.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private final BulletPool bulletPool;
    private final Rect worldBounds;
    private final ExplosionPool explosionPool;

    public EnemyPool(BulletPool bulletPool, Rect worldBounds, ExplosionPool explosionPool) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.explosionPool = explosionPool;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, worldBounds, explosionPool);
    }
}
