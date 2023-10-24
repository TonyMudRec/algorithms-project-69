package hexlet.code;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    private static Map<String, List<String>> indexMap = new HashMap<>();

    public static List<String> search(List<Map<String, String>> docs, String searchTarget) {
        if (docs.isEmpty()) {
            return new ArrayList<>();
        }
        var partsOfTarget = searchTarget.split(" ");
        if (partsOfTarget.length > 1) {
            return getRelevanceListForWord(docs, searchTarget);
        }
        return getIndexMap(docs).get(searchTarget);
    }

    public static double getTF(Map<String, String> doc, String searchTarget) {
        var wordInDocCount = 0;
        var words = doc.get("text").split(" ");
        for (var word : words) {
            word = wordConvert(word);
            if (word.equals(wordConvert(searchTarget))) {
                wordInDocCount++;
            }
        }

        return (double) wordInDocCount / words.length;
    }

    public static double getIDF(List<Map<String, String>> docs, String searchTarget) {
        var termCount = 0;
        for (var doc : docs) {
            var words = doc.get("text").split(" ");
            for (var word : words) {
                word = wordConvert(word);
                if (word.equals(wordConvert(searchTarget))) {
                    termCount++;
                    break;
                }
            }
        }
        return Math.log(1 + (docs.size() - termCount + 1) / (termCount + 0.5));
    }

    public static double getTFIDF(double TF, double IDF) {
        return TF * IDF;
    }

    public static List<String> getRelevanceListForWord(List<Map<String, String>> docs, String searchTarget) {

        TreeMap<Double, String> relevanceMap = new TreeMap<>();
        for (Map<String, String> doc : docs) {
            var words = doc.get("text").split(" ");
            var sumOfRelevance = 0.0;
            for (var word : words) {
                word = wordConvert(word);
                var partsOfTarget = searchTarget.split(" ");
                for (var part : partsOfTarget) {
                    if (word.equals(part)) {
                        sumOfRelevance += getTFIDF(getTF(doc, word), getIDF(docs, word));
                    }
                }
            }
            if (sumOfRelevance > 0) {
                relevanceMap.put(sumOfRelevance, doc.get("id"));
            }
        }
        List<String> result = new ArrayList<>(relevanceMap.values().stream().toList());
        Collections.reverse(result);
        return result;
    }

    public static String wordConvert(String word) {
        return Pattern.compile("[\\w']+")
                .matcher(word)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining());
    }

    public static Map<String, List<String>> getIndexMap(List<Map<String, String>> docs) {
        if (!indexMap.isEmpty()) {
            return indexMap;
        }

        Map<String, List<String>> result = new HashMap<>();

        for (Map<String, String> doc : docs) {
            if (doc.isEmpty()) {
                continue;
            }
            var words = doc.get("text").split(" ");
            for (var word : words) {
                word = wordConvert(word);
                result.put(word, getRelevanceListForWord(docs, word));
            }
        }
        indexMap = result;
        return result;
    }
}
