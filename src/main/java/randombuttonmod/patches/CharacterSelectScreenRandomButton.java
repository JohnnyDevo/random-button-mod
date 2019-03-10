package randombuttonmod.patches;

import basemod.CustomCharacterSelectScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import randombuttonmod.RandomButtonMod;
import randombuttonmod.buttons.RandomButton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CharacterSelectScreenRandomButton {

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "updateButtons"
    )
    public static class UpdateRandomButton {
        public static void Prefix(CharacterSelectScreen __instance) {
            if (RandomButtonMod.randomButton == null) {
                RandomButtonMod.randomButton = new RandomButton();
                RandomButtonMod.randomButton.show();
                RandomButtonMod.randomButton.isDisabled = false;
                RandomButtonMod.logger.info("Random Button successfully initialized!");
            }
            RandomButton button = RandomButtonMod.randomButton;
            button.update();
            if (button.hb.clicked) {
                try {
                    if (__instance instanceof CustomCharacterSelectScreen) {
                        CustomCharacterSelectScreen screen = (CustomCharacterSelectScreen)__instance;
                        Field optionsField = screen.getClass().getDeclaredField("allOptions");
                        optionsField.setAccessible(true);
                        ArrayList<CharacterOption> allOptions = (ArrayList<CharacterOption>)optionsField.get(__instance);
                        ArrayList<CharacterOption> legalOptions = new ArrayList<>();
                            for (CharacterOption option : allOptions) {
                                if (!option.locked && !option.selected) {
                                    legalOptions.add(option);
                                }
                            }
                        CharacterOption randomOption = legalOptions.get(MathUtils.random(legalOptions.size()-1));
                        if (!__instance.options.contains(randomOption)) {
                            screen.options.forEach(o -> o.selected = false);
                            int randomOptionIndex = allOptions.indexOf(randomOption);
                            Field selectIndexField = screen.getClass().getDeclaredField("selectIndex");
                            Field optionsIndexField = screen.getClass().getDeclaredField("optionsIndex");
                            Field optionsPerIndexField = screen.getClass().getDeclaredField("optionsPerIndex");
                            selectIndexField.setAccessible(true);
                            optionsIndexField.setAccessible(true);
                            optionsPerIndexField.setAccessible(true);
                            int optionsPerIndex = (int)optionsPerIndexField.get(screen);
                            selectIndexField.set(screen, randomOptionIndex / optionsPerIndex);
                            optionsIndexField.set(screen, optionsPerIndex * (int)selectIndexField.get(screen));
                            int endIndex = (int)optionsIndexField.get(screen) + optionsPerIndex;
                            screen.options = new ArrayList<>(allOptions.subList((int)optionsIndexField.get(screen), Math.min(allOptions.size(), endIndex)));
                            screen.options.forEach(o -> o.selected = false);
                            Method positionButtonsMethod = screen.getClass().getDeclaredMethod("positionButtons");
                            positionButtonsMethod.setAccessible(true);
                            positionButtonsMethod.invoke(screen);
                        }
                        randomOption.hb.clicked = true;
                    } else {
                        ArrayList<CharacterOption> legalOptions = new ArrayList<>();
                        for (CharacterOption option : __instance.options) {
                            if (!option.locked && !option.selected) {
                                legalOptions.add(option);
                            }
                        }
                        CharacterOption randomOption = legalOptions.get(MathUtils.random(legalOptions.size()-1));
                        randomOption.hb.clicked = true;
                    }
                } catch(NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                button.hb.clicked = false;
            }
        }
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "render"
    )
    public static class RenderRandomButton {
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb) {
            if (RandomButtonMod.randomButton == null) {
                RandomButtonMod.randomButton = new RandomButton();
                RandomButtonMod.randomButton.show();
                RandomButtonMod.randomButton.isDisabled = false;
                RandomButtonMod.logger.info("Random Button successfully initialized!");
            }
            RandomButtonMod.randomButton.render(sb);
        }
    }
}
