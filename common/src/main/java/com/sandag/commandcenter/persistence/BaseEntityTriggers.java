package com.sandag.commandcenter.persistence;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import com.sandag.commandcenter.model.BaseEntity;

// PrePersist/PreUpdate annotations on BaseEntity stopped working, and 
//   it isn't clear why they ever worked with Hibernate sessions...
@Component
public class BaseEntityTriggers extends EmptyInterceptor
{

    private static final long serialVersionUID = -8449091908192497038L;

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
    {
        if (entity instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.onUpdate();
            setTimestamps(baseEntity, propertyNames, currentState);
            return true;
        }
        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
    {
        if (entity instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) entity;
            baseEntity.onCreate();
            setTimestamps(baseEntity, propertyNames, state);
            return true;
        }
        return false;
    }

    private void setTimestamps(BaseEntity entity, String[] propertyNames, Object[] state)
    {
        setState("created", entity.getCreated(), propertyNames, state);
        setState("updated", entity.getUpdated(), propertyNames, state);
    }
    
    private void setState(String fieldName, Object value, String[] propertyNames, Object[] state) {
        int position = getPosition(fieldName, propertyNames);
        if (position >=0) 
        {
            state[position] = value;
        }

    }
    
    private int getPosition(String columnName, String[] propertyNames) 
    {
        for ( int i=0; i < propertyNames.length; i++ ) {
            if (columnName.equals( propertyNames[i] ) ) {
                return i;
            }
        }
        return -1;
    }
        
}
