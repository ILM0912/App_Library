package com.marlena.library.mapper;

import com.marlena.library.domain.Author;
import com.marlena.library.domain.People;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthorMapper {
    public static Author authorFromJson(JSONObject jsonObject) {
        Author author = null;
        try {
            author = new Author(jsonObject.getInt("id"),
                    jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }

    public static Author authorFromBookJson(JSONObject jsonObject) {
        Author author = null;
        try {
            author = authorFromJson(jsonObject.getJSONObject("author"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }
}
