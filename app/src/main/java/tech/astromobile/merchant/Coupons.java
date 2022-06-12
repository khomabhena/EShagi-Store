package tech.astromobile.merchant;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Coupons extends AppCompatActivity {

    Context context;
    XConversions xConversions;
    DBOperations dbOperations;
    SQLiteDatabase db;

    CardView cardSave;
    RecyclerView recyclerView;
    EditText etAmount;
    ProgressBar progressBar;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    List listCoupons;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = this;
        xConversions = new XConversions(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        cardSave = findViewById(R.id.cardSave);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        etAmount = findViewById(R.id.etAmount);

        cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });


        RecyclerView[] recyclerViews = {recyclerView};
        xConversions.initializeRecyclerviewLayouts(recyclerViews, LinearLayoutManager.VERTICAL);
        loadData();
    }

    private void save() {
        String amount = etAmount.getText().toString().trim();

        if (amount.equals("")) {
            xConversions.showToast("Enter coupon amount", false);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String key = FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCoupons)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0")).push().getKey();
        SetterCoupons setterCoupons = new SetterCoupons(key, Float.parseFloat(amount), true, "");
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCoupons)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                .child(key)
                .setValue(setterCoupons)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            xConversions.showToast("saved", false);
                            loadData();
                            etAmount.setText("");
                        } else
                            xConversions.showToast("Saving Failed", false);
                    }
                });
    }

    private void loadData() {
        listCoupons = new ArrayList();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childCoupons)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterCoupons setter = snapshot.getValue(SetterCoupons.class);
                            listCoupons.add(setter);
                        }

                        adapter = new Adapter(listCoupons);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List listAdapter;

        public Adapter(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_coupon;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Adapter.Holder holder = new Adapter.Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Adapter.Holder holder, int position) {
            holder.bind((SetterCoupons) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvAmount, tvMerchantName;
            CardView cardStatus;

            public Holder(View itemView) {
                super(itemView);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                tvMerchantName = itemView.findViewById(R.id.tvMerchantName);
            }

            void bind(final SetterCoupons setter) {
                tvMerchantName.setText(prefs.getString(AppInfo.MERCHANT_NAME, ""));
                tvAmount.setText(xConversions.getFullPrice(setter.getAmount()));
            }
        }

    }

}
