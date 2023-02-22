package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.runHistory.TinyCard;
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

    @SpirePatch2(clz = TinyCard.class, method = "render")
    public static class ShowCardMasteriesInHistoryPatch {
        @SpireInsertPatch(
                rloc = 74-67,
                localvars = {"text"}
        )
        public static void setCurrentRun(TinyCard __instance, SpriteBatch sb, @ByRef String[] text) {
            if (MasteryChallenge.cardAndRunMap.getOrDefault(__instance.card.cardID, "").equals(currentRunTimestamp)){
                text[0] = "[" + text[0] + "]";
            }
        }
    }

//    @SpirePatch2(clz = TinyCard.class, method = "render")
//    public static class ShowCardMasteriesInHistoryColorPatch {
//        @SpireInsertPatch(
//                rloc = 80-67,
//                localvars = {"textColor"}
//        )
//        public static void setCurrentRun(TinyCard __instance, SpriteBatch sb, @ByRef Color[] textColor) {
//            if (MasteryChallenge.cardAndRunMap.getOrDefault(__instance.card.cardID, "").equals(currentRunTimestamp)){
//                if (!__instance.hb.hovered){
//                    textColor[0] = Color.CYAN;
//                }
//            }
//        }
//    }
}
