package com.supcom.agritrade;

import android.provider.ContactsContract;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PostData {

    private String Type;
    private String Price;
    private String Description;
    private static String image;
    static ArrayList<PostData> contacts = new ArrayList<PostData>();


    PostData(String type, String price, String description, String image) {
        this.Type = type;
        this.Price = price;
        this.Description = description;
        this.image = image;

    }

    public String getType() {
        return Type;
    }

    public String getPrice() {
        return Price;
    }

    public String getDescription() {
        return Description;
    }

    public static String getImage() {
        return image;
    }

    public static ArrayList<PostData> getContactsList() {

        return contacts;
    }

    public static void AddToContactsList(PostData p) {
        contacts.add(p);
    }

    public static void clear() {
        contacts.clear();
    }
}

