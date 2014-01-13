package com.sandag.commandcenter.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sandag.commandcenter.model.Sequence;

@Repository
public class SequenceDao
{
	
	@Autowired
    protected SessionFactory sessionFactory;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int next()
    {
    	Session session = sessionFactory.getCurrentSession();
    	
        Sequence sequence = (Sequence) session.createCriteria(Sequence.class).uniqueResult();
        if (sequence == null)
        {
            sequence = new Sequence();
            session.save(sequence);
            sequence.setValue(1);
        } else
        {
            sequence.setValue(sequence.getValue() + 1);
        }
        session.update(sequence);
        return sequence.getValue();
    }

}
