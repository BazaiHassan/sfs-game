package io.github.hbazai.fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.hbazai.fighter.FighterGame;
import io.github.hbazai.fighter.resources.Assets;
import io.github.hbazai.fighter.resources.GlobalVariables;

public class GameScreen implements Screen {
    private final FighterGame game;
    private final ExtendViewport viewport;
    //Background/ring
    private Texture backgroundTexture;
    private Texture frontRopesTexture;

    public GameScreen(FighterGame game) {
        this.game = game;

        // Setup the viewport
        viewport =  new ExtendViewport(
            GlobalVariables.WORLD_WIDTH,
            GlobalVariables.MIN_WORLD_HEIGHT,
            GlobalVariables.WORLD_WIDTH,
            0
        );
        // Create the game area
        createGameArea();
    }

    private void createGameArea() {
        // Get the ring texture from assets
        backgroundTexture = game.assets.manager.get(Assets.BACKGROUND_TEXTURE);
        frontRopesTexture = game.assets.manager.get(Assets.FRONT_REPOES_TEXTURE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Set the sprite batch to use camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // Begin drawing
        game.batch.begin();

        // Draw the background
        game.batch.draw(
            backgroundTexture,
            0,
            0,
            backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // End drawing
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Update viewport with the new screen size
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
