package com.jose.biblioteca1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void quitarLog() {
		Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
	}
    public static void main( String[] args )
    {
    	quitarLog();
    	
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        if(session != null) {
        	System.out.println("Sesion abierta");
        } else {
        	System.out.println("Fallo en la sesion");
        }
    }
}
