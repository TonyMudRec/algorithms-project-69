package hexlet.code;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class SearchEngineTest {
        // Текст документов
        String doc1 = "I can't shoot straight unless I've had a pint!";
        String doc2 = "Don't shoot shoot shoot that thing at me.";
        String doc3 = "I'm your shooter. It's me.";
        String doc4 = "shoot me, shoot me, shoot me!";

        // создание документа
        // документ имеет два атрибута "id" и "text"
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
        List<String> correct = List.of("doc4", "doc2", "doc3", "doc1");
        assertThat(result).isEqualTo(correct);
    }

    @Test
    void searchWithEmptyDocTest() {
        List<String> result = SearchEngine.search(new ArrayList<Map<String, String>>(), "shoot");
        assertThat(result).isNullOrEmpty();
    }

    @Test
    void searchWithRelevanceOrder() {
        List<String> result = SearchEngine.search(docs, "shoot");
        List<String> correct = List.of("doc4", "doc2", "doc1");
        assertThat(result).isEqualTo(correct);
    }
}