package fr.mangashoten.dataLayer.exception;

public class TomeNotFoundException extends Exception {
    private String _id;

    public TomeNotFoundException(){
        super();
        this._id = "";
    }
    public TomeNotFoundException(String id){
        super();
        this._id = id;
    }

    @Override
    public String getMessage(){
        StringBuilder strB = new StringBuilder();
        strB.append("Tome inconnu.");
        if(this._id != ""){
            strB.append(" id : ");
            strB.append(this._id);
        }
        return strB.toString();
    }
}
