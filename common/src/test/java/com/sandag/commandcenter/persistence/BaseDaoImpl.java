package com.sandag.commandcenter.persistence;

import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.BaseServiceEntity;

@Service
public class BaseDaoImpl  extends BaseDao<BaseServiceEntity, Integer>
{

    public BaseDaoImpl()
    {
        super(BaseServiceEntity.class);
    }

}
