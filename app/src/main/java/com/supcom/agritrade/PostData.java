package com.supcom.agritrade;

import android.provider.ContactsContract;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PostData implements Serializable {

    @Exclude
    private String id;
    private String Type;
    private String Price;
    private String Description;
    private String unite;
    private String image;
    private String date;
    private boolean ratingState;

    private String posterID;
    static ArrayList<PostData> contacts = new ArrayList<PostData>();


    PostData(String type, String price, String q, String image, String u, String date) {
        this.Type = type;
        this.Price = price;
        this.Description = q;
        this.image = image;
        unite = u;
        this.date = date;

    }



    public String getDate() {
        return date;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String id) {
        this.posterID = id;
    }

    public boolean getRatingState() {
        return ratingState;
    }

    public void setRatingState(boolean rs) {
        this.ratingState = rs;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public String getPrice() {
        return Price;
    }

    public String getDescription() {  return Description;  }

    public String getUnite() { return unite;  }

    public String getImage() {
        return image;
    }

    public static ArrayList<PostData> getContactsList() { return contacts; }

    public static void AddToContactsList(PostData p) {
        contacts.add(p);
    }

    public static void clear() {
        contacts.clear();
    }
}

