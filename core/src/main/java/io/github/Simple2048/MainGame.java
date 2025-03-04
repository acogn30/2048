package io.github.Simple2048;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ColorMaker;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class MainGame extends ApplicationAdapter {
    private Board board;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private ScoreManager scoreManager;
    private Stage stage;
    private Skin skin;
    private TextButton restartButton;
    private boolean gameOverDialogShown = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        scoreManager = new ScoreManager();
        board = new Board(scoreManager); // 將遊戲邏輯放在 Board 類中
        shapeRenderer = new ShapeRenderer();

        // 建立 Stage 與 Skin，並建立 Restart 按鈕
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // 請確保 uiskin.json 存在於 assets 資料夾
        restartButton = new TextButton("Restart", skin);
        restartButton.setSize(100, 50);

        float xPercent = 0.92f; // 代表螢幕寬度的 80%
        float yPercent = 0.72f; // 代表螢幕高度的 90%

        float x = Gdx.graphics.getWidth() * xPercent;
        float y = Gdx.graphics.getHeight() * yPercent;

        // 如果你希望按鈕的左下角對齊這個點，可以直接用 setPosition(x, y)。
        // 如果希望按鈕的右上角對齊 (x, y)，可以減去按鈕的寬高：
        x -= restartButton.getWidth();
        y -= restartButton.getHeight();

        restartButton.setPosition(x, y);
        // 當按下按鈕時，重置遊戲
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                restartGame();
            }
        });
        stage.addActor(restartButton);

        // 建立 InputMultiplexer 同時處理 Stage 和鍵盤輸入
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // 先處理 Stage 輸入

        multiplexer.addProcessor(new InputAdapter() { // 再處理鍵盤輸入
            @Override
            public boolean keyDown(int keycode) {
                boolean moved = false;
                switch (keycode) {
                    case Input.Keys.LEFT:
                        moved = board.slideLeft();
                        break;
                    case Input.Keys.RIGHT:
                        moved = board.slideRight();
                        break;
                    case Input.Keys.UP:
                        moved = board.slideUp();
                        break;
                    case Input.Keys.DOWN:
                        moved = board.slideDown();
                        break;
                }
                if (moved) {
                    board.addRandomTile();
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 先讓 stage 更新 (處理按鈕點擊等)
        stage.act(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 先用 ShapeRenderer 畫出每個 tile 的背景
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int value = board.getCell(i, j);
                // 呼叫 ColorMaker 取得 tile 背景顏色
                Color tileColor = ColorMaker.getTileColor(value);
                shapeRenderer.setColor(tileColor);
                // 假設每個 tile 的尺寸為 75x75，根據需要調整 x,y 座標
                float tileX = 100 + j * 80;
                float tileY = 400 - i * 80 - 75;
                float tileSize = 75;
                shapeRenderer.rect(tileX, tileY, tileSize, tileSize);
            }
        }
        shapeRenderer.end();

        batch.begin();
        // 用 GlyphLayout 計算文字寬高，以置中數字
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int value = board.getCell(i, j);
                String text = value != 0 ? String.valueOf(value) : "";
                if (!text.isEmpty()) {
                    // 取得 tile 的位置與尺寸（與上面一致）
                    float tileX = 100 + j * 80;
                    float tileY = 400 - i * 80 - 75;
                    float tileSize = 75;
                    // 計算 tile 的中心
                    float tileCenterX = tileX + tileSize / 2f;
                    float tileCenterY = tileY + tileSize / 2f;
                    // 使用 GlyphLayout 取得文字寬高
                    GlyphLayout layout = new GlyphLayout(font, text);
                    float textWidth = layout.width;
                    float textHeight = layout.height;
                    // 計算文字繪製的左下角座標，讓文字置中
                    float textX = tileCenterX - textWidth / 2f;
                    float textY = tileCenterY + textHeight / 2f; // 注意：文字是以基線繪製
                    font.draw(batch, text, textX, textY);
                }
            }
        }

        // 顯示分數與最高分（例如顯示在螢幕上方）
        font.draw(batch, "Score: " + scoreManager.getCurrentScore(), 20, 450);
        font.draw(batch, "High Score: " + scoreManager.getHighScore(), 20, 430);
        batch.end();

        // 如果遊戲結束，彈出對話框 (只彈一次)
        if (board.isGameOver() && !gameOverDialogShown) {
            gameOverDialogShown = true;
            showGameOverDialog();
        }

        // 最後畫出 Stage 上的 UI (按鈕與對話框)
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }

    private void showGameOverDialog() {
        Dialog gameOverDialog = new Dialog("Game Over", skin) {
            protected void result(Object object) {
                // 當玩家按下按鈕時，object 將返回對應的值
                if (object.equals(true)) {
                    restartGame();
                    gameOverDialogShown = false;
                }
            }
        };
        gameOverDialog.text("No more moves!\nStart next round?");
        gameOverDialog.button("Next Round", true);
        gameOverDialog.show(stage);
    }

    private void restartGame() {
        scoreManager.reset(); // 重置分數（你可以選擇保留 high score）
        board = new Board(scoreManager); // 重新建立棋盤
    }
}
