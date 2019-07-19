package com.easyfitness.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easyfitness.DAO.bodymeasures.BodyMeasure;
import com.easyfitness.DAO.bodymeasures.DAOBodyMeasure;
import com.easyfitness.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOProfil extends DAOBase {

    // Contacts table name
    public static final String TABLE_NAME = "EFprofil";

    public static final String KEY = "_id";                             // 아이디를 통해 사용자 프로필 구분 하는 듯(KEY라는 변수 이용)
    public static final String NAME = "name";                           // NAME = 이름 변수
    public static final String CREATIONDATE = "creationdate";       // CREATIONDATE = 생성일 변수
    public static final String SIZE = "size";                          // SIZE = 사이즈 변수
    public static final String BIRTHDAY = "birthday";                 // BIRTHDAY = 생일 저장하는 변수
    public static final String PHOTO = "photo";                         // PHOTO = 사진 저장하는 변수
    public static final String GENDER = "gender";                       // GENDER = 성별 저장하는 변수

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CREATIONDATE + " DATE, " + NAME + " TEXT, " + SIZE + " INTEGER, " + BIRTHDAY + " DATE, " + PHOTO + " TEXT, " + GENDER + " INTEGER);";
    // 사용자 프로필 테이블을 생성하는 문구 생성
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private Cursor mCursor = null;

    //DAOFonte mDAOFonte = null;


    public DAOProfil(Context context) {
        super(context);
    }

    /**
     * @param m DBOProfil Profile a ajouter a la base
     */
    public void addProfil(Profile m) {
        // Check if profil already exists
        Profile check = getProfil(m.getName());
        if (check != null) return;          // 체크해서 널값이라면 아무것도 안함

        SQLiteDatabase db = this.getWritableDatabase();     // 데이터베이스 SQL LITE 사용하는 것 확인 // SQL LITE DB에 저장

        ContentValues value = new ContentValues();

        value.put(DAOProfil.CREATIONDATE, DateConverter.dateToDBDateStr(new Date()));
        value.put(DAOProfil.NAME, m.getName());
        value.put(DAOProfil.BIRTHDAY, DateConverter.dateToDBDateStr(m.getBirthday()));
        value.put(DAOProfil.SIZE, m.getSize());
        value.put(DAOProfil.PHOTO, m.getPhoto());
        value.put(DAOProfil.GENDER, m.getGender());
                                                                    // 위와 같이 변수들을 이용해서 데이터들을 PUT해줌
        db.insert(DAOProfil.TABLE_NAME, null, value);       // DB 에 넣어주는 것을 확인 가능

        close();
    }

    /**
     * @param pName String Nom du profil a ajouter a la base
     */
    public void addProfil(String pName) {
        // Check if profil already exists
        Profile check = getProfil(pName);
        if (check != null) return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(DAOProfil.CREATIONDATE, DateConverter.dateToDBDateStr(new Date()));
        value.put(DAOProfil.NAME, pName);
        //value.put(DAOProfil.BIRTHDAY, DateConverter.dateToDBDateStr(m.getBirthday()));
        //value.put(DAOProfil.SIZE, 0);

        db.insert(DAOProfil.TABLE_NAME, null, value);           // 얘도 위와 마찬가지 인듯

        close();
    }

    /**
     * @param id long id of the Profile
     */
    public Profile getProfil(long id) {                                                     // PROFILE정보가 있다면 IMPORT해오는 기능이 있던데 그것에 관련된 코드      key=?로 되어 있는 걸로 봐서 key값으로 찾거나 새로 입력해주는 듯   creationdate,name,size,birthdaty,photo,gender데이터를 가져와줌
        SQLiteDatabase db = this.getReadableDatabase();                                        // 아래 구문들은 존재하는 데이터를 가져오는 것들  ( getReadableDatabase() 의미가 데이터베이스를 얻어오는 것이라고 함 )
        if (mCursor != null) mCursor.close();
        mCursor = null;
        mCursor = db.query(TABLE_NAME,
            new String[]{KEY, CREATIONDATE, NAME, SIZE, BIRTHDAY, PHOTO, GENDER},
            KEY + "=?",
            new String[]{String.valueOf(id)},
            null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();

            Profile value = new Profile(mCursor.getLong(mCursor.getColumnIndex(DAOProfil.KEY)),
                DateConverter.DBDateStrToDate(mCursor.getString(mCursor.getColumnIndex(DAOProfil.CREATIONDATE))),
                mCursor.getString(mCursor.getColumnIndex(DAOProfil.NAME)),
                mCursor.getInt(mCursor.getColumnIndex(DAOProfil.SIZE)),
                mCursor.getString(mCursor.getColumnIndex(DAOProfil.BIRTHDAY)) != null ? DateConverter.DBDateStrToDate(mCursor.getString(mCursor.getColumnIndex(DAOProfil.BIRTHDAY))) : new Date(0),
                mCursor.getString(mCursor.getColumnIndex(DAOProfil.PHOTO)),
                mCursor.getInt(mCursor.getColumnIndex(DAOProfil.GENDER))
            );
            mCursor.close();
            close();

            // return value
            return value;
        } else {
            mCursor.close();
            close();
            return null;
        }

    }

    /**
     * @param name String name of the Profile
     */
    public Profile getProfil(String name) {
        SQLiteDatabase db = this.getReadableDatabase();                             // 얘도 마찬가지로 프로필 데이터를 얻어오는 것  name으로 가져오거나 입력하는 듯  key,creationdate,size,birthday,photo,gender 데이터 가져와줌
        if (mCursor != null) mCursor.close();
        mCursor = null;
        mCursor = db.query(TABLE_NAME,
            new String[]{KEY, CREATIONDATE, NAME, SIZE, BIRTHDAY, PHOTO, GENDER},
            NAME + "=?",
            new String[]{name},
            null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();

            Profile value = new Profile(mCursor.getLong(mCursor.getColumnIndex(DAOProfil.KEY)),
                DateConverter.DBDateStrToDate(mCursor.getString(mCursor.getColumnIndex(DAOProfil.CREATIONDATE))),
                mCursor.getString(mCursor.getColumnIndex(DAOProfil.NAME)),
                mCursor.getInt(mCursor.getColumnIndex(DAOProfil.SIZE)),
                mCursor.getString(mCursor.getColumnIndex(DAOProfil.BIRTHDAY)) != null ? DateConverter.DBDateStrToDate(mCursor.getString(mCursor.getColumnIndex(DAOProfil.BIRTHDAY))) : new Date(0),
                mCursor.getString(mCursor.getColumnIndex(DAOProfil.PHOTO)),
                mCursor.getInt(mCursor.getColumnIndex(DAOProfil.GENDER))
            );

            mCursor.close();
            close();

            // return value
            return value;
        } else {
            close();
            return null;
        }

    }

    // Getting All Profils
    public List<Profile> getProfilsList(String pRequest) {                                  // 모든 것을 그대로 가져오거나 하는듯
        List<Profile> valueList = new ArrayList<>();
        // Select All Query

        SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        mCursor = db.rawQuery(pRequest, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                Profile value = new Profile(mCursor.getLong(mCursor.getColumnIndex(DAOProfil.KEY)),
                    DateConverter.DBDateStrToDate(mCursor.getString(mCursor.getColumnIndex(DAOProfil.CREATIONDATE))),
                    mCursor.getString(mCursor.getColumnIndex(DAOProfil.NAME)),
                    mCursor.getInt(mCursor.getColumnIndex(DAOProfil.SIZE)),
                    mCursor.getString(mCursor.getColumnIndex(DAOProfil.BIRTHDAY)) != null ? DateConverter.DBDateStrToDate(mCursor.getString(mCursor.getColumnIndex(DAOProfil.BIRTHDAY))) : new Date(0),
                    mCursor.getString(mCursor.getColumnIndex(DAOProfil.PHOTO)),
                    mCursor.getInt(mCursor.getColumnIndex(DAOProfil.GENDER))
                );

                // Adding value to list
                valueList.add(value);
            } while (mCursor.moveToNext());
        }

        close();
        // return value list
        return valueList;
    }

    public Cursor GetCursor() {
        return mCursor;
    }

    // Getting All Profils
    public List<Profile> getAllProfils() {                                      //앱에서 프로필 선택하면 profile page가 나타나는데 그거에 대한 리스트 보여주는 것같음 (전부)
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + KEY + " DESC";

        // return value list
        return getProfilsList(selectQuery);
    }

    // Getting Top 10 Profils
    public List<Profile> getTop10Profils() {                                            //앱에서 프로필 선택하면 profile page가 나타나는데 그거에 대한 리스트 보여주는 것같음 (10개만)
        // Select All Query
        String selectQuery = "SELECT TOP 10 * FROM " + TABLE_NAME + " ORDER BY " + KEY + " DESC";

        // return value list
        return getProfilsList(selectQuery);
    }

    // Getting All Machines
    public String[] getAllProfil() {

        SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;

        // Select All Machines
        String selectQuery = "SELECT DISTINCT  " + NAME + " FROM " + TABLE_NAME + " ORDER BY " + NAME + " ASC";
        mCursor = db.rawQuery(selectQuery, null);

        int size = mCursor.getCount();

        String[] valueList = new String[size];

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            int i = 0;
            do {
                String value = mCursor.getString(0);
                valueList[i] = value;
                i++;
            } while (mCursor.moveToNext());
        }

        close();

        // return value list
        return valueList;
    }

    // Getting last record
    public Profile getLastProfil() {

        SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;

        // Select All Machines
        String selectQuery = "SELECT MAX(" + KEY + ") FROM " + TABLE_NAME;
        mCursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        mCursor.moveToFirst();
        long value = Long.parseLong(mCursor.getString(0));

        Profile prof = this.getProfil(value);
        mCursor.close();
        close();

        // return value list
        return prof;
    }

    // Updating single value
    public int updateProfile(Profile m) {
        SQLiteDatabase db = this.getWritableDatabase();                 // profile 수정할때 사용하는 듯

        ContentValues value = new ContentValues();
        value.put(DAOProfil.CREATIONDATE, DateConverter.dateToDBDateStr(m.getCreationDate()));
        value.put(DAOProfil.NAME, m.getName());
        value.put(DAOProfil.BIRTHDAY, DateConverter.dateToDBDateStr(m.getBirthday()));
        value.put(DAOProfil.SIZE, m.getSize());
        value.put(DAOProfil.PHOTO, m.getPhoto());
        value.put(DAOProfil.GENDER, m.getGender());

        // updating row
        return db.update(TABLE_NAME, value, KEY + " = ?",
            new String[]{String.valueOf(m.getId())});
    }

    // Deleting single Profile
    public void deleteProfil(Profile m) {
        deleteProfil(m.getId());
    }               //profile 클릭시 delete구문 나오는데, 이때 사용되는듯

    // Deleting single Profile
    public void deleteProfil(long id) {
        open();

        // Supprime les enregistrements de poids
        DAOWeight mWeightDb;
        mWeightDb = new DAOWeight(null); // null car a ce moment le DatabaseHelper est cree depuis bien longtemps.
        List<ProfileWeight> valueList = mWeightDb.getWeightList(getProfil(id));
        for (int i = 0; i < valueList.size(); i++) {
            mWeightDb.deleteMeasure(valueList.get(i).getId());
        }

        // Supprime les enregistrements de measure de body
        DAOBodyMeasure mBodyDb;
        mBodyDb = new DAOBodyMeasure(null); // null car a ce moment le DatabaseHelper est cree depuis bien longtemps.
        List<BodyMeasure> bodyMeasuresList = mBodyDb.getBodyMeasuresList(getProfil(id));
        for (int i = 0; i < bodyMeasuresList.size(); i++) {
            mBodyDb.deleteMeasure(bodyMeasuresList.get(i).getId());
        }

        // Supprime le profile
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY + " = ?",
            new String[]{String.valueOf(id)});

        close();
    }


    // Getting Profils Count
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        open();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        cursor.close();
        close();

        // return count
        return value;
    }

    /* DEBUG ONLY */
    public void populate() {
        Date date = new Date();
        Date dateBirthday = DateConverter.getNewDate();
        Profile m = new Profile(0, date, "Champignon", 120, dateBirthday, null, 0);
        this.addProfil(m);
        m = new Profile(0, date, "Musclor", 150, dateBirthday, null, 0);
        this.addProfil(m);
    }
}
