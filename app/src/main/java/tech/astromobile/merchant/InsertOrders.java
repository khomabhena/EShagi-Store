package tech.astromobile.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertOrders extends AsyncTask<SetterOrders, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertOrders(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterOrders... params) {
        SetterOrders setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Orders.KEY, setter.getKey());
        values.put(DBContract.Orders.UID, setter.getMerchantUid());
        values.put(DBContract.Orders.ORDER_CODE, setter.getOrderCode());
        values.put(DBContract.Orders.DELIVERY_AREA_KEY, setter.getDeliveryAreaKey());
        values.put(DBContract.Orders.NOTES, setter.getNotes());
        values.put(DBContract.Orders.NAME, setter.getName());
        values.put(DBContract.Orders.SURNAME, setter.getSurname());
        values.put(DBContract.Orders.CONFIRMATION_CODE, setter.getConfirmationCode());
        values.put(DBContract.Orders.ADDRESS, setter.getAddress());
        values.put(DBContract.Orders.ORDER_CHARGE, setter.getOrderCharge());
        values.put(DBContract.Orders.DELIVERY_CHARGE, setter.getDeliveryCharge());
        values.put(DBContract.Orders.PROCESSING_CHARGE, setter.getProcessingCharge());
        values.put(DBContract.Orders.SERVICE_CHARGE, setter.getServiceCharge());
        values.put(DBContract.Orders.TAKEAWAY_CHARGE, setter.getTakeawayCharge());
        values.put(DBContract.Orders.TIMESTAMP, setter.getTimestamp());
        values.put(DBContract.Orders.DELIVERY_START, setter.getDeliveryStart());
        values.put(DBContract.Orders.DELIVER_BEFORE, setter.getDeliverBefore());
        values.put(DBContract.Orders.DELIVERED_ON, setter.getDeliveredOn());
        values.put(DBContract.Orders.IS_PAID, setter.isPaid() ? "yes": "no");
        values.put(DBContract.Orders.IS_PREPARING, setter.isPreparing() ? "yes": "no");
        values.put(DBContract.Orders.IS_CLUB, setter.isClub() ? "yes": "no");
        values.put(DBContract.Orders.IS_DONE, setter.isDone() ? "yes": "no");
        values.put(DBContract.Orders.IS_DELIVERED, setter.isDelivered() ? "yes": "no");
        values.put(DBContract.Orders.IS_TAKEAWAY, setter.isTakeaway() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Orders.TABLE_NAME, null, values);
        else
            db.update(DBContract.Orders.TABLE_NAME, values, DBContract.Orders.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getOrderKeys(db);

        return null;
    }

}
