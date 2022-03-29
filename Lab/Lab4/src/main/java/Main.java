import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello");
        Session session = HibernateUtil.getSessionFactory().openSession();

        /*
        Dane testowe
         */
        Tower testTower = new Tower("Tower1", 10);
        Mage test1 = new Mage("T1", 10, testTower);
        testTower.addMage(test1);
        Mage test2 = new Mage("T2", 3, testTower);
        testTower.addMage(test2);
        Tower tower2 = new Tower("Tower2", 15);
        Mage test3 = new Mage("T3", 2, tower2);
        tower2.addMage(test3);
        Mage test4 = new Mage("T4", 6, tower2);
        tower2.addMage(test4);


        session.beginTransaction();
        session.save(test1);
        session.save(test2);
        session.save(test3);
        session.save(test4);
        session.save(testTower);
        session.save(tower2);
        session.getTransaction().commit();

        Scanner scan = new Scanner(System.in);

        while(true){
            System.out.println("Podaj polecenie (help - lista dostępnych poleceń):");
            String cmd = scan.nextLine();

            if(cmd.toLowerCase().equals("help")){
                System.out.println("quit - wyjście \n add - dodaj nową encje \n get - wykonaj zapytanie");
            }else if(cmd.toLowerCase().equals("quit")){
                break;
            }else if(cmd.toLowerCase().equals("add")){
                System.out.println("Tower czy Mage?");
                String tOrM = scan.nextLine();
                if(tOrM.toLowerCase().equals("tower")){
                    System.out.println("Podaj nazwę:");
                    String name = scan.nextLine();
                    System.out.println("Podaj wysokość:");
                    int height = Integer.valueOf(scan.nextLine());
                    Tower tower = new Tower(name, height);

                    System.out.println("Czy chcesz dodać magów:");
                    String answer = scan.nextLine();

                    if(answer.toLowerCase().equals("tak")){
                        while(true){
                            session.beginTransaction();
                            Query queryMage = session.createQuery("FROM Mage WHERE tower = null");
                            List resultMage = queryMage.getResultList();

                            if(resultMage.isEmpty()){
                                System.out.println("Nie ma więcej magów do dodania.");
                                session.getTransaction().commit();
                                break;
                            }
                            for(int i =0; i < resultMage.size(); i++){
                                System.out.println(i +": " + resultMage.get(i));
                            }
                            System.out.println("Podaj numer maga, którego chcesz dodać (ujemny kończy dodawanie)");
                            int mageNumber = Integer.valueOf(scan.nextLine());
                            if(mageNumber >= 0 && mageNumber < resultMage.size()) {
                                Mage chosen = (Mage)resultMage.get(mageNumber);


                                Mage toUpdate = session.load(Mage.class, chosen.getName());
                                System.out.println("Dodawnie maga " + toUpdate);
                                toUpdate.setTower(tower);
                                tower.addMage(toUpdate);

                                session.update(toUpdate);
                                session.saveOrUpdate(tower);
                                session.getTransaction().commit();

                            }else{
                                session.saveOrUpdate(tower);
                                session.getTransaction().commit();
                                break;
                            }

                        }

                    }else{
                        session.beginTransaction();
                        session.save(tower);
                        session.getTransaction().commit();
                    }




                }else if(tOrM.toLowerCase().equals("mage")){
                    System.out.println("Podaj imię: ");
                    String name = scan.nextLine();
                    System.out.println("Podaj poziom: ");
                    int lvl = Integer.valueOf(scan.nextLine());
                    System.out.println("Czy chcesz przypisac maga do wieży?");
                    Tower towerToAdd = null;
                    String answer = scan.nextLine();


                    if(answer.toLowerCase().equals("tak")){

                        Query queryTower = session.createQuery("FROM Tower");
                        List resultTower = queryTower.getResultList();

                        for(int i =0; i < resultTower.size(); i++){
                            System.out.println(i + ": " + resultTower.get(i));
                        }
                        int mageTower = Integer.valueOf(scan.nextLine());
                        towerToAdd = (Tower)resultTower.get(mageTower);


                    }


                    Mage newMage = new Mage(name,lvl, towerToAdd);
                    if(towerToAdd != null){
                        towerToAdd.addMage(newMage);
                    }
                    session.beginTransaction();
                    session.save(newMage);
                    session.getTransaction().commit();
                }else{
                    System.out.println("Nierozpoznane polecenie. Spróbuj ponownie.");
                }



            }else if(cmd.toLowerCase().equals("get")) {
                System.out.println("Podaj imię:");
                String name = scan.nextLine();
                Mage result = (Mage) session.get(Mage.class, name);

                if (result != null) {
                    System.out.println(result);
                } else {
                    System.out.println("W bazie nie ma takiego maga");
                }
            }else if(cmd.toLowerCase().equals("delete")){
                System.out.println("Tower czy Mage");
                String answer = scan.nextLine();
                if(answer.toLowerCase().equals("tower")){
                    System.out.println("Podaj nazwę wieży:");
                    String name = scan.nextLine();

                    session.beginTransaction();
                    Tower  toDelete = session.find(Tower.class, name);

                    Query magesToNullQuery = session.createQuery("FROM Mage WHERE tower = " +"'"+ toDelete.getName() + "'");
                    List magesToNull = magesToNullQuery.getResultList();
                    for (Object m : magesToNull){
                        toDelete.removeMage((Mage)m);
                        ((Mage) m).setTower(null);
                        session.update(m);
                    }


                    session.delete(toDelete);
                    session.getTransaction().commit();

                }else if(answer.toLowerCase().equals("mage")){
                    System.out.println("Podaj nazwę maga:");
                    String name = scan.nextLine();

                    session.beginTransaction();
                    Mage toDelete = session.find(Mage.class, name);

                    if(toDelete.getTower() != null) {
                        Tower toUpdate = session.find(Tower.class, toDelete.getTower().getName());
                        toUpdate.removeMage(toDelete);
                    }

                    session.delete(toDelete);
                    session.getTransaction().commit();
                }else{
                    System.out.println("Niezrozumiała odpowiedź. Spróbuj ponownie.");
                }
            }else if(cmd.toLowerCase().equals("sql")) {
                //get min
                int minLevel = testTower.getMages().get(0).getLevel();
                for(Mage m : testTower.getMages()){
                    if(m.getLevel() < minLevel){
                        minLevel = m.getLevel();
                    }
                }
                System.out.println(minLevel);
                String sql = "FROM Mage WHERE tower = 'Tower2' AND level > " + minLevel;
                Query query = session.createQuery(sql);
                // getResultList tylko dla SELECT (nie dla DELETE I UPDATE)
                List result = query.getResultList();
                System.out.println(result);
            }else if(cmd.toLowerCase().equals("list all")){
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
