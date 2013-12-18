package com.sandag.commandcenter.persistence;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.User;

@Service
public class UserService extends BaseService<User, Integer>
{

    public UserService()
    {
        super(User.class);
    }

    public User create(String email)
    {
        // selecting then inserting b/c checking violated unique constraint isn't straightforward 
        //   (just a ConstraintViolationException with a generic message
        //    and parsing the wrapped Cause message is fragile)
        User user = (User) getSession()
            .createCriteria(User.class)
            .add(Restrictions.eq("email", email))
            .uniqueResult();
        if (user == null)
        {
            user = new User();
            user.setEmail(email);
            create(user);
        } 
        return user;
    }

}
