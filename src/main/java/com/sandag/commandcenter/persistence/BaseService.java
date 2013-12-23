package com.sandag.commandcenter.persistence;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class BaseService<T, PK extends Serializable>
{
    @Autowired
    protected SessionFactory sessionFactory;

    private Class<T> type;

    public BaseService(Class<T> type)
    {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public PK create(T o)
    {
        return (PK) getSession().save(o);
    }

    @SuppressWarnings("unchecked")
    public T read(PK id)
    {
        return (T) getSession().get(type, id);
    }

    public void update(T o)
    {
        getSession().update(o);
    }

    public void delete(T o)
    {
        getSession().delete(o);
    }

    public void delete(PK id)
    {
        Session session = getSession();
        @SuppressWarnings("unchecked")
        T o = (T) session.load(type, id);
        session.delete(o);
    }

    protected Session getSession()
    {
        return sessionFactory.getCurrentSession();
    }

    protected Criteria startQuery()
    {
        return this.getSession().createCriteria(type);
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

}
