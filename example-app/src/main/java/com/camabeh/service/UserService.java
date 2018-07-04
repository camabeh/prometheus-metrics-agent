package com.camabeh.service;

import com.camabeh.model.User;

import java.util.Arrays;
import java.util.List;

public class UserService {
  public List<User> getUsers() throws InterruptedException {
    int sleepTime = (int) (Math.random() * 4) * 1000;
    System.out.println("Artifical sleep for " + sleepTime / 1000 + " seconds.");
    Thread.sleep(sleepTime);
    return Arrays.asList(new User(1, "email1@gmail.com"), new User(2, "email2@gmail.com"));
  }
}
