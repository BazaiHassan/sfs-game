package io.github.hbazai.fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Locale;

import io.github.hbazai.fighter.FighterGame;
import io.github.hbazai.fighter.objects.Fighter;
import io.github.hbazai.fighter.resources.Assets;
import io.github.hbazai.fighter.resources.GlobalVariables;

public class GameScreen implements Screen, InputProcessor {
    private final FighterGame game;
    private final ExtendViewport viewport;

    // game
    private enum GameState {
        RUNNING,
        PAUSE,
        GAME_OVER
    }

    private GameState gameState;
    private GlobalVariables.Difficulty difficulty = GlobalVariables.Difficulty.EASY;

    // rounds
    private enum RoundState {
        STARTING,
        IN_PROGRESS,
        ENDING
    }

    private RoundState roundState;
    private float roundStateTime;
    public static final float START_ROUND_DELAY = 2f;
    public static final float END_ROUND_DELAY = 2f;
    private int currentRound;
    public static final int MAX_ROUNDS = 3;


    private int roundWin = 0, roundLost = 0;
    private static final float MAX_ROUND_TIME = 99.99f;
    private float roundTimer = MAX_ROUND_TIME;
    public static final float CRITICAL_ROUND_TIME = 10f;
    public static final Color CRITICAL_ROUND_TIME_COLOR = Color.RED;
    // fonts
    private BitmapFont smallFont, mediumFont, largeFont;
    private static final Color DEFUALT_FONT_COLOR = Color.WHITE;

    // HUD
    public static final Color HEALTH_BAR_COLOR = Color.RED;
    public static final Color HEALTH_BAR_BACKGROUND_COLOR = GlobalVariables.GOLD;


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
    public static final float FIGHTER_CONTACT_DISTANCE_X = 7.5f;
    public static final float FIGHTER_CONTACT_DISTANCE_Y = 1.5f;

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

        // Setup fonts
        setupFonts();
    }

    private void setupFonts() {
        smallFont = game.assets.manager.get(Assets.SMALL_FONT);
        smallFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        smallFont.setColor(DEFUALT_FONT_COLOR);
        smallFont.setUseIntegerPositions(false);

        mediumFont = game.assets.manager.get(Assets.MEDIUM_FONT);
        mediumFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        mediumFont.setColor(DEFUALT_FONT_COLOR);
        mediumFont.setUseIntegerPositions(false);

        largeFont = game.assets.manager.get(Assets.LARGE_FONT);
        largeFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        largeFont.setColor(DEFUALT_FONT_COLOR);
        largeFont.setUseIntegerPositions(false);
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

        // Start the game
        startGame();
    }

    private void startGame() {
        gameState = GameState.RUNNING;
        roundWin = roundLost = 0;

        // Start round 1
        currentRound = 1;
        startRound();
    }

    private void startRound() {
        // get the fighters ready
        game.player.getReady(PLAYER_START_POSITION_X, FIGHTER_START_POSITION_Y);
        game.opponent.getReady(OPPONET_START_POSITION_X, FIGHTER_START_POSITION_Y);

        // Start the round
        roundState = RoundState.STARTING;
        roundStateTime = 0f;
        roundTimer = MAX_ROUND_TIME;
    }

    private void endRound() {
        // End the round
        roundState = RoundState.ENDING;
        roundStateTime = 0f;
    }

    private void winRound() {
        // player wins the round and opponet loses
        game.player.win();
        game.opponent.lose();
        roundWin++;

        // end the round
        endRound();
    }

    private void loseRound() {
        // player loses the round and opponet wins
        game.player.lose();
        game.opponent.win();
        roundLost++;

        // end the round
        endRound();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the game
        update(delta);

        // Set the sprite batch & the shape renderer to use the viewport's camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

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

        // Draw the HUD
        renderHUD();

        // if the round is starting, draw the start round text
        if (roundState == RoundState.STARTING) {
            renderStartRoundText();
        }

        // End drawing
        game.batch.end();
    }

    private void renderHUD() {
        float HUDMargin = 1f;

        // draw the rounds won to lost ratio
        smallFont.draw(
            game.batch,
            "WINS:" + roundWin + " - " + roundLost,
            HUDMargin,
            viewport.getWorldHeight() - HUDMargin
        );

        // Draw the difficulty settings
        String text = "DIFFICULTY: ";
        switch (difficulty) {
            case EASY:
                text += "EASY";
                break;
            case MEDIUM:
                text += "MEDIUM";
                break;
            default:
                text += "HARD";
                break;
        }
        smallFont.draw(
            game.batch,
            text,
            viewport.getMaxWorldWidth() - HUDMargin,
            viewport.getWorldHeight() - HUDMargin,
            0,
            Align.right,
            false
        );

        // setup the layout sizes and positioning
        float healthBarPadding = 0.5f;
        float healthBarHeight = smallFont.getCapHeight() + healthBarPadding * 2f;
        float healthBarMaxWidth = 32f;
        float healthBarBackgroundPadding = 0.2f;
        float healthBarBackgroundHeight = healthBarHeight + healthBarBackgroundPadding * 2f;
        float healthBarBackgroundWidth = healthBarMaxWidth + healthBarBackgroundPadding * 2f;
        float healthBarBackgroundMarginTop = 0.8f;
        float healthBarBackgroundPositionY = viewport.getWorldHeight() - HUDMargin - smallFont.getCapHeight() - healthBarBackgroundMarginTop - healthBarBackgroundHeight;
        float healthBarPositionY = healthBarBackgroundPositionY + healthBarBackgroundPadding;
        float fighterNamePositionY = healthBarPositionY + healthBarHeight - healthBarPadding;

        game.batch.end();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw the fighter healthBar background rectangles
        game.shapeRenderer.setColor(HEALTH_BAR_BACKGROUND_COLOR);
        game.shapeRenderer.rect(HUDMargin, healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight);
        game.shapeRenderer.rect(viewport.getWorldWidth() - HUDMargin - healthBarBackgroundWidth, healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight);

        // draw the fighter healthBar rectangles
        game.shapeRenderer.setColor(HEALTH_BAR_COLOR);
        float healthBarWidth = healthBarMaxWidth * game.player.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(HUDMargin + healthBarBackgroundPadding, healthBarPositionY, healthBarWidth, healthBarHeight);
        healthBarWidth = healthBarMaxWidth * game.opponent.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(viewport.getWorldWidth() - HUDMargin - healthBarBackgroundPadding - healthBarWidth, healthBarPositionY, healthBarWidth, healthBarHeight);

        game.shapeRenderer.end();
        game.batch.begin();

        // Draw the fighter name
        smallFont.draw(game.batch, game.player.getName(), HUDMargin + healthBarBackgroundPadding + healthBarPadding, fighterNamePositionY);
        smallFont.draw(game.batch, game.opponent.getName(), viewport.getWorldWidth() - HUDMargin - healthBarBackgroundPadding - healthBarPadding, fighterNamePositionY, 0, Align.right, false);

        // Draw Round Timer
        if (roundTimer < CRITICAL_ROUND_TIME) {
            mediumFont.setColor(CRITICAL_ROUND_TIME_COLOR);
        }
        mediumFont.draw(
            game.batch,
            String.format(Locale.getDefault(), "%02d", (int) roundTimer),
            viewport.getWorldWidth() / 2f - mediumFont.getSpaceXadvance() * 2.3f,
            viewport.getWorldHeight() - HUDMargin
        );
        mediumFont.setColor(DEFUALT_FONT_COLOR);
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

    private void renderStartRoundText() {
        String text;
        if (roundStateTime < START_ROUND_DELAY * 0.5f) {
            text = "ROUND " + currentRound;
        } else {
            text = "FIGHT!";
        }
        mediumFont.draw(
            game.batch,
            text,
            viewport.getWorldWidth() / 2f,
            viewport.getWorldHeight() / 2f,
            0,
            Align.center,
            false
        );
    }

    private void update(float deltaTime) {

        if (roundState == RoundState.STARTING && roundStateTime >= START_ROUND_DELAY) {
            // if the start round delay has been reached, start the fight
            roundState = RoundState.IN_PROGRESS;
            roundStateTime = 0f;
        } else if (roundState == RoundState.ENDING && roundStateTime >= END_ROUND_DELAY) {
            // if the end round delay has been reached and player has won or lost more than half of the max number of rounds,
            // end the game; otherwise, start the next round
            if (roundWin > MAX_ROUNDS / 2 || roundLost > MAX_ROUNDS / 2) {
                gameState = GameState.GAME_OVER;
            } else {
                currentRound++;
                startRound();
            }
        } else {
            // increament the round state time by delta time
            roundStateTime += deltaTime;
        }

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

        if (roundState == RoundState.IN_PROGRESS) {
            // if the round is in progress, decrease the round timer by deltaTime
            roundTimer -= deltaTime;

            if (roundTimer <= 0f) {
                // if the round timer has finished and player has the same or more life than opponent, player wins the round;
                // otherwise, player loses the round
                if (game.player.getLife() >= game.opponent.getLife()) {
                    winRound();
                } else {
                    loseRound();
                }
            }

            // Check if the fighters are within contact distance
            if (areWithinContactDistance(game.player.getPosition(), game.opponent.getPosition())) {
                if (game.player.isAttackActive()) {
                    // if the fighters are within contact distance and player is actively attacking, opponent gets hit
                    game.opponent.getHit(Fighter.HIT_STRENGTH);
                    // Deactivate player contact to avoid multiple decreasing
                    game.player.madeContact();

                    // Check if opponent has lost
                    if (game.opponent.hasLost()) {
                        // If opponent has lost, player win the round
                        winRound();
                    }
                }
            }
        }
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

    private boolean areWithinContactDistance(Vector2 position1, Vector2 position2) {
        // Determine if the position are within the distance in which contact is possible
        float xDistance = Math.abs(position1.x - position2.x);
        float yDistance = Math.abs(position1.y - position2.y);

        return xDistance <= FIGHTER_CONTACT_DISTANCE_X && yDistance <= FIGHTER_CONTACT_DISTANCE_Y;
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
        if (keycode == Input.Keys.SPACE) {
            if (gameState == GameState.RUNNING) {
                // If the game is running the space key has been pressed, skip any round delay
                if (roundState == RoundState.STARTING) {
                    roundStateTime = START_ROUND_DELAY;
                } else if (roundState == RoundState.ENDING) {
                    roundStateTime = END_ROUND_DELAY;
                }
            } else if (gameState == GameState.GAME_OVER) {
                // if the game is over and the space key has been pressed, restart the game
                startGame();
            }
        } else {
            if (roundState == RoundState.IN_PROGRESS) {
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
            }

            // check if the player has pressed block or attack key
            if (keycode == Input.Keys.B) {
                game.player.block();
            } else if (keycode == Input.Keys.F) {
                game.player.punch();
            } else if (keycode == Input.Keys.V) {
                game.player.kick();
            }
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
        if (keycode == Input.Keys.B) {
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
        // Convert the screen coordinates of the touch/click into world coordinates
        Vector3 position = new Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(position, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        if (gameState == GameState.RUNNING) {
            if (roundState == RoundState.STARTING) {
                // if the round is starting and the screen has been touched, skip the start round delay
                roundStateTime = START_ROUND_DELAY;
            } else if (roundState == RoundState.ENDING) {
                // if the round is ending and the screen has been touched, skip the end round delay
                roundStateTime = END_ROUND_DELAY;
            }
        }
        return true;
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
