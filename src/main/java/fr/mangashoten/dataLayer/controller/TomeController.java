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
@RequestMapping(value="/tome", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TomeController {

    @Autowired
    private TomeService tomeService;
    @Autowired
    private UserService userService;

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


    /**
     * Retire un tome de la bilibothèque de l'utilisateur
     * @param user_id
     * @param tome_id
     * @return
     */
    @DeleteMapping(value="/{tome_id}/delete/{user_id}/")
    public ResponseEntity removeTomeFromUserLibrary(@PathVariable int user_id, @PathVariable int tome_id){
        try{
            userService.deleteTomeFromUserLibrary(user_id, tome_id);
            log.info("Tome {} retiré de la bibliothèque de l'utilisateur {}", tome_id, user_id);
            return ResponseEntity.ok().build();
        }
        catch(UserNotFoundException | TomeNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error("Erreur inconnue lors de l'ajout du tome {} à la bibliothèque de l'utilisateur {}", tome_id, user_id);
            return ResponseEntity.badRequest().build();
        }
    }

}
