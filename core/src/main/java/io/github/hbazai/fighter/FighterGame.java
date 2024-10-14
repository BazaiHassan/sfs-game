package io.github.hbazai.fighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.hbazai.fighter.objects.Fighter;
import io.github.hbazai.fighter.resources.Assets;
import io.github.hbazai.fighter.screens.GameScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class FighterGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Assets assets;

    // Screen
    public GameScreen gameScreen;

    // Fighters
    public Fighter player, opponent;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        assets = new Assets();

        // load all assets
        assets.load();
        assets.manager.finishLoading(); // Later we wiil create a loading screen here

        // Initialize Fighters
        player = new Fighter(this,"H Bazai",new Color(1f,0.2f,0.2f,1f));
        opponent = new Fighter(this, "Aboozar", new Color(0.25f, 0.7f, 1f, 1f));

        // initialize the game screen and switch it
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        assets.dispose();
    }
}
