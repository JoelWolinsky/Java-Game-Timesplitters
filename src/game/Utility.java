package game;

import java.util.Random;

/**
 * This class is used globally as a collection of utility methods and tools such as returning random values in ranges
 * etc.
 */

public class Utility {

    public static int getRandomIntInRangeSeeded(int min, int max) {

        return new Random(System.currentTimeMillis()).nextInt(max - min) + min;
    }

    public static float getRandomFloatInRangeSeeded(float min, float max) {

        return min + new Random(System.currentTimeMillis()).nextFloat() * (max - min);
    }

    public static int getRandomElementInRangeSeeded(int[] array) {

        return array[new Random(System.currentTimeMillis()).nextInt(array.length)];
    }

    public static int getRandomIntInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static float getRandomFloatInRange(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

}
