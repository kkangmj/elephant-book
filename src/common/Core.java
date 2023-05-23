package common;

import java.io.File;

public class Core {

    private final static String BASE_PATH = "src/resources/";
    private final static String ORIGINAL_FILE = "eid_tags.txt";
    private final static String ENCODED_FILE = "eid_tags_encoded.bin";
    private final static String DECODED_FILE = "eid_tags_decoded.txt";

    public static String getOriginalFile() {
        return new File(BASE_PATH + ORIGINAL_FILE).getAbsolutePath();
    }

    public static String getEncodedFile() {
        return new File(BASE_PATH + ENCODED_FILE).getAbsolutePath();
    }

    public static String getDecodedFile() {
        return new File(BASE_PATH + DECODED_FILE).getAbsolutePath();
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
