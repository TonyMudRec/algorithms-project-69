### Hexlet tests and linter status:
[![Actions Status](https://github.com/TonyMudRec/algorithms-project-69/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/TonyMudRec/algorithms-project-69/actions)

**Поисковый движок**

В основе поискового движка лежит подход, в котором используется инвертированный индекс. Он сокращает время на поиск до O(1) при условии, что поиск ведется по одному слову. Поиск сразу нескольких слов будет затрачивать O(n), где n - количество документов в списке. На вход подается список документов, и слово (либо несколько слов сразу). Программа возвращает список документов, в которых присутствует это слово в порядке релевантности (от большего к меньшему).

```java
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
```
