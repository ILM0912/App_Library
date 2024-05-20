package com.marlena.library.DB;

import com.marlena.library.domain.Author;
import com.marlena.library.domain.Book;
import com.marlena.library.domain.People;

import java.util.ArrayList;
import java.util.List;

public class DB {
    public DB(){};

    public static final List<Book> BOOK_LIST = new ArrayList<>();
    public static final List<Author> AUTHOR_LIST = new ArrayList<>();
    public static final List<People> PEOPLE_LIST = new ArrayList<>();
}
