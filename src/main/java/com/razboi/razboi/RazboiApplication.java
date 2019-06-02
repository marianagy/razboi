package com.razboi.razboi;

import com.razboi.razboi.persistence.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;


@ComponentScan("com")
@SpringBootApplication
public class RazboiApplication {

	public static void main(String[] args) {

		SpringApplication.run(RazboiApplication.class, args);
		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));

//		Session session = null;
//		Transaction transaction = null;
//		try {
//			session = HibernateUtils.getSessionFactory().openSession();
//			transaction = session.beginTransaction();
//
//			Player player = new Player();
//			player.setUsername("asdf");
//			player.setID(1);
//			session.save(player);
//			System.out.println(HibernateUtils.getAlldata());
//
//			transaction.commit();
//		} catch (Exception e) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			e.printStackTrace();
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//
//		HibernateUtils.shutdown();
	}



}
