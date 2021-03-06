package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;

public class Bullet extends BaseSprite {

    private final Vector2 v = new Vector2();

    private Rect worldBounds;
    private int damage;
    private BaseSprite owner;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public void set(
            BaseSprite owner,
            TextureRegion region,
            Vector2 pos0,
            Vector2 v0,
            float height,
            Rect worldBounds,
            int damage
    ) {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    @Override
    public void destroy(boolean isExploded) {
        super.destroy(isExploded);
    }

    public int getDamage() {
        return damage;
    }

    public BaseSprite getOwner() {
        return owner;
    }
}
