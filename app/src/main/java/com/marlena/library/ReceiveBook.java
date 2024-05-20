package com.marlena.library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.marlena.library.DB.DB;
import com.marlena.library.Rest.LibrariApiImpl;
import com.marlena.library.domain.Book;
import com.marlena.library.mapper.BookMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ReceiveBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_receive_book);

        Button search = findViewById(R.id.search);
        EditText edit = findViewById(R.id.edit);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(ReceiveBook.this);
                JsonArrayRequest arrayRequest= new JsonArrayRequest(Request.Method.GET,
                        LibrariApiImpl.BASE_URL + "/book",
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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String text = edit.getText().toString();
                                if (text.length() == 5 && checkIfAllDigits(text)) {
                                    for (Book book : DB.BOOK_LIST) {
                                        if (book.getId() == Integer.parseInt(text)) {
                                            if (book.getState().equals("not")) {
                                                Toast.makeText(ReceiveBook.this, "Книга находится в библиотеке, проверьте код", Toast.LENGTH_LONG).show();
                                                break;
                                            }
                                            else {
                                                new LibrariApiImpl(ReceiveBook.this).updateBook(Integer.parseInt(text), "not", 0);
                                                Toast.makeText(ReceiveBook.this, text + " была возвращена", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ReceiveBook.this, MainActivity.class);
                                                startActivity(intent);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(ReceiveBook.this, "Код книги должен содержать 5 цифр", Toast.LENGTH_LONG).show();
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
        });
    }

    public static boolean checkIfAllDigits(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}