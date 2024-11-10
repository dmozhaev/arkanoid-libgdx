package com.arkanoid.game.model;

import java.time.LocalDateTime;

public class HighScore {

    public Integer score;

    public String dateTime;

    public HighScore() {
    }

    public HighScore(Integer score, String dateTime) {
        this.score = score;
        this.dateTime = dateTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
