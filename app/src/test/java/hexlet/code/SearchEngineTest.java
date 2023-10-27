package hexlet.code;

import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class SearchEngineTest {
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

    @Test
    void searchOneWordTest() {
        List<String> result = SearchEngine.search(docs, "pint");
        List<String> correct = List.of("doc1");

        assertThat(result).isEqualTo(correct);
    }

    @Test
    void searchMultipleWordsTest() {
        List<String> result = SearchEngine.search(docs, "shoot at me");
        List<String> correct = List.of("doc2", "doc4", "doc1");

        assertThat(result).isEqualTo(correct);
    }

    @Test
    void getIDFTest() {
        double result = SearchEngine.getIDF(docs, "shoot");

        assertThat(result).isCloseTo(0.125, withinPercentage(1));
    }

    @Test
    void searchWithEmptyDocTest() {
        List<String> result = SearchEngine.search(new ArrayList<Map<String, String>>(), "shoot");

        assertThat(result).isNullOrEmpty();
    }

    @Test
    void searchWithRelevanceOrder() {
        List<String> result = SearchEngine.search(docs, "shoot");
        List<String> correct = List.of("doc2", "doc4", "doc1");

        assertThat(result).isEqualTo(correct);
    }

    @Test
    void getIndexTest() {
        List<String> docCheckList1 = List.of("doc3");
        List<String> docCheckList2 = List.of("doc2", "doc4", "doc1");
        List<String> docCheckList3 = List.of();
        Map.Entry<String, List<String>> correct1 = new AbstractMap.SimpleEntry<>("shooter", docCheckList1);
        Map.Entry<String, List<String>> correct2 = new AbstractMap.SimpleEntry<>("shoot", docCheckList2);
        Map.Entry<String, List<String>> correct3 = new AbstractMap.SimpleEntry<>("me", docCheckList3);

        assertThat(SearchEngine.getIndexMap(docs))
                .contains(correct1)
                .contains(correct2)
                .contains(correct3);
        System.out.println(SearchEngine.getIndexMap(docs));
    }
}