package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.Editor;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.repository.EditorRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLayerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EditorServiceTest {

    @Autowired
    EditorRepository editorRepository;

    @Autowired
    EditorService editorService;

    @Test
    void get_EditorsTest() {
        // GIVEN
        List<Editor> initEditors = new ArrayList<>();
        List<Tome> initTomes = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            Editor editorToSend = new Editor();
            editorToSend.setEditorId(index);
            editorToSend.setName("test" + index);
            editorToSend.setWebUrl("Url" + index);
            editorToSend.setTomes(initTomes);

            Editor genericEditor = editorRepository.save(editorToSend);
            initEditors.add(editorToSend);
        }

        // WHEN
        Iterable<Editor> foundEditors = editorService.getEditors();
        List<Editor> result = new ArrayList<>();
        foundEditors.forEach(editor -> result.add(editor));

        // THEN
        assertEquals(initEditors.get(0).getEditorId(), result.get(0).getEditorId());
        assertEquals(initEditors.get(1).getEditorId(), result.get(1).getEditorId());
    }

    @Test
    void get_EditorByIdTest() {
        // GIVEN
        Editor editorToSend = new Editor();
        List<Tome> initTomes = new ArrayList<>();

        editorToSend.setEditorId(1);
        editorToSend.setName("test");
        editorToSend.setWebUrl("Url");
        editorToSend.setTomes(initTomes);

        Editor genericEditor = editorRepository.save(editorToSend);

        // WHEN
        Optional<Editor> foundEditor = editorService.getEditorById(genericEditor.getEditorId());

        // THEN
        assertEquals(genericEditor.getEditorId(), foundEditor.get().getEditorId());
    }
}