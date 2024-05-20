package com.marlena.library;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.marlena.library.Adapter.BookAdapter;
import com.marlena.library.DB.DB;
import com.marlena.library.Rest.LibrariApiImpl;

import java.util.Objects;

public class AllBooks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_books);
        new LibrariApiImpl(this).fillBooks();
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.rv_books);
        bookAdapter = new BookAdapter(this, DB.BOOK_LIST);
        recyclerView.setAdapter(bookAdapter);
    }

    public void updateAdapter() {
        bookAdapter.notifyDataSetChanged();
    }
}