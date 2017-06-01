package com.sharity.sharityUser.BO;

import com.parse.ParseGeoPoint;

import java.io.Serializable;

/**
 * Created by Moi on 21/11/15.
 */
public class CharityDons implements Serializable{

    String _nom;
    String _descipriton;
    String objectid;
    String email;
    String addresse;
    String url;
    String Siret;
    String teleponeNumber;
    String cause;
    String scope;
    String RIB;
    ParseGeoPoint geoPoint;
    byte[] _image;

    // Empty constructor


    // constructor
    public CharityDons(String objectid,String nom, String description, byte[] image) {
        this._nom = nom;
        this._descipriton = description;
        this._image = image;
        this.objectid=objectid;

    }

    public CharityDons(String objectid,String nom, String description,byte[] image,String email, String addresse, String url,String Siret, String teleponeNumber,String cause, String scope, String RIB, ParseGeoPoint geoPoint) {
        this._nom = nom;
        this._descipriton = description;
        this._image = image;
        this.objectid=objectid;
        this.email=email;
        this.addresse=addresse;
        this.url=url;
        this.Siret=Siret;
        this.teleponeNumber=teleponeNumber;
        this.cause=cause;
        this.scope=scope;
        this.RIB=RIB;
        this.geoPoint=geoPoint;
    }


    public byte[] get_image() {
        return _image;
    }

    public String get_descipriton() {
        return _descipriton;
    }

    public String get_nom() {
        return _nom;
    }

    public String getObjectid() {
        return objectid;
    }

    public ParseGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public String getAddresse() {
        return addresse;
    }

    public String getCause() {
        return cause;
    }

    public String getEmail() {
        return email;
    }

    public String getRIB() {
        return RIB;
    }

    public String getScope() {
        return scope;
    }

    public String getSiret() {
        return Siret;
    }

    public String getTeleponeNumber() {
        return teleponeNumber;
    }

    public String getUrl() {
        return url;
    }
}
