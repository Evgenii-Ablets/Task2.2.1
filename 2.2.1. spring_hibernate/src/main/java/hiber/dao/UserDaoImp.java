package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }
    @Transactional
    public User getUserByCar(String model, int series) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().createQuery("from User u join fetch u.car where u.car.model = :model and u.car.series = :series", User.class)
                .setParameter("model", model)
                .setParameter("series", series)
                .getSingleResult()).stream().findFirst().orElse(null);
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
