package hexlet.code;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    private static Map<String, List<String>> indexMap;

    private static HashSet<String> allWords;

    public static List<String> search(List<Map<String, String>> docs, String target) {
        if (docs == null || docs.isEmpty() || target == null || target.isEmpty()) {
            return null;
        }

        return getResultOfSearch(docs, target);
    }


    public static double getTFIDF(int allDocs, int termCount, double tf) {
        var idf = Math.log(1 + (allDocs - termCount + 1) / (termCount + 0.5));
        return idf * tf;
    }

    public static List<String> getResultOfSearch(List<Map<String, String>> docs, String target) {
        var partsOfTarget = target.trim().split(" ");

        if (partsOfTarget.length == 1) {
            return getIndexMap(docs).get(target);
        }

        LinkedList<List<String>> listOfResults = new LinkedList<>();

        for (var targetPart : partsOfTarget) {
            var tmp = getIndexMap(docs).get(targetPart);
            if (!tmp.isEmpty()) {
                listOfResults.add(tmp);
            }
        }

        return getResultList(listOfResults);
    }

    private static List<String> getResultList(LinkedList<List<String>> listOfResults) {
        var maxListSize = 0;
        var biggestListIndex = 0;
        for (int i = 0; i < listOfResults.size(); i++) {
            var partOfResult = listOfResults.get(i);
            if (maxListSize < partOfResult.size()) {
                biggestListIndex = i;
                maxListSize = partOfResult.size();
            }
        }

        List<String> result = new ArrayList<>();
        LinkedList<String> tmp = new LinkedList<>(listOfResults.get(biggestListIndex));
        for (var docName : tmp) {
            var isContains = true;
            for (var partOfResult : listOfResults) {
                if (!partOfResult.contains(docName)) {
                    isContains = false;
                }
            }
            if (isContains) {
                result.add(docName);
            }
        }

        return result;
    }


    public static String wordConvert(String word) {
        return Pattern.compile("[\\w']+")
                .matcher(word)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining());
    }

    public static List<HashMap<String, Double>> getTFList(List<Map<String, String>> docs) {
        allWords = new HashSet<>();
        List<HashMap<String, Double>> result = new ArrayList<>();
        for (var doc : docs) {
            var words = doc.get("text").trim().split(" ");
            HashMap<String, Double> tmpMap = new HashMap<>();
            var size = words.length;
            for (var word : words) {
                word = wordConvert(word);
                allWords.add(word);
                if (!tmpMap.containsKey(word)) {
                    tmpMap.put(word, 1.0);
                } else {
                    var count = tmpMap.get(word);
                    tmpMap.put(word, ++count);
                }
            }
            for (Map.Entry<String, Double> entry : tmpMap.entrySet()) {
                entry.setValue(entry.getValue() / size);
            }
            result.add(tmpMap);
        }
        return result;
    }

    public static List<String> getListOfRelevance(String word, List<HashMap<String, Double>> listOfUsages, List<Map<String, String>> docs) {
        List<String> result = new ArrayList<>();
        var allDocsSize = docs.size();
        for (int i = 0; i < allDocsSize; i++) {
            var usagesMap = listOfUsages.get(i);
            var docName = docs.get(i).get("id");
            if (usagesMap.containsKey(word)) {
                result.add(docName);
            }
        }

        var resultSize = result.size();
        if (resultSize < 2) {
            return result;
        }

        NavigableMap<Double, String> treemap = new TreeMap<>(Collections.reverseOrder());

        for (int i = 0; i < allDocsSize; i++) {
            var docName = docs.get(i).get("id");
            if (result.contains(docName)) {
                var tfidf = getTFIDF(allDocsSize, resultSize, listOfUsages.get(i).getOrDefault(word, 0.0));
                treemap.put(tfidf, docName);
            }
        }
        result.clear();
        result.addAll(treemap.values());
        return result;
    }

    public static Map<String, List<String>> getIndexMap(List<Map<String, String>> docs) {
        if (indexMap != null) {
            return indexMap;
        }

        var tfList = getTFList(docs);
        Map<String, List<String>> result = new HashMap<>();
        for (var word : allWords) {
            var relevance = getListOfRelevance(word, tfList, docs);
            result.put(word, relevance);
        }

        indexMap = result;
        return indexMap;
    }
}
