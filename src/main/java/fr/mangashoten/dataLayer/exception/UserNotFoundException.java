package fr.mangashoten.dataLayer.exception;

public class UserNotFoundException extends Exception {

    private int _id;
    private String _username;

    public UserNotFoundException(){
        super();
        this._id = -1;
        this._username = "";
    }
    public UserNotFoundException(int id){
        super();
        this._id = id;
        this._username = "";
    }
    public UserNotFoundException(String username){
        super();
        this._username = username;
        this._id = -1;
    }

    @Override
    public String getMessage(){
        StringBuilder strB = new StringBuilder();
        strB.append("Utilisateur inconnu.");
        if(this._id != -1){
            strB.append(" id : ");
            strB.append(this._id);
        }
        if(!this._username.equals("")){
            strB.append(" username : ");
            strB.append(this._username);
        }
        return strB.toString();
}
}
