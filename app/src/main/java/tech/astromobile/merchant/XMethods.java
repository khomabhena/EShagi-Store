package tech.astromobile.merchant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import tech.astromobile.merchant.SetterSelection;

public class XMethods {

    Context context;
    DBOperations dbOperations;
    SQLiteDatabase db;

    private int yearX, monthX, dayX;
    private static final int xDATE_DIALOG_ID = 275;

    public XMethods(Context context) {
        this.context = context;
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
    }

    void showNotification(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().equals("");
    }

    void showNotification(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
/*
    void uploadImageToFirebase(Intent data) {
        Uri uri = data.getData();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("meal-pics/"+ uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Image UploadFailed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    }
                });
            }
        });

    }

    void uploadImageToFirebase(final long lastId, SetterNotes setterNotes) {
        Uri uri = Uri.fromFile(new File(setterNotes.getLocalUrl()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child(setterNotes.getPhoneNumber() + "/" + uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Image UploadFailed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContract.Notes.ONLINE_URL, uri.toString());
                        updateDatabaseRecord(db, DBContract.Notes.TABLE_NAME, DBContract.Notes.ID, lastId, contentValues);
                    }
                });
            }
        });
    }
*/
    void sendEmail(String toEmail, String subject, String message, Uri uri) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{toEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (uri != null)
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivities(new Intent[]{Intent.createChooser(emailIntent, "Sending Email.....")});
    }

    LinearLayoutManager initializeSingleRecyclerviewLayouts(RecyclerView recyclerViews, int layout) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, layout, false);
        recyclerViews.setLayoutManager(layoutManager);
        recyclerViews.setHasFixedSize(true);

        return layoutManager;
    }

//    Bitmap getBitmapFromPath(String photoPath) {
//        Bitmap bitmap = null;
//        try {
//            bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }

    String[] getArrayFromList(List list) {
        String[] arrayMax = new String[]{"Choose Subject"};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        for (int x = 0; x < list.size(); x++) {
            listKeys.add(String.valueOf(list.get(x)));
        }

        String[] stockArr = new String[listKeys.size()];
        stockArr = listKeys.toArray(stockArr);

        return stockArr;
    }

    List getListFromStringArray(String[] array) {
        List list = new ArrayList();
        for (String anArray : array) {
            SetterSelection setterSelection = new SetterSelection(anArray);
            list.add(setterSelection);
        }

        return list;
    }

    public void pickDate(long maxTimestamp, String title) {
        Calendar calendar = Calendar.getInstance();
        yearX = calendar.get(Calendar.YEAR);
        monthX = calendar.get(Calendar.MONTH);
        dayX = calendar.get(Calendar.DAY_OF_MONTH);
        createFancyDateTimePicker(xDATE_DIALOG_ID, maxTimestamp, title).show();
    }

    private Dialog createFancyDateTimePicker(int id, long maxTimestamp, String title) {
        Calendar calendar = Calendar.getInstance();
        switch (id) {
            case xDATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(context, xDateSetListener, yearX, monthX, dayX);

                dialog.setCancelable(false);
                dialog.setTitle(title);
                if (maxTimestamp == 101)
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                else
                    dialog.getDatePicker().setMaxDate(maxTimestamp);
                return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener xDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yearX = year;
            monthX = monthOfYear;
            dayX = dayOfMonth;
            String builder = getTheValue(dayX) + "-" + StaticVariables.monthsSmall[monthX + 1] + "-" + getTheValue(yearX);
            //tvDateOfBith.setText(builder);

            generateTimestamp(monthX, yearX, dayX);
            insertDateIntoChatAnswer(builder);
        }
    };

    public void closeKeyboard(EditText editText) {
        InputMethodManager imm =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(editText, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void insertDateIntoChatAnswer(String date) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        /*if (currentapiVersion <= 19) {
            KYC.getInstance().sendMessage(date);
            KYC.isStop = false;
        } else {
            KYC.isStop = false;
            KYC.getInstance().sendMessage(date);
        }*/
    }

    public void generateTimestamp(int month, int year, int day){

        long timestamp;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, day);
        timestamp = c.getTimeInMillis();

        //KYC.dob = timestamp;
    }

    boolean deleteDatabaseRecord(SQLiteDatabase db, String tableName, String record, String value) {
        return db.delete(tableName, record + "=?", new String[]{value}) > 0;
    }

    boolean updateDatabaseRecord(SQLiteDatabase db, String tableName, String record, long dbId, ContentValues contentValues) {
        return db.update(tableName, contentValues,record + "=?", new String[]{String.valueOf(dbId)}) > 0;
    }

    boolean updateDatabaseRecord(SQLiteDatabase db, String tableName, String record, String dbId, ContentValues contentValues) {
        return db.update(tableName, contentValues,record + "=?", new String[]{dbId}) > 0;
    }


    double formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        String numberAsString = decimalFormat.format(price);

        return Double.parseDouble(numberAsString);
    }

    String getFullAMount(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String numberAsString = decimalFormat.format(price);

        if (numberAsString.length() - (numberAsString.indexOf(".") + 1) < 2)
            numberAsString += "0";

        return "" + numberAsString;
    }

    public String getDate(long timestamp) {
        String[] dates = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        int month = calendar.get(Calendar.MONTH);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        return day + "-" + dates[month] + "-" + year;
    }

    private String getTheValue(int value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }

    String getFullDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        return hour + ":" + min + " " + day + "-" + month + "-" + year;
    }

    int getMonthsLeft(long startTimestamp, long endTimestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTimestamp);

        int months = 0;
        while (startTimestamp < endTimestamp) {
            months++;
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 2);
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            startTimestamp = calendar.getTimeInMillis();
        }

        return months;
    }

    long addMonths(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long endTimestamp = 0;

        for (int x = 0; x < months; x++) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            endTimestamp = calendar.getTimeInMillis();
        }

        return endTimestamp;
    }

    long getNextMonthTimestamp(long currentTimestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimestamp);

        int nextMonth = calendar.get(Calendar.MONTH)  + 1;

        calendar.set(Calendar.MONTH, nextMonth);

        return calendar.getTimeInMillis();
    }

    String getWeekDay(long timestamp) {
        String[] weekDays = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int day = calendar.get(Calendar.DAY_OF_WEEK);

        return weekDays[day];
    }

    String getTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        return hour + ":" + min;
    }

    String getTimeLeft(long deliverBefore) {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp > deliverBefore)
            return "Due";

        long sec = 1000;
        long min = 60000;
        long hour = 3600000;
        long day = 86400000;

        long timeBetween = deliverBefore - currentTimestamp;
        long days = timeBetween / day;
        long daysMod = timeBetween % day;
        long hours = daysMod / hour;
        long hoursMod = daysMod % hour;
        long minutes = hoursMod / min;

        StringBuilder builder = new StringBuilder();
        builder.append(getTheValue(hours)).append(" hrs ").append(getTheValue(minutes)).append(" min");

        return String.valueOf(builder);
    }

    private String getTheValue(long value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }

    public int getOTP() {
        String[] alpha = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        Random random = new Random();
        //String a = alpha[checkNum(random.nextInt(26))];
        //String b = alpha[checkNum(random.nextInt(26))];
        int c = random.nextInt(9);
        int d = random.nextInt(9);
        int e = random.nextInt(9);
        int f = random.nextInt(9);
        int cj = 1458;
        //String g = alpha[checkNum(random.nextInt(26))];
        //String h = alpha[checkNum(random.nextInt(26))];

        //return a + b + g + h + "-" + getDate(false) + "-" + getDate(true);
        return Integer.parseInt("" + c + d + e + f);
    }

    private int checkNum(int a) {
        if (a == 0 || a == 27) {
            if (a == 0) {
                a += 1;
            }
            if (a == 27) {
                a -= 1;
            }
        }

        return a;
    }
}
