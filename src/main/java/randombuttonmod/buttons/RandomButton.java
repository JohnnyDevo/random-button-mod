package randombuttonmod.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

public class RandomButton {
    private static final String ID = "randomcharacterbutton:RandomCharacterButton";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = uiStrings.TEXT;
    private static final float SHOW_X = Settings.WIDTH - 256.0f * Settings.scale;
    private static final float DRAW_Y = 256.0f * Settings.scale;
    private static final float HIDE_X = SHOW_X + 400.0f * Settings.scale;
    private static final Color HOVER_BLEND_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);
    private static final Color TEXT_DISABLED_COLOR = new Color(0.6f, 0.6f, 0.6f, 1.0f);
    private static final float TEXT_OFFSET_X = 136.0f * Settings.scale;
    private static final float TEXT_OFFSET_Y = 57.0f * Settings.scale;
    private static final float HITBOX_W = 300.0f * Settings.scale;
    private static final float HITBOX_H = 100.0f * Settings.scale;
    private static final float W = 512.0f;
    private static final float H = 256.0f;
    private Color glowColor = Color.WHITE.cpy();
    private float current_x;
    private float target_x;
    public boolean isHidden;
    private float glowAlpha;
    public Hitbox hb = new Hitbox(HITBOX_W, HITBOX_H);
    public boolean isDisabled;
    public boolean isHovered;
    private String buttonText = TEXT[0];

    public RandomButton() {
        current_x = HIDE_X;
        target_x = this.current_x;
        isHidden = true;
        isDisabled = true;
        isHovered = false;
        glowAlpha = 0.0f;
        hb.move(SHOW_X + 106.0f * Settings.scale, DRAW_Y + 60.0f * Settings.scale);
    }

    public void update() {
        if (!isHidden) {
            updateGlow();
            hb.update();
            if (InputHelper.justClickedLeft && hb.hovered && !isDisabled) {
                hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
            if (hb.justHovered && !isDisabled) {
                CardCrawlGame.sound.play("UI_HOVER");
            }
            isHovered = hb.hovered;
            if (CInputActionSet.select.isJustPressed()) {
                CInputActionSet.select.unpress();
                hb.clicked = true;
            }
        }
        if (current_x != target_x) {
            current_x = MathUtils.lerp(current_x, target_x, Gdx.graphics.getDeltaTime() * 9.0f);
            if (Math.abs(current_x - target_x) < Settings.UI_SNAP_THRESHOLD) {
                current_x = target_x;
            }
        }
    }

    private void updateGlow() {
        glowAlpha += Gdx.graphics.getDeltaTime() * 3.0f;
        if (glowAlpha < 0.0f) {
            glowAlpha *= -1.0f;
        }
        final float tmp = MathUtils.cos(glowAlpha);
        if (tmp < 0.0f) {
            glowColor.a = -tmp / 2.0f + 0.3f;
        }
        else {
            glowColor.a = tmp / 2.0f + 0.3f;
        }
    }

    public void show() {
        if (isHidden) {
            glowAlpha = 0.0f;
            target_x = SHOW_X;
            isHidden = false;
        }
    }

    public void render(final SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderShadow(sb);
        sb.setColor(glowColor);
        renderOutline(sb);
        sb.setColor(Color.PURPLE);
        renderButton(sb);
        sb.setColor(Color.WHITE);
        if (hb.hovered && !isDisabled && !hb.clickStarted) {
            sb.setBlendFunction(770, 1);
            sb.setColor(HOVER_BLEND_COLOR);
            renderButton(sb);
            sb.setBlendFunction(770, 771);
        }
        if (isDisabled) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, buttonText, current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, TEXT_DISABLED_COLOR);
        }
        else if (hb.clickStarted) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, buttonText, current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, Color.LIGHT_GRAY);
        }
        else if (hb.hovered) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, buttonText, current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, Settings.LIGHT_YELLOW_COLOR);
        }
        else {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, buttonText, current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, Settings.LIGHT_YELLOW_COLOR);
        }
        if (!isHidden) {
            hb.render(sb);
        }
    }

    private void renderShadow(final SpriteBatch sb) {
        sb.draw(ImageMaster.CONFIRM_BUTTON_SHADOW, current_x - W / 2, DRAW_Y - (H / 2), W / 2, H / 2, W, H, Settings.scale, Settings.scale, 0.0f, 0, 0, (int)W, (int)H, false, false);
    }

    private void renderOutline(final SpriteBatch sb) {
        sb.draw(ImageMaster.CONFIRM_BUTTON_OUTLINE, current_x - W / 2, DRAW_Y - (H / 2), W / 2, H / 2, W, H, Settings.scale, Settings.scale, 0.0f, 0, 0, (int)W, (int)H, false, false);
    }

    private void renderButton(final SpriteBatch sb) {
        sb.draw(ImageMaster.CONFIRM_BUTTON, current_x - W / 2, DRAW_Y - (H / 2), W / 2, H / 2, W, H, Settings.scale, Settings.scale, 0.0f, 0, 0, (int)W, (int)H, false, false);
    }
}
