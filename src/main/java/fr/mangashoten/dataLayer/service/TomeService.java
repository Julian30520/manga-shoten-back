package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.TomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TomeService {

    @Autowired
    private TomeRepository tomeRepository;

    public ArrayList<Tome> getTomes() {
        Iterable<Tome> iteTomes = tomeRepository.findAll();
        ArrayList<Tome> arrayTomes = new ArrayList<>();
        iteTomes.iterator().forEachRemaining(arrayTomes::add);
        return arrayTomes;
    }

    public Tome getTomeById(int tomeId) {
        var optTome = tomeRepository.findById(tomeId);
        try{
            return optTome.get();
        }catch(NoSuchElementException ex){
            return null;
        }
    }

    public Tome addTome(Tome tome) {
        return tomeRepository.save(tome);
    }

    public void deleteTome(Tome tome) {
        tomeRepository.delete(tome);
    }

}
