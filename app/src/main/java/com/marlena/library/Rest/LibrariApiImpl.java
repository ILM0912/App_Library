package com.marlena.library.Rest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marlena.library.AllBooks;
import com.marlena.library.BadUsers;
import com.marlena.library.DB.DB;
import com.marlena.library.MainActivity;
import com.marlena.library.domain.Author;
import com.marlena.library.domain.Book;
import com.marlena.library.domain.People;
import com.marlena.library.mapper.AuthorMapper;
import com.marlena.library.mapper.BookMapper;
import com.marlena.library.mapper.peopleMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LibrariApiImpl implements LibraryApi{
    private final Context context;
    //public static final String BASE_URL = "http://192.168.172.108:2807";
    public static final String BASE_URL = "http://192.168.20.108:2807";

    private Response.ErrorListener errorListener;
    public LibrariApiImpl(Context context) {
        this.context = context;
        errorListener = volleyError -> Log.d("API TEST", volleyError.toString());
    }

    @Override
    public void fillBook() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest arrayRequest= new JsonArrayRequest(Request.Method.GET,
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener
        );
        requestQueue.add(arrayRequest);
    }

    @Override
    public void fillAuthor() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest arrayRequest= new JsonArrayRequest(Request.Method.GET,
                BASE_URL + "/author",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        DB.AUTHOR_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Author author = AuthorMapper.authorFromJson(jsonObject);
                                DB.AUTHOR_LIST.add(author);
                            }
                            Log.d("API TEST", DB.AUTHOR_LIST.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener
        );
        requestQueue.add(arrayRequest);
    }

    @Override
    public void fillPeople() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest arrayRequest= new JsonArrayRequest(Request.Method.GET,
                BASE_URL + "/people",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        DB.PEOPLE_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                People people = peopleMapper.peopleFromJson(jsonObject);
                                DB.PEOPLE_LIST.add(people);
                            }
                            Log.d("API TEST", DB.PEOPLE_LIST.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener
        );
        requestQueue.add(arrayRequest);
    }

    @Override
    public void updateBook(int id, String newState, int people) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                BASE_URL + "/book/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        fillBook();
                    }
                },
                errorListener
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("date", newState);
                params.put("person", String.valueOf(people));

                return params;
            }
        };
        requestQueue.add(request);
    }
    @Override
    public void fillBooks() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                            ((AllBooks) context).updateAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                errorListener
        );
        requestQueue.add(arrayRequest);
    }
}
