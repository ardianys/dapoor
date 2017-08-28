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
    private String id;
    private String name;
    private String servings;
    private String image;

    public Recipe(Parcel in){
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(steps, Step.CREATOR);
        id = in.readString();
        name = in.readString();
        servings = in.readString();
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
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(servings);
        dest.writeString(image);
    }

    public String buildIngredients(){
        String output = name + "\n";
        for(int i=0; i< ingredients.size(); i++){
            Ingredient ingredient = ingredients.get(i);
            if (ingredient != null ){
                output += ingredient.getQuantity() + " " +
                        ingredient.getMeasure() + " " +
                        ingredient.getIngredient() + "\n";
            }
        }
        return output;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
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