package com.bt.om.common;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: ServiceImpl.java, v 0.1 2016年12月2日 下午3:01:39 tanyong Exp $
 */
public class ServiceImpl<M extends BaseMapper<T, PK>, T, PK extends Serializable>
                        implements IService<T, PK> {

    @Autowired
    protected   M baseMapper;

    @Override
    public int deleteByPrimaryKey(PK id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T entity) {
        return baseMapper.insert(entity);
    }

    @Override
    public int insertSelective(T entity) {
        return baseMapper.insertSelective(entity);
    }

    @Override
    public T selectByPrimaryKey(PK id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T entity) {
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return baseMapper.updateByPrimaryKey(t);
    }

}
