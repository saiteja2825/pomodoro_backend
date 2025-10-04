package com.example.pomo.DTOs;
import com.example.pomo.model.User;
public class LoginResponse {
    public User user;
    public String token;

    public LoginResponse(User user,String token){
        this.user=user;
        this.token=token;
    }

}
