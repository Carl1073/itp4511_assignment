/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.controller.auth;

import ict.bean.UserBean;
import ict.database.User;
import ict.util.Command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author slt8ky
 */
public class CheckUserName extends Command {

    public CheckUserName(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void execute() {
        try {
            String username = req.getParameter("username");
            UserBean bean = User.queryUserByName(username);
            res.setContentType("application/json");
            JSONObject json = new JSONObject();
            json.put("exists", bean != null);
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
