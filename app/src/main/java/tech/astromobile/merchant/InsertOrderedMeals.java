package tech.astromobile.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class InsertOrderedMeals extends AsyncTask<SetterMeals, Void, Void> {

    private SQLiteDatabase db;
    private DBOperations dbOperations;
    private String orderKey;


    public InsertOrderedMeals(Context context, String orderKey) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.orderKey = orderKey;
    }

    @Override
    protected Void doInBackground(SetterMeals... params) {
        SetterMeals setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.OrderedMeals.ORDER_KEY, orderKey);
        values.put(DBContract.OrderedMeals.KEY, setter.getKey());
        values.put(DBContract.OrderedMeals.NAME, setter.getName());
        values.put(DBContract.OrderedMeals.LINK, setter.getLink());
        values.put(DBContract.OrderedMeals.PRICE, setter.getPrice());
        values.put(DBContract.OrderedMeals.LIMIT, setter.getLimit());
        values.put(DBContract.OrderedMeals.SIZE, setter.getSize());
        values.put(DBContract.OrderedMeals.CATEGORY_KEY, setter.getCategory());
        values.put(DBContract.OrderedMeals.TAKEAWAY_CHARGE, setter.getTakeawayCharge());
        values.put(DBContract.OrderedMeals.DESCRIPTION, setter.getDescription());
        values.put(DBContract.OrderedMeals.IS_AVAILABLE, setter.isAvailable() ? "yes": "no");
        values.put(DBContract.OrderedMeals.IS_HOME_DELIVERY, setter.isHomeDelivery() ? "yes": "no");
        values.put(DBContract.OrderedMeals.TIMESTAMP, setter.getTimestamp());

        //if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.OrderedMeals.TABLE_NAME, null, values);
        /*else
            db.update(DBContract.OrderedMeals.TABLE_NAME, values, DBContract.OrderedMeals.KEY + "='" + setter.getKey() + "'", null);*/


        return null;
    }

}
