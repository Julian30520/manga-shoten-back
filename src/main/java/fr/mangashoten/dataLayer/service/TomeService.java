package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.TomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TomeService {

    @Autowired
    private TomeRepository tomeRepository;

    public Iterable<Tome> getTomes() {
        return tomeRepository.findAll();
    }

    public Optional<Tome> getTomeById(int tomeId) {
        return tomeRepository.findById(tomeId);
    }

    public Tome addTome(Tome tome) {
        return tomeRepository.save(tome);
    }

    public void deleteTome(Tome tome) {
        tomeRepository.delete(tome);
    }

}
