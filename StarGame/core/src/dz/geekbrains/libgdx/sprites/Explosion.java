package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dz.geekbrains.libgdx.math.Rect;

public class Explosion extends BaseSprite {
    private static final float EXPLOSION_INTERVAL = 0.03f;
    private float explosionTime;
    private final Sound explosionSound;
    private boolean isStartSound = false;

    public Explosion (TextureRegion [] textureRegions, Sound explosionSound) {
        regions = textureRegions;
        this.explosionSound = explosionSound;
    }


    @Override
    public void update(float delta) {
        explosionTime += delta;
        if (explosionTime > EXPLOSION_INTERVAL) {
            if (frame == (regions.length - 1)) {
                isStartSound = false;
                destroy();
            } else {
                if (!isStartSound) {
                    explosionSound.play (getHeight() * 2.0f);
                }
                frame++;
                explosionTime = 0.0f;
                isStartSound = true;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
