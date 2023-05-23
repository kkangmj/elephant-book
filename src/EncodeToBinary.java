import java.io.*;
import java.util.StringTokenizer;

import static common.Core.*;

public class EncodeToBinary {

    public static void main(String[] args) {
        String inputFilePath = getOriginalFile();
        String outputFilePath = getEncodedFile();

        try (
                BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
                DataOutputStream os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFilePath)))
        ) {
            String line;
            final long startTime = System.nanoTime();

            while ((line = br.readLine()) != null) {
                StringTokenizer lineTokenizer = new StringTokenizer(line, "\t");

                String topic = lineTokenizer.nextToken();
                String tags = lineTokenizer.nextToken();

                int topicLength = topic.length();

                StringTokenizer tagsTokenizer = new StringTokenizer(tags, ",");
                int tagLength = tagsTokenizer.countTokens();

                // Write topic and tag length for decode
                os.writeByte(topicLength);
                os.writeByte(tagLength);

                // Write topic
                os.writeChars(topic);

                int[] tagTokens = new int[tagLength];
                int i = 0;

                while (tagsTokenizer.hasMoreTokens()) {
                    tagTokens[i] = Integer.parseInt(tagsTokenizer.nextToken());
                    i++;
                }

                // Encode tag
                int[] encodedTag = encode(tagTokens);

                // Write tag
                for (int v : encodedTag) {
                    if (v != 0) os.writeByte(v);
                }
            }

            os.flush();

            final long endTime = System.nanoTime();
            System.out.println("[Encoding Completed] eid_tags.txt => eid_tags_encoded.bin\nTotal execution time: " + (endTime - startTime) / 1000000 + "ms");
        } catch (IOException e) {
            System.out.println("파일 읽기/쓰기에 실패했습니다. Cause: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("데이터 형식이 유효하지 않습니다. Cause: " + e.getMessage());
        }
    }
}
