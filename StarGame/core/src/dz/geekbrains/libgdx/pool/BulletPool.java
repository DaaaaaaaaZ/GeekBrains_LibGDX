package dz.geekbrains.libgdx.pool;

import dz.geekbrains.libgdx.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

}
