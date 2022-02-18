package fr.mangashoten.dataLayer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.mangashoten.dataLayer.exception.MangaNotFoundException;
import fr.mangashoten.dataLayer.exception.TomeNotFoundException;
import fr.mangashoten.dataLayer.model.Manga;
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
    @Autowired
    private MangaService mangaService;

    public ArrayList<Tome> getTomes() {
        Iterable<Tome> iteTomes = tomeRepository.findAll();
        ArrayList<Tome> arrayTomes = new ArrayList<>();
        iteTomes.iterator().forEachRemaining(arrayTomes::add);
        return arrayTomes;
    }

    public Tome getTomeById(int tomeId) throws TomeNotFoundException {

        var optTome = tomeRepository.findById(tomeId);
        try{
            return optTome.get();
        }catch(NoSuchElementException ex){
            throw new TomeNotFoundException(tomeId);
        }
    }

    /**
     * Récupère un tome spécifique du manga donné dans la base de données Manga-shoten
     * @param mangaId L'id du manga dont on veut le tome spécifique
     * @param numero Le numéro du tome voulu dans le manga
     * @return
     */
    public Tome getTomeByMangaIdAndNumber(String mangaId, int numero) throws MangaNotFoundException, JsonProcessingException, TomeNotFoundException {

        Manga manga = mangaService.getMangaById(mangaId);
        Tome tome = tomeRepository.getByMangaAndTomeNumber(manga, numero);
        if(tome == null) throw new TomeNotFoundException();
        else return tome;
    }


    public Tome addTome(Tome tome) {
        return tomeRepository.save(tome);
    }

    public void deleteTome(Tome tome) {
        tomeRepository.delete(tome);
    }

}
