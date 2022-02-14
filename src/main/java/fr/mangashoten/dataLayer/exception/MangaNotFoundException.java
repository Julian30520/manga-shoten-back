package fr.mangashoten.dataLayer.exception;

public class MangaNotFoundException extends Exception {
    private String mangaId;

    public MangaNotFoundException(String mangaId) {
        super();
        this.mangaId = mangaId;
    }

    public MangaNotFoundException(){
        super();
        this.mangaId = "";
    }

    @Override
    public String getMessage() {
        if(this.mangaId == "") return "Manga introuvable.";
        else return String.format("Manga %s introuvable.", this.mangaId);
    }
}
