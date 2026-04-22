/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.java;

import ict.db.*;

/**
 *
 * @author 240708635
 */
public class StartUp {

    // Change the method to accept parameters
    public static void startUp(String url, String username, String password) {
        UserDB userDB = new UserDB(url, username, password);
        userDB.createTable();
        ClinicDB clinicDB = new ClinicDB(url, username, password);
        clinicDB.createTable();
        ServiceDB serviceDB = new ServiceDB(url, username, password);
        serviceDB.createTable();
        
        QueueDB queueDB = new QueueDB(url, username, password);
        queueDB.createTable();
        AppointmentDB appointmentDB = new AppointmentDB(url, username, password);
        appointmentDB.createTable();
        ClinicServiceDB clinicServiceDB = new ClinicServiceDB(url, username, password);
        clinicServiceDB.createTable();
        IncidentLogDB incidentLogDB = new IncidentLogDB(url, username, password);
        incidentLogDB.createTable();
        NotificationDB notificationDB = new NotificationDB(url, username, password);
        notificationDB.createTable();
    }
}
