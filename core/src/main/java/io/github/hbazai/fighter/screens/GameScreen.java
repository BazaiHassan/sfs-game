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

    // Fighters
    private static final float PLAYER_START_POSITION_X = 16f;
    public static final float OPPONET_START_POSITION_X = 51f;
    public static final float FIGHTER_START_POSITION_Y = 15f;

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

        // get the fighters ready
        game.player.getReady(PLAYER_START_POSITION_X, FIGHTER_START_POSITION_Y);
        game.opponent.getReady(OPPONET_START_POSITION_X, FIGHTER_START_POSITION_Y);
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

        // Update the game
        update(delta);

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

        // Draw the fighters
        renderFighters();

        // End drawing
        game.batch.end();
    }

    private void renderFighters() {
        // Draw player
        game.player.render(game.batch);

        // Draw oppenet
        game.opponent.render(game.batch);
    }

    private void update(float deltaTime){
        game.player.update(deltaTime);
        game.opponent.update(deltaTime);
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
