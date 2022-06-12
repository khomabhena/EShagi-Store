package tech.astromobile.merchant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.transition.TransitionManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class XConversions {
    private Context context;

    XConversions(Context context) {
        this.context = context;
    }

    List getListFromStringArray(List list, String[] array) {
        for (String anArray : array) {
            list.add(anArray);
        }

        return list;
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(context.getWindowToken(), 0);
    }

    String[] getArrayFromList(List list) {
        String[] arrayMax = new String[]{"Choose category"};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        for (int x = 0; x < list.size(); x++) {
            SetterCategories setterCategories = (SetterCategories) list.get(x);
            listKeys.add(setterCategories.getName());
        }

        String[] stockArr = new String[listKeys.size()];
        stockArr = listKeys.toArray(stockArr);

        return stockArr;
    }

    void insertGlideImage(String imageLink, ImageView ivImage) {
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageLink);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)/*
                    .placeholder(circularProgressDrawable())*/
                    .into(ivImage);
        } catch (Exception e) {
            //
        }
    }

    void initializeRecyclerviewLayouts(RecyclerView[] recyclerViews, int layout) {
        for (int x = 0; x < recyclerViews.length; x++) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, layout, false);
            recyclerViews[x].setLayoutManager(layoutManager);
            recyclerViews[x].setHasFixedSize(true);
        }
    }

    LinearLayoutManager initializeSingleRecyclerviewLayouts(RecyclerView recyclerViews, int layout) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, layout, false);
            recyclerViews.setLayoutManager(layoutManager);
            recyclerViews.setHasFixedSize(true);

            return layoutManager;
    }

    void spinnerSetAdapter(Spinner spinner, String[] array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, array);
        spinner.setAdapter(adapter);
    }

    public ArrayAdapter<String> setSpinnerAdapter(Spinner spinner, List listCategories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, getArrayFromList(listCategories));
        spinner.setAdapter(adapter);

        return adapter;
    }

    CircularProgressDrawable circularProgressDrawable() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        return circularProgressDrawable;
    }

    void setPriceAndDiscount(double discount, double price, TextView tvPrice, TextView tvDiscount) {
        if (discount == 0) {
            tvDiscount.setText("");
            tvPrice.setText(getFullPrice(price));
        } else {
            double newPrice = price - ((discount/100) * price);
            String dis = "was " + getFullPrice(price) + " now its ";
            tvDiscount.setText(dis);
            tvPrice.setText(getFullPrice(newPrice));
        }
    }

    void addAnotherZero(String amount) {
        if (amount.length() - amount.indexOf(".") - 1 < 2)
            amount += "0";
    }


    boolean isOrderAvailable(List listOrders, SetterMeals setterMeals, ImageView ivRemove, TextView tvCount, CardView cardAdd) {
        boolean isOrdered = false;
        int count = 0;
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals orders = (SetterMeals) listOrders.get(x);
            if (orders.getKey().equals(setterMeals.getKey())) {
                isOrdered = true;
                count++;
            }
        }

        if (isOrdered) {
            if (cardAdd != null)
                cardAdd.setVisibility(View.INVISIBLE);
            ivRemove.setImageResource(R.drawable.ic_remove_green);
        } else {
            if (cardAdd != null)
                cardAdd.setVisibility(View.VISIBLE);
            ivRemove.setImageResource(R.drawable.ic_remove_gray);
        }
        tvCount.setText(String.valueOf(count));

        return isOrdered;
    }

    public boolean isLimitBelow(List listMealsOrdered, SetterMeals setter) {
        if (setter.getLimit() == 0)
            return true;
        int limit = 0;
        for (int x = 0; x < listMealsOrdered.size(); x++) {
            SetterMeals orders = (SetterMeals) listMealsOrdered.get(x);
            if (orders.getKey().equals(setter.getKey()))
                limit++;
        }

        return limit <= setter.getLimit();
    }

    void removeOrder(MainActivity.Adapter adapter, SetterMeals setter, List listOrders) {
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals order = (SetterMeals) listOrders.get(x);
            if (order.getKey().equals(setter.getKey())) {
                listOrders.remove(x);
                adapter.notifyDataSetChanged();
                break;
            }
        }
        playRemoveSound();
    }

    /*void removeOrder(Cart.Adapter adapter, SetterMeals setter, List listOrders) {
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals order = (SetterMeals) listOrders.get(x);
            if (order.getKey().equals(setter.getKey())) {
                listOrders.remove(x);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
*/

    boolean calculateOrderAmount(List listOrders, TextView tvOrderItems, TextView tvOrderAmount) {
        double price = 0;
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals setter = (SetterMeals) listOrders.get(x);
            price += setter.getPrice();
        }

        tvOrderAmount.setText(getFullPrice(price));

        String items = "" + listOrders.size();
        tvOrderItems.setText(items);

        if (listOrders.size() == 0) {
            tvOrderItems.setText("");
            tvOrderAmount.setText("");
        }

        return listOrders.size() == 0;
    }

    double getOrderCharge() {
        double orderCharge = 0;
        for (int x = 0; x < MainActivity.listMealsOrdered.size(); x++) {
            SetterMeals setterMeals = (SetterMeals) MainActivity.listMealsOrdered.get(x);

            orderCharge += setterMeals.getPrice();
        }

        Checkout.setterOrders.setOrderCharge(orderCharge);

        return getPrice(orderCharge);
    }

    double calculateOrderCharge() {
        double orderCharge = 0;
        for (int x = 0; x < MainActivity.listMealsOrdered.size(); x++) {
            SetterMeals setterMeals = (SetterMeals) MainActivity.listMealsOrdered.get(x);

            orderCharge += setterMeals.getPrice();
        }

        Checkout.setterOrders.setOrderCharge(orderCharge);

        return formatPrice(orderCharge);
    }

    double formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        String numberAsString = decimalFormat.format(price);

        return Double.parseDouble(numberAsString);
    }

    double getTakeAwayCharge(boolean isTakeaway) {
        if (!isTakeaway)
            return 0;

        double takeawayCharge = 0;

        for (int x = 0; x < MainActivity.listMealsOrdered.size(); x++) {
            SetterMeals setterMeals = (SetterMeals) MainActivity.listMealsOrdered.get(x);

            takeawayCharge += setterMeals.getTakeawayCharge();
        }

        return getPrice(takeawayCharge);
    }

    String getFullPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String numberAsString = decimalFormat.format(price);

        if (numberAsString.length() - (numberAsString.indexOf(".") + 1) < 2)
            numberAsString += "0";

        return "$" + numberAsString;
    }

    double getPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String numberAsString = decimalFormat.format(price);

        return Double.parseDouble(numberAsString);
    }

    String getFormattedDouble(double price) {
        String charge = String.valueOf(price);
        if (charge.length() - (charge.indexOf(".") + 1) < 2)
            charge += "0";

        return charge;
    }


    List getUniqueList(List listOrders) {
        List listUnique = new ArrayList();
        for (int x = 0; x < listOrders.size(); x++) {
            SetterMeals order = (SetterMeals) listOrders.get(x);
            if (!isOrderRecorded(listUnique, order)) {
                listUnique.add(order);
            }
        }

        return listUnique;
    }

    private boolean isOrderRecorded(List listUnique, SetterMeals order) {
        boolean isAvailable = false;
        for (int x = 0; x < listUnique.size(); x++) {
            SetterMeals meals = (SetterMeals) listUnique.get(x);

            if (meals.getKey().equals(order.getKey()))
                isAvailable = true;
        }

        return isAvailable;
    }

    public SetterOrders calculateDeliveryTime(SetterOrders setterOrders, int startHour, int endHour, int minute) {
        if (startHour != 0 && endHour != 0) {
            setterOrders.setDeliveryStart(getHourTimestamp(startHour, minute));
            setterOrders.setDeliverBefore(getHourTimestamp(endHour, minute));
        } else {
            long end = System.currentTimeMillis() + (60000 * 40);
            setterOrders.setDeliveryStart(System.currentTimeMillis() + (60000 * 3));
            setterOrders.setDeliverBefore(end);
        }

        return setterOrders;
    }

    public boolean isWithinDeliveryTime(int startHour, int endHour, int minute) {
        long currentTime = System.currentTimeMillis();
        if (startHour != 0 && endHour != 0) {
            long deliveryTimeEnd = getHourTimestamp(endHour, minute);
            return deliveryTimeEnd - currentTime > (60000 * 40);
        } else
            return true;
    }

    private long getHourTimestamp(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTimeInMillis();
    }

    public String getOrderNumber() {
        String[] alpha = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        Random random = new Random();
        String a = alpha[checkNum(random.nextInt(26))];
        String b = alpha[checkNum(random.nextInt(26))];
        int c = random.nextInt(9);
        int d = random.nextInt(9);
        int e = random.nextInt(9);
        int f = random.nextInt(9);
        String g = alpha[checkNum(random.nextInt(26))];
        String h = alpha[checkNum(random.nextInt(26))];

        //return a + b + g + h + "-" + getDate(false) + "-" + getDate(true);
        return a + c + d + e + f;
    }

    public void setTextColor(TextView textView, int textColor, boolean isBold) {
        textView.setTextColor(context.getResources().getColor(textColor));
        if (isBold) {
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextSize(17);
        } else {
            textView.setTypeface(null, Typeface.NORMAL);
            textView.setTextSize(13);
        }
    }

    void showToast(String message, boolean isLong) {
        if (isLong)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private String getDate(boolean b) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 0);
        //calendar.set(Calendar.MINUTE, 0);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        if (b)
            return year + month + day;
        else
            return hour + min;
    }

    private String getTheValue(int value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }

    boolean isToday(long timestamp) {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        int dayToday = calendarToday.get(Calendar.DAY_OF_MONTH);
        int monthToday = calendarToday.get(Calendar.MONTH) + 1;
        int yearToday = calendarToday.get(Calendar.YEAR);

        return (day == dayToday && month == monthToday && year == yearToday);
    }

    boolean isMonth(long timestamp) {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        int dayToday = calendarToday.get(Calendar.DAY_OF_MONTH);
        int monthToday = calendarToday.get(Calendar.MONTH) + 1;
        int yearToday = calendarToday.get(Calendar.YEAR);


        return (month == monthToday && year == yearToday);
    }

    boolean isYear(long timestamp) {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);


        int dayToday = calendarToday.get(Calendar.DAY_OF_MONTH);
        int monthToday = calendarToday.get(Calendar.MONTH) + 1;
        int yearToday = calendarToday.get(Calendar.YEAR);

        return (year == yearToday);
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
/*
    public void doDeliveryChecks(Checkout.AdapterDA adapter, TextView tvArea, String key, List listAdapter) {
        for (int x = 0; x < listAdapter.size(); x++) {
            SetterDeliveryArea setterDeliveryArea = (SetterDeliveryArea) listAdapter.get(x);

            if (setterDeliveryArea.getKey().equals(key))
                setTextColor(tvArea, R.color.green, true);
            else
                setTextColor(tvArea, R.color.black, false);

            adapter.notifyDataSetChanged();
        }
    }

    public void doDeliveryTimeChecks(Checkout.AdapterDT adapter, TextView textView, String key, List listAdapter) {
        for (int x = 0; x < listAdapter.size(); x++) {
            SetterDeliveryTime setterDeliveryTime = (SetterDeliveryTime) listAdapter.get(x);

            if (setterDeliveryTime.getKey().equals(key)) {
                setTextColor(textView, R.color.green, true);
            } else {
                setTextColor(textView, R.color.black, false);
            }

            adapter.notifyDataSetChanged();
        }
    }*/

    public void openEcocashDialer(String code) {
        String harsh = Uri.encode("#");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:*151*2*2*" + code + harsh));
        context.startActivity(intent);
    }

    public void swipeViews(ConstraintLayout constLayout, ConstraintSet constraintSet) {
        TransitionManager.beginDelayedTransition(constLayout);
        constraintSet.applyTo(constLayout);
    }

    public void playAddSound() {
        /*final MediaPlayer mp = MediaPlayer.create(context, R.raw.add2);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });*/

        try {
            //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.add));
            //Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playRemoveSound() {
        /*final MediaPlayer mp = MediaPlayer.create(context, R.raw.add2);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });*/

        try {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.remove);
            Ringtone r = RingtoneManager.getRingtone(context, uri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playCheckoutSound() {
        /*final MediaPlayer mp = MediaPlayer.create(context, R.raw.add2);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });*/

        try {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.add2);
            Ringtone r = RingtoneManager.getRingtone(context, uri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getOrderStatus(SetterOrders setter) {
        if (setter.isDone())
            return "Finished";
        else if (setter.isPreparing())
            return "Preparing";
        else
            return "Pending";
    }
}
