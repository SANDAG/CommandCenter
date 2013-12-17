package com.sandag.commandcenter.persistence;

import org.springframework.stereotype.Service;

import com.sandag.commandcenter.model.BaseServiceEntity;

@Service
public class BaseServiceImpl  extends BaseService<BaseServiceEntity, Integer>
{

    public BaseServiceImpl()
    {
        super(BaseServiceEntity.class);
    }

}
