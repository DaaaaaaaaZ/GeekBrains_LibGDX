package dz.geekbrains.libgdx;

import com.badlogic.gdx.Game;
import dz.geekbrains.libgdx.screens.ForLesson3Screen;

public class StarGame extends Game {
	@Override
	public void create () {
		setScreen(new ForLesson3Screen());
	}
}
