import org.hibernate.SessionFactory;
import java.io.File;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try{
            return new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml.tld")).buildSessionFactory();
        }catch(Throwable ex){
            System.err.println("Initial session factory failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void shutdown(){
        getSessionFactory().close();
    }
}
