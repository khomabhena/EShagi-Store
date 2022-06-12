package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Context context;
    XConversions xConversions;
    //ConstraintSet constraintSetMain = new ConstraintSet();
    //ConstraintSet constraintSetCheckout = new ConstraintSet();
    ConstraintLayout constMain;
    CardView /*cardTop,*/ cardCheckout/*, cardOrders*/;
    TextView tvCheckout;

    static boolean isCheckout = false;

    RecyclerView recyclerView, recyclerViewMeals;
    TextView tvOrderItems, tvOrderAmount, tvMerchantName;
    ProgressBar progressBar;
    //CardView cardCart;

    DBOperations dbOperations;
    SQLiteDatabase db;
    AdapterCategories adapterCategories;
    List listKeys, listKeysMeals;
    static String selectedCategory = "";
    Adapter adapter;

    static List listMealsOrdered;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    LinearLayoutManager layoutManagerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        xConversions = new XConversions(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        listKeys = dbOperations.getCategoryKeys(db);
        listKeysMeals = dbOperations.getMealKeys(db);
        FirebaseAuth.getInstance().signInAnonymously();

        if (!prefs.getBoolean(AppInfo.IS_LOGGED_IN, false)) {
            startActivity(new Intent(context, Login.class));
            finish();
            return;
        }

        constMain = findViewById(R.id.constMain);
        cardCheckout = findViewById(R.id.cardCheckout);
        tvCheckout = findViewById(R.id.tvCheckout);
        tvMerchantName = findViewById(R.id.tvMerchantName);
        tvCheckout.setOnClickListener(this);

        if (listMealsOrdered == null)
            listMealsOrdered = new ArrayList();
        if (selectedCategory.equals(""))
            selectedCategory = dbOperations.getFirstCategory(db);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewMeals = findViewById(R.id.recyclerViewMeals);
        tvOrderItems = findViewById(R.id.tvOrderItems);
        tvOrderAmount = findViewById(R.id.tvOrderAmount);

        RecyclerView[] recyclerViews = {recyclerView};
        layoutManagerCategory = xConversions.initializeSingleRecyclerviewLayouts(recyclerView, LinearLayoutManager.HORIZONTAL);
        RecyclerView[] recyclerViewsMeal = {recyclerViewMeals};
        xConversions.initializeRecyclerviewLayouts(recyclerViewsMeal, LinearLayoutManager.VERTICAL);

        //cardOrders.setOnClickListener(this);
        //cardCart.setOnClickListener(this);

        new BackTaskCat().execute();
        new BackTask().execute();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        loadCategories();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        checkCheckout();

        getVitals();
        loadCategories();
        tvMerchantName.setText(prefs.getString(AppInfo.MERCHANT_NAME, "no merchant"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCheckout();

        getVitals();
        loadCategories();
    }

    private void checkCheckout() {
        if (listMealsOrdered != null) {
            boolean isOrdersEmpty = xConversions.calculateOrderAmount(listMealsOrdered, tvOrderItems, tvOrderAmount);
            if (isOrdersEmpty) {
                //xConversions.swipeViews(constMain, constraintSetMain);
                cardCheckout.setVisibility(View.GONE);
            } else
                cardCheckout.setVisibility(View.VISIBLE);
                //xConversions.swipeViews(constMain, constraintSetCheckout);
        }
    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.cardTop) {
            if (isCheckout) {
                xConversions.swipeViews(constMain, constraintSetMain);
                isCheckout = false;
            } else {
                xConversions.swipeViews(constMain, constraintSetCheckout);
                isCheckout = true;
            }
        }*/

        switch (v.getId()) {
            /*case R.id.cardOrders:
                startActivity(new Intent(context, Settings.class));
                break;*/
            case R.id.tvCheckout:
                startActivity(new Intent(context, Checkout.class));
                break;
        }
    }

    private void getVitals() {
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childAdmin)
                .child(StaticVals.childMerchant)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        SetterMerchant setterMerchant = dataSnapshot.getValue(SetterMerchant.class);
                        editor = prefs.edit();
                        //editor.putLong(AppInfo.TCC_MERCHANT_CODE, setterMerchant.getCode());
                        editor.putString(AppInfo.TCC_MERCHANT, setterMerchant.getName());
                        editor.apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCharges)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        SetterCharges setterCharges = dataSnapshot.getValue(SetterCharges.class);
                        editor = prefs.edit();
                        editor.putFloat(AppInfo.FLO_SERVICE_CHARGE, (float) setterCharges.getServiceCharge());
                        editor.putFloat(AppInfo.FLO_ITEM_PROCESSING, (float) setterCharges.getItemProcessing());
                        editor.putFloat(AppInfo.FLO_LESS_THAN_40, (float) setterCharges.getLessThan40());
                        editor.putFloat(AppInfo.FLO_PERCENT_FOR_ABOVE_40, (float) setterCharges.getPercentForAbove40());
                        editor.apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadCategories() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCategories)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            progressBar.setVisibility(View.GONE);
                            return;
                        }

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterCategories setter = snapshot.getValue(SetterCategories.class);

                            new InsertCategories(context, listKeys).execute(setter);
                        }

                        new BackTaskCat().execute();
                        loadData();
                        if (selectedCategory.equals(""))
                            selectedCategory = dbOperations.getFirstCategory(db);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }



    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childItems)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            progressBar.setVisibility(View.GONE);
                            return;
                        }

                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshot: snap.getChildren()) {
                                SetterMeals setter = snapshot.getValue(SetterMeals.class);

                                new InsertMeals(context, listKeysMeals).execute(setter);
                            }
                        }

                        new BackTask().execute();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                });
    }

    public void openMenu(View view) {
        startActivity(new Intent(context, Settings.class));
    }

    private class BackTask extends AsyncTask<Void, SetterMeals, Integer> {

        List listInternal;

        @Override
        protected Integer doInBackground(Void... params) {

            Cursor cursor = dbOperations.getMeals(db, selectedCategory);
            int count = cursor.getCount();

            String key, type, name, size, description, link;
            int limit;
            double price, takeawayCharge;
            boolean isAvailable, isHomeDelivery;
            long timestamp;

            listInternal = new ArrayList();
            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Meals.KEY));
                type = cursor.getString(cursor.getColumnIndex(DBContract.Meals.CATEGORY_KEY));
                name = cursor.getString(cursor.getColumnIndex(DBContract.Meals.NAME));
                size = cursor.getString(cursor.getColumnIndex(DBContract.Meals.SIZE));
                description = cursor.getString(cursor.getColumnIndex(DBContract.Meals.DESCRIPTION));
                link = cursor.getString(cursor.getColumnIndex(DBContract.Meals.LINK));
                limit = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.Meals.LIMIT)));
                price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBContract.Meals.PRICE)));
                takeawayCharge = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBContract.Meals.TAKEAWAY_CHARGE)));
                isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Meals.IS_AVAILABLE)).equals("yes");
                isHomeDelivery = cursor.getString(cursor.getColumnIndex(DBContract.Meals.IS_HOME_DELIVERY)).equals("yes");
                timestamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Meals.TIMESTAMP)));


                SetterMeals setter = new SetterMeals(key, type, name, size, description, link, "", limit, price,
                        takeawayCharge, isAvailable, isHomeDelivery, timestamp);

                if (isAvailable)
                    publishProgress(setter);
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterMeals... values) {
            listInternal.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            adapter = new Adapter(listInternal);
            recyclerViewMeals.setAdapter(adapter);
        }
    }

    private class BackTaskCat extends AsyncTask<Void, SetterCategories, Integer> {

        List listInternal;

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getCategory(db);
            int count = cursor.getCount();

            String key, name;
            int position;
            boolean isAvailable;

            listInternal = new ArrayList();
            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY));
                name = cursor.getString(cursor.getColumnIndex(DBContract.Categories.NAME));
                position = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.Categories.POSITION)));
                isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Categories.IS_AVAILABLE)).equals("yes");

                SetterCategories setter = new SetterCategories(key, name, position, isAvailable);

                if (isAvailable)
                    publishProgress(setter);
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterCategories... values) {
            listInternal.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count != 0) {
                adapterCategories = new AdapterCategories(listInternal);
                recyclerView.setAdapter(adapterCategories);
            }
        }
    }

    class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.Holder> {

        private List listAdapter;

        public AdapterCategories(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_categories_2;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterCategories) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName;

            public Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
            }

            void bind(final SetterCategories setter) {
                tvName.setText(setter.getName());

                if (selectedCategory.equals("")) {
                    if (getAdapterPosition() == 0)
                        xConversions.setTextColor(tvName, R.color.shagiGray, true);
                    else
                        xConversions.setTextColor(tvName, R.color.black, false);
                } else {
                    if (setter.getKey().equals(selectedCategory))
                        xConversions.setTextColor(tvName, R.color.shagiGray, true);
                    else
                        xConversions.setTextColor(tvName, R.color.black, false);
                }

                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedCategory.equals("")) {
                            if (getAdapterPosition() == 0)
                                xConversions.setTextColor(tvName, R.color.shagiGray, true);
                            else
                                xConversions.setTextColor(tvName, R.color.black, false);
                        } else {
                            if (setter.getKey().equals(selectedCategory))
                                xConversions.setTextColor(tvName, R.color.shagiGray, true);
                            else
                                xConversions.setTextColor(tvName, R.color.black, false);
                        }

                        selectedCategory = setter.getKey();
                        new BackTask().execute();
                        //new BackTaskCat().execute();

                        adapterCategories.notifyDataSetChanged();
                    }
                });
            }
        }

    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List listAdapter;

        public Adapter(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_meals;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterMeals) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName, tvCount, tvPrice, tvSize, tvDescription, tvTakeaway;
            ImageView ivImage, ivRemove, ivAdd;
            CardView cardAdd, cardAdd2;
            ConstraintLayout constMeal;
            View viewSpace;

            public Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                ivImage = itemView.findViewById(R.id.ivImage);
                ivRemove = itemView.findViewById(R.id.ivRemove);
                tvCount = itemView.findViewById(R.id.tvCount);
                ivAdd = itemView.findViewById(R.id.ivAdd);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                cardAdd = itemView.findViewById(R.id.cardAdd);
                cardAdd2 = itemView.findViewById(R.id.cardAdd2);
                tvSize = itemView.findViewById(R.id.tvSize);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                constMeal = itemView.findViewById(R.id.constMeal);
                tvTakeaway= itemView.findViewById(R.id.tvTakeaway);
                viewSpace = itemView.findViewById(R.id.viewSpace);
            }

            void bind(final SetterMeals setter) {
                final boolean[] isViewDesc = {false};
                tvName.setText(setter.getName());
                tvDescription.setText(setter.getDescription());
                tvSize.setText(setter.getSize());
                tvPrice.setText(xConversions.getFullPrice(setter.getPrice()));
                xConversions.insertGlideImage(setter.getLink(), ivImage);
                xConversions.isOrderAvailable(listMealsOrdered, setter, ivRemove, tvCount, cardAdd);

                /*if (setter.isHomeDelivery())
                    tvTakeaway.setVisibility(View.VISIBLE);
                else
                    tvTakeaway.setVisibility(View.GONE);*/

                //tvName.setText("" + getAdapterPosition() + "/n" + listAdapter.size());
                if (getAdapterPosition() == listAdapter.size() -1)
                    viewSpace.setVisibility(View.VISIBLE);
                else
                    viewSpace.setVisibility(View.GONE);

                ivRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        xConversions.removeOrder(adapter, setter, listMealsOrdered);
                        xConversions.calculateOrderAmount(listMealsOrdered, tvOrderItems, tvOrderAmount);
                        xConversions.isOrderAvailable(listMealsOrdered, setter, ivRemove, tvCount, cardAdd);
                        checkCheckout();
                    }
                });
                ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (xConversions.isLimitBelow(listMealsOrdered, setter)) {
                            listMealsOrdered.add(setter);
                            adapter.notifyDataSetChanged();
                            xConversions.calculateOrderAmount(listMealsOrdered, tvOrderItems, tvOrderAmount);
                            xConversions.isOrderAvailable(listMealsOrdered, setter, ivRemove, tvCount, cardAdd);
                            checkCheckout();
                            xConversions.playAddSound();
                        } else
                            xConversions.showToast("You have reached the limit for this item!!", false);
                    }
                });
                cardAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (xConversions.isLimitBelow(listMealsOrdered, setter)) {
                            listMealsOrdered.add(setter);
                            adapter.notifyDataSetChanged();
                            xConversions.calculateOrderAmount(listMealsOrdered, tvOrderItems, tvOrderAmount);
                            xConversions.isOrderAvailable(listMealsOrdered, setter, ivRemove, tvCount, cardAdd);
                            checkCheckout();
                            cardAdd2.setVisibility(View.VISIBLE);
                            xConversions.playAddSound();
                        } else
                            xConversions.showToast("You have reached the limit for this item!!", false);
                    }
                });

                constMeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isViewDesc[0]) {
                            isViewDesc[0] = false;
                            tvDescription.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            cardAdd2.setVisibility(View.VISIBLE);
                            xConversions.isOrderAvailable(listMealsOrdered, setter, ivRemove, tvCount, cardAdd);
                        } else {
                            isViewDesc[0] = true;
                            tvDescription.setVisibility(View.VISIBLE);
                            cardAdd.setVisibility(View.GONE);
                            cardAdd2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

    }

}
