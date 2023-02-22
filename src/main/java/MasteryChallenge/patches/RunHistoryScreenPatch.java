package MasteryChallenge.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;

public class RunHistoryScreenPatch {
    public static String currentRunTimestamp = "";

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
}
