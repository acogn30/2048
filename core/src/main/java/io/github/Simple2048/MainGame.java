package io.github.Simple2048;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        board = new Board(); // 將遊戲邏輯放在 Board 類中
        shapeRenderer = new ShapeRenderer();

        
        // 設定輸入處理
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                boolean moved = false;
                switch(keycode) {
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
        for (int i = 0; i < Board.SIZE; i++){
            for (int j = 0; j < Board.SIZE; j++){
                int value = board.getCell(i, j);
                // 呼叫 ColorMaker 取得 tile 背景顏色
                Color tileColor = ColorMaker.getTileColor(value);
                shapeRenderer.setColor(tileColor);
                // 這裡假設每個 tile 的尺寸為 75x75，根據需要調整 x,y 座標
                shapeRenderer.rect(100 + j * 80, 400 - i * 80 - 75, 75, 75);
            }
        }
        shapeRenderer.end();


        batch.begin();
        // 根據 Board 狀態繪製棋盤
        for (int i = 0; i < Board.SIZE; i++){
            for (int j = 0; j < Board.SIZE; j++){
                int value = board.getCell(i, j);
                String text = value != 0 ? String.valueOf(value) : "";
                font.draw(batch, text, 100 + j * 80, 400 - i * 80);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
