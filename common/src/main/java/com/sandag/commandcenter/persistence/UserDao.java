package com.sandag.commandcenter.persistence;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sandag.commandcenter.model.User;

@Repository
public class UserDao extends BaseDao<User, Integer>
{

    public UserDao()
    {
        super(User.class, Integer.class);
    }

    public User fetchOrCreate(String principal)
    {
        // selecting then inserting b/c checking violated unique constraint isn't straightforward 
        //   (just a ConstraintViolationException with a generic message
        //    and parsing the wrapped Cause message is fragile)
        User user = (User) startQuery()          
            .add(Restrictions.eq("principal", principal))
            .uniqueResult();
        if (user == null)
        {
            user = new User();
            user.setPrincipal(principal);
            create(user);
        }
        return user;
    }

}
