package dz.geekbrains.libgdx.pools;

import dz.geekbrains.libgdx.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

}
