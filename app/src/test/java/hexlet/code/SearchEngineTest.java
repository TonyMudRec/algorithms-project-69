package hexlet.code;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class SearchEngineTest {
    @Test
    void searchTest() {
        // Текст документов
        var doc1 = "I can't shoot straight unless I've had a pint!";
        var doc2 = "Don't shoot shoot shoot that thing at me.";
        var doc3 = "I'm your shooter.";

        // создание документа
        // документ имеет два атрибута "id" и "text"
        List<Map<String, String>> docs = List.of(
                Map.of("id", "doc1", "text", doc1),
                Map.of("id", "doc2", "text", doc2),
                Map.of("id", "doc3", "text", doc3)
        );

        // поисковый движок проводит поиск
        List<String> result = SearchEngine.search(docs, "shoot");
        List<String> correct = List.of("doc1", "doc2");
        assertThat(result).isEqualTo(correct);

        List<String> result1 = SearchEngine.search(new ArrayList<Map<String, String>>(), "shoot");
        assertThat(result1).isNullOrEmpty();
    }
}