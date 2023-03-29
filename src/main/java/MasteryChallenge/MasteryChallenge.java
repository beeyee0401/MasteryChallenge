package MasteryChallenge;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.stats.RunData;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@SpireInitializer
public class MasteryChallenge implements PostInitializeSubscriber {
    private static final Logger logger = LogManager.getLogger(MasteryChallenge.class.getName());
    public static HashMap<String, String> cardAndRunMap;
    public static HashMap<String, String> relicAndRunMap;

    public MasteryChallenge() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new MasteryChallenge();
    }

    public void receivePostInitialize() {
        initializeMasteries();
    }

    public static void initializeMasteries(){
        cardAndRunMap = new HashMap<>();
        relicAndRunMap = new HashMap<>();
        Gson gson = new Gson();

        FileHandle[] subfolders = Gdx.files.local("runs" + File.separator).list();
        ArrayList<RunData> runFiles2023 = new ArrayList<>();
        for (FileHandle subFolder : subfolders) {
            if (CardCrawlGame.saveSlot == 0) {
                // Profile 0 runs, which has no prefix
                if (subFolder.name().contains("0_") || subFolder.name().contains("1_") || subFolder.name().contains("2_")) {
                    continue;
                }
            } else {
                if (!subFolder.name().contains(CardCrawlGame.saveSlot + "_")) {
                    continue;
                }
            }

            for (FileHandle file : subFolder.list()) {
                try {
                    RunData data = gson.fromJson(file.readString(), RunData.class);
                    if (data != null && data.timestamp == null) {
                        data.timestamp = file.nameWithoutExtension();
                        String exampleDaysSinceUnixStr = "17586";
                        boolean assumeDaysSinceUnix = data.timestamp.length() == exampleDaysSinceUnixStr.length();
                        if (assumeDaysSinceUnix) {
                            try {
                                long days = Long.parseLong(data.timestamp);
                                data.timestamp = Long.toString(days * 86400L);
                            } catch (NumberFormatException ex) {
                                data = null;
                            }
                        }
                    }
                    if (data != null){
                        long seconds = Long.parseLong(data.timestamp);
                        int year = Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault()).getYear();
                        if (year < 2023){
                            continue;
                        }
                        runFiles2023.add(data);
                    }
                }
                catch (JsonSyntaxException ex){
                    logger.info("Failed to load RunData from JSON file: " + file.path());
                }
            }
        }

        runFiles2023.sort(RunData.orderByTimestampDesc);
        Collections.reverse(runFiles2023);
        for (RunData data : runFiles2023) {
            if (data.ascension_level != 20) {
                continue;
            }

            if (!data.victory) {
                continue;
            }

            updateMasteries(data.relics, data.master_deck, data.timestamp);
        }

        System.out.println("card masteries: " + cardAndRunMap);
        System.out.println("relic masteries: " + relicAndRunMap);
    }

    public static void updateMasteries(List<String> relicIds, List<String> cardIdsForMetrics, String runTimestamp){
        for (String relic : relicIds) {
            if (!relicAndRunMap.containsKey(relic)) {
                relicAndRunMap.put(relic, runTimestamp);
            }
        }

        HashMap<String, Integer> cardCounts = new HashMap<>();
        for (String s : cardIdsForMetrics) {
            String cardName = s.split("\\+")[0];
            cardCounts.put(cardName, cardCounts.getOrDefault(cardName, 0) + 1);
        }

        for (String card : cardCounts.keySet()) {
            if (cardCounts.get(card) >= 2 && !cardAndRunMap.containsKey(card)) {
                cardAndRunMap.put(card, runTimestamp);
            }
        }
    }
}
