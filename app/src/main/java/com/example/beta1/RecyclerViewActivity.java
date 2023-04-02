package com.example.beta1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.beta1.classes.User;
import com.example.beta1.classes.UserAdapter;
import com.example.beta1.profilePages.ShowProfileActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class RecyclerViewActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Users");
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
    }


    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("join", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
//                Toast.makeText(RecyclerViewActivity.this, "The id of the Document is "+id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RecyclerViewActivity.this, ShowProfileActivity.class);
                i.putExtra("userid", id);
                startActivity(i);
            }
        });

    }

    private void searchMonthRecyclerView(String s){

        Query query1 = notebookRef.whereEqualTo("month", s).orderBy("member", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<User> options1 = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query1, User.class)
                .build();

        adapter.updateOptions(options1);

//        adapter = new UserAdapter(options);
//
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.getRecycledViewPool().clear();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
//                Toast.makeText(RecyclerViewActivity.this, "The id of the Document is "+id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RecyclerViewActivity.this, ShowProfileActivity.class);
                i.putExtra("userid", id);
                startActivity(i);
            }
        });

    }

    private void searchNameRecyclerView(String s){

        Query query2 = notebookRef.whereEqualTo("name".toLowerCase(), s).orderBy("join", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<User> options2 = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query2, User.class)
                .build();

        adapter.updateOptions(options2);

//        adapter = new UserAdapter(options);
//
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.getRecycledViewPool().clear();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
//                Toast.makeText(RecyclerViewActivity.this, "The id of the Document is "+id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RecyclerViewActivity.this, ShowProfileActivity.class);
                i.putExtra("userid", id);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem item1 = menu.findItem(R.id.search1);
        SearchView searchView1=(SearchView)item1.getActionView();

        MenuItem item2 = menu.findItem(R.id.search2);
        SearchView searchView2=(SearchView)item2.getActionView();

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s1) {
                searchMonthRecyclerView(s1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s1) {
                searchMonthRecyclerView(s1);
                return false;
            }
        });

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s2) {
                searchNameRecyclerView(s2);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s2) {
                searchNameRecyclerView(s2);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void onbtnclick(View view) {
        startActivity(new Intent(RecyclerViewActivity.this, NewUserActivity.class));
    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpRecyclerView();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}