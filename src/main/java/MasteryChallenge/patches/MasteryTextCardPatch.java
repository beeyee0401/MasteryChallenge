package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Pride;
import com.megacrit.cardcrawl.cards.optionCards.*;
import com.megacrit.cardcrawl.cards.tempCards.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class MasteryTextCardPatch {
    public static boolean shouldShow = true;
    public static final HashSet<String> uncollectibleCards = new HashSet<>(Arrays.asList(
            Pride.ID,
            Beta.ID,
            Expunger.ID,
            Insight.ID,
            Miracle.ID,
            Omega.ID,
            Safety.ID,
            Shiv.ID,
            Smite.ID,
            ThroughViolence.ID,
            BecomeAlmighty.ID,
            ChooseCalm.ID,
            ChooseWrath.ID,
            FameAndFortune.ID,
            LiveForever.ID
    ));

    @SpirePatch2(clz = AbstractCard.class, method = "renderTitle", paramtypez = {SpriteBatch.class})
    public static class RenderTitlePatch {
        @SpirePostfixPatch
        public static void patch(AbstractCard __instance, SpriteBatch sb) {
            if (shouldShow && !uncollectibleCards.contains(__instance.cardID) && __instance.type != AbstractCard.CardType.STATUS){
                boolean isMastered = MasteryChallenge.cardAndRunMap.containsKey(__instance.cardID);
                if (!isMastered){
                    float xPos, yPos, offsetY;
                    BitmapFont font;
                    String text = "Not Mastered";
                    if (__instance.isFlipped || __instance.isLocked || __instance.transparency <= 0.0F)
                        return;
                    font = FontHelper.cardTitleFont;
                    xPos = __instance.current_x;
                    yPos = __instance.current_y;
                    offsetY = 400.0F * Settings.scale * __instance.drawScale / 2.0F;
                    BitmapFont.BitmapFontData fontData = font.getData();
                    float originalScale = fontData.scaleX;
                    float scaleMulti = 0.8F;
                    fontData.setScale(scaleMulti * (__instance.drawScale * 0.85f));
                    Color color = Settings.CREAM_COLOR.cpy();
                    color.a = __instance.transparency;
                    FontHelper.renderRotatedText(sb, font, text, xPos, yPos, 0.0F, offsetY, __instance.angle, true, color);
                    fontData.setScale(originalScale);
                }
            }
        }
    }

    @SpirePatch2(clz = CardGroup.class, method = "renderHand")
    public static class PreventRenderHandPatch {
        @SpirePrefixPatch
        public static void patchRenderHand(CardGroup __instance, SpriteBatch sb, AbstractCard exceptThis) {
            shouldShow = AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom().isBattleOver;
        }
    }

    @SpirePatch2(clz = CardRewardScreen.class, method = "customCombatOpen")
    public static class PreventCombatCardsPatch {
        @SpirePrefixPatch
        public static void patchCustomCombatOpen(CardRewardScreen __instance, ArrayList<AbstractCard> choices, String text, boolean skippable) {
            shouldShow = false;
        }
    }

    @SpirePatch2(clz = CardRewardScreen.class, method = "open")
    public static class ShouldTextInCardReward {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header) {
            shouldShow = true;
        }
    }

    @SpirePatch2(clz = ShopScreen.class, method = "render")
    public static class ShopScreenPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
            shouldShow = true;
        }
    }

    @SpirePatch2(clz = CardLibraryScreen.class, method = "render")
    public static class CardLibraryScreenPatch {
        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen __instance, SpriteBatch sb) {
            shouldShow = true;
        }
    }
}

