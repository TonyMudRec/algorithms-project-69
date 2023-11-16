package hexlet.code;

import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        String doc1 = "I can't shoot straight unless I've had a pint! Pour me!";
        String doc2 = "Don't shoot shoot shoot that thing at me.";
        String doc3 = "I'm your shooter. It's me.";
        String doc4 = "shoot me, shoot me!";

        List<Map<String, String>> docs = List.of(
                Map.of("id", "doc1", "text", doc1),
                Map.of("id", "doc2", "text", doc2),
                Map.of("id", "doc3", "text", doc3),
                Map.of("id", "doc4", "text", doc4)
        );

        System.out.println(SearchEngine.search(docs, "shoot me")); // [doc4, doc2, doc1, doc3]
    }
}
