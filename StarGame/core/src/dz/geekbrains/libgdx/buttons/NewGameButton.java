package dz.geekbrains.libgdx.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.sprites.PlayerShip;

public class NewGameButton extends BaseButton {
    private PlayerShip ship;
    private static final float HEIGHT = 0.04f;
    private static final float TOP = -0.4f;

    public NewGameButton (TextureRegion textureRegion, PlayerShip ship) {
        super (textureRegion);
        this.ship = ship;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setTop(TOP);
    }

    @Override
    public void action() {
        ship.flushDestroy();
    }
}
