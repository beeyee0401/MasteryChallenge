package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.mainMenu.SaveSlot;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class SaveSlotPatch {
    @SpirePatch(clz = SaveSlot.class, method = "update")
    public static class UpdateSavePatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void patch(SaveSlot __instance){
            MasteryChallenge.initializeMasteries();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate (CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "saveSlot");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                lines[0]++;
                return lines;
            }
        }
    }
}
