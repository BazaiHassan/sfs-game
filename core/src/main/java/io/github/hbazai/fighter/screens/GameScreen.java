package io.github.hbazai.fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.hbazai.fighter.FighterGame;
import io.github.hbazai.fighter.resources.Assets;
import io.github.hbazai.fighter.resources.GlobalVariables;

public class GameScreen implements Screen, InputProcessor {
    private final FighterGame game;
    private final ExtendViewport viewport;
    //Background/ring
    private Texture backgroundTexture;
    private Texture frontRopesTexture;

    // Bound of Ring
    public static final float RING_MIN_X = 7f;
    public static final float RING_MAX_X = 60f;
    public static final float RING_MIN_Y = 4f;
    public static final float RING_MAX_Y = 22f;
    public static final float RING_SLOPE = 3.16f;

    // Fighters
    private static final float PLAYER_START_POSITION_X = 16f;
    public static final float OPPONET_START_POSITION_X = 51f;
    public static final float FIGHTER_START_POSITION_Y = 15f;

    public GameScreen(FighterGame game) {
        this.game = game;

        // Setup the viewport
        viewport = new ExtendViewport(
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
        // Process user input
        Gdx.input.setInputProcessor(this);
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

        // Draw the front ropes
        game.batch.draw(
            frontRopesTexture,
            0,
            0,
            frontRopesTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            frontRopesTexture.getHeight() * GlobalVariables.WORLD_SCALE
        );
        // End drawing
        game.batch.end();
    }

    private void renderFighters() {
        // Use y coordinates to determine which fighter to draw first
        if (game.player.getPosition().y > game.opponent.getPosition().y) {
            // Draw player
            game.player.render(game.batch);

            // Draw oppenet
            game.opponent.render(game.batch);
        } else {
            // Draw oppenet
            game.opponent.render(game.batch);

            // Draw player
            game.player.render(game.batch);
        }
    }

    private void update(float deltaTime) {
        game.player.update(deltaTime);
        game.opponent.update(deltaTime);

        // make sure fighters are facing each other
        if (game.player.getPosition().x <= game.opponent.getPosition().x) {
            game.player.faceRight();
            game.opponent.faceLeft();
        } else {
            game.player.faceLeft();
            game.opponent.faceRight();
        }

        // Keep the fighters within the bounds of the ring
        keepWithinRingBounds(game.player.getPosition());
        keepWithinRingBounds(game.opponent.getPosition());
    }

    private void keepWithinRingBounds(Vector2 position) {
        if (position.y < RING_MIN_Y) {
            position.y = RING_MIN_Y;
        } else if (position.y > RING_MAX_Y) {
            position.y = RING_MAX_Y;
        }

        if (position.x < position.y / RING_SLOPE + RING_MIN_X) {
            position.x = position.y / RING_SLOPE + RING_MIN_X;
        } else if (position.x > position.y / -RING_SLOPE + RING_MAX_X) {
            position.x = position.y / -RING_SLOPE + RING_MAX_X;
        }
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

    @Override
    public boolean keyDown(int keycode) {
        // Check if the player has pressed a movement key
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            game.player.moveLeft();
        } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            game.player.moveRight();
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            game.player.moveUp();
        } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            game.player.moveDown();
        }

        // check if the player has pressed block or attack key
        if(keycode == Input.Keys.B){
            game.player.block();
        } else if (keycode == Input.Keys.F) {
            game.player.punch();
        } else if (keycode == Input.Keys.V) {
            game.player.kick();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Check if player release the pressed key
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            game.player.stopMovingLeft();
        } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            game.player.stopMovingRight();
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            game.player.stopMovingUp();
        } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            game.player.stopMovingDown();
        }

        // if the player released the block key
        if(keycode == Input.Keys.B){
            game.player.stopBlocking();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
