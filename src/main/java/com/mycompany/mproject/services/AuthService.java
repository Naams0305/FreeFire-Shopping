/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mproject.services;

import com.mycompany.mproject.model.User;

/**
 *
 * @author Administrator
 */
public class AuthService {
    public User checkLogin(String username, String password) {
        //call db de check where ....
        if (username.equals("admin") && password.equals("admin")) {
            return new User(1, "Naams", "0473242114", "Quy Nhon", "abc@gmail.com");
        }
        
        return null;
    }
}
