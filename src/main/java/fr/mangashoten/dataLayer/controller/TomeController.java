package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.exception.MangaNotFoundException;
import fr.mangashoten.dataLayer.exception.TomeNotFoundException;
import fr.mangashoten.dataLayer.exception.UserNotFoundException;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.service.TomeService;
import fr.mangashoten.dataLayer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value="/tomes", produces = MediaType.APPLICATION_JSON_VALUE)
public class TomeController {

    @Autowired
    private TomeService tomeService;

    private static final Logger log = LoggerFactory.getLogger(TomeController.class);



    /**
     * Récupère les informations d'un tome
     * @param mangaId
     * @param tomeNumber
     * @return
     */
    @GetMapping(value="/{tomeNumber}/manga/{mangaId}")
    public ResponseEntity<Tome> getTomeByNumber(@PathVariable String mangaId, @PathVariable int tomeNumber){
        try{
            Tome tome = tomeService.getTomeByMangaIdAndNumber(mangaId, tomeNumber);
            return ResponseEntity.ok(tome);
        }
        catch(MangaNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }



}
