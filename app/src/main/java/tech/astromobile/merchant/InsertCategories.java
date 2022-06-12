package tech.astromobile.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertCategories extends AsyncTask<SetterCategories, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertCategories(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterCategories... params) {
        SetterCategories setter = params[0];


        ContentValues values = new ContentValues();
        values.put(DBContract.Categories.KEY, setter.getKey());
        values.put(DBContract.Categories.NAME, setter.getName());
        values.put(DBContract.Categories.POSITION, setter.getPosition());
        values.put(DBContract.Categories.IS_AVAILABLE, setter.isAvailable() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Categories.TABLE_NAME, null, values);
        else
            db.update(DBContract.Categories.TABLE_NAME, values, DBContract.Categories.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getCategoryKeys(db);

        return null;
    }

}
