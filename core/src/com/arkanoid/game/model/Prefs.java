package com.arkanoid.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.arkanoid.game.Constants.*;

public class Prefs {

    public Preferences file;

    public List<HighScore> highScores;

    public Prefs() {
        this.file = Gdx.app.getPreferences(PREF_FILE);
    }

    public Preferences getFile() {
        return file;
    }

    public void setFile(Preferences file) {
        this.file = file;
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<HighScore> highScores) {
        this.highScores = highScores;
    }

    /**
     * Init high scores
     */
    public void initHighScores() {
        Json json = new Json();
        String jsonString = this.file.getString(HIGH_SCORES_KEY, "[]");
        List<HighScore> highScores = json.fromJson(ArrayList.class, HighScore.class, jsonString);
        highScores.sort(Comparator.comparing(HighScore::getScore).reversed());
        this.highScores = highScores;
    }

    /**
     * Check and save high scores, if necessary
     */
    public void checkAndSaveHighScores(int currentScore) {
        if (this.highScores.size() < HIGH_SCORES_SIZE || currentScore > this.highScores.getLast().getScore()){
            Json json = new Json();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            HighScore newHighScore = new HighScore(currentScore, now.format(formatter));
            this.highScores.add(newHighScore);
            this.highScores.sort(Comparator.comparing(HighScore::getScore).reversed());
            if (this.highScores.size() > HIGH_SCORES_SIZE) {
                this.highScores.removeLast();
            }

            String jsonString = json.toJson(this.highScores);
            this.file.putString(HIGH_SCORES_KEY, jsonString);
            this.file.flush();
        }
    }
}
