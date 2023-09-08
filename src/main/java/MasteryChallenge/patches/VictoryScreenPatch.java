package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import MasteryChallenge.config.Config;
import MasteryChallenge.effects.RandomColorPetalEffect;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class VictoryScreenPatch {
    private static final UIStrings newVictoryScreenStrings = CardCrawlGame.languagePack.getUIString("VictoryScreen2");
    private static final UIStrings baseVictoryScreenStrings = CardCrawlGame.languagePack.getUIString("VictoryScreen");

    @SpirePatch(clz = Metrics.class, method = "gatherAllDataAndSave")
    public static class UpdateMasteryData {
        @SpirePostfixPatch
        public static void patch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            if (AbstractDungeon.ascensionLevel < MasteryChallenge.config.getIntKeyOrSetDefault(Config.minAscLevel, 20)
                    || death || !trueVictor) {
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

    @SpirePatch(clz = DynamicBanner.class, method = "render")
    public static class UpdateVictoryBanner {
        @SpirePostfixPatch
        public static void patch(DynamicBanner __instance, SpriteBatch sb, @ByRef String[] ___label) {
            if (___label != null && ___label.length > 0 && ___label[0] != null &&
                    (___label[0].equals(newVictoryScreenStrings.TEXT[12]) || ___label[0].equals(baseVictoryScreenStrings.TEXT[12]))){
                if (MasteryChallenge.isComplete && !MasteryChallenge.config.getBoolKeyOrSetDefault(Config.hasShownCompleted, false)){
                    ___label[0] = newVictoryScreenStrings.TEXT[12];
                } else {
                    ___label[0] = baseVictoryScreenStrings.TEXT[12];
                }
            }
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "updateVfx")
    public static class AddVictoryScreenEffect {
        @SpirePostfixPatch
        public static void patch(VictoryScreen __instance, ArrayList<AbstractGameEffect> ___effect) {
            if (MasteryChallenge.isComplete && !MasteryChallenge.config.getBoolKeyOrSetDefault(Config.hasShownCompleted, false)) {
                ___effect.add(new RandomColorPetalEffect());
            }
        }
    }
}
