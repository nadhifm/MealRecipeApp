package com.example.mealrecipeapp.data.remote.response;

public class ConnectUserResponse {
    private final String username;
    private final String spoonacularPassword;
    private final String hash;

    public ConnectUserResponse(String username, String spoonacularPassword, String hash) {
        this.username = username;
        this.spoonacularPassword = spoonacularPassword;
        this.hash = hash;
    }

    public String getSpoonacularPassword() {
        return spoonacularPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getHash() {
        return hash;
    }
}
