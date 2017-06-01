package com.sharity.sharityUser.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.BusinessTransaction;
import com.sharity.sharityUser.BO.User;

import java.util.ArrayList;

import static android.R.attr.name;
import static com.facebook.internal.FacebookRequestErrorClassification.KEY_NAME;
import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;

/**
 * Created by Moi on 21/11/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Contacts table name
    private static final String DATABASE_NAME = "UserManager";

    private static final String User = "UserTable";
    private static final String KEY_OBJECTID = "objectid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CODE = "code";

    private static final String KEY_SHAREPOINT = "sharepoint";
    private static final String KEY_SHAREPOINT_DEPENSE = "sharepointdepense";
    private static final String KEY_SHAREPOINT_STOCK = "sharepointstock";
    private static final String KEY_SOLDE = "solde";



    private static final String Business = "BusinessTable";
    private static final String KEY_BISOBJECTID = "objectidbus";
    private static final String KEY_BISUSERNAME = "usernamebus";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_RIB = "rib";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_OFFICERNAME = "officername";
    private static final String KEY_BUSINESSNAME = "businessname";
    private static final String KEY_TELEPHONE = "phone";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_LATITUDE="latitude";
    private static final String KEY_LONGITUDE="longitude";
    private static final String KEY_SIRET = "siret";
    private static final String KEY_EMAILVERIFIED="emailverified";

    private static final String BusinessTransaction = "BusinessTransaction";
    private static final String KEY_BISTRANSOBJECTID = "objectidTransaction";
    private static final String KEY_BISTRANSAPPROVED = "transactionApproved";
    private static final String KEY_BISTRANSTYPE = "transactionType";
    private static final String KEY_BISTRANSAMOUNT = "transactionAmount";
    private static final String KEY_BISTRANSCLIENTNAME= "transactionClient";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
         String CREATE_TABLE_IMAGE = "CREATE TABLE " + User + "("+KEY_OBJECTID + " TEXT,"+ KEY_USERNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_IMAGE + " BLOB,"+ KEY_CODE + " TEXT," + KEY_SHAREPOINT + " TEXT,"+ KEY_SHAREPOINT_DEPENSE + " TEXT);";
        db.execSQL(CREATE_TABLE_IMAGE);

        String CREATE_TABLE_BUSINESS = "CREATE TABLE " + Business + "("+KEY_BISOBJECTID + " TEXT,"+ KEY_BISUSERNAME + " TEXT," + KEY_OWNER +" TEXT,"
                + KEY_OFFICERNAME +" TEXT," + KEY_BUSINESSNAME +" TEXT," + KEY_RIB +" TEXT,"+ KEY_SIRET +" TEXT,"
                + KEY_TELEPHONE +" TEXT," + KEY_ADDRESS +" TEXT," + KEY_LATITUDE +" TEXT," + KEY_LONGITUDE +" TEXT,"+ KEY_MAIL +" TEXT,"+ KEY_EMAILVERIFIED + " TEXT,"+ KEY_SOLDE + " TEXT,"+ KEY_SHAREPOINT_DEPENSE + " TEXT,"+ KEY_SHAREPOINT_STOCK + " TEXT);";
        db.execSQL(CREATE_TABLE_BUSINESS);

        String CREATE_TABLE_BusinessTransactionPayment = "CREATE TABLE " + BusinessTransaction + "("+ KEY_BISTRANSOBJECTID +" TEXT," + KEY_BISTRANSAPPROVED +" TEXT," + KEY_BISTRANSAMOUNT +" TEXT,"+ KEY_BISTRANSCLIENTNAME +" TEXT,"+ KEY_BISTRANSTYPE + " TEXT);";
        db.execSQL(CREATE_TABLE_BusinessTransactionPayment);

    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User);
        db.execSQL("DROP TABLE IF EXISTS " + Business);
        db.execSQL("DROP TABLE IF EXISTS " + BusinessTransaction);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addUserProfil(com.sharity.sharityUser.BO.User user) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_OBJECTID, user.get_id());
        cv.put(KEY_USERNAME, user.get_name());
        cv.put(KEY_EMAIL,user.get_email());
        cv.put(KEY_IMAGE, user.getPictureprofil());
        cv.put(KEY_CODE, user.get_code());
        cv.put(KEY_SHAREPOINT, user.get_sharepoint());
        cv.put(KEY_SHAREPOINT_DEPENSE, user.get_sharepoint_depense());
        db.insert(User, null, cv);
        db.close(); // Closing database connection
    }

    public byte[] getPictureProfile() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        byte[] image = cursor.getBlob(3);
        return image;
    }

    public String getUserId() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String id = cursor.getString(0);
        return id;
    }

    public String getUsername() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String image = cursor.getString(1);
        return image;
    }

    public String getUserSharepoints() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String image = cursor.getString(5);
        return image;
    }

    public String getUserSharepointsDepense() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String image = cursor.getString(6);
        return image;
    }

    public int UpdateUserSharepoints(String value, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SHAREPOINT, value);
        return db.update(User, cv, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(objectId) });
    }

    public int UpdateUserSharepoints_depense(String value, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SHAREPOINT_DEPENSE, value);
        return db.update(User, cv, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(objectId) });
    }

    public String getCodeUser() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String code = cursor.getString(4);
        return code;
    }
    // Getting single contact
    public com.sharity.sharityUser.BO.User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(User
                , new String[] { KEY_OBJECTID,
                        KEY_USERNAME, KEY_EMAIL,KEY_IMAGE,KEY_CODE,KEY_SHAREPOINT,KEY_SHAREPOINT_DEPENSE}, KEY_OBJECTID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        com.sharity.sharityUser.BO.User contact = new User(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getBlob(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
        // return contact
        return contact;
    }

    // G

    // Updating single contact
    public int updateUser(com.sharity.sharityUser.BO.User contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OBJECTID, contact.get_id());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_USERNAME, contact.get_name());
        values.put(KEY_IMAGE, contact.getPictureprofil());
        values.put(KEY_CODE, contact.get_code());
        values.put(KEY_SHAREPOINT, contact.get_sharepoint());
        values.put(KEY_SHAREPOINT_DEPENSE, contact.get_sharepoint_depense());

        // updating row
        return db.update(User, values, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }


    public void deleteAllUser() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(User, null, null);
        db.close();
    }


    // Deleting single contact
    public void deleteContact(com.sharity.sharityUser.BO.User contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(User, KEY_OBJECTID + " = ?",new String[] { String.valueOf(contact.get_id()) });
            db.close();
    }



    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + User;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    /*/*
    BUSINESS
     */

    public void addProProfil(Business user) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISOBJECTID, user.get_id());
        cv.put(KEY_BISUSERNAME, user.get_username());
        cv.put(KEY_MAIL,user.get_email());
        cv.put(KEY_SIRET,user.get_Siret());
        cv.put(KEY_OFFICERNAME,user.get_officerName());
        cv.put(KEY_BUSINESSNAME,user.get_businessName());
        cv.put(KEY_TELEPHONE,user.get_telephoneNumber());
        cv.put(KEY_OWNER,user.get_id());
        cv.put(KEY_RIB,user.get_RIB());
        cv.put(KEY_ADDRESS, user.get_address());
        cv.put(KEY_LATITUDE,String.valueOf(user.getLatitude()));
        cv.put(KEY_LONGITUDE,String.valueOf(user.get_longitude()));
        cv.put(KEY_EMAILVERIFIED,user.getEmailveried());
        cv.put(KEY_SOLDE,user.getSolde());
        cv.put(KEY_SHAREPOINT_DEPENSE,user.getSharepoint_depense());
        cv.put(KEY_SHAREPOINT_STOCK,user.getSharepoint_stock());


        db.insert(Business, null, cv);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int UpdateProProfil(Business user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISOBJECTID, user.get_id());
        cv.put(KEY_BISUSERNAME, user.get_username());
        cv.put(KEY_MAIL,user.get_email());
        cv.put(KEY_SIRET,user.get_Siret());
        cv.put(KEY_OFFICERNAME,user.get_officerName());
        cv.put(KEY_BUSINESSNAME,user.get_businessName());
        cv.put(KEY_TELEPHONE,user.get_telephoneNumber());
        cv.put(KEY_OWNER,user.get_id());
        cv.put(KEY_RIB,user.get_RIB());
        cv.put(KEY_ADDRESS, user.get_address());
        cv.put(KEY_LATITUDE,String.valueOf(user.getLatitude()));
        cv.put(KEY_LONGITUDE,String.valueOf(user.get_longitude()));
        cv.put(KEY_EMAILVERIFIED,user.getEmailveried());
        cv.put(KEY_SOLDE,user.getSolde());
        cv.put(KEY_SHAREPOINT_DEPENSE,user.getSharepoint_depense());
        cv.put(KEY_SHAREPOINT_STOCK,user.getSharepoint_stock());


        // updating row
        return db.update(Business, cv, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
    }



    public Business getBusiness(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Business
                , new String[] { KEY_BISOBJECTID,
                        KEY_BISUSERNAME,KEY_OWNER,KEY_OFFICERNAME,KEY_BUSINESSNAME,KEY_RIB,KEY_SIRET,KEY_TELEPHONE,KEY_ADDRESS,KEY_LATITUDE,KEY_LONGITUDE,KEY_MAIL,KEY_EMAILVERIFIED,KEY_SOLDE, KEY_SHAREPOINT_DEPENSE, KEY_SHAREPOINT_STOCK}, KEY_BISOBJECTID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
            cursor.moveToFirst();

        Business contact = new Business(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15));
        // return contact
        return contact;
    }


    public void deleteAllBusiness() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(Business, null, null);
        db.close();
    }

    public String getBusinessId() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(0);
        return objectid;
    }

    public String getBusinessName() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(4);
        return objectid;
    }

    public String getBusinessLatitude() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(9);
        return objectid;
    }

    public String getBusinessLongitude() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(10);
        return objectid;
    }

    public String getEmailverified() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(12);
        return objectid;
    }

    public String getBusinessSolde() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(13);
        return objectid;
    }

    public String getBusiness_SharepointDepense() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(14);
        return objectid;
    }

    public String getBusiness_SharepointStock() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(15);
        return objectid;
    }

    public int setBusinessSolde(String value, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SOLDE, value);
        return db.update(Business, cv, KEY_BISOBJECTID + " = ?",
                new String[] { String.valueOf(objectId) });
    }

    public int setBusiness_StockSP(String value, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SHAREPOINT_STOCK, value);
        return db.update(Business, cv, KEY_BISOBJECTID + " = ?",
                new String[] { String.valueOf(objectId) });
    }

    public int setBusiness_DepenseSP(String value, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SHAREPOINT_DEPENSE, value);
        return db.update(Business, cv, KEY_BISOBJECTID + " = ?",
                new String[] { String.valueOf(objectId) });
    }


    public int UpdateEmailVerified(Business user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISOBJECTID, user.get_id());
        cv.put(KEY_EMAILVERIFIED,user.getEmailveried());
        // updating row
        return db.update(Business, cv, KEY_BISOBJECTID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
    }


    public int getBusinessCount() {
        String countQuery = "SELECT  * FROM " + Business;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }



    /**
     * BUSINESS TRANSACTION PAIMENT
     *
     * */

    public void addBusinessTransaction(BusinessTransaction user) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISTRANSOBJECTID, user.getTransactionId());
        cv.put(KEY_BISTRANSAPPROVED, user.isApproved());
        cv.put(KEY_BISTRANSAMOUNT,user.getAmount());
        cv.put(KEY_BISTRANSCLIENTNAME,user.getClientName());
        cv.put(KEY_BISTRANSTYPE,user.getType());
        db.insert(BusinessTransaction, null, cv);
        db.close(); // Closing database connection
    }


    public String getTransaction(String objectidTransaction) {
        Cursor cursor = null;
        String empName = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT objectidTransaction FROM BusinessTransaction WHERE objectidTransaction=?", new String[] {objectidTransaction + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                empName = cursor.getString(cursor.getColumnIndex("objectidTransaction"));
            }
            return empName;
        }finally {
            cursor.close();
        }
    }

    //---deletes a Transaction---


    public Integer deleteTransaction (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("BusinessTransaction",KEY_BISTRANSOBJECTID +" = ? ",
                new String[] { id });
    }


    public ArrayList<BusinessTransaction> getAllTransactions() {
        ArrayList<BusinessTransaction> array_list = new ArrayList<BusinessTransaction>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + BusinessTransaction, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new BusinessTransaction(res.getString(res.getColumnIndex(KEY_BISTRANSOBJECTID)),res.getString(res.getColumnIndex(KEY_BISTRANSAPPROVED)),res.getString(res.getColumnIndex(KEY_BISTRANSAMOUNT)),res.getString(res.getColumnIndex(KEY_BISTRANSCLIENTNAME)),res.getString(res.getColumnIndex(KEY_BISTRANSTYPE))));
            res.moveToNext();
        }
        return array_list;
    }

    public int getBusinessTransactionCount() {
        String countQuery = "SELECT  * FROM " + BusinessTransaction;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}