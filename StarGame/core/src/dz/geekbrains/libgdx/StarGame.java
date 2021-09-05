package dz.geekbrains.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture imgFon;
	private TextureRegion regFon;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgFon = new Texture("Space.jpg");
		regFon = new TextureRegion(imgFon, 0, 0, 400, 600);
	}

	@Override
	public void render () {
		//ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(regFon, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgFon.dispose();
	}
}
