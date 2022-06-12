package tech.astromobile.merchant;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddCategories extends AppCompatActivity {

    Context context;
    XConversions xConversions;
    DBOperations dbOperations;
    SQLiteDatabase db;
    Adapter adapter;

    SharedPreferences prefs;
    ProgressBar progressBar;
    FloatingActionButton fabSave;
    EditText etPosition;
    CardView cardSave;
    AutoCompleteTextView etName;
    TextView tvMerchantName;

    List listKeys;
    RecyclerView recyclerView, recyclerViewArrange;
    String[] arrayCats = new String[]{""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        xConversions = new XConversions(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        listKeys = dbOperations.getCategoryKeys(db);

        etName = findViewById(R.id.etName);
        fabSave = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewArrange = findViewById(R.id.recyclerViewArrange);
        etPosition = findViewById(R.id.etPosition);
        cardSave = findViewById(R.id.cardSave);
        tvMerchantName = findViewById(R.id.tvMerchantName);

        RecyclerView[] recyclerViews = {recyclerView};
        xConversions.initializeRecyclerviewLayouts(recyclerViews, LinearLayoutManager.VERTICAL);

        tvMerchantName.setText(prefs.getString(AppInfo.MERCHANT_NAME, ""));
        cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();

            }
        });
        loadData();
        new BackTask().execute();
    }

    private void loadData() {
        final List list = new ArrayList();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCategories)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterCategories setter = snapshot.getValue(SetterCategories.class);

                            new InsertCategories(context, listKeys).execute(setter);
                        }

                        new BackTask().execute();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCategories)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                SetterCategories setterCategories = snap.getValue(SetterCategories.class);
                                list.add(setterCategories);
                            }
                        }
                        arrayCats = xConversions.getArrayFromList(list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_dropdown_item_1line, arrayCats);
                        etName.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void save() {
        String name = etName.getText().toString().trim();
        int position = Integer.parseInt(etPosition.getText().toString().trim());
        if (name.equals(""))
            xConversions.showToast("Enter category name!!", false);
        else {
            progressBar.setVisibility(View.VISIBLE);
            String key = FirebaseDatabase.getInstance().getReference().child(StaticVals.childCategories).push().getKey();
            final SetterCategories setterCategories = new SetterCategories(key, name, 0, true);

            new InsertCategories(context, listKeys).execute(setterCategories);
            new BackTask().execute();
            FirebaseDatabase.getInstance().getReference()
                    .child(StaticVals.childCategories)
                    .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                    .child(key)
                    .setValue(setterCategories)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                xConversions.showToast("Saved", true);
                                progressBar.setVisibility(View.GONE);
                                etName.setText("");

                                new InsertCategories(context, listKeys).execute(setterCategories);
                                new BackTask().execute();
                            }
                        }
                    });
        }
    }

    private class BackTask extends AsyncTask<Void, SetterCategories, Integer> {

        List listInternal;

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getCategory(db);
            int count = cursor.getCount();

            String key, name;
            boolean isAvailable;

            listInternal = new ArrayList();
            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY));
                name = cursor.getString(cursor.getColumnIndex(DBContract.Categories.NAME));
                isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Categories.IS_AVAILABLE)).equals("yes");

                SetterCategories setter = new SetterCategories(key, name, 0, isAvailable);

                //if (isAvailable)
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
                adapter = new Adapter(listInternal);
                recyclerView.setAdapter(adapter);
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
            int layoutIdForListItem = R.layout.row_categories;
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
            TextView tvName, tvStatus;
            CardView cardStatus;

            public Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                cardStatus = itemView.findViewById(R.id.cardStatus);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }

            void bind(final SetterCategories setter) {
                tvName.setText(setter.getName());

                if (setter.isAvailable())
                    tvStatus.setText("Online");
                else
                    tvStatus.setText("Offline");

                cardStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (setter.isAvailable())
                            setter.setAvailable(false);
                        else
                            setter.setAvailable(true);
                        FirebaseDatabase.getInstance().getReference()
                                .child(StaticVals.childCategories)
                                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                                .child(setter.getKey())
                                .setValue(setter)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            if (setter.isAvailable())
                                                tvStatus.setText("Online");
                                            else
                                                tvStatus.setText("Offline");
                                        }
                                    }
                                });
                    }
                });
            }
        }

    }

}
