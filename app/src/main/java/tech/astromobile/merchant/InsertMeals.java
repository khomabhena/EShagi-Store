package tech.astromobile.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertMeals extends AsyncTask<SetterMeals, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertMeals(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterMeals... params) {
        SetterMeals setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Meals.KEY, setter.getKey());
        values.put(DBContract.Meals.NAME, setter.getName());
        values.put(DBContract.Meals.LINK, setter.getLink());
        values.put(DBContract.Meals.PRICE, setter.getPrice());
        values.put(DBContract.Meals.LIMIT, setter.getLimit());
        values.put(DBContract.Meals.SIZE, setter.getSize());
        values.put(DBContract.Meals.CATEGORY_KEY, setter.getCategory());
        values.put(DBContract.Meals.TAKEAWAY_CHARGE, setter.getTakeawayCharge());
        values.put(DBContract.Meals.DESCRIPTION, setter.getDescription());
        values.put(DBContract.Meals.IS_AVAILABLE, setter.isAvailable() ? "yes": "no");
        values.put(DBContract.Meals.IS_HOME_DELIVERY, setter.isHomeDelivery() ? "yes": "no");
        values.put(DBContract.Meals.TIMESTAMP, setter.getTimestamp());

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Meals.TABLE_NAME, null, values);
        else
            db.update(DBContract.Meals.TABLE_NAME, values, DBContract.Meals.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getMealKeys(db);

        return null;
    }

}
