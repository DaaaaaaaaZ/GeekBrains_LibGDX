package dz.geekbrains.libgdx.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dz.geekbrains.libgdx.math.Rect;

public class ExitButton extends BaseButton {

    private static final float HEIGHT = 0.2f;
    private static final float PADDING = 0.03f;

    public ExitButton(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setRight(worldBounds.getRight() - PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
