package com.camabeh;

import com.camabeh.service.UserService;

public class App {
  public static void main(String[] args) throws InterruptedException {
    UserService service = new UserService();
    for (int i = 0; i < 30; ++i) {
      System.out.println(service.getUsers());
    }
  }
}
