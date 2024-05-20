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

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.bookList = bookList;
    }

    private class Myholder extends RecyclerView.ViewHolder{
        private TextView name, author, state;
        public Myholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bookName);
            author = itemView.findViewById(R.id.bookAuthor);
            state = itemView.findViewById(R.id.bookState);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.book_item, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Book book = DB.BOOK_LIST.get(position);

        ((Myholder)holder).name.setText(book.getName());
        ((Myholder)holder).author.setText(book.getAuthor().getName());
        if (book.getState().equals("not"))
            ((Myholder)holder).state.setText("Книга находится в библиотеке");
        else
            ((Myholder)holder).state.setText("Срок возврата: "+book.getState());
    }

    @Override
    public int getItemCount() {
        return DB.BOOK_LIST.size();
    }
}
