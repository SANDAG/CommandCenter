package com.sandag.commandcenter.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sandag.commandcenter.model.Sequence;

@Repository
public class SequenceDao extends BaseDao<Sequence, Integer>
{

    public SequenceDao()
    {
        super(Sequence.class);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int next()
    {
        Sequence sequence = (Sequence) startQuery().uniqueResult();
        if (sequence == null)
        {
            sequence = new Sequence();
            create(sequence);
            sequence.setValue(1);
        } else
        {
            sequence.setValue(sequence.getValue() + 1);
        }
        update(sequence);
        return sequence.getValue();
    }

}
