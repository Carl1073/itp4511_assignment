/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author slt8ky
 */
public class ClassLoaderHelper {
    private static void loadClasses(File dir, String packageName, String action, HttpServletRequest req,
            HttpServletResponse res) {
        for (String path : dir.list()) {
            File file = new File(dir + "/" + path);
            if (file.isDirectory()) {
                loadClasses(file, packageName, action, req, res);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().replace(".class", "");
                if (action.equalsIgnoreCase(className)) {
                    try {
                        Class<?> clazz = Class
                                .forName(packageName + "." + dir.getName() + "." + className);
                        Constructor<?> ctor = clazz.getConstructor(HttpServletRequest.class, HttpServletResponse.class);
                        Object instance = ctor.newInstance(req, res);
                        ((Command) instance).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void load(String packageName, String action, HttpServletRequest req, HttpServletResponse res) {
        String path = packageName.replace(".", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(path);
        if (url != null) {
            File file = new File(url.getFile());
            if (file.isDirectory())
                loadClasses(file, packageName, action, req, res);
        }
    }
}
