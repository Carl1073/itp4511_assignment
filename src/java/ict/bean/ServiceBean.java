/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.bean;

import ict.ocp.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author 240708635
 */
public class ServiceBean implements Serializable{
    private String serviceId;
    private String _custId;
    private Date date;
    private Time time;
    private String location;
    private ServiceType serviceType;
    private ServiceMode serviceMode;
    private String status;
}
