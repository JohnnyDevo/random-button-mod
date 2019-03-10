package randombuttonmod;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import randombuttonmod.buttons.RandomButton;

import java.nio.charset.StandardCharsets;

@SpireInitializer
public class RandomButtonMod implements EditStringsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(RandomButtonMod.class.getName());
    public static RandomButton randomButton;

    public RandomButtonMod(){
        BaseMod.subscribe(this);
    }

    //Used by @SpireInitializer
    @SuppressWarnings("unused")
    public static void initialize(){
        new RandomButtonMod();
        logger.info("RandomButtonMod successfully initialized!");
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeImg = new Texture("randombuttonmod/images/badge.png");
        ModPanel settingsPanel = new ModPanel(); //we can add UI elements to this settings panel as desired
        BaseMod.registerModBadge(badgeImg, "Random Character Button Mod", "Johnny Devo", "Adds a \"Random Character\" Button to the character select screen.", settingsPanel);
    }

    @Override
    public void receiveEditStrings() {
        String languageString = "randombuttonmod/strings/" + getLanguageString(Settings.language);
        String uiStrings = Gdx.files.internal(languageString + "/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
    }

    private String getLanguageString(Settings.GameLanguage language) { //for future localization support
        switch (language) {
//            case ZHS:
//                return "zhs";
            default:
                return "eng";
        }
    }
}
