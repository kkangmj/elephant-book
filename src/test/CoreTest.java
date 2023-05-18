package test;

import static common.Core.encode;

public class CoreTest {

    public static void main(String[] args) {
        // Test encodeToBinary()
        int[] tags = {1402221, 1471124};
        int[] binaryTags = encode(tags);

        // 1402221 -> 00010101 01100101 01101101 => 01010101 01001010 11101101 (85 74 128+109)
        check(binaryTags[0], 0);
        check(binaryTags[1], 85);
        check(binaryTags[2], 74);
        check(binaryTags[3], 237);

        // 1471124 - 1402221 = 68903 -> 00000001 00001101 00100111 => 00000101 00011010 10100111 (4 26 128+39)
        check(binaryTags[4], 0);
        check(binaryTags[5], 4);
        check(binaryTags[6], 26);
        check(binaryTags[7], 167);
    }

    private static void check(int a, int b) {
        if (a == b) {
            System.out.println("[TRUE] " + a + ", " + b);
        } else {
            System.out.println("[FALSE] " + a + ", " + b);
        }
    }
}
