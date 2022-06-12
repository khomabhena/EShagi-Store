package tech.astromobile.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertDeliveryArea extends AsyncTask<SetterDeliveryArea, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertDeliveryArea(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterDeliveryArea... params) {
        SetterDeliveryArea setter = params[0];


        ContentValues values = new ContentValues();
        values.put(DBContract.DeliveryArea.KEY, setter.getKey());
        values.put(DBContract.DeliveryArea.AREA, setter.getArea());
        values.put(DBContract.DeliveryArea.DESCRIPTION, setter.getDescription());
        values.put(DBContract.DeliveryArea.CHARGE, setter.getCharge());
        values.put(DBContract.DeliveryArea.POSITION, setter.getPosition());
        values.put(DBContract.DeliveryArea.IS_AVAILABLE, setter.isAvailable() ? "yes": "no");
        values.put(DBContract.DeliveryArea.IS_CLUB, setter.isClub() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.DeliveryArea.TABLE_NAME, null, values);
        else
            db.update(DBContract.DeliveryArea.TABLE_NAME, values, DBContract.DeliveryArea.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getDeliveryAreaKeys(db);

        return null;
    }

}
