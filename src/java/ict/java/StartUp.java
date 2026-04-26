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

    private static UserDB userDB;
    private static ClinicDB clinicDB;
    private static ServiceDB serviceDB;
    private static QueueDB queueDB;
    private static AppointmentDB appointmentDB;
    private static TimeslotDB timeslotDB;
    private static IncidentLogDB incidentLogDB;
    private static NotificationDB notificationDB;

    // Change the method to accept parameters
    public static void startUp(String url, String username, String password) {

        clinicDB = new ClinicDB(url, username, password);
        clinicDB.createTable();
        if (clinicDB.queryClinic().isEmpty()) {
            clinicDB.addRecord(new ClinicBean(1, "Chai Wan", "12, ABC Street, Chai Wan", Time.valueOf("09:00:00"),
                    Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(2, "Tseung Kwan O", "34, DEF Street, Tseung Kwan O",
                    Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(3, "Sha Tin", "56, GHI Street, Sha Tin", Time.valueOf("09:00:00"),
                    Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(4, "Tuen Mun", "78, JKL Street, Tuen Mun", Time.valueOf("09:00:00"),
                    Time.valueOf("18:00:00"), true));
            clinicDB.addRecord(new ClinicBean(5, "Tsing Yi", "90, BNM Street, Tsing Yi", Time.valueOf("09:00:00"),
                    Time.valueOf("18:00:00"), true));
        }

        userDB = new UserDB(url, username, password);
        userDB.createTable();
        insertSampleData();

        serviceDB = new ServiceDB(url, username, password);
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

        queueDB = new QueueDB(url, username, password);
        queueDB.createTable();

        appointmentDB = new AppointmentDB(url, username, password);
        appointmentDB.createTable();

        timeslotDB = new TimeslotDB(url, username, password);
        timeslotDB.createTable();
        Date today = Date.valueOf(LocalDate.now());
        generateTimeslot(today);

        // Calculate the date for tomorrow
        LocalDate tomorrowLocalDate = LocalDate.now().plusDays(1);
        Date tomorrow = Date.valueOf(tomorrowLocalDate);
        generateTimeslot(tomorrow);

        incidentLogDB = new IncidentLogDB(url, username, password);
        incidentLogDB.createTable();

        notificationDB = new NotificationDB(url, username, password);
        notificationDB.createTable();

        SystemSettingsDB systemSettingsDB = new SystemSettingsDB(url, username, password);
        systemSettingsDB.createTable();
    }

    public static void generateTimeslot(Date date) {
        if (timeslotDB.queryTimeslotbyDate(date).isEmpty()) {

            ArrayList<ClinicBean> clinicBeans = clinicDB.queryClinic();
            for (ClinicBean clinicBean : clinicBeans) {

                LocalTime startTime = clinicBean.getOpenTime().toLocalTime();
                LocalTime endTime = clinicBean.getCloseTime().toLocalTime();
                ArrayList<ServiceBean> serviceBeans = serviceDB.queryService();

                for (ServiceBean serviceBean : serviceBeans) {
                    int duration = serviceBean.getDuration();

                    for (LocalTime t = startTime; t.isBefore(endTime); t = t.plusMinutes(duration)) {
                        // Pass 'tomorrow' instead of 'today'
                        TimeslotBean newSlot = new TimeslotBean(0, clinicBean.getClinicId(), serviceBean.getServiceId(),
                                date, Time.valueOf(t), 2);
                        timeslotDB.addRecord(newSlot);

                        System.out.println("Generating slot for tomorrow (" + date + ") at: " + t);
                    }
                }
            }
        }
    }

    public static void insertSampleData() {
        // 1. Patient Sample
        UserBean patient = new UserBean();
        patient.setUsername("patient");
        patient.setPassword("patient");
        patient.setFullName("John Doe");
        patient.setEmail("john.doe@email.com");
        patient.setPhone("0123456789");
        patient.setGender("M");
        patient.setRole("Patient");
        patient.setClinicId(1);

        // 2. Staff Sample
        UserBean staff = new UserBean();
        staff.setUsername("staff");
        staff.setPassword("staff");
        staff.setFullName("Sarah Jenkins");
        staff.setEmail("s.jenkins@clinic.com");
        staff.setPhone("0111222333");
        staff.setGender("F");
        staff.setRole("Staff");
        staff.setClinicId(1);

        // 3. Admin Sample
        UserBean admin = new UserBean();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setFullName("System Administrator");
        admin.setEmail("admin@hospital.org");
        admin.setPhone("0999888777");
        admin.setGender("O");
        admin.setRole("Admin");
        admin.setClinicId(0); // Admin might not be tied to a specific clinic

        // Execute additions
        userDB.addRecord(patient);
        userDB.addRecord(staff);
        userDB.addRecord(admin);

        System.out.println("Sample records processed successfully.");
    }
}
