package dz.geekbrains.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import dz.geekbrains.libgdx.screens.ForLesson2Screen;

public class StarGame extends Game {
	@Override
	public void create () {
		setScreen(new ForLesson2Screen());
	}
}
