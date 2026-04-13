/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import ict.bean.*;

/**
 *
 * @author Tong
 */
public class LoginResult {

    public int status;
    public UserBean user;

    public LoginResult() {}

    public LoginResult(int status, UserBean user) {
        this.status = status;
        this.user = user;
    }
}
