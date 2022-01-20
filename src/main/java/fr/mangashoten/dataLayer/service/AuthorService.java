package fr.mangashoten.dataLayer.service;


import fr.mangashoten.dataLayer.model.Author;
import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.repository.AuthorRepository;
import fr.mangashoten.dataLayer.repository.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MangaRepository mangaRepository;

    public Iterable<Author> getAllAuthor(){
        return authorRepository.findAll();
    }
    public Author getAuthorById(int authorId){
        return authorRepository.findById(authorId).get();
    }
    public Author addAuthor(Author author){
       return authorRepository.save(author);
    }
    public void deleteAuthor(int idAuthor){
        authorRepository.deleteById(idAuthor);
    }


    public String getAuthorFullName(String mangaName){
        Manga manga;
        try {
           manga = mangaRepository.findByTitleEn(mangaName).get();
        } catch (Exception e) {
            manga = null;
        }
        return manga.getAuthor().getName();
    }

}
