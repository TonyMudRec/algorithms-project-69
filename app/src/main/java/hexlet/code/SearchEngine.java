package hexlet.code;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    private static Map<String, List<String>> indexMap;
    private static List<Map<String, Double>> allWordsTFList;


    public static List<String> search(List<Map<String, String>> docs, String target) {
        if (docs == null || docs.isEmpty() || target == null || target.isEmpty()) {
            return new ArrayList<>();
        }

        var targetArr = target.trim().split(" ");

        if (targetArr.length == 1) {
            return getIndexMap(docs).getOrDefault(targetArr[0], new ArrayList<>());
        }

        return getCalculatedResult(targetArr, docs);
    }


    public static double getTFIDF(int allDocs, int termCount, double tf) {
        var idf = Math.log(1 + (allDocs - termCount + 1) / (termCount + 0.5));
        return idf * tf;
    }

    private static List<String> getCalculatedResult(String[] targets, List<Map<String, String>> docs) {
        var resultSortedMap = new TreeMap<Double, String>(Comparator.reverseOrder());
        for (String target : targets) {
            for (Map<String, Double> docMap : getAllWordsTFList(docs)) {
                if (docMap.containsKey(target)) {
                    resultSortedMap.put(
                            getTFIDF(docs.size(),
                                    getIndexMap(docs).get(target).size(),
                                    docMap.get(target)),
                            target);
                }
            }
        }
        return resultSortedMap.values().stream().toList();
    }


    public static String wordConvert(String word) {
        return Pattern.compile("[\\w']+")
                .matcher(word)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining());
    }

    public static List<Map<String, Double>> getAllWordsTFList(List<Map<String, String>> docs) {
        allWordsTFList = new LinkedList<>();

        for (var doc : docs) {
            Map<String, Double> docMap = new HashMap<>();
            var words = doc.get("text").trim().split(" ");
            var size = words.length;

            for (var word : words) {
                word = wordConvert(word);
                var tf = docMap.getOrDefault(word, 0.0);
                docMap.put(word, tf + (1 / size));
            }

            allWordsTFList.add(docMap);
        }

        return allWordsTFList;
    }

    public static List<String> getListOfRelevance(String word, List<Map<String, String>> docs) {
        var resultSortedMap = new TreeMap<Double, String>(Comparator.reverseOrder());
        for (int i = 0; i < allWordsTFList.size(); i++) {
            if (allWordsTFList.get(i).containsKey(word)) {
                resultSortedMap.put(allWordsTFList.get(i).get(word), docs.get(i).get("id"));
            }
        }

        return resultSortedMap.values().stream().toList();
    }

    public static Map<String, List<String>> getIndexMap(List<Map<String, String>> docs) {
        if (indexMap != null) {
            return indexMap;
        }

        indexMap = new HashMap<>();
        for (Map<String, Double> tfMap : getAllWordsTFList(docs)) {
            for (var word : tfMap.keySet()) {
                indexMap.put(word, getListOfRelevance(word, docs));
            }
        }

        return indexMap;
    }
}
