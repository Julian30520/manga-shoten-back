package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Editor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepository extends CrudRepository<Editor, String> {
}
