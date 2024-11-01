package io.github.hbazai.fighter.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.ObjectMap;

public class Assets {
    // Asset manager
    public final AssetManager manager = new AssetManager();

    // Gameplay Assets
    public static final String BACKGROUND_TEXTURE = "textures/Background.png";
    public static final String FRONT_REPOES_TEXTURE = "textures/FrontRopes.png";
    public static final String GAMEPLAY_BUTTONS_ATLAS = "textures/GameplayButtons.atlas";
    public static final String BLOOD_ATLAS = "textures/Blood.atlas";

    public static final String IDLE_SPRITE_SHEET = "sprites/IdleSpriteSheet.png";
    public static final String WALK_SPRITE_SHEET = "sprites/WalkSpriteSheet.png";
    public static final String PUNCH_SPRITE_SHEET = "sprites/PunchSpriteSheet.png";
    public static final String KICK_SPRITE_SHEET = "sprites/KickSpriteSheet.png";
    public static final String HURT_SPRITE_SHEET = "sprites/HurtSpriteSheet.png";
    public static final String BLOCK_SPRITE_SHEET = "sprites/BlockSpriteSheet.png";
    public static final String WIN_SPRITE_SHEET = "sprites/WinSpriteSheet.png";
    public static final String LOSE_SPRITE_SHEET = "sprites/LoseSpriteSheet.png";

    // Fonts
    public static final String ROBOTO_REGULAR = "/home/deckard/LibGdx/Projects/FighterGame/assets/fonts/roboto_regular.ttf";
    public static final String SMALL_FONT = "smallfont.ttf";
    public static final String MEDIUM_FONT = "mediumfont.ttf";
    public static final String LARGE_FONT = "largefont.ttf";

    // Audio Assets
    public static final String BLOCK_SOUND = "audio/block.mp3";
    public static final String BOO_SOUND = "audio/boo.mp3";
    public static final String CHEER_SOUND = "audio/cheer.mp3";
    public static final String CLICK_SOUND = "audio/click.mp3";
    public static final String HIT_SOUND = "audio/hit.mp3";
    public static final String MUSIC = "audio/music.oog";

    // Menu Assets
    public static final String MENU_ITEMS = "textures/MenuItems.atlas";

    public final ObjectMap<String, FontInfo> fonts = new ObjectMap<>();

    public void load() {
        loadGamePlayAssets();
        loadFonts();
    }

    private void loadGamePlayAssets() {
        // Adding texture filter to make assests more better in terms of graphics
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;


        manager.load(BACKGROUND_TEXTURE, Texture.class, parameter);
        manager.load(FRONT_REPOES_TEXTURE, Texture.class, parameter);
        manager.load(IDLE_SPRITE_SHEET, Texture.class, parameter);
        manager.load(WALK_SPRITE_SHEET, Texture.class, parameter);
        manager.load(PUNCH_SPRITE_SHEET, Texture.class, parameter);
        manager.load(KICK_SPRITE_SHEET, Texture.class, parameter);
        manager.load(HURT_SPRITE_SHEET, Texture.class, parameter);
        manager.load(BLOCK_SPRITE_SHEET, Texture.class, parameter);
        manager.load(WIN_SPRITE_SHEET, Texture.class, parameter);
        manager.load(LOSE_SPRITE_SHEET, Texture.class, parameter);
        manager.load(GAMEPLAY_BUTTONS_ATLAS, TextureAtlas.class);
        manager.load(BLOOD_ATLAS, TextureAtlas.class);
    }

    private void loadFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class,".ttf",new FreetypeFontLoader(resolver));
        // Load small font
        FreetypeFontLoader.FreeTypeFontLoaderParameter smallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallFont.fontFileName = ROBOTO_REGULAR;
        smallFont.fontParameters.size = 32;
        smallFont.fontParameters.minFilter = Texture.TextureFilter.Linear;
        smallFont.fontParameters.magFilter = Texture.TextureFilter.Linear;
        manager.load(SMALL_FONT, BitmapFont.class, smallFont);

        // Load medium font
        FreetypeFontLoader.FreeTypeFontLoaderParameter mediumFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mediumFont.fontFileName = ROBOTO_REGULAR;
        mediumFont.fontParameters.size = 106;
        mediumFont.fontParameters.borderWidth = 4;
        mediumFont.fontParameters.minFilter = Texture.TextureFilter.Linear;
        mediumFont.fontParameters.magFilter = Texture.TextureFilter.Linear;
        manager.load(MEDIUM_FONT, BitmapFont.class, mediumFont);

        // Load large font
        FreetypeFontLoader.FreeTypeFontLoaderParameter largeFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        largeFont.fontFileName = ROBOTO_REGULAR;
        largeFont.fontParameters.size = 150;
        largeFont.fontParameters.minFilter = Texture.TextureFilter.Linear;
        largeFont.fontParameters.magFilter = Texture.TextureFilter.Linear;
        manager.load(LARGE_FONT, BitmapFont.class, largeFont);
    }

    public void dispose() {
        manager.dispose();
    }

    // Custom class to hold font information
    public static class FontInfo {
        public final String name;
        public final int size;

        public FontInfo(String name, int size) {
            this.name = name;
            this.size = size;
        }

    }

}

