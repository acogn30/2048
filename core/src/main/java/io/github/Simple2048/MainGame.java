package io.github.Simple2048;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        scoreManager = new ScoreManager();
        board = new Board(scoreManager); // 將遊戲邏輯放在 Board 類中
        shapeRenderer = new ShapeRenderer();

        // 設定輸入處理
        Gdx.input.setInputProcessor(new InputAdapter() {
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
    }

    @Override
    public void render() {
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
