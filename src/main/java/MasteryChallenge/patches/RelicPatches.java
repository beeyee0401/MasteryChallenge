package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class RelicPatches {
    @SpirePatch2(clz = AbstractRelic.class, method = "renderOutline", paramtypez = {SpriteBatch.class, boolean.class})
    public static class AbstractRelicPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void patch(AbstractRelic __instance, SpriteBatch sb, boolean inTopPanel) {
            boolean isMastered = MasteryChallenge.relicAndRunMap.containsKey(__instance.relicId);
            if (!isMastered){
                sb.setColor(Settings.RED_RELIC_COLOR);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate (CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRelic.class, "PASSIVE_OUTLINE_COLOR");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                lines[0]++;
                return lines;
            }
        }
    }

    @SpirePatch2(clz = AbstractRelic.class, method = "renderOutline", paramtypez = {Color.class, SpriteBatch.class, boolean.class})
    public static class AbstractRelicLibraryPatch {
        @SpireInsertPatch (
                rloc = 0
        )
        public static void patch(AbstractRelic __instance, @ByRef Color[] c, SpriteBatch sb, boolean inTopPanel) {
            boolean isMastered = MasteryChallenge.relicAndRunMap.containsKey(__instance.relicId);
            if (!isMastered){
                c[0] = Settings.RED_RELIC_COLOR;
            }

            // This is only the run history screen (as far as I know smiley face)
            if (c[0] == Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR) {
                if (MasteryChallenge.relicAndRunMap.get(__instance.relicId).equals(RunHistoryScreenPatch.currentRunTimestamp)){
                    c[0] = Settings.GREEN_RELIC_COLOR;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate (CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRelic.class, "PASSIVE_OUTLINE_COLOR");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                lines[0]++;
                return lines;
            }
        }
    }

    @SpirePatch2(clz = SingleRelicViewPopup.class, method = "renderRarity")
    public static class SingleRelicViewPopupPatch {
        @SpirePostfixPatch
        public static void patch(SingleRelicViewPopup __instance, SpriteBatch sb) {
            AbstractRelic relic = ReflectionHacks.getPrivate(__instance, SingleRelicViewPopup.class, "relic");
            boolean isMastered = MasteryChallenge.relicAndRunMap.containsKey(relic.relicId);
            if (!isMastered){
                FontHelper.renderWrappedText(sb, FontHelper.cardDescFont_N, "Not Mastered", (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 215.0F * Settings.scale, 9999.0F, Settings.RED_TEXT_COLOR, 1.0F);
            }
        }
    }
}

