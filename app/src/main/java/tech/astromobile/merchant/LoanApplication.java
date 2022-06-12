package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static tech.astromobile.merchant.Checkout.setterOrders;

public class LoanApplication extends AppCompatActivity {
    Context context;
    SharedPreferences prefs;
    XMethods xMethods;
    DBOperations dbOperations;
    SQLiteDatabase db;
    XConversions xConversions;
    SharedPreferences.Editor editor;
    static String natId = "";

    EditText etOTP;
    TextView etAmount;
    TextView tvPaymentPeriod,tvAccountNumber,/*tvMicroFinanceName,*/ tvBankName,
            tvName;

    ProgressBar progressBar;
    //EditText etPayeeCode;
    //CheckBox checkAgree;
    static double LIMIT = 10000;
    static double INTEREST_RATE = 0;
    static int PAYMENT_PERIOD = 3;
    static double AMOUNT = 0;
    static double AMOUNT_DISBURSED = 0;
    static double MONTHLY_PAYMENT = 0;
    int SELECTED_MFI = 0;
    boolean isBankDisbursement = true;
    SetterClient setterClient;
    SetterBankingDetails setterBank;
    SeekBar seekBar;
    RecyclerView recyclerViewSelection;

    static List listMFI;
    static List listPaymentPeriod;
    static List listBankCharges;
    AdapterSelection adapterSelection;
    static String MFI_UID = "";
    int OTP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = this;
        xMethods = new XMethods(context);
        xConversions = new XConversions(context);
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        natId = getIntent().getExtras().getString("natId");
        AMOUNT = getIntent().getExtras().getDouble("total");
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        etAmount = findViewById(R.id.etAmount);
        tvName = findViewById(R.id.tvName);
        //tvMonthlyPayment =  findViewById(R.id.tvMonthlyPayment);
        tvPaymentPeriod =  findViewById(R.id.tvPaymentPeriod);
        tvAccountNumber =  findViewById(R.id.tvAccountNumber);
        //tvMicroFinanceName =  findViewById(R.id.tvMicroFinanceName);
        tvBankName = findViewById(R.id.tvBankName);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);
        etOTP = findViewById(R.id.etOTP);
        recyclerViewSelection = findViewById(R.id.recyclerViewSelection);
        String amount = "$" + xMethods.getFullAMount(AMOUNT) + " ZWL";
        etAmount.setText(amount);


        xMethods.initializeSingleRecyclerviewLayouts(recyclerViewSelection, LinearLayoutManager.HORIZONTAL);

        progressBar.setVisibility(View.VISIBLE);
        isBankDisbursement = true;

        getMicroFinancesAndPaymentPeriod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PAYMENT_PERIOD = progress + 1;
                tvPaymentPeriod.setText("" + PAYMENT_PERIOD + " Months");
                getMicroFinancesAndPaymentPeriod();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                tvPaymentPeriod.setText("" + PAYMENT_PERIOD + " Months");
                getMicroFinancesAndPaymentPeriod();
            }
        });
        tvBankName.setText(prefs.getString(AppInfo.MERCHANT_BANK, ""));
        tvAccountNumber.setText(String.valueOf(prefs.getLong(AppInfo.MERCHANT_ACCOUNT_NUMBER, 0)));
        xConversions.showToast("" + calculateMonthlyPayment(130.50, 5.2, 3), true);
    }

    private void getMicroFinancesAndPaymentPeriod() {
        if (listMFI == null || listPaymentPeriod == null) {
            listMFI = new ArrayList();
            listPaymentPeriod = new ArrayList();
            listBankCharges = new ArrayList();

            FirebaseDatabase.getInstance().getReference()
                    .child("microFinances")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists())
                                return;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //for (DataSnapshot snap : snapshot.getChildren()) {
                                SetterMicroFinance setterMicroFinance = snapshot.getValue(SetterMicroFinance.class);
                                listMFI.add(setterMicroFinance);
                                //}
                            }

                            FirebaseDatabase.getInstance().getReference()
                                    .child("additionalBankCharges")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists())
                                                return;

                                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                SetterBankCharges setterBankCharges = snapshot.getValue(SetterBankCharges.class);
                                                listBankCharges.add(setterBankCharges);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                            FirebaseDatabase.getInstance().getReference()
                                    .child("paymentPeriod")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists())
                                                return;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    SetterPaymentPeriod setterPaymentPeriod = snap.getValue(SetterPaymentPeriod.class);
                                                    listPaymentPeriod.add(setterPaymentPeriod);
                                                }
                                            }
                                            getSuitableMicroFinancesFromList();
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
        } else
            getSuitableMicroFinancesFromList();
    }

    private void getSuitableMicroFinancesFromList() {
        List list = new ArrayList();
        for (int x = 0; x < listPaymentPeriod.size(); x++) {
            SetterPaymentPeriod setterPaymentPeriod = (SetterPaymentPeriod) listPaymentPeriod.get(x);
            if (setterPaymentPeriod.getPaymentPeriod() == PAYMENT_PERIOD) {
                list.add(setterPaymentPeriod);
            }
        }
        if (list.size() == 0) {
            SetterPaymentPeriod setter = new SetterPaymentPeriod("", "", 0, 0);
            list.add(setter);
        }
        adapterSelection = new AdapterSelection(list, context);
        recyclerViewSelection.setAdapter(adapterSelection);
    }



    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference()
                .child("client")
                .child(natId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;
                        setterClient = dataSnapshot.getValue(SetterClient.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("approvalStatus")
                .child(natId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            return;
                        } else {
                            //for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterApprovalStatus setter = dataSnapshot.getValue(SetterApprovalStatus.class);
                            OTP = setter.getOtp();

                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        getCreditLimit();
    }

    double calculateMonthlyPayment(double loanAmount, double interestRate, int paymentPeriod) {
        interestRate /= 100;
        double monthlyPayment = loanAmount/(Math.pow((1+ interestRate), paymentPeriod) - 1)/
                (interestRate*Math.pow((1+interestRate), paymentPeriod));

        return xMethods.formatPrice(monthlyPayment);
    }

    public void applyForLoan(View view) {
        String otp = etOTP.getText().toString().trim();
        if (AMOUNT == 0 && MONTHLY_PAYMENT == 0 && MFI_UID.equals("")) {
            xMethods.showNotification("Select Micro Finance of choice");
            return;
        }
        if (otp.equals("")) {
            xMethods.showNotification(view, "Enter Customer OTP");
            return;
        }
        if (OTP != Integer.parseInt(otp)) {
            xMethods.showNotification(view, "OTP didn't match");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        String key = FirebaseDatabase.getInstance().getReference().child("LoanApplication").child(natId).push().getKey();
        String agreementKey = FirebaseDatabase.getInstance().getReference().child("loanAgreement").push().getKey();

        SetterLoanApplication setterLoanApplication = new SetterLoanApplication(key, MFI_UID,
                prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "merchant"),
                natId,
                prefs.getString(AppInfo.MERCHANT_BANK, ""), setterOrders.getKey(),
                AMOUNT, MONTHLY_PAYMENT, LIMIT,
                prefs.getLong(AppInfo.MERCHANT_ACCOUNT_NUMBER, 0), setterClient.getPhoneNumber(),
                "false", false,
                isBankDisbursement, xMethods.addMonths(PAYMENT_PERIOD),
                0,
                xMethods.getNextMonthTimestamp(System.currentTimeMillis()),
                "timeStamp", INTEREST_RATE,
                true, prefs.getString(AppInfo.MERCHANT_NAME, "merchant name"));

        SetterLoanAgreement setterLoanAgreement = new SetterLoanAgreement(agreementKey, key, natId, true);
        FirebaseDatabase.getInstance().getReference()
                .child("loanAgreement")
                .child(agreementKey)
                .setValue(setterLoanAgreement);

        FirebaseDatabase.getInstance().getReference()
                .child("LoanApplication")
                .child(natId)
                .child(key)
                .setValue(setterLoanApplication)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isComplete()) {
                            //tvMonthlyPayment.setText("$0 ZWL");
                            tvPaymentPeriod.setText("0");
                            Toast.makeText(context, "Credit Application Successful", Toast.LENGTH_LONG).show();
                            placeOrder();
                        } else
                            Toast.makeText(context, "Loan Application Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void placeOrder() {
        xConversions.showToast("Recording Your Order", true);
        setterOrders.setClientUid(natId);
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
                                setterMeals.setOrderKey(setterOrders.getKey());
                                FirebaseDatabase.getInstance().getReference()
                                        .child(StaticVals.childOrderedItems)
                                        .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, ""))
                                        .child(natId)
                                        .push()
                                        .setValue(setterMeals);
                                new InsertOrderedMeals(context, setterOrders.getKey()).execute(setterMeals);
                            }

                            MainActivity.listMealsOrdered = null;
                            startActivity(new Intent(context, MainActivity.class));
                            xConversions.showToast("Order Successful", true);
                        }
                    }
                });
    }

    void getCreditLimit() {
        FirebaseDatabase.getInstance().getReference()
                .child("approvalStatus")
                .child(natId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (!dataSnapshot.exists())
                            return;
                        //for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        SetterApprovalStatus setter = dataSnapshot.getValue(SetterApprovalStatus.class);
                        LIMIT = setter.getCreditLimit();
                        //}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
/*
    class AdapterSelection extends RecyclerView.Adapter<AdapterSelection.Holder> {
        private List listAdapter;
        Context context;
        List<String> listTime;

        public AdapterSelection(List listAdapter, Context context) {
            this.listAdapter = listAdapter;
            this.context = context;
            listTime = new ArrayList<>();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_microfinace;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterPaymentPeriod) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName, tvInterestRate, tvMonthlyPayment;
            ConstraintLayout constLay;
            Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvInterestRate = itemView.findViewById(R.id.tvInterestRate);
                tvMonthlyPayment = itemView.findViewById(R.id.tvMonthlyPayment);
                constLay = itemView.findViewById(R.id.constLay);
            }

            void bind(final SetterPaymentPeriod setter) {
                if (setter.getMfiUid().equals("") && setter.getKey().equals("")
                        && setter.getInterestRate() == 0 && setter.getPaymentPeriod() == 0) {
                    tvName.setText("Micro Finance");
                    tvInterestRate.setText("not");
                    tvMonthlyPayment.setText("FOUND");
                    constLay.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_mfi_red));
                } else {
                    if (SELECTED_MFI == getAdapterPosition()) {
                        MFI_UID = setter.getMfiUid();
                        INTEREST_RATE = setter.getInterestRate();
                        PAYMENT_PERIOD = setter.getPaymentPeriod();
                        MONTHLY_PAYMENT = calculateMonthlyPayment(AMOUNT, setter.getInterestRate(), setter.getPaymentPeriod());
                        constLay.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_mfi_shagi_gray));

                        tvName.setTextColor(context.getResources().getColor(R.color.white));
                        tvInterestRate.setTextColor(context.getResources().getColor(R.color.white));
                        tvMonthlyPayment.setTextColor(context.getResources().getColor(R.color.white));
                    }

                    String interestRate = "" + setter.getInterestRate() + "% interest";
                    double monthlyPay = calculateMonthlyPayment(AMOUNT, setter.getInterestRate(), setter.getPaymentPeriod());
                    String monthlyPayment = "$" + xMethods.getFullAMount(monthlyPay) + " ZWL monthly";
                    tvInterestRate.setText(interestRate);
                    tvName.setText(getMfiNameWithThisUid(setter.getMfiUid()));
                    tvMonthlyPayment.setText(monthlyPayment);
                }
                constLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SELECTED_MFI = getAdapterPosition();
                        getMicroFinancesAndPaymentPeriod();
                    }
                });
            }

        }

        private String getMfiNameWithThisUid(String mfiUid) {
            for (int x = 0; x < listMFI.size(); x++) {
                SetterMicroFinance setterMicroFinance = (SetterMicroFinance) listMFI.get(x);
                if (setterMicroFinance.getUid().equals(mfiUid))
                    return setterMicroFinance.getName();
            }

            return "";
        }

    }*/

    class AdapterSelection extends RecyclerView.Adapter<AdapterSelection.Holder> {
        private List listAdapter;
        Context context;
        List<String> listTime;

        public AdapterSelection(List listAdapter, Context context) {
            this.listAdapter = listAdapter;
            this.context = context;
            listTime = new ArrayList<>();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_mfi_shop;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterPaymentPeriod) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName, tvInterestRate, tvMonthlyPayment, tvPaymentType, tvTotalCharges, tvAmountDisbursed, tvP;
            CardView card1;

            Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvInterestRate = itemView.findViewById(R.id.tvInterestRate);
                tvMonthlyPayment = itemView.findViewById(R.id.tvMonthlyPayment);
                tvAmountDisbursed = itemView.findViewById(R.id.tvAmountDisbursed);
                tvTotalCharges = itemView.findViewById(R.id.tvTotalCharges);
                tvPaymentType = itemView.findViewById(R.id.tvPaymentType);
                tvP = itemView.findViewById(R.id.tvP);
                card1 = itemView.findViewById(R.id.card1);
            }

            void bind(final SetterPaymentPeriod setter) {
                if (setter.getMfiUid().equals("") && setter.getKey().equals("")
                        && setter.getInterestRate() == 0 && setter.getPaymentPeriod() == 0) {
                    tvName.setText("Micro Finance not found");
                    tvInterestRate.setText("change payment period");
                    tvMonthlyPayment.setText("none");
                    tvName.setTextColor(context.getResources().getColor(R.color.black));
                    tvInterestRate.setTextColor(context.getResources().getColor(R.color.black));
                    tvMonthlyPayment.setTextColor(context.getResources().getColor(R.color.black));
                    card1.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_mfi_red));
                } else {
                    if (SELECTED_MFI == getAdapterPosition()) {
                        MFI_UID = setter.getMfiUid();
                        INTEREST_RATE = setter.getInterestRate();
                        PAYMENT_PERIOD = setter.getPaymentPeriod();
                        MONTHLY_PAYMENT = calculateMonthlyPayment(AMOUNT, setter.getInterestRate(), setter.getPaymentPeriod());
                        card1.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_mfi_shagi_gray));

                        tvName.setTextColor(context.getResources().getColor(R.color.white));
                        tvInterestRate.setTextColor(context.getResources().getColor(R.color.white));
                        tvMonthlyPayment.setTextColor(context.getResources().getColor(R.color.white));

                        AMOUNT_DISBURSED = AMOUNT + getTotalCharges(setter.getMfiUid());
                    }

                    String interestRate = "" + setter.getInterestRate() + "%";
                    double monthlyPay = calculateMonthlyPayment(AMOUNT, setter.getInterestRate(), setter.getPaymentPeriod());
                    String monthlyPayment = "$" + xMethods.getFullAMount(monthlyPay);
                    tvInterestRate.setText(interestRate);
                    tvName.setText(getMfiNameWithThisUid(setter.getMfiUid()));
                    tvMonthlyPayment.setText(monthlyPayment);
                    tvPaymentType.setText(getMicroFinancePaymentType(setter.getMfiUid()));

                    double disbursed = AMOUNT + getTotalCharges(setter.getMfiUid());
                    tvAmountDisbursed.setText("$" + xMethods.getFullAMount(disbursed));
                    String totalCharges = "$" + xMethods.getFullAMount(disbursed - AMOUNT);
                    tvTotalCharges.setText(totalCharges);

                }
                card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SELECTED_MFI = getAdapterPosition();
                        getMicroFinancesAndPaymentPeriod();
                    }
                });
            }

        }

        /*float calculateTotalLoan(String mfiUid) {
            double totalLoan = AMOUNT + getTotalCharges(mfiUid);
        }*/

        private float getTotalCharges(String mfiUid) {
            float charges = 0;
            for (int x = 0; x < listBankCharges.size(); x++) {
                SetterBankCharges setter = (SetterBankCharges) listBankCharges.get(x);
                if (setter.getUid().equals(mfiUid)) {
                    if (setter.getAdminFees() != 0) {
                        charges += ((setter.getAdminFees() / 100) * AMOUNT);
                    }if (setter.getLifeInsurance() != 0)
                        charges += ((setter.getLifeInsurance()/100) * AMOUNT);
                    if (setter.getTaxPrincipalAmount() != 0)
                        charges += ((setter.getTaxPrincipalAmount()/100) * AMOUNT);
                }
            }
            return charges;
        }

        private String getMicroFinancePaymentType(String mfiUid) {
            String type = "";
            for (int x = 0; x < listMFI.size(); x++) {
                SetterMicroFinance setterMicroFinance = (SetterMicroFinance) listMFI.get(x);
                if (setterMicroFinance.getUid().equals(mfiUid))
                    if (setterMicroFinance.isFixed())
                        type = "Fixed";
                    else
                        type = "Reducing Balance";
            }

            return type;
        }

        private String getMfiNameWithThisUid(String mfiUid) {
            for (int x = 0; x < listMFI.size(); x++) {
                SetterMicroFinance setterMicroFinance = (SetterMicroFinance) listMFI.get(x);
                if (setterMicroFinance.getUid().equals(mfiUid))
                    return setterMicroFinance.getName();
            }

            return "";
        }

    }

}
