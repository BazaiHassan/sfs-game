package io.github.hbazai.fighter.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    // Asset manager
    public final AssetManager manager = new AssetManager();

    // Gameplay Assets
    public static final String BACKGROUND_TEXTURE = "textures/Background.png";
    public static final String FRONT_REPOES_TEXTURE = "textures/FrontRopes.png";
    public static final String GAMEPLAY_BUTTONS_ATLAS = "textures/GameplayButtons.atlas";
    public static final String BLOOD_ATLAS = "textures/Blood.atlas";

    public static final String IDLE_SPRITE_SHEET ="sprites/IdleSpriteSheet.png";
    public static final String WALK_SPRITE_SHEET ="sprites/WalkSpriteSheet.png";
    public static final String PUNCH_SPRITE_SHEET ="sprites/PunchSpriteSheet.png";
    public static final String KICK_SPRITE_SHEET = "sprites/KickSpriteSheet.png";
    public static final String HURT_SPRITE_SHEET = "sprites/HurtSpriteSheet.png";
    public static final String BLOCK_SPRITE_SHEET = "sprites/BlockSpriteSheet.png";
    public static final String WIN_SPRITE_SHEET = "sprites/WinSpriteSheet.png";
    public static final String LOSE_SPRITE_SHEET = "sprites/LoseSpriteSheet.png";

    // Fonts
    public static final  String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String SMALL_FONT = "smallfont.ttf";
    public static final String MEDIUM_FONT = "mediumfont.ttf";
    public static final String LARGE_FONT = "largefont.ttf";

    // Audio Assets
    public static final String BLOCK_SOUND = "audio/block.mp3";
    public static final String BOO_SOUND = "audio/boo.mp3";
    public static final String CHEER_SOUND = "audio/cheer.mp3";
    public static final String CLICK_SOUND= "audio/click.mp3";
    public static final String HIT_SOUND = "audio/hit.mp3";
    public static final String MUSIC = "audio/music.oog";

    // Menu Assets
    public static final String MENU_ITEMS = "textures/MenuItems.atlas";

    public void load(){
        loadGamePlayAssets();
    }

    private void loadGamePlayAssets(){
        manager.load(BACKGROUND_TEXTURE, Texture.class);
        manager.load(FRONT_REPOES_TEXTURE, Texture.class);
        manager.load(IDLE_SPRITE_SHEET, Texture.class);
        manager.load(WALK_SPRITE_SHEET, Texture.class);
        manager.load(PUNCH_SPRITE_SHEET, Texture.class);
        manager.load(KICK_SPRITE_SHEET, Texture.class);
        manager.load(HURT_SPRITE_SHEET, Texture.class);
        manager.load(BLOCK_SPRITE_SHEET, Texture.class);
        manager.load(WIN_SPRITE_SHEET, Texture.class);
        manager.load(LOSE_SPRITE_SHEET, Texture.class);
        manager.load(GAMEPLAY_BUTTONS_ATLAS, TextureAtlas.class);
        manager.load(BLOOD_ATLAS, TextureAtlas.class);
    }

    public void dispose(){
        manager.dispose();
    }
}
