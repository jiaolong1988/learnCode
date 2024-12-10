package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计每个字的数量
 * @author jiaolong
 * @date 2024/12/10 11:00
 **/
public class CharacterCount {
    public static void main(String[] args) {
        String filePath = "D:/test/huiyi.txt"; // 替换为你的文件路径
        Map<Character, Integer> charCountMap = new HashMap<>();

        // 读取文件并统计字数
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                //去除所有标点符号
                String regex = "[\\p{P}\\p{S}]";
                String result = line.replaceAll(regex, "");
                for (char c : result.toCharArray()) {
                    charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 按照字数进行降序排序
        List<Map.Entry<Character, Integer>> sortedEntries = charCountMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toList());

        // 输出结果
        for (Map.Entry<Character, Integer> entry : sortedEntries) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
