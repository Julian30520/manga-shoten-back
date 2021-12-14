package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Editor;
import fr.mangashoten.dataLayer.repository.EditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditorService {

    @Autowired
    private EditorRepository editorRepository;

    public Iterable<Editor> getEditors() {
        return editorRepository.findAll();
    }

    public Optional<Editor> getEditorById(int editorId) {
        return editorRepository.findById(editorId);
    }


}
