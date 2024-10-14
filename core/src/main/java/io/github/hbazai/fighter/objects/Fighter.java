package io.github.hbazai.fighter.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.hbazai.fighter.FighterGame;
import io.github.hbazai.fighter.resources.Assets;
import io.github.hbazai.fighter.resources.GlobalVariables;

public class Fighter {
    // Number of framw rows and columns in each animation
    // This is because the images that we are going to use i this game
    // are combinination of character movement images
    private static final int FRAME_ROWS = 2, FRAME_COLS = 3;

    // Figther speed of movement
    public static final float MOVEMENT_SPEED = 10f;

    // Max life
    public static final float MAX_LIFE = 100f;

    // Hit Strength
    public static final float HIT_STRENGTH = 5f;

    // Damage factor when is blocking
    public static final float BLOCK_DAMAGE_FACTOR = 0.2f;

    // Distinguishing Details
    private String name;
    private Color color;

    // state
    public enum State {
        BLOCK,
        HURT,
        IDLE,
        KICK,
        LOSE,
        PUNCH,
        WALK,
        WIN
    }

    private State state;
    private float stateTime;
    private State renderState;
    private float renderStateTime;

    private final Vector2 position = new Vector2();
    private final Vector2 movementDirection = new Vector2();

    private float life;
    private int facing;
    private boolean madeContact;

    // Animations
    private Animation<TextureRegion> blockAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> kickAnimation;
    private Animation<TextureRegion> loseAnimation;
    private Animation<TextureRegion> punchAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> winAnimation;

    public Fighter(FighterGame game, String name, Color color) {
        this.name = name;
        this.color = color;

        // Init ANimations
        initializeBlockAnimation(game.assets.manager);
        initializeHurtAnimation(game.assets.manager);
        initializeIdleAnimation(game.assets.manager);
        initializeKickAnimation(game.assets.manager);
        initializeLoseAnimation(game.assets.manager);
        initializePunchAnimation(game.assets.manager);
        initializeWalkAnimation(game.assets.manager);
        initializeWinAnimation(game.assets.manager);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getLife() {
        return life;
    }

    public void getReady(float positionX, float positionY) {
        state = renderState = State.IDLE;
        stateTime = renderStateTime = 0f;
        position.set(positionX, positionY);
        movementDirection.set(0, 0);
        life = MAX_LIFE;
        madeContact = false;
    }

    public void render(SpriteBatch batch) {
        // Get the current animation frame
        TextureRegion currentFrame;
        switch (renderState) {
            case BLOCK:
                currentFrame = blockAnimation.getKeyFrame(renderStateTime, true);
                break;
            case HURT:
                currentFrame = hurtAnimation.getKeyFrame(renderStateTime, false);
                break;
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(renderStateTime, true);
                break;
            case KICK:
                currentFrame = kickAnimation.getKeyFrame(renderStateTime, false);
                break;
            case LOSE:
                currentFrame = loseAnimation.getKeyFrame(renderStateTime, false);
                break;
            case PUNCH:
                currentFrame = punchAnimation.getKeyFrame(renderStateTime, false);
                break;
            case WALK:
                currentFrame = walkAnimation.getKeyFrame(renderStateTime, true);
                break;
            default:
                currentFrame = winAnimation.getKeyFrame(renderStateTime, true);
        }

        batch.setColor(color);
        batch.draw(
            currentFrame,
            position.x,
            position.y,
            currentFrame.getRegionWidth() * GlobalVariables.WORLD_SCALE * 0.5f,
            0,
            currentFrame.getRegionWidth() * GlobalVariables.WORLD_SCALE,
            currentFrame.getRegionHeight() * GlobalVariables.WORLD_SCALE,
            facing,
            1,
            0
        );
        batch.setColor(1, 1, 1, 1);

    }

    public void update(float deltaTime) {
        // increment the state time by deltaTime
        stateTime += deltaTime;

        // Only update the render state if delta time is greater than zero
        if (deltaTime > 0) {
            renderState = state;
            renderStateTime = stateTime;
        }

        if (state == State.WALK) {
            // if the fighter is walking, move in the direction of the movement direction vector
            position.x += movementDirection.x * MOVEMENT_SPEED * deltaTime;
            position.y += movementDirection.y * MOVEMENT_SPEED * deltaTime;
        } else if (
            (state == State.PUNCH && punchAnimation.isAnimationFinished(stateTime)) ||
                (state == State.KICK && kickAnimation.isAnimationFinished(stateTime)) ||
                (state == State.HURT && hurtAnimation.isAnimationFinished(stateTime))
        ) {
            // if the animation has finished and the movement direction is set, start walking, otherwise, go to IDLE
            if (movementDirection.x != 0 || movementDirection.y != 0) {
                changeState(State.WALK);
            } else {
                changeState(State.IDLE);
            }
        }
    }

    public void faceLeft() {
        facing = -1;
    }

    public void faceRight() {
        facing = 1;
    }

    private void changeState(State newState) {
        state = newState;
        stateTime = 0f;
    }

    private void setMovement(float x, float y) {
        movementDirection.set(x, y);
        if (state == State.WALK && x == 0 && y == 0) {
            changeState(State.IDLE);
        } else if (state == State.IDLE && (x != 0 || y != 0)) {
            changeState(State.WALK);
        }
    }

    public void moveLeft() {
        setMovement(-1, movementDirection.y);
    }

    public void moveRight() {
        setMovement(1, movementDirection.y);
    }

    public void moveUp() {
        setMovement(movementDirection.x, 1);
    }

    public void moveDown() {
        setMovement(movementDirection.x, -1);
    }

    public void stopMovingLeft() {
        if (movementDirection.x == -1) {
            setMovement(0, movementDirection.y);
        }
    }

    public void stopMovingRight() {
        if (movementDirection.x == 1) {
            setMovement(0, movementDirection.y);
        }
    }

    public void stopMovingUp() {
        if (movementDirection.y == 1) {
            setMovement(movementDirection.x, 0);
        }
    }

    public void stopMovingDown() {
        if (movementDirection.y == -1) {
            setMovement(movementDirection.x, 0);
        }
    }

    public void block() {
        if (state == State.IDLE || state == State.WALK) {
            changeState(State.BLOCK);
        }
    }

    public void stopBlocking() {
        if (state == State.BLOCK) {
            if (movementDirection.x != 0 || movementDirection.y != 0) {
                changeState(State.WALK);
            } else {
                changeState(State.IDLE);
            }
        }
    }

    public boolean isBlocking() {
        return state == State.BLOCK;
    }

    public void punch() {
        if (state == State.IDLE || state == State.WALK) {
            changeState(State.PUNCH);

            // Start attacking but still no contact
            madeContact = false;
        }
    }

    public void kick() {
        if (state == State.IDLE || state == State.WALK) {
            changeState(State.KICK);

            // Start attacking but still no contact
            madeContact = false;
        }
    }

    public void madeContact() {
        madeContact = true;
    }

    public boolean hasMadeContact() {
        return madeContact;
    }

    public boolean isAttackActive() {
        // The attack is only active if the fighter has not made the contact and the attack ...
        // ... animation has not just started or is almost finish
        if (hasMadeContact()) {
            return false;
        } else if (state == State.PUNCH) {
            return stateTime > punchAnimation.getAnimationDuration() * 0.33f && stateTime < punchAnimation.getAnimationDuration() * 0.66f;
        } else if (state == State.KICK) {
            return stateTime > kickAnimation.getAnimationDuration() * 0.33f && stateTime < kickAnimation.getAnimationDuration() * 0.66f;
        } else {
            return false;
        }
    }

    public void getHit(float damage) {
        if (state == State.HURT || state == State.WIN || state == State.LOSE) return;
        // Reduce the fighter's life by the full damage amount or a fraction of it if the fighter is blocking
        life -= state == State.BLOCK ? damage * BLOCK_DAMAGE_FACTOR : damage;

        if (life <= 0f) {
            // if no life remains --> LOSE
            lose();
        } else if (state != State.BLOCK) {
            // if is not blocking
            changeState(State.HURT);
        }

    }

    public void lose() {
        changeState(State.LOSE);
        life = 0f;
    }

    public boolean hasLost() {
        return state == State.LOSE;
    }

    public void win() {
        changeState(State.WIN);
    }

    public boolean isAttacking() {
        return state == State.PUNCH || state == State.KICK;
    }

    private void initializeBlockAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        blockAnimation = new Animation<>(0.03f, frames);
    }

    private void initializeHurtAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.HURT_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        hurtAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeIdleAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.IDLE_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        idleAnimation = new Animation<>(0.1f, frames);
    }

    private void initializeKickAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.KICK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        kickAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeLoseAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.LOSE_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        loseAnimation = new Animation<>(0.05f, frames);
    }

    private void initializePunchAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.PUNCH_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        punchAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeWalkAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.WALK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        walkAnimation = new Animation<>(0.08f, frames);
    }

    private void initializeWinAnimation(AssetManager assetManager) {
        Texture spriteSheet = assetManager.get(Assets.WIN_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        winAnimation = new Animation<>(0.05f, frames);
    }


    private TextureRegion[] getAnimationFrames(Texture spriteSheet) {
        TextureRegion[][] temp = TextureRegion.split(
            spriteSheet,
            spriteSheet.getWidth() / FRAME_COLS,
            spriteSheet.getHeight() / FRAME_ROWS
        );

        TextureRegion[] frames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = temp[i][j];
            }
        }

        return frames;
    }
}
