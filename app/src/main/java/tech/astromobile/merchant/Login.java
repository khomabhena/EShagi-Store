package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    EditText etOTP, etNationalID;
    ProgressBar progressBar;
    static boolean isSplashScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        FirebaseAuth.getInstance().signInAnonymously();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        etOTP = findViewById(R.id.etOTP);
        etNationalID = findViewById(R.id.etNationalID);
        progressBar = findViewById(R.id.progressBar);
        /*if (!isSplashScreen) {
            isSplashScreen = true;
            startActivity(new Intent(context, SplashScreen.class));
            finish();
            return;
        }*/
    }

    public void openLogin(View view) {
        if (etOTP.getText().toString().equals("") && etNationalID.getText().toString().equals(""))
            Toast.makeText(context, "Enter your OTP and Licence Number", Toast.LENGTH_LONG).show();
        else {
            progressBar.setVisibility(View.VISIBLE);
            final int otp = Integer.parseInt(etOTP.getText().toString().trim());
            String natId = etNationalID.getText().toString().trim();
            if (prefs.getInt(AppInfo.OTP, 0000) == otp && prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "natId").equals(natId)) {
                editor = prefs.edit();
                editor.putBoolean(AppInfo.IS_LOGGED_IN, true);
                editor.apply();
                startActivity(new Intent(context, MainActivity.class));
            } else
                FirebaseDatabase.getInstance().getReference()
                        .child(StaticVals.childMerchant)
                        .child(natId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                                    etOTP.setText("");
                                    etNationalID.setText("");
                                } else {
                                    //for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    SetterMerchant setter = dataSnapshot.getValue(SetterMerchant.class);

                                    if (otp == setter.getOtp()) {
                                        editor = prefs.edit();
                                        editor.putString(AppInfo.MERCHANT_NAME, setter.getName());
                                        editor.putString(AppInfo.MERCHANT_BANK, setter.getBank());
                                        editor.putLong(AppInfo.MERCHANT_ACCOUNT_NUMBER, setter.getAccountNumber());
                                        editor.putString(AppInfo.MERCHANT_LICENCE_NUMBER, String.valueOf(setter.getLicenceNumber()));
                                        editor.putBoolean(AppInfo.IS_LOGGED_IN, true);
                                        editor.putInt(AppInfo.OTP, otp);
                                        editor.apply();


                                        startActivity(new Intent(context, MainActivity.class));
                                    } else {
                                        Toast.makeText(context, "OTP did'nt match", Toast.LENGTH_SHORT).show();
                                    }
                                    //}
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                etOTP.setText("");
                                etNationalID.setText("");
                                progressBar.setVisibility(View.GONE);
                                FirebaseAuth.getInstance().signInAnonymously();
                                //new XMethods(context).showNotification("" + databaseError.getMessage());
                            }
                        });
        }
    }

    public void openKYC(View view) {
        //startActivity(new Intent(context, KYC.class));
    }

    public void openPlayStore(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=tech.astromobile.e_shagi"));
        startActivity(i);
    }
}
