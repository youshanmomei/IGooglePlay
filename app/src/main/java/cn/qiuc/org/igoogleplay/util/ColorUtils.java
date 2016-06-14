package cn.qiuc.org.igoogleplay.util;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by admin on 2016/6/15.
 */
public class ColorUtils {
    public static int genrateBeautifulColor() {
        Random random = new Random();

        int red = 30 + random.nextInt(200);
        int green = 30 + random.nextInt(200);
        int blue = 30 + random.nextInt(200);

        return Color.rgb(red,green, blue);
    }
}
