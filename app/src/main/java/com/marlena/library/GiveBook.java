package com.marlena.library;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class GiveBook extends AppCompatActivity {

    private int idBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_give_book);

        Button search = findViewById(R.id.search);
        EditText book = findViewById(R.id.book);
        EditText user = findViewById(R.id.user);
        TextView info = findViewById(R.id.info);
        LinearLayout layout = findViewById(R.id.layout);
        CalendarView calendarView = findViewById(R.id.calendarView);

        layout.setVisibility(View.INVISIBLE);
        Button give = findViewById(R.id.give);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(GiveBook.this);
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

                                String edit = book.getText().toString();
                                if (edit.length() == 5 && checkIfAllDigits(edit)) {
                                    idBook = Integer.parseInt(edit);
                                    if (isGiven(idBook)){
                                        Toast.makeText(GiveBook.this, "Книги нет в наличии", Toast.LENGTH_LONG).show();
                                        layout.setVisibility(View.INVISIBLE);
                                        info.setText("Информация о книге");
                                    }
                                    else {

                                        layout.setVisibility(View.VISIBLE);
                                        for (Book book : DB.BOOK_LIST) {
                                            if (book.getId() == idBook) {
                                                info.setText(book.getName() + "\n" + book.getAuthor().getName());
                                                break;
                                            }
                                        }
                                    }
                                }
                                else {
                                    info.setText("Информация о книге");
                                    layout.setVisibility(View.INVISIBLE);
                                    Toast.makeText(GiveBook.this, "Код книги должен содержать 5 цифр", Toast.LENGTH_LONG).show();
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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year);
                give.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edit = user.getText().toString();
                        if (edit.length() == 5 && checkIfAllDigits(edit)) {
                            for (Book book: DB.BOOK_LIST) {
                                if (book.getId() == idBook) {
                                    new LibrariApiImpl(GiveBook.this).updateBook(idBook, date, Integer.parseInt(edit));
                                    Toast.makeText(GiveBook.this, "Книга с названием: " + book.getName() +"\nВыдана читателю:" + edit, Toast.LENGTH_LONG).show();
                                }
                            }
                            Intent intent = new Intent(GiveBook.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(GiveBook.this, "Код читателя должен содержать 5 цифр", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    public static boolean isGiven(int idBook) {

        for (Book book : DB.BOOK_LIST) {
            Log.d("check", book.getState() + book.getPeople().getId());
            if (book.getId() == idBook) {
                Log.d("check", String.valueOf((book.getState()!="not")));
                return(!Objects.equals(book.getState(), "not") || book.getPeople().getId() != 0);
            }
        }
        return true;
    }
}