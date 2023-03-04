package MasteryChallenge.patches;

import MasteryChallenge.MasteryChallenge;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;

import java.util.HashMap;

public class DeckScreenPatch {
    private static final HashMap<String, String> cards = new HashMap<>();
    private static final HashMap<String, String> relics = new HashMap<>();

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "render")
    public static class RenderMasteryCandidates {
        @SpirePrefixPatch
        public static void patch(MasterDeckViewScreen __instance, SpriteBatch sb) {
            MasteryTextCardPatch.shouldShow = true;
            String header = "Mastery Candidates";

            Color color = Color.GOLD;
            float increment = 30.0f;
            float baseY = Settings.HEIGHT * 0.8f;
            float baseX = 25 * Settings.xScale;
            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, header, baseX, baseY, color);
            baseY -= increment;

            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, "Cards", baseX, baseY, color);
            baseY -= increment;

            if (cards.isEmpty()){
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    cards.put(card.cardID, card.originalName);
                }
            }

            for (String cardId : cards.keySet()) {
                if (!MasteryChallenge.cardAndRunMap.containsKey(cardId)){
                    FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, cards.get(cardId), baseX, baseY, Settings.CREAM_COLOR);
                    baseY -= increment;
                }
            }

            baseY -= increment;
            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, "Relics", baseX, baseY, color);
            baseY -= increment;

            if (relics.isEmpty()) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    relics.put(relic.relicId, relic.name);
                }
            }

            for (String relicId : relics.keySet()) {
                if (!MasteryChallenge.relicAndRunMap.containsKey(relicId)){
                    FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, relics.get(relicId), baseX, baseY, Settings.CREAM_COLOR);
                    baseY -= increment;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class ResetCards {
        @SpirePrefixPatch
        public static void patch() {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
                cards.clear();
                relics.clear();
            }
        }
    }
}
