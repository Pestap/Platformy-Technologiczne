import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello");
        Session session = HibernateUtil.getSessionFactory().openSession();

        /*
        Dane testowe
         */
        Tower testTower = new Tower("TOWER", 10);
        Mage test1 = new Mage("T1", 1, testTower);
        Mage test2 = new Mage("T2", 10, testTower);
        Mage test3 = new Mage("T3", 4, testTower);
        Mage test4 = new Mage("T4", 5, testTower);


        session.beginTransaction();
        session.save(test1);
        session.save(test2);
        session.save(test3);
        session.save(test4);
        session.save(testTower);
        session.getTransaction().commit();
        Scanner scan = new Scanner(System.in);

        while(true){
            System.out.println("Podaj polecenie:");
            String cmd = scan.nextLine();

            if(cmd.equals("help")){
                System.out.println("quit - wyjście \n add - dodaj nową encje \n get - wykonaj zapytanie");
            }else if(cmd.equals("quit")){
                break;
            }else if(cmd.equals("add")){
                System.out.println("Tower czy Mage?");
                String tOrM = scan.nextLine();
                if(tOrM.equals("Tower")){
                    System.out.println("Podaj nazwę:");
                    String name = scan.nextLine();
                    System.out.println("Podaj wysokość:");
                    int height = Integer.valueOf(scan.nextLine());
                    Tower tower = new Tower(name, height);

                }else if(tOrM.equals("Mage")){
                    System.out.println("Podaj imię: ");
                    String name = scan.nextLine();
                    System.out.println("Podaj poziom: ");
                    int lvl = Integer.valueOf(scan.nextLine());
                    Mage newMage = new Mage(name,lvl, null);

                    session.beginTransaction();
                    session.save(newMage);
                    session.getTransaction().commit();
                }else{
                    System.out.println("Nierozpoznane polecenie. Spróbuj ponownie.");
                }



            }else if(cmd.equals("get")){
                System.out.println("Podaj imię:");
                String name = scan.nextLine();
                Mage result = (Mage)session.get(Mage.class, name);

                if(result != null){
                    System.out.println(result);
                }else{
                    System.out.println("W bazie nie ma takiego maga");
                }

            }else if(cmd.equals("SQL")) {
                System.out.println("Podaj zapytanie w języku SQL: ");
                String sql = scan.nextLine();
                Query query = session.createQuery(sql);
                // getResultList tylko dla SELECT (nie dla DELETE I UPDATE)
                List result = query.getResultList();
                System.out.println(result);
            }else if(cmd.equals("List all")){
                Query queryMage = session.createQuery("FROM Mage");
                Query queryTower = session.createQuery("FROM Tower");
                List resultMage = queryMage.getResultList();
                List resultTower = queryTower.getResultList();
                System.out.println("Mages:");
                for(Object m : resultMage){
                    System.out.println(m);
                }
                System.out.println("=============");
                System.out.println("Towers:");
                for(Object t : resultTower){
                    System.out.println(t);
                }
            }else{
                System.out.println("Nierozpoznane polecenie. Spróbuj ponownie.");
            }



        }


        HibernateUtil.shutdown();
    }
}
