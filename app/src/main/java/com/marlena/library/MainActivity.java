package com.marlena.library;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.marlena.library.Rest.LibrariApiImpl;
import com.marlena.library.Rest.LibraryApi;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        Button giveBook = findViewById(R.id.giveBook);
        Button receiveBook = findViewById(R.id.receiveBook);
        Button badUsers = findViewById(R.id.badUsers);
        Button allBooks = findViewById(R.id.allBooks);
        Button newUser = findViewById(R.id.newUser);

        /*new LibrariApiImpl(this).addPeople("Витя", "+79043540555", "1111 222222");
        new LibrariApiImpl(this).fillBook();
        new LibrariApiImpl(this).fillAuthor();*/

        //new LibrariApiImpl(this).updateBook(10001, "18-05-2005", 10001);


        giveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GiveBook.class);
                startActivity(intent);
            }
        });

        receiveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReceiveBook.class);
                startActivity(intent);
            }
        });

        badUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LibrariApiImpl(MainActivity.this).fillBook();
                Intent intent = new Intent(MainActivity.this, BadUsers.class);
                startActivity(intent);
            }
        });

        allBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllBooks.class);
                startActivity(intent);
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewUser.class);
                startActivity(intent);
            }
        });
    }
}