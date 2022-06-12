package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {

    Context context;
    XConversions xConversions;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    List listOrders, lisClients,listLoans, listOrderedItems;
    Adapter adapter;
    ProgressBar progressBar;
    static List listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        xConversions = new XConversions(context);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        RecyclerView[] recyclerViewsMeal = {recyclerView};
        xConversions.initializeRecyclerviewLayouts(recyclerViewsMeal, LinearLayoutManager.VERTICAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listOrders == null || listItems == null) {
            listOrders = new ArrayList();
            listItems = new ArrayList();
            listLoans = new ArrayList();
        } else {
            adapter = new Adapter(listOrders, context);
            recyclerView.setAdapter(adapter);
        }
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childOrders)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, ""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        listOrders = new ArrayList();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                SetterOrders setterOrders = snap.getValue(SetterOrders.class);
                                if (listOrders.size() == 0)
                                    listOrders.add(setterOrders);
                                else
                                    listOrders.add(0, setterOrders);
                            }
                        }

                        FirebaseDatabase.getInstance().getReference()
                                .child(StaticVals.childOrderedItems)
                                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, ""))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists())
                                            return;
                                        listItems = new ArrayList();
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                            for (DataSnapshot snapshot1: snapshot.getChildren()) {
                                                //for (DataSnapshot snap: snapshot1.getChildren()) {
                                                    SetterMeals setterMeals = snapshot1.getValue(SetterMeals.class);
                                                    listItems.add(setterMeals);
                                                //}
                                            }
                                        }

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("client")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (!dataSnapshot.exists()) {
                                                            progressBar.setVisibility(View.GONE);
                                                            return;
                                                        }
                                                        lisClients = new ArrayList();
                                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                            SetterClient setterClient = snapshot.getValue(SetterClient.class);
                                                            lisClients.add(setterClient);
                                                        }


                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("LoanApplication")
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (!dataSnapshot.exists())
                                                                            return;
                                                                        listLoans = new ArrayList();
                                                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                                            for (DataSnapshot snap: snapshot.getChildren()) {
                                                                                SetterLoanApplication setterLoanApplication = snap.getValue(SetterLoanApplication.class);
                                                                                listLoans.add(setterLoanApplication);
                                                                            }
                                                                        }
                                                                        adapter = new Adapter(listOrders, context);
                                                                        recyclerView.setAdapter(adapter);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private List listAdapter;
        Context context;
        List<String> listTime;

        public Adapter(List listAdapter, Context context) {
            this.listAdapter = listAdapter;
            this.context = context;
            listTime = new ArrayList<>();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_orders3;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterOrders) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvCustomerName, tvNationalID, tvOrderCode, tvOrderDate, tvOrderType,
                    tvItemsOrdered, tvCreditApplicationStatus, tvTotalAmount;
            CardView card1;
            Holder(View itemView) {
                super(itemView);
                tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
                tvNationalID = itemView.findViewById(R.id.tvNationalID);
                tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
                tvOrderType = itemView.findViewById(R.id.tvOrderType);
                tvItemsOrdered = itemView.findViewById(R.id.tvItemsOrdered);
                card1 = itemView.findViewById(R.id.card1);
                tvCreditApplicationStatus = itemView.findViewById(R.id.tvCreditApplicationStatus);
                tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            }

            void bind(final SetterOrders setter) {
                tvCustomerName.setText(getClientName(setter.getClientUid()));
                tvNationalID.setText(getClientNationalId(setter.getClientUid()));
                tvOrderCode.setText(setter.getOrderCode());
                tvOrderDate.setText(xConversions.getFullDate(setter.getTimestamp()));
                tvItemsOrdered.setText(getItemsOrdered(setter.getKey()));
                tvCreditApplicationStatus.setText(getCreditStatus(setter.getKey()));
                tvTotalAmount.setText(getTotalAmount(setter.getKey()));
                card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OrderedItems.class);
                        intent.putExtra("key", setter.getKey());
                        startActivity(intent);
                    }
                });
            }

        }

        private String getTotalAmount(String orderKey) {
            for (int x = 0; x < listLoans.size(); x++) {
                SetterLoanApplication setter = (SetterLoanApplication) listLoans.get(x);
                if (setter.getClientOfficerUid().equals(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "")))
                    if (setter.getOrderKey().equals(orderKey)) {
                        return xConversions.getFullPrice(setter.getAmount());
                    }
            }

            return "***";
        }

        private String getCreditStatus(String orderKey) {
            for (int x = 0; x < listLoans.size(); x++) {
                SetterLoanApplication setter = (SetterLoanApplication) listLoans.get(x);
                if (setter.getClientOfficerUid().equals(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "")))
                    if (setter.getOrderKey().equals(orderKey)) {
                        if (setter.isApproved().equals("review"))
                            return "Reviewing";
                        if (setter.isApproved().equals("false"))
                            return "Pending";
                        else if (setter.isApproved().equals("true"))
                            return "Loan Approved";
                        else if (setter.isApproved().equals("disbursed"))
                            return "Disbursed";
                    }
            }

            return "***";
        }

        private String getItemsOrdered(String key) {
            int  count = 0;
            for (int x = 0; x < listItems.size(); x++) {
                SetterMeals setterMeals = (SetterMeals) listItems.get(x);
                if (setterMeals.getOrderKey().equals(key))
                    count++;
            }

            return "" + count;
        }

        private String getClientNationalId(String clientUid) {
            for (int x = 0; x < lisClients.size(); x++) {
                SetterClient setterClient = (SetterClient) lisClients.get(x);
                if (setterClient.getUid().equals(clientUid))
                    return setterClient.getNatId();
            }

            return "";
        }

        private String getClientName(String clientUid) {
            for (int x = 0; x < lisClients.size(); x++) {
                SetterClient setterClient = (SetterClient) lisClients.get(x);
                if (setterClient.getUid().equals(clientUid))
                    return setterClient.getName() + " " + setterClient.getSurname();
            }

            return "";
        }

    }
}
