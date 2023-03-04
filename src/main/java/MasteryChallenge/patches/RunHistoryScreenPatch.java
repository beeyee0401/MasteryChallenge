package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.runHistory.TinyCard;
import com.megacrit.cardcrawl.screens.stats.RunData;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.HashMap;

public class RunHistoryScreenPatch {
    public static String currentRunTimestamp = null;
    private static final Color yellow = new Color(1.0f, 1.0f, 0f, 1f);
    public static final HashMap<String, String> cardDisplayStringToCardId = new HashMap<>();

    @SpirePatch2(clz = RunHistoryScreen.class, method = "renderRunHistoryScreen")
    public static class ShowRelicMasteriesPatch {
        @SpirePrefixPatch
        public static void setCurrentRun(RunHistoryScreen __instance, SpriteBatch sb) {
            RunData currentRun = ReflectionHacks.getPrivate(__instance, RunHistoryScreen.class, "viewedRun");
            if (currentRun != null) {
                currentRunTimestamp = currentRun.timestamp;
            }
        }
    }

    @SpirePatch2(clz = RunHistoryScreen.class, method = "hide")
    public static class ResetCurrentRun {
        @SpirePostfixPatch
        public static void setCurrentRun(RunHistoryScreen __instance) {
            currentRunTimestamp = null;
        }
    }

    @SpirePatch2(clz = RunHistoryScreen.class, method = "reloadWithRunData")
    public static class ClearCardDisplayStringToCardId {
        @SpirePostfixPatch
        public static void patch(RunHistoryScreen __instance, RunData runData) {
            cardDisplayStringToCardId.clear();
        }
    }

    @SpirePatch2(clz = TinyCard.class, method = "render")
    public static class ShowCardMasteriesInHistoryPatch {
        @SpireInsertPatch(
            rloc = 74-67,
            localvars = {"text"}
        )
        public static void mapMessageToCardId(TinyCard __instance, SpriteBatch sb, String text) {
            if (MasteryChallenge.cardAndRunMap.getOrDefault(__instance.card.cardID, "").equals(currentRunTimestamp)){
                cardDisplayStringToCardId.put(text, __instance.card.cardID);
            }
        }
    }

    @SpirePatch2(
            clz = FontHelper.class,
            method = "renderSmartText",
            paramtypez = {SpriteBatch.class, BitmapFont.class, String.class, float.class, float.class, float.class, float.class, Color.class}
    )
    public static class ShowCardMasteriesInHistoryIconPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"curWidth"}
        )
        public static void renderStar(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor, float curWidth) {
            if (currentRunTimestamp != null){
                String cardId = cardDisplayStringToCardId.get(msg);
                if (cardId != null && MasteryChallenge.cardAndRunMap.getOrDefault(cardId, "").equals(currentRunTimestamp)){
                    sb.setColor(yellow);
                    sb.draw(ImageMaster.TINY_STAR,
                            x + curWidth + (3f * Settings.xScale),
                            y - (20f * Settings.yScale),
                            23f * Settings.xScale,
                            23f * Settings.yScale);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate (CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[] {lines[lines.length - 1]};
            }
        }
    }
}
