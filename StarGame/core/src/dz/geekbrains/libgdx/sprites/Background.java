package dz.geekbrains.libgdx.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dz.geekbrains.libgdx.math.Rect;


public class Background extends BaseSprite {

    public Background(Texture texture) {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        this.pos.set(worldBounds.pos);
        setHeightProportion(worldBounds.getHeight());
    }
}
