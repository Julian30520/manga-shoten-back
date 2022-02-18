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

    /**
     * Obtient les information d'un tome dans la base de Manga-shoten (donc déjà extrait)
     * @param tomeId
     * @return
     * @throws TomeNotFoundException
     */
    public Tome getTomeById(int tomeId) throws TomeNotFoundException {

        var optTome = tomeRepository.findById(tomeId);
        try{
            return optTome.get();
        }catch(NoSuchElementException ex){
            throw new TomeNotFoundException(tomeId);
        }
    }

    /**
     * Récupère un tome spécifique du manga donné. Si les données du manga en questions ne sont pas encore
     * dans la base de manga-shoten, celles-ci sont automatiquement extraites)
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

}
