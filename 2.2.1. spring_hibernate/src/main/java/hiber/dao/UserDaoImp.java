package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }


    public List<User> getUserByCar(String model, int series) {
        return sessionFactory.getCurrentSession().createQuery(
                "from User u join fetch u.car where u.car.model = :model and u.car.series = :series", User.class)
                .setParameter("model", model)
                .setParameter("series", series)
                .getResultList();
    }

    public void addCar(User user, String model, int series) {
        user.setCar(new Car(model, series));
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User user left join fetch user.car");
        return query.getResultList();
    }

}
