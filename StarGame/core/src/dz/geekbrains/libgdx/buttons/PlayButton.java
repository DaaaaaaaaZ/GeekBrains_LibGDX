package dz.geekbrains.libgdx.buttons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.screens.GameScreen;

public class PlayButton extends BaseButton {

    private final Game game;

    private static final float HEIGHT = 0.25f;
    private static final float PADDING = 0.03f;

    public PlayButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setLeft(worldBounds.getLeft() + PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
