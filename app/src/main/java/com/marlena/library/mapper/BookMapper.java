package com.marlena.library.mapper;

import com.marlena.library.domain.Book;

import org.json.JSONException;
import org.json.JSONObject;

public class BookMapper {
    public static Book bookFromJson(JSONObject jsonObject) {
        Book book = null;
        try {
            book = new Book(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("state"),
                    AuthorMapper.authorFromBookJson(jsonObject),
                    peopleMapper.peopleFromBookJson(jsonObject)
                    );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
}
