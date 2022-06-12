package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    Context context;
    SharedPreferences prefs;
    XConversions xConversions;
    SharedPreferences.Editor editor;
    TextView tvMerchantName, tvApproved, tvPending, tvDeclined, tvTodayNumbers, tvMonthNumbers, tvYearNumbers, tvCoupon;
    ProgressBar progressBar;
    Switch switchCoupon;

    List listOrders, listOrderLoans, listYourClients;
    String licenceNumber = "";
    static SetterMerchant setterMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        xConversions = new XConversions(context);
        listOrders = new ArrayList(); listOrderLoans = new ArrayList(); listYourClients = new ArrayList();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        licenceNumber = prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "");
        tvMerchantName = findViewById(R.id.tvMerchantName);
        tvApproved = findViewById(R.id.tvApproved);
        tvPending = findViewById(R.id.tvPending);
        tvDeclined = findViewById(R.id.tvDeclined);
        progressBar = findViewById(R.id.progressBar);

        tvTodayNumbers= findViewById(R.id.tvTodayNumbers);
        tvMonthNumbers = findViewById(R.id.tvMonthNumbers);
        tvYearNumbers = findViewById(R.id.tvYearNumbers);
        tvMerchantName = findViewById(R.id.tvMerchantName);
        tvCoupon = findViewById(R.id.tvCoupon);
        switchCoupon = findViewById(R.id.switchCoupon);
        tvMerchantName.setText(prefs.getString(AppInfo.MERCHANT_NAME, ""));
        tvCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Coupons.class));
            }
        });
        switchCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference()
                        .child(StaticVals.childMerchant)
                        .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists())
                                    return;

                                progressBar.setVisibility(View.GONE);
                                setterMerchant = dataSnapshot.getValue(SetterMerchant.class);

                                if (setterMerchant.isOfferingCoupons()) {
                                    setterMerchant.setOfferingCoupons(false);
                                    FirebaseDatabase.getInstance().getReference()
                                            .child(StaticVals.childMerchant)
                                            .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                                            .setValue(setterMerchant)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    switchCoupon.setChecked(setterMerchant.isOfferingCoupons());
                                                }
                                            });
                                } else {
                                    setterMerchant.setOfferingCoupons(true);
                                    FirebaseDatabase.getInstance().getReference()
                                            .child(StaticVals.childMerchant)
                                            .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                                            .setValue(setterMerchant)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    switchCoupon.setChecked(setterMerchant.isOfferingCoupons());
                                                    startActivity(new Intent(context, Coupons.class));
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
        getCoupValue();

        FirebaseDatabase.getInstance().getReference()
                .child("LoanApplication")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        progressBar.setVisibility(View.GONE);
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                SetterLoanApplication setterLoanApplication = snap.getValue(SetterLoanApplication.class);
                                if (setterLoanApplication.getClientOfficerUid().equals(licenceNumber))
                                    listOrderLoans.add(setterLoanApplication);
                            }
                        }

                        updateFields();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void getCoupValue() {
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childMerchant)
                .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        setterMerchant = dataSnapshot.getValue(SetterMerchant.class);
                        switchCoupon.setChecked(setterMerchant.isOfferingCoupons());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateFields() {
        int approved = 0, pending = 0, declined = 0;

        for (int x = 0; x < listOrderLoans.size(); x++) {
            SetterLoanApplication setterLoanApplication = (SetterLoanApplication) listOrderLoans.get(x);
            if (setterLoanApplication.isApproved().equals("review") || setterLoanApplication.isApproved().equals("false"))
                pending++;
            if (setterLoanApplication.isApproved().equals("true"))
                approved++;
            if (setterLoanApplication.isApproved().equals("disapproved"))
                declined++;
        }

        tvPending.setText("" + approved); tvPending.setText("" + pending); tvDeclined.setText("" + declined);
        getNumbersByDates();
    }

    private void getNumbersByDates() {
        int today = 0, month = 0, year = 0;
        for (int x = 0; x < listOrderLoans.size(); x++) {
            SetterLoanApplication setterLoanApplication = (SetterLoanApplication) listOrderLoans.get(x);
            long orderTime = setterLoanApplication.getTimestampApply();
            if (xConversions.isToday(orderTime))
                today++;
            if (xConversions.isMonth(orderTime))
                month++;
            if (xConversions.isYear(orderTime))
                year++;
        }

        tvTodayNumbers.setText("" + today); tvMonthNumbers.setText("" + month); tvYearNumbers.setText("" + year);
    }

    public void addCats(View view) {
        startActivity(new Intent(context, AddCategories.class));
    }

    public void addGoods(View view) {
        startActivity(new Intent(context, Items.class));
    }

    public void logout(View view) {
        editor = prefs.edit();
        editor.putBoolean(AppInfo.IS_LOGGED_IN, false);
        editor.apply();
        xConversions.showToast("Logged Out", true);
        startActivity(new Intent(context, Login.class));
    }

    public void openOrders(View view) {
        startActivity(new Intent(context, Orders.class));
    }
}
