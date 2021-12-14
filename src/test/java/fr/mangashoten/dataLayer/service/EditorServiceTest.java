package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Editor;
import fr.mangashoten.dataLayer.repository.EditorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditorServiceTest {

    @Autowired
    EditorRepository editorRepository;

    @Autowired
    EditorService editorService;

    @Test
    void get_EditorsTest() {
    }

    @Test
    void get_EditorByIdTest() {
    }
}