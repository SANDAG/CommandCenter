package com.sandag.commandcenter.persistence;

import java.util.List;

import org.hibernate.criterion.Order;
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

}
