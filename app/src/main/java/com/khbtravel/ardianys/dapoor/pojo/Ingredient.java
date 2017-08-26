package com.khbtravel.ardianys.dapoor.pojo;

/**
 * Created by ardianys on 8/25/17.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Ingredient implements Parcelable
{

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient(Parcel in){
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    public int describeContents() {
        return 0;
    }

}