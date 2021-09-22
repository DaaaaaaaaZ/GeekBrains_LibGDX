package dz.geekbrains.libgdx.pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.sprites.Explosion;
import dz.geekbrains.libgdx.utils.Regions;

public class ExplosionPool extends SpritesPool <Explosion>{
    private final TextureRegion [] textureRegions;
    private final Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

    public ExplosionPool (TextureAtlas atlas) {
        textureRegions = Regions.split(atlas.findRegion("explosion"), 9, 9, 74);
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(textureRegions, explosionSound);
    }

    public Explosion obtain(Rect rect) {
        Explosion tempExplosion = obtain();
        tempExplosion.setFirstFrame();
        tempExplosion.set (rect);
        return tempExplosion;
    }

    @Override
    public void dispose() {
        super.dispose();
        explosionSound.dispose();
    }
}
