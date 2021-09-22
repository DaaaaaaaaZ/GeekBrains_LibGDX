package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.pools.ExplosionPool;
import dz.geekbrains.libgdx.utils.Regions;

public class BaseSprite extends Rect {

    private float angle;
    protected float scale = 1;
    protected TextureRegion[] regions;
    protected int frame;
    protected Rect worldBounds;
    private boolean destroyed;
    protected static ExplosionPool explosionPool;

    public BaseSprite () {

    }

    public BaseSprite(TextureRegion region) {
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public BaseSprite(TextureRegion region, int rows, int cols, int frames) {
        this.regions = Regions.split(region, rows, cols, frames);
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void setExplosionPool (ExplosionPool expPool) {
        explosionPool = expPool;
    }

    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    public void update(float delta) {

    }

    public void setFirstFrame () {
        frame = 0;
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean keyDown (int key) {
        return false;
    }

    public boolean keyUp (int key) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }


    public void destroy() {
        destroyed = true;
    }

    public void destroy (boolean isExploded) {
        if (isExploded) {
            explosionPool.obtain(this);
        }
        destroy ();
    }

    public void flushDestroy() {
        destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
