package io.github;

import com.badlogic.gdx.graphics.Color;

public class ColorMaker {
       public static Color getTileColor(int value) {
        if (value == 0) {
            return new Color(0.8f, 0.8f, 0.8f, 1f); // 空白的顏色
        }
        // 使用數學計算根據數值動態生成顏色
        int exponent = (int)(Math.log(value) / Math.log(2));
        // 根據 exponent 來設定 hue 值，這裡只是個示範：讓數值越大，色相越偏暖
        float hue =(360 - (exponent - 1) * 20) % 360;
        if (hue < 0) hue += 360;
        return hslToRgb(hue, 0.7f, 0.6f);
    }

    // 一個簡單的 HSL 轉換成 RGB 的方法
    public static Color hslToRgb(float h, float s, float l) {
        h = h % 360;
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = l - c / 2;
        float r = 0, g = 0, b = 0;
        if (h < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (h < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (h < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (h < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (h < 300) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }
        return new Color(r + m, g + m, b + m, 1f);
    }
}
