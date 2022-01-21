package fr.mangashoten.dataLayer.exception;

public class TomeNotFoundException extends Exception {
    private int _id;

    public TomeNotFoundException(){
        super();
        this._id = -1;
    }
    public TomeNotFoundException(int id){
        super();
        this._id = id;
    }

    @Override
    public String getMessage(){
        StringBuilder strB = new StringBuilder();
        strB.append("Tome inconnu.");
        if(this._id != -1){
            strB.append(" id : ");
            strB.append(this._id);
        }
        return strB.toString();
    }
}
