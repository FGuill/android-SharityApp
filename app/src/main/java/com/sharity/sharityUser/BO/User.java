package com.sharity.sharityUser.BO;

/**
 * Created by Moi on 21/11/15.
 */
public class User {

    //private variables
    String _id;
    String _name;
    String _email;
    String _code;
    String _sharepoint;
    String _sharepoint_depense;
    byte[] pictureprofil;

    // Empty constructor
    public User(){

    }
    // constructor
    public User(String id, String name, String email,byte[] pictureprofil){
        this._id = id;
        this._name = name;
        this._email=email;
        this.pictureprofil = pictureprofil;
    }

    public User(String id, String name, String email,byte[] pictureprofil,String code,String sharepoint,String sharepoint_depense){
        this._id = id;
        this._name = name;
        this._email=email;
        this.pictureprofil = pictureprofil;
        this._code=code;
        this._sharepoint=sharepoint;
        this._sharepoint_depense=sharepoint_depense;
    }

    // constructor

    public byte[] getPictureprofil() {
        return pictureprofil;
    }

    public String get_id() {
        return _id;
    }

    public String get_email() {
        return _email;
    }

    public String get_name() {
        return _name;
    }

    public String get_code() {
        return _code;
    }

    public String get_sharepoint() {
        return _sharepoint;
    }

    public String get_sharepoint_depense() {
        return _sharepoint_depense;
    }
}
