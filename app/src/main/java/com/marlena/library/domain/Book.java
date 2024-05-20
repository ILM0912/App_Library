package com.marlena.library.domain;

public class Book {
    private int id;
    private String name;
    private String state;
    private Author author;
    private People people;

    public Book(int id, String name, String state, Author author, People people) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.author = author;
        this.people = people;
    }

    public Book(String name, String state, Author author, People people) {
        this.name = name;
        this.state = state;
        this.author = author;
        this.people = people;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public Author getAuthor() {
        return author;
    }

    public People getPeople() {
        return people;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", author=" + author +
                ", people=" + people +
                '}';
    }
}
