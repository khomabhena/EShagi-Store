package tech.astromobile.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class AddGoods extends AppCompatActivity {

    CameraPhoto cameraPhoto;
    final int CAMERA_REQUEST = 123;
    GalleryPhoto galleryPhoto;
    final int GALLERY_REQUEST = 27277;
    private static final int REQUEST_WRITE_IMAGE = 1994;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    String imageFilePath;

    Context context;
    CardView cardSave;
    DBOperations dbOperations;
    SQLiteDatabase db;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ImageView ivImage;
    Switch switchAvailable, switchHomeDelivery;
    Spinner spinner;
    EditText etName, etPrice, etTakeawayCharge, etSize, etDescription;
    ProgressBar progressBar;
    TextView tvMerchantName;
    List listCategories = new ArrayList();

    static boolean isAvailable = true, isHomeDelivery = true;
    String type = "", LINK = "";
    XConversions xConversions;

    String name;
    String catName;
    String price;
    String takeawayCharge;
    String size;
    String description;
    ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        galleryPhoto = new GalleryPhoto(context);
        cameraPhoto = new CameraPhoto(context);
        xConversions = new XConversions(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        //listCategories = dbOperations.getCategorySetter(db);
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);

        cardSave = findViewById(R.id.cardSave);
        ivImage = findViewById(R.id.ivImage);
        switchAvailable = findViewById(R.id.switchAvailable);
        switchHomeDelivery = findViewById(R.id.switchHomeDelivery);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);
        etTakeawayCharge = findViewById(R.id.etTakeawayCharge);
        etSize = findViewById(R.id.etSize);
        etDescription = findViewById(R.id.etDescription);
        tvMerchantName = findViewById(R.id.tvMerchantName);

        //xConversions.spinnerSetAdapter(spinner, StaticVals.arrayTypes);
        setSpinnerAdapter();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    return;
                SetterCategories setterCategories = (SetterCategories) listCategories.get(i - 1);
                type = setterCategories.getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tvMerchantName.setText(prefs.getString(AppInfo.MERCHANT_NAME, ""));
        switchAvailable.setChecked(isAvailable);
        switchHomeDelivery.setChecked(isHomeDelivery);
        cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        if (getIntent().getExtras() != null) {
            name = getIntent().getExtras().getString("name");
            price = getIntent().getExtras().getString("price");
            takeawayCharge = getIntent().getExtras().getString("takeaway");
            size = getIntent().getExtras().getString("size");
            description = getIntent().getExtras().getString("description");
            LINK = getIntent().getExtras().getString("link");
            type = getIntent().getExtras().getString("category");

            etName.setText(name); etPrice.setText(price); etTakeawayCharge.setText(takeawayCharge);
            etSize.setText(size); etDescription.setText(description);
            xConversions.insertGlideImage(LINK, ivImage);
            spinnerAdapter = xConversions.setSpinnerAdapter(spinner, listCategories);
            int pos = spinnerAdapter.getPosition(getCategoryWithThisKey(type));
            spinner.setSelection(pos);
        }
    }

    private String getCategoryWithThisKey(String type) {
        for (int x = 0; x < listCategories.size(); x++) {
            SetterCategories setterCategories = (SetterCategories) listCategories.get(x);
            if (setterCategories.getKey().equals(type))
                return setterCategories.getName();
        }

        return "";
    }

    private void setSpinnerAdapter() {
        new BackTask().execute();
    }

    private class BackTask extends AsyncTask<Void, SetterCategories, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getCategory(db);
            int count = cursor.getCount();

            String key, name;
            boolean isAvailable;

            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY));
                name = cursor.getString(cursor.getColumnIndex(DBContract.Categories.NAME));
                isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Categories.IS_AVAILABLE)).equals("yes");

                SetterCategories setter = new SetterCategories(key, name, 0, isAvailable);

                if (isAvailable)
                    publishProgress(setter);
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterCategories... values) {
            listCategories.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            spinnerAdapter = xConversions.setSpinnerAdapter(spinner, listCategories);
        }
    }

    private void save() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String takeawayCharge = etTakeawayCharge.getText().toString().trim();
        String size = etSize.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        if (LINK.equals(""))
            showToast("Select item image");
        else if (type.equals(""))
            showToast("Select Category");
        else if (name.equals(""))
            showToast("Fill in name");
        else if (size.equals(""))
            showToast("Fill in size");
        else if (price.equals(""))
            showToast("Fill in price");/*
        else if (takeawayCharge.equals(""))
            showToast("Set  delivery charge to at least 0");*/
        else {
            progressBar.setVisibility(View.VISIBLE);
            if (takeawayCharge.equals(""))
                takeawayCharge = "0";

            String key = FirebaseDatabase.getInstance().getReference().child(StaticVals.childItems)
                    .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0")).child(type).push().getKey();

            SetterMeals setterMeals = new SetterMeals(key, type, name, size, description, LINK, "", 0,
                    Double.parseDouble(price), 0, isAvailable, isHomeDelivery, System.currentTimeMillis());

            FirebaseDatabase.getInstance().getReference()
                    .child(StaticVals.childItems)
                    .child(prefs.getString(AppInfo.MERCHANT_LICENCE_NUMBER, "0"))
                    .child(type)
                    .child(key)
                    .setValue(setterMeals)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("Saved");
                                etName.setText(""); etPrice.setText(""); LINK = ""; type = ""; etDescription.setText("");
                                ivImage.setImageResource(0);

                                //xConversions.spinnerSetAdapter(spinner, StaticVals.arrayTypes);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public void onSwitchClick(View view) {
        isAvailable = switchAvailable.isChecked();
        isHomeDelivery = switchHomeDelivery.isChecked();
    }




    public void getImageFromStorage(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_WRITE_IMAGE);
            } else {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        } else {
            startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
        }
    }

    public void getImageFromCamera(View view) {
        openCameraIntent();
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,context.getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            handlePosterResult(data);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAPTURE_IMAGE) {
            handlePosterResult(imageFilePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features that required the permission
                    startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                } else {
                    Toast.makeText(context, "Allowed access to your storage.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void handlePosterResult(Intent data) {
        Uri uri = data.getData();
        progressBar.setVisibility(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("meal-pics/"+ uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Image UploadFailed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-accType, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        LINK = uri.toString();

                        xConversions.insertGlideImage(uri.toString(), ivImage);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void handlePosterResult(String photoPath) {
        Uri uri = Uri.fromFile(new File(photoPath));
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(context, "Processing Image", Toast.LENGTH_SHORT).show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("lancet-forms/"+ uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Image UploadFailed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-accType, and download URL.
                final Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        LINK = uri.toString();

                        xConversions.insertGlideImage(uri.toString(), ivImage);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}
