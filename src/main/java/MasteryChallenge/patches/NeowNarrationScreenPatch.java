package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import MasteryChallenge.config.Config;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cutscenes.NeowNarrationScreen;
import com.megacrit.cardcrawl.localization.CharacterStrings;

public class NeowNarrationScreenPatch {
    private static final CharacterStrings newNeowStrings = CardCrawlGame.languagePack.getCharacterString("PostCreditsNeow2");
    private static final CharacterStrings baseNeowStrings = CardCrawlGame.languagePack.getCharacterString("PostCreditsNeow");

    @SpirePatch2(clz = NeowNarrationScreen.class, method = "open")
    public static class NeowNarrationPatch {
        @SpirePrefixPatch
        public static void replaceString(NeowNarrationScreen __instance, @ByRef CharacterStrings[] ___charStrings) {
            if (MasteryChallenge.isComplete && !MasteryChallenge.config.getBoolKeyOrSetDefault(Config.hasShownCompleted, false)){
                ___charStrings[0] = newNeowStrings;
                MasteryChallenge.config.setBoolKey(Config.hasShownCompleted, true);
            } else {
                ___charStrings[0] = baseNeowStrings;
            }
        }
    }
}
