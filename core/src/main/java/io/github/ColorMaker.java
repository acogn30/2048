package io.github;

import com.badlogic.gdx.graphics.Color;

public class ColorMaker {
    private static final Color[] baseColors = {
            new Color(1f, 0.6f, 0.6f, 1f), // 較亮的紅色
            new Color(1f, 0.75f, 0.5f, 1f), // 較亮的橙色
            new Color(1f, 1f, 0.7f, 1f), // 較亮的黃色
            new Color(0.6f, 1f, 0.6f, 1f), // 較亮的綠色
            new Color(0.6f, 0.6f, 1f, 1f), // 較亮的藍色
    };

    public static Color getTileColor(int value) {
        if (value == 0) {
            return new Color(0.8f, 0.8f, 0.8f, 1f);
        }
        int exponent = (int) (Math.log(value) / Math.log(2));
        int index = exponent % baseColors.length;
        int iteration = exponent / baseColors.length;

        Color c = new Color(baseColors[index]); // 複製
        // 讓每多一圈就加深
        float lerpAmount = Math.min(iteration * 0.1f, 1f);
        c.lerp(Color.BLACK, lerpAmount);

        return c;
    }
}
