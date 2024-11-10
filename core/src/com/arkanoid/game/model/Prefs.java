package com.arkanoid.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.arkanoid.game.Constants.*;

public class Prefs {

    public Preferences file;

    public List<Integer> highScores;

    public Prefs() {
        this.file = Gdx.app.getPreferences(PREF_FILE);
    }

    public Preferences getFile() {
        return file;
    }

    public void setFile(Preferences file) {
        this.file = file;
    }

    public List<Integer> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<Integer> highScores) {
        this.highScores = highScores;
    }

    /**
     * Init high scores
     */
    public void initHighScores() {
        List<Integer> highScores = new ArrayList<>();
        Json json = new Json();
        int[] ints = json.fromJson(int[].class, this.file.getString(HIGH_SCORES_KEY));
        if (ints != null) {
            for (int i : ints) {
                highScores.add(i);
            }
        }
        highScores.sort(Collections.reverseOrder());
        this.highScores = highScores;
    }

    /**
     * Check and save high scores, if necessary
     */
    public void checkAndSaveHighScores(int currentScore) {
        if (this.highScores.size() < HIGH_SCORES_SIZE || currentScore > this.highScores.getLast()){
            Json json = new Json();
            this.highScores.add(currentScore);
            this.highScores.sort(Collections.reverseOrder());
            if (this.highScores.size() > HIGH_SCORES_SIZE) {
                this.highScores.removeLast();
            }
            int[] array = new int[this.highScores.size()];
            for(int i = 0; i < this.highScores.size(); i++) array[i] = this.highScores.get(i);
            this.file.putString(HIGH_SCORES_KEY, json.toJson(array));
            this.file.flush();
        }
    }
}
