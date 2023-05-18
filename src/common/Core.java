package common;

import java.io.File;

public class Core {

    private final static String BASE_PATH = "src/resources/";
    private final static String INPUT_FILE = "eid_tags.txt";

    public static String getInputFilePath() {
        return new File(BASE_PATH + INPUT_FILE).getAbsolutePath();
    }

    public static String getOutputFilePath(String fileName) {
        return new File(BASE_PATH + fileName).getAbsolutePath();
    }

    public static int[] encode(int[] tags) {
        int previous = 0;
        int[] encoded = new int[4 * tags.length];

        for (int i = 0; i < tags.length; i++) {
            int gap = tags[i] - previous;
            int index = 4 * i + 3;

            if (gap < 128) {
                encoded[index] = gap | 128;
            } else {
                while (gap >= 128) {
                    int mod = gap % 128;
                    int div = gap / 128;

                    if (index % 4 == 3) {
                        encoded[index] = mod | 128;
                    } else {
                        encoded[index] = mod;
                    }

                    gap = div;
                    index--;
                }

                encoded[index] = gap;
            }

            previous = tags[i];
        }

        return encoded;
    }
}
