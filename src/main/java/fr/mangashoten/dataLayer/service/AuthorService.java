package fr.mangashoten.dataLayer.service;


import fr.mangashoten.dataLayer.model.Author;
import fr.mangashoten.dataLayer.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Iterable<Author> getAllAuthor(){
        return authorRepository.findAll();
    }
    public Optional<Author> getAuthorById(int authorId){
        return authorRepository.findById(authorId);
    }
    public Author addAuthor(Author author){
       return authorRepository.save(author);
    }

    public void deleteAuthor(int idAuthor){
        authorRepository.deleteById(idAuthor);
    }

}
