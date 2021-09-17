package dz.geekbrains.libgdx.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dz.geekbrains.libgdx.sprites.BaseSprite;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends BaseSprite> {

    protected final List<T> activeObjects = new ArrayList<>();
    protected final List<T> freeObjects = new ArrayList<>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        return object;
    }

    public void updateActiveSprites(float delta) {
        for (BaseSprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        for (BaseSprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    public void freeAllDestroyedActiveSprites() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }

    public void restart () {
        if (activeObjects.size() > 0) {
            for (int i = 0; i < activeObjects.size(); i++) {
                T sprite = activeObjects.get(i);
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    private void free(T object) {
        if (activeObjects.remove(object)) {
            freeObjects.add(object);
        }
    }
}
