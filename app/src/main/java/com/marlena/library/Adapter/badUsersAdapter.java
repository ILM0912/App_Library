package com.marlena.library.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marlena.library.DB.DB;
import com.marlena.library.R;
import com.marlena.library.domain.Book;

import java.util.List;

public class badUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Book> bookList;

    public badUsersAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.bookList = bookList;
    }

    private class Myholder extends RecyclerView.ViewHolder{
        private TextView name, peopleName, peoplePhone, peoplePassport, bookState;
        public Myholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bookNameandAuthor);
            peopleName = itemView.findViewById(R.id.peopleName);
            peoplePhone = itemView.findViewById(R.id.peoplePhone);
            peoplePassport = itemView.findViewById(R.id.peoplePassport);
            bookState = itemView.findViewById(R.id.bookState);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bad_user_item, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        ((Myholder)holder).name.setText(book.getName() + ". "+ book.getAuthor().getName());
        ((Myholder)holder).peopleName.setText(book.getPeople().getName());
        ((Myholder)holder).peoplePhone.setText(book.getPeople().getPhone());
        ((Myholder)holder).peoplePassport.setText(book.getPeople().getPassport());
        ((Myholder)holder).bookState.setText(book.getState());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
