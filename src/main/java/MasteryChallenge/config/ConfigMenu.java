package MasteryChallenge.config;

import MasteryChallenge.MasteryChallenge;
import basemod.BaseMod;
import basemod.ModLabeledButton;
import basemod.ModPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;
import org.apache.commons.lang3.ArrayUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.IntStream;

public class ConfigMenu extends ModPanel implements DropdownMenuListener {
    DropdownMenu dayDropdown;
    DropdownMenu monthDropdown;
    DropdownMenu yearDropdown;
    DropdownMenu ascDropdown;
    DropdownMenu cardCountDropdown;
    ModLabeledButton refreshButton;
    public ConfigMenu(){
        super();

        String[] days = IntStream.range(1, 32).mapToObj(String::valueOf).toArray(String[]::new);
        dayDropdown = new DropdownMenu(this, days, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        int dayIndex = ArrayUtils.indexOf(days, String.valueOf(MasteryChallenge.config.getIntKeyOrSetDefault(Config.startDay, 1)));
        dayDropdown.setSelectedIndex(dayIndex);

        String[] months = IntStream.range(1, 13).mapToObj(String::valueOf).toArray(String[]::new);
        monthDropdown = new DropdownMenu(this, months, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        int monthIndex = ArrayUtils.indexOf(months, String.valueOf(MasteryChallenge.config.getIntKeyOrSetDefault(Config.startMonth, 1)));
        monthDropdown.setSelectedIndex(monthIndex);

        String[] years = IntStream.range(2020, 2030).mapToObj(String::valueOf).toArray(String[]::new);
        yearDropdown = new DropdownMenu(this, years, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        int yearIndex = ArrayUtils.indexOf(years, String.valueOf(MasteryChallenge.config.getIntKeyOrSetDefault(Config.startYear, 2023)));
        yearDropdown.setSelectedIndex(yearIndex);

        String[] ascLevels = IntStream.range(0, 21).mapToObj(String::valueOf).toArray(String[]::new);
        ascDropdown = new DropdownMenu(this, ascLevels, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        int ascIndex = ArrayUtils.indexOf(ascLevels, String.valueOf(MasteryChallenge.config.getIntKeyOrSetDefault(Config.minAscLevel, 20)));
        ascDropdown.setSelectedIndex(ascIndex);

        String[] cardCountRequirement = IntStream.range(1, 4).mapToObj(String::valueOf).toArray(String[]::new);
        cardCountDropdown = new DropdownMenu(this, cardCountRequirement, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        int cardCountIndex = ArrayUtils.indexOf(cardCountRequirement, String.valueOf(MasteryChallenge.config.getIntKeyOrSetDefault(Config.cardCount, 2)));
        cardCountDropdown.setSelectedIndex(cardCountIndex);

        refreshButton = new ModLabeledButton("Refresh", 400.0F, 375,
                Settings.CREAM_COLOR, Color.GOLD, FontHelper.tipHeaderFont,
                null,
                (button) -> MasteryChallenge.initializeMasteries());
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        refreshButton.render(sb);
        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "If an invalid date is picked, it will choose the first day of the chosen month",
                400.0F * Settings.xScale,
                525.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);

        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "Refresh button must be clicked to refresh your mastery data to the options chosen",
                575.0F * Settings.xScale,
                430.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);

        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "Challenge Start Day:",
                400.0F * Settings.xScale,
                600.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);
        float dropdownX = 650.0F;
        dayDropdown.render(sb, dropdownX * Settings.xScale, 600.0F * Settings.yScale);

        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "Challenge Start Month:",
                400.0F * Settings.xScale,
                675.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);
        monthDropdown.render(sb, dropdownX * Settings.xScale, 675.0F * Settings.yScale);

        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "Challenge Start Year:",
                400.0F * Settings.xScale,
                750.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);
        yearDropdown.render(sb, dropdownX * Settings.xScale, 750.0F * Settings.yScale);

        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "Minimum Copies of Card:",
                950.0F * Settings.xScale,
                675.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);
        cardCountDropdown.render(sb,1250.0F * Settings.xScale, 675.0F * Settings.yScale);

        FontHelper.renderSmartText(sb,
                FontHelper.tipBodyFont,
                "Minimum Ascension Level:",
                950.0F * Settings.xScale,
                750.0F * Settings.yScale,
                420.0F * Settings.xScale,
                30.0f * Settings.yScale,
                Settings.CREAM_COLOR);
        ascDropdown.render(sb,1250.0F * Settings.xScale, 750.0F * Settings.yScale);
    }

    @Override
    public void update() {
        dayDropdown.update();
        monthDropdown.update();
        yearDropdown.update();
        refreshButton.update();
        ascDropdown.update();
        cardCountDropdown.update();

        if (InputHelper.pressedEscape) {
            BaseMod.modSettingsUp = false;
            InputHelper.pressedEscape = false;
        }

        if (!BaseMod.modSettingsUp) {
            this.waitingOnEvent = false;
            Gdx.input.setInputProcessor(this.oldInputProcessor);
            CardCrawlGame.mainMenuScreen.lighten();
            CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
            CardCrawlGame.cancelButton.hideInstantly();
            this.isUp = false;
        }
    }

    public void changedSelectionTo(DropdownMenu dropdownMenu, int i, String s) {
        if (dropdownMenu == dayDropdown){
            int startMonth = MasteryChallenge.config.getIntKeyOrSetDefault(Config.startMonth, 1);
            int startYear = MasteryChallenge.config.getIntKeyOrSetDefault(Config.startYear, 2023);
            ZonedDateTime startDate;
            try {
                startDate = ZonedDateTime.of(startYear, startMonth, Integer.parseInt(s), 0, 0, 0, 0, ZoneId.systemDefault());
                MasteryChallenge.config.setIntKey(Config.startDay, startDate.getDayOfMonth());
            } catch (Exception ex) {
                MasteryChallenge.config.setIntKey(Config.startDay, 1);
                dayDropdown.setSelectedIndex(0);
            }
        } else if (dropdownMenu == monthDropdown){
//            int startDay = MasteryChallenge.config.getIntKeyOrSetDefault(Config.startDay, 1);
//            int startYear = MasteryChallenge.config.getIntKeyOrSetDefault(Config.startYear, 2023);
//            ZonedDateTime startDate;
//
//            try {
//                startDate = ZonedDateTime.of(startYear, Integer.parseInt(s), startDay, 0, 0, 0, 0, ZoneId.systemDefault());
//                MasteryChallenge.config.setIntKey(Config.startMonth, startDate.getMonthValue());
//            } catch (Exception ex) {
//                MasteryChallenge.config.setIntKey(Config.startMonth, 1);
//                dayDropdown.setSelectedIndex(0);
//            }
            MasteryChallenge.config.setIntKey(Config.startMonth, Integer.parseInt(s));
        } else if (dropdownMenu == yearDropdown){
            MasteryChallenge.config.setIntKey(Config.startYear, Integer.parseInt(s));
        } else if (dropdownMenu == ascDropdown){
            MasteryChallenge.config.setIntKey(Config.minAscLevel, Integer.parseInt(s));
        } else if (dropdownMenu == cardCountDropdown){
            MasteryChallenge.config.setIntKey(Config.cardCount, Integer.parseInt(s));
        }
    }
}
