package tech.astromobile.merchant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3; //8
    private static final String DB_NAME = "eshagi_store.db";
    //private String localUid;

    Context context;

    DBOperations(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_12);
        db.execSQL(QUERY_13);
        db.execSQL(QUERY_KEYWORDS);
        db.execSQL(QUERY_QUEST);
        db.execSQL(QUERY_RESPONSES);
        db.execSQL(QUERY_ACTIONS);
        db.execSQL(QUERY_ORDERED_MEALS);
        db.execSQL(QUERY_ACTIONS_ONE);
        db.execSQL(QUERY_ACTIONS_TWO);
        db.execSQL(QUERY_ACTIONS_THREE);
        db.execSQL(QUERY_ACTIONS_FOUR);
        db.execSQL(QUERY_ACTIONS_FIVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DeliveryArea.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Meals.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Keywords.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Orders.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DeliveryTime.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Categories.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.OrderedMeals.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions1.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions2.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions3.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions4.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions5.TABLE_NAME);

        onCreate(db);
    }

    Cursor getCategory(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.Categories.ID,
                DBContract.Categories.KEY,
                DBContract.Categories.NAME,
                DBContract.Categories.POSITION,
                DBContract.Categories.IS_AVAILABLE
        };

// + DBContract.Event.DESCRIPTION + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Categories.TABLE_NAME, projections,
                null,
                null,
                DBContract.Categories.KEY,
                null,
                DBContract.Categories.POSITION + " ASC",
                null
        );

        return cursor;
    }

    List getCategorySetter(SQLiteDatabase db) {
        Cursor cursor;
        List listCats = new ArrayList();

        String[] projections = {
                DBContract.Categories.ID,
                DBContract.Categories.KEY,
                DBContract.Categories.NAME,
                DBContract.Categories.POSITION,
                DBContract.Categories.IS_AVAILABLE
        };

// + DBContract.Event.DESCRIPTION + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Categories.TABLE_NAME, projections,
                null,
                null,
                DBContract.Categories.KEY,
                null,
                DBContract.Categories.POSITION + " ASC",
                null
        );

        String key, name;
        boolean isAvailable;

        while (cursor.moveToNext()) {

            key = cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY));
            name = cursor.getString(cursor.getColumnIndex(DBContract.Categories.NAME));
            isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Categories.IS_AVAILABLE)).equals("yes");

            SetterCategories setter = new SetterCategories(key, name, 0, isAvailable);

            if (isAvailable)
                listCats.add(setter);
        }

        return listCats;
    }

    List<String> getCategoryKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Categories.KEY};
        cursor = db.query(true, DBContract.Categories.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY)));
        }
        cursor.close();

        return listKeys;
    }


    Cursor getDeliveryArea(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.DeliveryArea.ID,
                DBContract.DeliveryArea.KEY,
                DBContract.DeliveryArea.AREA,
                DBContract.DeliveryArea.DESCRIPTION,
                DBContract.DeliveryArea.CHARGE,
                DBContract.DeliveryArea.IS_AVAILABLE,
                DBContract.DeliveryArea.IS_CLUB,
                DBContract.DeliveryArea.POSITION
        };

// + DBContract.Event.DESCRIPTION + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.DeliveryArea.TABLE_NAME, projections,
                null,
                null,
                DBContract.DeliveryArea.KEY,
                null,
                DBContract.DeliveryArea.POSITION + " ASC",
                null
        );

        return cursor;
    }

    List<String> getDeliveryAreaKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.DeliveryArea.KEY};
        cursor = db.query(true, DBContract.DeliveryArea.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.DeliveryArea.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getOrders(SQLiteDatabase db, String uid) {
        Cursor cursor;

        String[] projections = {
                DBContract.Orders.ID,
                DBContract.Orders.UID,
                DBContract.Orders.ORDER_CODE,
                DBContract.Orders.KEY,
                DBContract.Orders.NOTES,
                DBContract.Orders.NAME,
                DBContract.Orders.SURNAME,
                DBContract.Orders.CONFIRMATION_CODE,
                DBContract.Orders.ADDRESS,
                DBContract.Orders.ORDER_CHARGE,
                DBContract.Orders.DELIVERY_CHARGE,
                DBContract.Orders.PROCESSING_CHARGE,
                DBContract.Orders.SERVICE_CHARGE,
                DBContract.Orders.TIMESTAMP,
                DBContract.Orders.DELIVERY_START,
                DBContract.Orders.DELIVER_BEFORE,
                DBContract.Orders.DELIVERED_ON,
                DBContract.Orders.IS_PAID,
                DBContract.Orders.IS_PREPARING,
                DBContract.Orders.IS_CLUB,
                DBContract.Orders.IS_DONE,
                DBContract.Orders.IS_DELIVERED,
                DBContract.Orders.IS_TAKEAWAY,
                DBContract.Orders.TAKEAWAY_CHARGE,
                DBContract.Orders.DELIVERY_AREA_KEY
        };

        cursor = db.query(
                true,
                DBContract.Orders.TABLE_NAME, projections,
                DBContract.Orders.UID + "='" + uid + "'",
                null,
                DBContract.Orders.KEY,
                null,
                DBContract.Orders.TIMESTAMP + " DESC",
                null
        );

        return cursor;
    }

    Cursor getSingleClient(SQLiteDatabase db, String natID) {
        Cursor cursor;

        String[] projections = {
                DBContract.Client.ID,
                DBContract.Client.UID,
                DBContract.Client.EMAIL,
                DBContract.Client.AGENT_UID,
                DBContract.Client.IS_MALE,
                DBContract.Client.MAIDEN_SURNAME,
                DBContract.Client.SURNAME,
                DBContract.Client.TITLE,
                DBContract.Client.PASS_PHOTO_PATH,
                DBContract.Client.NAME,
                DBContract.Client.DOB,
                DBContract.Client.PAYSLIP_PATH,
                DBContract.Client.NAT_ID_PATH,
                DBContract.Client.MARITAL_STATUS,
                DBContract.Client.HOME_TELEPHONE,
                DBContract.Client.NAT_ID,
                DBContract.Client.MOBILE_NUMBER,
                DBContract.Client.SALARY,
                //DBContract.Client.ECOCASH_NUMBER,
                DBContract.Client.NUMBER_OF_DEPENDANTS,
                DBContract.Client.COUNTRY_OF_BIRTH_UID,
                DBContract.Client.IS_UPLOADED,
                DBContract.Client.IS_ZIM_RESIDENT
        };

        cursor = db.query(
                true,
                DBContract.Client.TABLE_NAME, projections,
                DBContract.Client.NAT_ID + "='" + natID + "'",
                null,
                DBContract.Client.NAT_ID,
                null,
                DBContract.Client.ID + " DESC",
                null
        );

        return cursor;
    }

    Cursor getSingleBank(SQLiteDatabase db, String natID) {
        Cursor cursor;

        String[] projections = {
                DBContract.BankingDetails.KEY,
                DBContract.BankingDetails.CLIENT_UID,
                DBContract.BankingDetails.BANK_NAME,
                DBContract.BankingDetails.ACCOUNT_NAME,
                DBContract.BankingDetails.BRANCH_NAME,
                DBContract.BankingDetails.ACCOUNT_NO,
                DBContract.BankingDetails.BRANCH_CODE,
                DBContract.BankingDetails.ACCOUNT_TYPE,
                DBContract.BankingDetails.TIMESTAMP
        };

        cursor = db.query(
                true,
                DBContract.BankingDetails.TABLE_NAME, projections,
                DBContract.BankingDetails.CLIENT_UID + "='" + natID + "'",
                null,
                DBContract.BankingDetails.NAT_ID,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    List<String> getOrderKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Orders.KEY};
        cursor = db.query(true, DBContract.Orders.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Orders.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getMeals(SQLiteDatabase db, String type) {
        Cursor cursor;

        String[] projections = {
                DBContract.Meals.ID,
                DBContract.Meals.CATEGORY_KEY,
                DBContract.Meals.KEY,
                DBContract.Meals.NAME,
                DBContract.Meals.SIZE,
                DBContract.Meals.LIMIT,
                DBContract.Meals.LINK,
                DBContract.Meals.PRICE,
                DBContract.Meals.DESCRIPTION,
                DBContract.Meals.IS_AVAILABLE,
                DBContract.Meals.IS_HOME_DELIVERY,
                DBContract.Meals.TIMESTAMP,
                DBContract.Meals.TAKEAWAY_CHARGE
        };

        cursor = db.query(
                true,
                DBContract.Meals.TABLE_NAME, projections,
                DBContract.Meals.CATEGORY_KEY + "='" + type + "'",
                null,
                DBContract.Meals.KEY,
                null,
                DBContract.Meals.TIMESTAMP + " ASC",
                null
        );

        return cursor;
    }

    String getOrderedMeals(SQLiteDatabase db, String key) {
        Cursor cursor;

        String[] projections = {
                DBContract.Meals.ID
        };

        cursor = db.query(
                true,
                DBContract.Meals.TABLE_NAME, projections,
                DBContract.OrderedMeals.ORDER_KEY + "='" + key + "'",
                null,
                DBContract.Meals.KEY,
                null,
                DBContract.Meals.TIMESTAMP + " ASC",
                null
        );

        return String.valueOf(cursor.getCount());
    }

    List<String> getMealKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Meals.KEY};
        cursor = db.query(true, DBContract.Meals.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Meals.KEY)));
        }
        cursor.close();

        return listKeys;
    }

    String getFirstCategory(SQLiteDatabase db) {
        Cursor cursor;
        String key = "";

        String[] projections = {DBContract.Categories.KEY};
        cursor = db.query(true, DBContract.Categories.TABLE_NAME, projections,
                DBContract.Categories.IS_AVAILABLE + "='yes' ",
                null,
                null, null, DBContract.Categories.POSITION + " DESC", null);

        while (cursor.moveToNext()) {
            key = cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY));
        }
        cursor.close();

        return key;
    }



    Cursor getDeliveryTime(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.DeliveryTime.ID,
                DBContract.DeliveryTime.KEY,
                DBContract.DeliveryTime.NAME,
                DBContract.DeliveryTime.START_HOUR,
                DBContract.DeliveryTime.END_HOUR,
                DBContract.DeliveryTime.MINUTE,
                DBContract.DeliveryTime.EXTRA_CHARGE,
                DBContract.DeliveryTime.POSITION,
                DBContract.DeliveryTime.IS_AVAILABLE
        };

        cursor = db.query(
                true,
                DBContract.DeliveryTime.TABLE_NAME, projections,
                null,
                null,
                DBContract.DeliveryTime.KEY,
                null,
                DBContract.DeliveryTime.POSITION + " ASC",
                null
        );

        return cursor;
    }

    List<String> getDeliveryTimeKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.DeliveryTime.KEY};
        cursor = db.query(true, DBContract.DeliveryTime.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.DeliveryTime.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getKeywords(SQLiteDatabase db, String language, String type) {
        Cursor cursor;
        String[] projections = {
                DBContract.Keywords.ID,
                DBContract.Keywords.KEY,
                DBContract.Keywords.NAME,
                DBContract.Keywords.TYPE,
                DBContract.Keywords.ENABLED
        };

        cursor = db.query(
                true,
                DBContract.Keywords.TABLE_NAME, projections,
                DBContract.Keywords.LANGUAGE + "='" + language + "' AND " +
                        DBContract.Keywords.TYPE + "='" + type + "' ",
                null,
                DBContract.Keywords.KEY,
                null,
                DBContract.Keywords.ID + " ASC",
                null
        );

        return cursor;
    }

    List<String> getKeywordsKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Keywords.KEY};
        cursor = db.query(true, DBContract.Keywords.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Keywords.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getChats(SQLiteDatabase db, String language) {
        Cursor cursor;
        String[] projections = {
                DBContract.Meals.ID,
                DBContract.Meals.KEY,
                DBContract.Meals.NAME,
                DBContract.Meals.LINK,
                DBContract.Meals.PRICE,
                DBContract.Meals.IS_AVAILABLE,
                DBContract.Meals.DESCRIPTION
        };
// + DBContract.Event.DESCRIPTION + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Meals.TABLE_NAME, projections,
                DBContract.Meals.TIMESTAMP + "='" + language + "'",
                null,
                DBContract.Meals.ID,
                null,
                DBContract.Meals.DESCRIPTION + " ASC",
                null
        );

        return cursor;
    }



    private static final String QUERY_12 = "CREATE TABLE "+ DBContract.DeliveryArea.TABLE_NAME +"("+
            DBContract.DeliveryArea.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.DeliveryArea.TIMESTAMP + " TEXT, " +
            DBContract.DeliveryArea.IS_SEEN + " TEXT, " +
            DBContract.DeliveryArea.IS_MALE + " TEXT, " +
            DBContract.DeliveryArea.KEY + " TEXT, " +
            DBContract.DeliveryArea.AREA + " TEXT, " +
            DBContract.DeliveryArea.DESCRIPTION + " TEXT, " +
            DBContract.DeliveryArea.CHARGE + " TEXT, " +
            DBContract.DeliveryArea.IS_AVAILABLE + " TEXT, " +
            DBContract.DeliveryArea.IS_CLUB + " TEXT, " +
            DBContract.DeliveryArea.POSITION + " TEXT, " +
            DBContract.DeliveryArea.MEDICAL_AID + " TEXT, " +
            DBContract.DeliveryArea.EMAIL + " TEXT, " +
            DBContract.DeliveryArea.SUFFIX + " TEXT, " +
            DBContract.DeliveryArea.NO + " TEXT, " +
            DBContract.DeliveryArea.ADDRESS + " TEXT, " +
            DBContract.DeliveryArea.EMPLOYER + " TEXT, " +
            DBContract.DeliveryArea.PHONE + " TEXT, " +
            DBContract.DeliveryArea.SPECIMEN_TYPE + " TEXT, " +
            DBContract.DeliveryArea.MEDICAL_LINK + " TEXT, " +
            DBContract.DeliveryArea.FORM_LINK + " TEXT, " +
            DBContract.DeliveryArea.PATIENT_DOB +" TEXT);";

    private static final String QUERY_13 = "CREATE TABLE "+ DBContract.Meals.TABLE_NAME +"("+
            DBContract.Meals.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Meals.TYPE + " TEXT, " +
            DBContract.Meals.KEY + " TEXT, " +
            DBContract.Meals.NAME + " TEXT, " +
            DBContract.Meals.LINK + " TEXT, " +
            DBContract.Meals.PRICE + " TEXT, " +
            DBContract.Meals.IS_AVAILABLE + " TEXT, " +
            DBContract.Meals.DESCRIPTION + " TEXT, " +
            DBContract.Meals.TIMESTAMP + " TEXT, " +
            DBContract.Meals.TAKEAWAY_CHARGE + " TEXT, " +
            DBContract.Meals.LIMIT + " TEXT, " +
            DBContract.Meals.SIZE + " TEXT, " +
            DBContract.Meals.IS_HOME_DELIVERY + " TEXT, " +
            DBContract.Meals.CATEGORY_KEY + " TEXT, " +
            DBContract.Meals.COl_13 + " TEXT, " +
            DBContract.Meals.COl_14 + " TEXT, " +
            DBContract.Meals.COl_15 + " TEXT, " +
            DBContract.Meals.COl_16 + " TEXT, " +
            DBContract.Meals.COl_17 + " TEXT, " +
            DBContract.Meals.COl_18 + " TEXT, " +
            DBContract.Meals.COl_19 + " TEXT, " +
            DBContract.Meals.COl_20 +" TEXT);";

    private static final String QUERY_ORDERED_MEALS = "CREATE TABLE "+ DBContract.OrderedMeals.TABLE_NAME +"("+
            DBContract.OrderedMeals.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.OrderedMeals.TYPE + " TEXT, " +
            DBContract.OrderedMeals.KEY + " TEXT, " +
            DBContract.OrderedMeals.NAME + " TEXT, " +
            DBContract.OrderedMeals.LINK + " TEXT, " +
            DBContract.OrderedMeals.PRICE + " TEXT, " +
            DBContract.OrderedMeals.IS_AVAILABLE + " TEXT, " +
            DBContract.OrderedMeals.DISCOUNT + " TEXT, " +
            DBContract.OrderedMeals.TIMESTAMP + " TEXT, " +
            DBContract.OrderedMeals.TAKEAWAY_CHARGE + " TEXT, " +
            DBContract.OrderedMeals.ORDER_KEY + " TEXT, " +
            DBContract.OrderedMeals.LIMIT + " TEXT, " +
            DBContract.OrderedMeals.SIZE + " TEXT, " +
            DBContract.OrderedMeals.DESCRIPTION + " TEXT, " +
            DBContract.OrderedMeals.IS_HOME_DELIVERY + " TEXT, " +
            DBContract.OrderedMeals.CATEGORY_KEY + " TEXT, " +
            DBContract.OrderedMeals.COl_15 + " TEXT, " +
            DBContract.OrderedMeals.COl_16 + " TEXT, " +
            DBContract.OrderedMeals.COl_17 + " TEXT, " +
            DBContract.OrderedMeals.COl_18 + " TEXT, " +
            DBContract.OrderedMeals.COl_19 + " TEXT, " +
            DBContract.OrderedMeals.COl_20 +" TEXT);";

    private static final String QUERY_QUEST = "CREATE TABLE "+ DBContract.Orders.TABLE_NAME +"("+
            DBContract.Orders.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Orders.UID + " TEXT, " +
            DBContract.Orders.ORDER_CODE + " TEXT, " +
            DBContract.Orders.KEY + " TEXT, " +
            DBContract.Orders.NOTES + " TEXT, " +
            DBContract.Orders.DELIVERY_AREA_KEY + " TEXT, " +
            DBContract.Orders.NAME + " TEXT, " +
            DBContract.Orders.SURNAME + " TEXT, " +
            DBContract.Orders.CONFIRMATION_CODE + " TEXT, " +
            DBContract.Orders.ADDRESS + " TEXT, " +
            DBContract.Orders.ORDER_CHARGE + " TEXT, " +
            DBContract.Orders.DELIVERY_CHARGE + " TEXT, " +
            DBContract.Orders.PROCESSING_CHARGE + " TEXT, " +
            DBContract.Orders.SERVICE_CHARGE + " TEXT, " +
            DBContract.Orders.TIMESTAMP + " TEXT, " +
            DBContract.Orders.DELIVER_BEFORE + " TEXT, " +
            DBContract.Orders.DELIVERED_ON + " TEXT, " +
            DBContract.Orders.IS_PAID + " TEXT, " +
            DBContract.Orders.IS_PREPARING + " TEXT, " +
            DBContract.Orders.IS_CLUB + " TEXT, " +
            DBContract.Orders.IS_DONE + " TEXT, " +
            DBContract.Orders.IS_DELIVERED + " TEXT, " +
            DBContract.Orders.IS_TAKEAWAY + " TEXT, " +
            DBContract.Orders.TAKEAWAY_CHARGE + " TEXT, " +
            DBContract.Orders.DELIVERY_START +" TEXT);";

    private static final String QUERY_RESPONSES = "CREATE TABLE "+ DBContract.DeliveryTime.TABLE_NAME +"("+
            DBContract.DeliveryTime.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.DeliveryTime.LOCAL_UID + " TEXT, " +
            DBContract.DeliveryTime.KEY + " TEXT, " +
            DBContract.DeliveryTime.START_HOUR + " TEXT, " +
            DBContract.DeliveryTime.END_HOUR + " TEXT, " +
            DBContract.DeliveryTime.MINUTE + " TEXT, " +
            DBContract.DeliveryTime.EXTRA_CHARGE + " TEXT, " +
            DBContract.DeliveryTime.IS_AVAILABLE + " TEXT, " +
            DBContract.DeliveryTime.NAME + " TEXT, " +
            DBContract.DeliveryTime.POSITION + " TEXT, " +
            DBContract.DeliveryTime.COl_9 + " TEXT, " +
            DBContract.DeliveryTime.COl_10 + " TEXT, " +
            DBContract.DeliveryTime.COl_11 + " TEXT, " +
            DBContract.DeliveryTime.COl_12 + " TEXT, " +
            DBContract.DeliveryTime.COl_13 + " TEXT, " +
            DBContract.DeliveryTime.COl_14 + " TEXT, " +
            DBContract.DeliveryTime.COl_15 + " TEXT, " +
            DBContract.DeliveryTime.COl_16 + " TEXT, " +
            DBContract.Orders.IS_DELIVERED + " TEXT, " +
            DBContract.DeliveryTime.COl_18 + " TEXT, " +
            DBContract.DeliveryTime.COl_19 + " TEXT, " +
            DBContract.DeliveryTime.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS = "CREATE TABLE "+ DBContract.Categories.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_KEYWORDS = "CREATE TABLE "+ DBContract.Keywords.TABLE_NAME +"("+
            DBContract.Keywords.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Keywords.LOCAL_UID + " TEXT, " +
            DBContract.Keywords.KEY + " TEXT, " +
            DBContract.Keywords.NAME + " TEXT, " +
            DBContract.Keywords.TYPE + " TEXT, " +
            DBContract.Keywords.ENABLED + " TEXT, " +
            DBContract.Keywords.QUESTION + " TEXT, " +
            DBContract.Keywords.TIMESTAMP + " TEXT, " +
            DBContract.Keywords.LANGUAGE + " TEXT, " +
            DBContract.Keywords.COl_8 + " TEXT, " +
            DBContract.Keywords.COl_9 + " TEXT, " +
            DBContract.Keywords.COl_10 + " TEXT, " +
            DBContract.Keywords.COl_11 + " TEXT, " +
            DBContract.Keywords.COl_12 + " TEXT, " +
            DBContract.Keywords.COl_13 + " TEXT, " +
            DBContract.Keywords.COl_14 + " TEXT, " +
            DBContract.Keywords.COl_15 + " TEXT, " +
            DBContract.Keywords.COl_16 + " TEXT, " +
            DBContract.Keywords.COl_17 + " TEXT, " +
            DBContract.Keywords.COl_18 + " TEXT, " +
            DBContract.Keywords.COl_19 + " TEXT, " +
            DBContract.Keywords.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_ONE = "CREATE TABLE "+ DBContract.Actions1.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_TWO = "CREATE TABLE "+ DBContract.Actions2.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_THREE = "CREATE TABLE "+ DBContract.Actions3.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_FOUR = "CREATE TABLE "+ DBContract.Actions4.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_FIVE = "CREATE TABLE "+ DBContract.Actions5.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";


}