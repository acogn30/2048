package io.github.Simple2048;

public class ScoreManager {
    private int currentScore;
    private int highScore;

    public ScoreManager() {
        currentScore = 0;
        highScore = loadHighScore();
    }

    // 當發生合併時調用
    public void addScore(int scoreToAdd) {
        currentScore += scoreToAdd;
        if (currentScore > highScore) {
            highScore = currentScore;
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    // 重置當前分數（例如開始新一局遊戲時）
    public void reset() {
        currentScore = 0;
    }

    // 載入歷史最高分，可以改為讀取文件或使用 Preferences 儲存
    private int loadHighScore() {
        // 範例中先返回 0，你可以改為從持久化儲存中讀取
        return 0;
    }

    // 儲存高分（可選）
    public void saveHighScore() {
        // 將 highScore 儲存到文件或 Preferences 中
    }
}
