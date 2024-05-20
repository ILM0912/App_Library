package com.marlena.library;

import static com.marlena.library.Rest.LibrariApiImpl.BASE_URL;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.marlena.library.Adapter.BookAdapter;
import com.marlena.library.Adapter.badUsersAdapter;
import com.marlena.library.DB.DB;
import com.marlena.library.Rest.LibrariApiImpl;
import com.marlena.library.domain.Book;
import com.marlena.library.mapper.BookMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BadUsers extends AppCompatActivity {
    private RecyclerView recyclerView;
    private badUsersAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bad_users);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.rv_bad);
        List<Book> bookList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(BadUsers.this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                BASE_URL + "/book",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        DB.BOOK_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Book book = BookMapper.bookFromJson(jsonObject);
                                DB.BOOK_LIST.add(book);
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            for (Book book1 : DB.BOOK_LIST) {
                                if (book1.getPeople().getId() != 0) {
                                    try {
                                        Date date = sdf.parse(book1.getState());
                                        Date today = new Date();
                                        if (today.after(date)) bookList.add(book1);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            bookAdapter = new badUsersAdapter(BadUsers.this, bookList);
                            recyclerView.setAdapter(bookAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("API TEST", volleyError.toString());
                    }
                }
        );
        requestQueue.add(arrayRequest);
    }
}