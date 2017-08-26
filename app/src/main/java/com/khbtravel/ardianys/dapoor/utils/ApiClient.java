package com.khbtravel.ardianys.dapoor.utils;

/**
 * Created by ardianys on 8/25/17.
 */

import com.khbtravel.ardianys.dapoor.pojo.Recipe;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}