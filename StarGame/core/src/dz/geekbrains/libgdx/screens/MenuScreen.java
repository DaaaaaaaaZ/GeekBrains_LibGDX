package dz.geekbrains.libgdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dz.geekbrains.libgdx.buttons.ExitButton;
import dz.geekbrains.libgdx.buttons.PlayButton;
import dz.geekbrains.libgdx.math.Rect;
import dz.geekbrains.libgdx.sprites.Background;
import dz.geekbrains.libgdx.sprites.BaseSprite;
import dz.geekbrains.libgdx.sprites.Star;


public class MenuScreen extends BaseScreen {
    private static final int STAR_COUNT = 256;

    private Game game;

    private Texture texBg;
    private Background bg;
    private TextureRegion regGameOver;
    private BaseSprite gameOver;

    private TextureAtlas atlas;
    private Star stars [];
    private ExitButton exitButton;
    private PlayButton playButton;
    private boolean isGameOver = false;

    public MenuScreen(Game game) {
        this.game = game;
    }

    public MenuScreen (Game game, boolean isGameOver) {
        this.game = game;
        this.isGameOver = isGameOver;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        exitButton.touchDown(touch, pointer, button);
        playButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        exitButton.touchUp(touch, pointer, button);
        playButton.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public void show() {
        super.show();

        texBg = new Texture("Space.jpg");
        bg = new Background(texBg);
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        if (isGameOver) {
            regGameOver = new TextureAtlas("textures/mainAtlas.tpack").findRegion(
                    "message_game_over");
            gameOver = new BaseSprite(regGameOver);
            gameOver.setHeightProportion(0.05f);
            gameOver.setTop(0.1f);
        }
        stars = new Star [STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }

        exitButton = new ExitButton(atlas);
        playButton = new PlayButton(atlas, game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update (delta);
        batch.begin();
        draw ();
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        bg.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }

        if (gameOver != null) {
            gameOver.resize(worldBounds);
        }
        exitButton.resize(worldBounds);
        playButton.resize(worldBounds);
    }

    private void update (float delta) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update (delta);
        }
    }

    private void draw () {
        bg.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw (batch);
        }

        if (gameOver != null) {
            gameOver.draw(batch);
        }
        exitButton.draw(batch);
        playButton.draw(batch);
    }

    @Override
    public void dispose() {
        super.dispose();

        texBg.dispose();
        atlas.dispose();
    }
}
