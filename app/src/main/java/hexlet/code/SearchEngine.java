package hexlet.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    private static final int BASIC_TEXT_CAPACITY = 20;

    public static List<String> search(List<Map<String, String>> docs, String pattern) {
        if (docs.isEmpty()) {
            return new ArrayList<>();
        }
        var relevanceList = getRelevanceList(BASIC_TEXT_CAPACITY);

        var relevanceCounter = 0;

        for (Map<String, String> doc : docs) {
            if (doc.isEmpty()) {
                continue;
            }
            var words = doc.get("text").split(" ");
            var partsOfPattern = pattern.split(" ");
            for (var word : words) {
                for (var partOfPattern : partsOfPattern) {
                    if (wordConvert(word).equals(partOfPattern)) {
                        relevanceCounter++;
                    }
                }
            }
            if (relevanceCounter > 0) {
                relevanceList.get(relevanceCounter).add(doc.get("id"));
                relevanceCounter = 0;
            }
        }
        return buildResult(relevanceList);
    }

    public static List<String> buildResult(List<LinkedList<String>> list) {
        LinkedList<String> result = new LinkedList<>();
        for (var docNames : list) {
            for (var doc : docNames) {
                result.addFirst(doc);
            }
        }
        return result;
    }

    public static List<LinkedList<String>> getRelevanceList(int size) {
        List<LinkedList<String>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new LinkedList<>());
        }
        return list;
    }

    public static String wordConvert(String word) {
        return Pattern.compile("\\w+")
                .matcher(word)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining());
    }
}
