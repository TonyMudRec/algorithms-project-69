package hexlet.code;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SearchEngine {

    public static List<String> search(List<Map<String, String>> docs, String pattern) {
        if (docs.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> result = new LinkedList<>();
        String[] words;
        for (Map<String, String> doc : docs) {
            if (doc.isEmpty()) {
                continue;
            }
            words = doc.get("text").split(" ");
            for (String word : words) {
                if (word.equals(pattern)) {
                    result.add(doc.get("id"));
                    break;
                }
            }
        }
        return result;
    }
}
