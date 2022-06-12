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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class OrderedItems extends AppCompatActivity {

    Context context;
    XConversions xConversions;
    //ConstraintSet constraintSetMain = new ConstraintSet();
    //ConstraintSet constraintSetCheckout = new ConstraintSet();
    ConstraintLayout constMain;

    static boolean isCheckout = false;

    RecyclerView recyclerViewMeals;
    ProgressBar progressBar;
    //CardView cardCart;

    Adapter adapter;

    static List listMealsOrdered;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    LinearLayoutManager layoutManagerCategory;
    String orderKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        orderKey = getIntent().getExtras().getString("key");
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        xConversions = new XConversions(context);

        constMain = findViewById(R.id.constMain);

        if (listMealsOrdered == null)
            listMealsOrdered = new ArrayList();

        progressBar = findViewById(R.id.progressBar);
        recyclerViewMeals = findViewById(R.id.recyclerViewMeals);

        RecyclerView[] recyclerViewsMeal = {recyclerViewMeals};
        xConversions.initializeRecyclerviewLayouts(recyclerViewsMeal, LinearLayoutManager.VERTICAL);

        //cardOrders.setOnClickListener(this);
        //cardCart.setOnClickListener(this);
        getItemsOrdered(orderKey);
    }

    private String getItemsOrdered(String key) {
        int  count = 0; List list = new ArrayList();
        for (int x = 0; x < Orders.listItems.size(); x++) {
            SetterMeals setterMeals = (SetterMeals) Orders.listItems.get(x);
            if (setterMeals.getOrderKey().equals(key))
                list.add(setterMeals);
        }


        adapter = new Adapter(list);
        recyclerViewMeals.setAdapter(adapter);

        return "" + count;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List listAdapter;

        public Adapter(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_items_ordered;
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
            ImageView ivImage;
            ConstraintLayout constMeal;
            View viewSpace;

            public Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                ivImage = itemView.findViewById(R.id.ivImage);
                tvCount = itemView.findViewById(R.id.tvCount);
                tvPrice = itemView.findViewById(R.id.tvPrice);
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

                if (getAdapterPosition() == listAdapter.size() -1)
                    viewSpace.setVisibility(View.VISIBLE);
                else
                    viewSpace.setVisibility(View.GONE);


                constMeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isViewDesc[0]) {
                            isViewDesc[0] = false;
                            tvDescription.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            isViewDesc[0] = true;
                            tvDescription.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }

    }

}
