package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Checkout extends AppCompatActivity {

    DBOperations dbOperations;
    SQLiteDatabase db;

    Context context;
    XConversions xConversions;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static SetterOrders setterOrders;
    static SetterClient setterClient;
    static SetterApprovalStatus setterApprovalStatus;
    static SetterLoanApplication setterLoanApplication;
    EditText etNationalID;
    TextView  tvServiceCharge, tvTotal, tvCustomerName,
            tvCustomerSurname, tvCreditLimit, tvSpendingAmount, tvCusReg, tvMerchantName, tvSubTotal;
    CardView cardReg, cardPay;
    ProgressBar progressBarCus;
    String natId = "";


    double charge;
    double itemProcessing = 0.5;
    static boolean isStillSearching = false;
    double total= 0;
    static double lastTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        xConversions = new XConversions(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();

        cardReg = findViewById(R.id.cardReg);
        cardPay = findViewById(R.id.cardPay);
        etNationalID = findViewById(R.id.etNationalID);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvServiceCharge = findViewById(R.id.tvServiceCharge);
        tvTotal = findViewById(R.id.tvTotal);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerSurname = findViewById(R.id.tvCustomerSurname);
        tvCreditLimit = findViewById(R.id.tvCreditLimit);
        //tvLoanApplied = findViewById(R.id.tvLoanApplied);
        tvSpendingAmount = findViewById(R.id.tvSpendingAmount);
        progressBarCus = findViewById(R.id.progressBarCus);
        tvCusReg = findViewById(R.id.tvCusReg);
        tvMerchantName = findViewById(R.id.tvMerchantName);

        initializeOrderSetter();
        calculateTotal();
        tvMerchantName.setText(prefs.getString(AppInfo.MERCHANT_NAME, ""));
        etNationalID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                natId = etNationalID.getText().toString().trim();
                if (natId.equals(""))
                    return;

                if (natId.length() > 7) {
                    progressBarCus.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference()
                            .child("client")
                            .child(natId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    progressBarCus.setVisibility(View.GONE);
                                    if (!dataSnapshot.exists()) {
                                        cardReg.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                    cardReg.setVisibility(View.GONE);
                                    //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    setterClient = dataSnapshot.getValue(SetterClient.class);

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("approvalStatus")
                                            .child(natId)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) {
                                                        return;
                                                    } else {
                                                        progressBarCus.setVisibility(View.GONE);
                                                        //for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                        setterApprovalStatus = dataSnapshot.getValue(SetterApprovalStatus.class);

                                                        if (setterApprovalStatus.getOtp() == 0 && setterApprovalStatus.getCreditLimit() == 0) {
                                                            Toast.makeText(context, "Client registration is pending", Toast.LENGTH_SHORT).show();
                                                            cardReg.setVisibility(View.VISIBLE);
                                                            tvCusReg.setText("Pending Verification");
                                                        } else {
                                                            cardReg.setVisibility(View.GONE);
                                                            Toast.makeText(context, "Client is registered", Toast.LENGTH_SHORT).show();
                                                            getLoanDetails(natId);
                                                            getLoanApprovalDetails(setterApprovalStatus);
                                                        }
                                                        //}
                                                    }
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getLoanDetails(String natId) {
        FirebaseDatabase.getInstance().getReference()
                .child("LoanApplication")
                .child(natId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            setterLoanApplication = snapshot.getValue(SetterLoanApplication.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        FirebaseAuth.getInstance().signInAnonymously();
                    }
                });
    }

    void getLoanApprovalDetails(SetterApprovalStatus setter) {
        tvCustomerName.setText(setterClient.getName());
        tvCustomerSurname.setText(setterClient.getSurname());

        double creditLimit = setterApprovalStatus.getCreditLimit();

        tvCreditLimit.setText("$"+ creditLimit + " ZWL");
        //tvLoanApplied.setText("$" + loan + " ZWL");

        double spending = setter.getCreditLimit() * 0.9;
        tvSpendingAmount.setText("$" + spending + " ZWL");

        calculateIfClientCanAffordPurchase(spending);
    }

    private void calculateIfClientCanAffordPurchase(double spending) {
        if (total > spending) {
            cardPay.setVisibility(View.GONE);
        } else {
            cardPay.setVisibility(View.VISIBLE);
            cardPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, LoanApplication.class);
                    intent.putExtra("natId", natId);
                    intent.putExtra("total", total);
                    startActivity(intent);
                }
            });
        }

    }

    private void placeOhder() {
        /*String otp = etClientOtp.getText().toString().trim();
        if (otp.equals(""))
            xConversions.showToast("Let customer enter PIN", true);
        else {
            if (Integer.parseInt(otp) != setterApprovalStatus.getOtp())
                xConversions.showToast("Wrong customer PIN", true);
            else {
                xConversions.showToast("Recording Your Order", true);
                setterOrders.setClub(false);
                setterOrders.setTimestamp(System.currentTimeMillis());
                FirebaseDatabase.getInstance().getReference()
                        .child(StaticVals.childOrders)
                        .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                        .child(natId)
                        .child(setterOrders.getKey())
                        .setValue(setterOrders)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    for (int x = 0; x < MainActivity.listMealsOrdered.size(); x++) {
                                        SetterMeals setterMeals = (SetterMeals) MainActivity.listMealsOrdered.get(x);

                                        FirebaseDatabase.getInstance().getReference()
                                                .child(StaticVals.childOrderedItems)
                                                .child(setterOrders.getKey())
                                                .push()
                                                .setValue(setterMeals);
                                        new InsertOrderedMeals(context, setterOrders.getKey()).execute(setterMeals);
                                    }
                                    setNewCreditLimit();

                                    MainActivity.listMealsOrdered = null;
                                    startActivity(new Intent(context, MainActivity.class));
                                    xConversions.showToast("Order Successful", true);
                                }
                            }
                        });
            }
        }*/
    }

    private void setNewCreditLimit() {
        double credLim = setterApprovalStatus.creditLimit;
        double newCredLim = credLim - total;
        setterApprovalStatus.setCreditLimit(newCredLim);
        setterLoanApplication.setCreditLimit(newCredLim);

        FirebaseDatabase.getInstance().getReference()
                .child("approvalStatus")
                .child(natId).setValue(setterApprovalStatus);

        FirebaseDatabase.getInstance().getReference()
                .child("LoanApplication")
                .child(natId)
                .child(setterLoanApplication.getKey()).setValue(setterLoanApplication);
    }


    private void calculateTotal() {
        double serviceCharge;
        //itemProcessing =  prefs.getFloat(AppInfo.FLO_ITEM_PROCESSING, (float) 0.01);
       // if (setterOrders.getOrderCharge() < 5)
            serviceCharge = setterOrders.getOrderCharge() * 0.5;/*
        else if (setterOrders.getOrderCharge() < 40)
            serviceCharge = prefs.getFloat(AppInfo.FLO_LESS_THAN_40, (float) 0.5);
        else
            serviceCharge = setterOrders.getOrderCharge() * prefs.getFloat(AppInfo.FLO_PERCENT_FOR_ABOVE_40, (float) 0.05);*/

        total = serviceCharge + xConversions.calculateOrderCharge() + setterOrders.getDeliveryCharge()
                + xConversions.getTakeAwayCharge(setterOrders.isTakeaway()) + MainActivity.listMealsOrdered.size() * itemProcessing;

        double subTotal = setterOrders.getOrderCharge() + xConversions.getTakeAwayCharge(setterOrders.isTakeaway());
        double service = serviceCharge + (MainActivity.listMealsOrdered.size() * itemProcessing) + setterOrders.getDeliveryCharge();
        tvSubTotal.setText(xConversions.getFullPrice(subTotal));
        tvServiceCharge.setText(xConversions.getFullPrice(service));
        tvTotal.setText(xConversions.getFullPrice(total));

        if (lastTotal != total) {
            xConversions.playAddSound();
            lastTotal = total;
        }

        setterOrders.setProcessingCharge(MainActivity.listMealsOrdered.size() * itemProcessing);
        setterOrders.setServiceCharge(serviceCharge);
        setterOrders.setTakeawayCharge(xConversions.getTakeAwayCharge(setterOrders.isTakeaway()));
        setterOrders.setTotal(xConversions.formatPrice(total));
    }

    void initializeOrderSetter() {
        String merchantUid = prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0");
        String key = FirebaseDatabase.getInstance().getReference().child(StaticVals.childOrders).push().getKey();

        setterOrders = new SetterOrders(merchantUid, "", key, xConversions.getOrderNumber(), null, null, null, null, null, null, null,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, false, false);
    }

}
