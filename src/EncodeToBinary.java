import java.io.*;
import java.util.StringTokenizer;

import static common.Core.*;

public class EncodeToBinary {

    public static void main(String[] args) {
        String inputFilePath = getInputFilePath();
        String outputFilePath = getOutputFilePath("eid_tags_compressed.bin");

        try (
                BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))
        ) {
            String line;
            final long startTime = System.nanoTime();

            while ((line = br.readLine()) != null) {
                StringTokenizer lineTokenizer = new StringTokenizer(line, "\t");

                String topic = lineTokenizer.nextToken();
                String tags = lineTokenizer.nextToken();

                int topicLength = topic.length();

                StringTokenizer tagsTokenizer = new StringTokenizer(tags, ",");
                int tagsLength = tagsTokenizer.countTokens();

                // Write topic and tag length for decode
                bw.write(topicLength);
                bw.write(tagsLength);

                // Write topic
                bw.write(topic);

                int[] tagTokens = new int[tagsLength];
                int i = 0;

                while (tagsTokenizer.hasMoreTokens()) {
                    tagTokens[i] = Integer.parseInt(tagsTokenizer.nextToken());
                    i++;
                }

                // Encode tag
                int[] encodedTag = encode(tagTokens);

                // Write tag
                for (int v : encodedTag) {
                    if (v != 0) bw.write(v);
                }
            }

            bw.flush();

            final long endTime = System.nanoTime();
            System.out.println("Completed compression eid_tags_gap_vb.txt file. Total execution time: " + (endTime - startTime) / 1000000 + "ms");
        } catch (IOException e) {
            System.out.println("파일 읽기에 실패했습니다. Cause: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("데이터 형식이 유효하지 않습니다. Cause: " + e.getMessage());
        }
    }
}
