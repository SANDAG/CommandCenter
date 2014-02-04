package com.sandag.commandcenter.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sandag.commandcenter.model.Cluster;

@Repository
public class ClusterDao extends BaseDao<Cluster, Integer>
{

    public ClusterDao()
    {
        super(Cluster.class, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public List<Cluster> readAll()
    {
        return startQuery().addOrder(Order.desc("updated")).list();
    }

    public boolean isActive(String name)
    {
        Criteria criteria = startQuery().add(Restrictions.eq("name", name.toLowerCase())).add(Restrictions.eqOrIsNull("active", true));
        return null != criteria.setMaxResults(1).uniqueResult();
    }

}
