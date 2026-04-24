/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.java;

import ict.bean.*;
import ict.db.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
        if (clinicDB.queryClinic().isEmpty()) {
            clinicDB.addRecord(new ClinicBean(1, "Chai Wan", "12, ABC Street, Chai Wan", Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(2, "Tseung Kwan O", "34, DEF Street, Tseung Kwan O", Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(3, "Sha Tin", "56, GHI Street, Sha Tin", Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(4, "Tuen Mun", "78, JKL Street, Tuen Mun", Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(5, "Tsing Yi", "90, BNM Street, Tsing Yi", Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), true));
        }

        ServiceDB serviceDB = new ServiceDB(url, username, password);
        serviceDB.createTable();
        if (serviceDB.queryService().isEmpty()) {
            serviceDB.addRecord(new ServiceBean(1, "General consultation",
                    "Standard check-up and medical advice.",
                    150, 60));
            serviceDB.addRecord(new ServiceBean(2, "Vaccination",
                    "Immunization shots for disease prevention.",
                    100, 60));
            serviceDB.addRecord(new ServiceBean(3, "Basic Health Screening",
                    "Testing vital signs and key health markers.",
                    200, 60));
        }

        QueueDB queueDB = new QueueDB(url, username, password);
        queueDB.createTable();

        AppointmentDB appointmentDB = new AppointmentDB(url, username, password);
        appointmentDB.createTable();

        TimeslotDB timeslotDB = new TimeslotDB(url, username, password);
        timeslotDB.createTable();
        Date today = Date.valueOf(LocalDate.now());
        if (timeslotDB.queryTimeslotbyDate(today).isEmpty()) {
            // for every clinic
            ArrayList<ClinicBean> ClinicBeans = clinicDB.queryClinic();
            for (ClinicBean clinicBean : ClinicBeans) {
                LocalTime startTime = clinicBean.getOpenTime().toLocalTime();
                LocalTime endTime = clinicBean.getCloseTime().toLocalTime();
                ArrayList<ServiceBean> serviceBeans = serviceDB.queryService();
                for (ServiceBean serviceBean : serviceBeans) {
                    int duration = serviceBean.getDuration();
                    for (LocalTime t = startTime; t.isBefore(endTime); t = t.plusMinutes(duration)) {
                        timeslotDB.addRecord(new TimeslotBean(0, clinicBean.getClinicId(), serviceBean.getServiceId(), today, Time.valueOf(t), 2));
                        System.out.println("Generating slot for: " + t);
                    }
                }
            }
        }

        // Calculate the date for tomorrow
        LocalDate tomorrowLocalDate = LocalDate.now().plusDays(1);
        Date tomorrow = Date.valueOf(tomorrowLocalDate);

        // Check if tomorrow's slots already exist
        if (timeslotDB.queryTimeslotbyDate(tomorrow).isEmpty()) {

            ArrayList<ClinicBean> clinicBeans = clinicDB.queryClinic();
            for (ClinicBean clinicBean : clinicBeans) {

                LocalTime startTime = clinicBean.getOpenTime().toLocalTime();
                LocalTime endTime = clinicBean.getCloseTime().toLocalTime();
                ArrayList<ServiceBean> serviceBeans = serviceDB.queryService();

                for (ServiceBean serviceBean : serviceBeans) {
                    int duration = serviceBean.getDuration();

                    for (LocalTime t = startTime; t.isBefore(endTime); t = t.plusMinutes(duration)) {
                        // Pass 'tomorrow' instead of 'today'
                        TimeslotBean newSlot = new TimeslotBean(0, clinicBean.getClinicId(), serviceBean.getServiceId(), tomorrow, Time.valueOf(t), 2);
                        timeslotDB.addRecord(newSlot);

                        System.out.println("Generating slot for tomorrow (" + tomorrow + ") at: " + t);
                    }
                }
            }
        }

        IncidentLogDB incidentLogDB = new IncidentLogDB(url, username, password);
        incidentLogDB.createTable();

        NotificationDB notificationDB = new NotificationDB(url, username, password);
        notificationDB.createTable();
    }
}
