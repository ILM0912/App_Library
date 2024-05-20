package com.marlena.library;

import static com.marlena.library.Rest.LibrariApiImpl.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marlena.library.Adapter.BookAdapter;
import com.marlena.library.DB.DB;
import com.marlena.library.Rest.LibrariApiImpl;
import com.marlena.library.domain.People;
import com.marlena.library.mapper.peopleMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        Button add = findViewById(R.id.add);
        EditText name = findViewById(R.id.name);
        EditText phone = findViewById(R.id.phone);
        EditText passport = findViewById(R.id.passport);
        Passport p = new Passport(passport);
        passport.addTextChangedListener(p);


        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String s = phone.getText().toString();
                    if (s.isEmpty()) {
                        phone.setText("+7");
                        phone.setSelection(2);
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameP = name.getText().toString();
                String phoneP = phone.getText().toString();
                String passportP = passport.getText().toString();
                if (nameP.length()==0) {
                    Toast.makeText(NewUser.this, "имя пользователя не может быть пустым", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isPhone(phoneP)) {
                    Toast.makeText(NewUser.this, "Номер телефона должен начинаться с +7 и содержать 12 символов", Toast.LENGTH_LONG).show();;
                    return;
                }
                if (!isPassport(passportP)) {
                    Toast.makeText(NewUser.this, "Паспортные данные должны быть в формате 'серия номер'", Toast.LENGTH_LONG).show();;
                    return;
                }
                RequestQueue requestQueue = Volley.newRequestQueue(NewUser.this);
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
                                    boolean isDubbing = false;
                                    for (People people : DB.PEOPLE_LIST) {
                                        Log.d("gh", people.getPassport());
                                        if (people.getPassport().equals(passportP) || people.getPhone().equals(phoneP)) {
                                            Toast.makeText(NewUser.this, "Пользователь с такими данными уже существует", Toast.LENGTH_LONG).show();
                                            isDubbing = true;
                                            break;
                                        }
                                    }
                                    if (!isDubbing) {
                                        RequestQueue requestQueue = Volley.newRequestQueue(NewUser.this);
                                        StringRequest request = new StringRequest(
                                                Request.Method.POST,
                                                BASE_URL + "/people",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        RequestQueue requestQueue = Volley.newRequestQueue(NewUser.this);
                                                        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
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
                                                                            for (People people : DB.PEOPLE_LIST) {
                                                                                if (people.getPassport().equals(passportP)) {
                                                                                    Toast.makeText(NewUser.this, "Пользователь создан, номер билета: " + people.getId(), Toast.LENGTH_LONG).show();
                                                                                    break;
                                                                                }
                                                                            }
                                                                            Intent intent = new Intent(NewUser.this, MainActivity.class);
                                                                            startActivity(intent);
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
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {
                                                        Log.d("API TEST", volleyError.toString());
                                                    }
                                                }
                                        ) {
                                            @Nullable
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {

                                                Map<String, String> params = new HashMap<>();

                                                params.put("name", nameP);
                                                params.put("passport", passportP);
                                                params.put("phone", phoneP);

                                                return params;
                                            }
                                        };
                                        requestQueue.add(request);
                                    }

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
        });
    }
    public boolean isPhone(String phone){
        return phone.length() == 12 && phone.charAt(0) == '+' && phone.charAt(1) == '7';
    }

    public boolean isPassport(String passport){
        String[] split = passport.split(" ");
        return split.length == 2 && split[0].length() == 4 && split[1].length() == 6;
    }
}

class Passport implements TextWatcher {
    private EditText editText;
    public Passport(EditText editText){
        this.editText = editText;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = editText.getText().toString();

        if (text.length() == 4) {
            String str = text + " ";
            editText.setText(str);
            editText.setSelection(editText.getText().toString().length());
        }
    }
}

