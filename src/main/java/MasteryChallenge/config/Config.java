package MasteryChallenge.config;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private SpireConfig spireConfig;
    public static final String startDay = "startDay";
    public static final String startMonth = "startMonth";
    public static final String startYear = "startYear";
    public static final String minAscLevel = "minAscLevel";
    public static final String cardCount = "cardCount";

    public Config() {
        Properties defaults = new Properties();
        defaults.put(startDay, 1);
        defaults.put(startMonth, 1);
        defaults.put(startYear, 2023);
        defaults.put(minAscLevel, 20);
        defaults.put(cardCount, 2);

        try {
            spireConfig = new SpireConfig("MasteryChallenge", "config", defaults);
            spireConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIntKey(String key, int val){
        spireConfig.setInt(key, val);

        try {
            spireConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIntKeyOrSetDefault(String key, int defaultValue){
        if (!spireConfig.has(key)){
            setIntKey(key, defaultValue);
        }
        return getIntKey(key);
    }

    public int getIntKey(String key){
        return spireConfig.getInt(key);
    }
}
