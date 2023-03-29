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
import java.util.LinkedHashMap;

public class DeckScreenPatch {
    private static final LinkedHashMap<String, String> cards = new LinkedHashMap<>();
    private static final HashMap<String, Integer> cardCounts = new HashMap<>();
    private static final HashMap<String, String> relics = new HashMap<>();
    private static final Color yellowTextColor = new Color(1f, 0.988f, 0.498f, 1f);
    private static final Color gold = Color.GOLD.cpy();

    @SpirePatch(clz = MasterDeckViewScreen.class, method = "render")
    public static class RenderMasteryCandidates {
        @SpirePrefixPatch
        public static void patch(MasterDeckViewScreen __instance, SpriteBatch sb) {
            MasteryTextCardPatch.shouldShow = true;

            if (cards.isEmpty()){
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (!MasteryChallenge.cardAndRunMap.containsKey(card.cardID)){
                        cards.put(card.cardID, card.originalName);
                        cardCounts.put(card.cardID, cardCounts.getOrDefault(card.cardID, 0) + 1);
                    }
                }
            }

            float baseY = Settings.HEIGHT * 0.8f;
            float baseX = 25 * Settings.xScale;

            float increment = 30.0f;
            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, "Mastery Candidates", baseX, baseY, gold);
            baseY -= increment;

            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, "Cards", baseX, baseY, gold);
            baseY -= increment;

            for (String cardId : cards.keySet()) {
                Color textColor = cardCounts.get(cardId) > 1 ? Settings.GREEN_TEXT_COLOR : yellowTextColor;
                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont,
                        cards.get(cardId) + " (x" + cardCounts.get(cardId) + ")", baseX + 10f, baseY, textColor);
                baseY -= increment;
            }

            baseY -= increment;

            if (relics.isEmpty()) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (!MasteryChallenge.relicAndRunMap.containsKey(relic.relicId)) {
                        relics.put(relic.relicId, relic.name);
                    }
                }
            }

            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, "Relics", baseX, baseY, gold);
            baseY -= increment;

            for (String relicId : relics.keySet()) {
                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, relics.get(relicId), baseX + 10f, baseY, Settings.CREAM_COLOR);
                baseY -= increment;
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class ResetCards {
        @SpirePrefixPatch
        public static void patch() {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
                cards.clear();
                cardCounts.clear();
                relics.clear();
            }
        }
    }
}
