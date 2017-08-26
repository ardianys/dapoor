package com.khbtravel.ardianys.dapoor.pojo;

/**
 * Created by ardianys on 8/25/17.
 */

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Recipe implements Parcelable
{

    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    private ArrayList<Step> steps = new ArrayList<Step>();
    private Integer id;
    private String name;
    private Integer servings;
    private String image;

    public Recipe(Parcel in){
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(steps, Step.CREATOR);
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
    }

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }
        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public int describeContents() {
        return 0;
    }

}