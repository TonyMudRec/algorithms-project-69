package hexlet.code;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    private static final int BASIC_TEXT_CAPACITY = 20;

    public static List<String> search(List<Map<String, String>> docs, String pattern) {
        if (docs.isEmpty()) {
            return new ArrayList<>();
        }

        return getRelevanceList(docs, pattern, BASIC_TEXT_CAPACITY);
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

    public static double getTFIDF(List<Map<String, String>> docs, String word) {
        int termCount = getIndex(docs).get(word).size();
        return Math.log(1 + (docs.size() - termCount + 1) / (termCount + 0.5));
    }

    public static List<String> getRelevanceList(List<Map<String, String>> docs, String pattern, int size) {
        List<LinkedList<String>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new LinkedList<>());
        }

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
                list.get(relevanceCounter).add(doc.get("id"));
                relevanceCounter = 0;
            }
        }
        return buildResult(list);
    }

    public static String wordConvert(String word) {
        return Pattern.compile("[\\w']+")
                .matcher(word)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining());
    }

    public static Map<String, List<String>> getIndex(List<Map<String, String>> docs) {
        Map<String, List<String>> result = new HashMap<>();
        for (Map<String, String> doc : docs) {
            if (doc.isEmpty()) {
                continue;
            }
            var words = doc.get("text").split(" ");
            for (var word : words) {
                result.put(wordConvert(word), getRelevanceList(docs, wordConvert(word), BASIC_TEXT_CAPACITY));
            }
        }
        return result;
    }
}
