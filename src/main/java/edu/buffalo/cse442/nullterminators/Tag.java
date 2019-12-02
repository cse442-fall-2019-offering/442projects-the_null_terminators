package edu.buffalo.cse442.nullterminators;


import javafx.scene.paint.Color;

public class Tag {
    private int _id;
    private String _name;
    private Color _color;

    Tag(int id, String name, String color) {
        _id = id;
        _name = name;
        _color = Color.web(color);
    }

    public int getId() {
        return _id;
    }

    Color getColor() {
        return _color;
    }

    String getName() {
        return _name;
    }
}
