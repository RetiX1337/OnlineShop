package com.company;

import com.company.configuration.DependencyManager;
import com.company.core.models.UserAuthenticationDTO;
import com.company.core.models.user.User;

import java.util.Scanner;

public class Main {
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO("111111@gmail.com", "123456");
        User user = DependencyManager.getInstance().getUserController().loginUser(userAuthenticationDTO);
        System.out.println(DependencyManager.getInstance().getProductController().getProductsWithQuantity(1L));
        System.out.println(user);
    }

}
