package com.marlena.library.mapper;

import com.marlena.library.domain.Author;
import com.marlena.library.domain.People;

import org.json.JSONException;
import org.json.JSONObject;

public class peopleMapper {

    public static People peopleFromJson(JSONObject jsonObject) {
        People people = null;
        try {
            people = new People(jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("phone"),
                    jsonObject.getString("passport"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }

    public static People peopleFromBookJson(JSONObject jsonObject) {
        People people = null;
        try {
            people = peopleFromJson(jsonObject.getJSONObject("people"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }
}
