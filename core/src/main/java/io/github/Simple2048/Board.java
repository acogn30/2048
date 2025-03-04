package io.github.Simple2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {

    public static final int SIZE = 4; // 棋盤尺寸 4x4
    private int[][] grid;
    private Random random = new Random();
    private ScoreManager scoreManager;



    public Board(ScoreManager scoreManager) {
        grid = new int[SIZE][SIZE];
        this.scoreManager = scoreManager;
        // 初始化時新增兩個隨機方塊
        addRandomTile();
        addRandomTile();
    }

    public Board() {
        this(new ScoreManager());
    }

    // 取得指定位置的數值
    public int getCell(int i, int j) {
        return grid[i][j];
    }

    // 新增一個方法，方便取得目前分數（或直接通過 ScoreManager）
    public int getScore() {
        return scoreManager.getCurrentScore();
    }

    // 向左滑動與合併
    public boolean slideLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            int[] row = grid[i];
            int[] newRow = new int[SIZE];
            int pos = 0;
            // 將非零元素向左壓縮
            for (int j = 0; j < SIZE; j++) {
                if (row[j] != 0) {
                    newRow[pos++] = row[j];
                }
            }
            // 合併相鄰且相同的數字
            for (int j = 0; j < SIZE - 1; j++) {
                if (newRow[j] != 0 && newRow[j] == newRow[j + 1]) {
                    newRow[j] *= 2;
                    // 每次合併，把合併後的數值交給 ScoreManager
                    scoreManager.addScore(newRow[j]);
                    newRow[j + 1] = 0;
                    moved = true;
                }
            }
            // 再次壓縮（合併後可能產生空格）
            int[] finalRow = new int[SIZE];
            pos = 0;
            for (int j = 0; j < SIZE; j++) {
                if (newRow[j] != 0) {
                    finalRow[pos++] = newRow[j];
                }
            }
            // 如果該行改變了，更新並標記已移動
            if (!Arrays.equals(grid[i], finalRow)) {
                grid[i] = finalRow;
                moved = true;
            }
        }
        return moved;
    }

    // 向右滑動：先反轉每一行，調用 slideLeft()，再反轉回來
    public boolean slideRight() {
        reverseRows();
        boolean moved = slideLeft();
        reverseRows();
        return moved;
    }

    // 向上滑動：將棋盤逆時針旋轉，調用 slideLeft()，再旋轉回來
    public boolean slideUp() {
        rotateLeft();
        boolean moved = slideLeft();
        rotateRight();
        return moved;
    }

    // 向下滑動：將棋盤順時針旋轉，調用 slideLeft()，再旋轉回來
    public boolean slideDown() {
        rotateRight();
        boolean moved = slideLeft();
        rotateLeft();
        return moved;
    }

    // 新增隨機方塊，90% 機率新增 2，10% 機率新增 4
    public void addRandomTile() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    emptyCells.add(new int[] { i, j });
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            int[] pos = emptyCells.get(random.nextInt(emptyCells.size()));
            grid[pos[0]][pos[1]] = random.nextDouble() < 0.9 ? 2 : 4;
        }
    }

    // 輔助方法：反轉每一行
    private void reverseRows() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE / 2; j++) {
                int temp = grid[i][j];
                grid[i][j] = grid[i][SIZE - j - 1];
                grid[i][SIZE - j - 1] = temp;
            }
        }
    }

    // 輔助方法：將棋盤順時針旋轉 90 度
    private void rotateRight() {
        int[][] newGrid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newGrid[j][SIZE - 1 - i] = grid[i][j];
            }
        }
        grid = newGrid;
    }

    // 輔助方法：將棋盤逆時針旋轉 90 度
    private void rotateLeft() {
        int[][] newGrid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newGrid[SIZE - 1 - j][i] = grid[i][j];
            }
        }
        grid = newGrid;
    }

    public boolean isGameOver() {
        // 如果有空格，就還沒結束
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }
        // 檢查水平相鄰
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (grid[i][j] == grid[i][j + 1]) {
                    return false;
                }
            }
        }
        // 檢查垂直相鄰
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE - 1; i++) {
                if (grid[i][j] == grid[i + 1][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
}
