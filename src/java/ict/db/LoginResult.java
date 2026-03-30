/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import ict.bean.PatientBean;

/**
 *
 * @author Tong
 */
public class LoginResult {

    public int status;
    public PatientBean patient;

    public LoginResult() {}

    public LoginResult(int status, PatientBean patient) {
        this.status = status;
        this.patient = patient;
    }
}
