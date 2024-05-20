package com.marlena.library.Rest;

import com.marlena.library.domain.People;

public interface LibraryApi {

    void fillBook();
    void fillAuthor();
    void fillPeople();
    void updateBook(int id, String newState, int people);
    void fillBooks();
}
