package com.agreader.model;

public class Item {

    private int Image;
    private String Text;

    public Item(String text, int image) {
        Image = image;
        Text = text;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
