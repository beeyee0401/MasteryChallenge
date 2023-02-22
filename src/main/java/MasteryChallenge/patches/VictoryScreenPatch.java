package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

public class VictoryScreenPatch {
    @SpirePatch(clz = Metrics.class, method = "gatherAllDataAndSave")
    public static class RenderMasteryCandidates {
        @SpirePostfixPatch
        public static void patch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            if (AbstractDungeon.ascensionLevel != 20 || death || !trueVictor) {
                return;
            }

            long lastPlaytimeEnd = ReflectionHacks.getPrivate(__instance, Metrics.class, "lastPlaytimeEnd");
            String runName = Long.toString(lastPlaytimeEnd);
            MasteryChallenge.updateMasteries(
                    AbstractDungeon.player.getRelicNames(),
                    AbstractDungeon.player.masterDeck.getCardIdsForMetrics(),
                    runName);
        }
    }
}
