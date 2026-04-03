/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author slt8ky
 */
public abstract class Command {

    public HttpServletRequest req;
    public HttpServletResponse res;
    public PrintWriter out;

    public Command(HttpServletRequest req, HttpServletResponse res) {
        try {
            this.req = req;
            this.res = res;
            this.out = res.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void execute();
}
