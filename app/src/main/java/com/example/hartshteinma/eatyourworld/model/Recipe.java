package com.example.hartshteinma.eatyourworld.model;

/**
 * Created by hartshteinma on 31/07/2017.
 */

public class Recipe {
    private String name, country, details, imgSrc, userId, recipeId;

    public Recipe() {
    }

    public Recipe(String name, String country, String details, String imgSrc, String userId, String recipeId) {
        this.name = name;
        this.country = country;
        this.details = details;
        this.imgSrc = imgSrc;
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", details='" + details + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
