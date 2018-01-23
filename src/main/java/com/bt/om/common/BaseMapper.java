package com.bt.om.common;

import java.io.Serializable;

/**
 * <p>
 * Mapper基础接口
 * </p>
 * <p>
 * 这个 Mapper 支持 id 泛型
 * </p> 
 * @author tanyong
 * @version $Id: BaseMapper.java, v 0.1 2016年12月2日 下午2:03:35 tanyong Exp $
 */
public interface BaseMapper<T, PK extends Serializable> {
    /**
     * 
     * 
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Serializable id);

    /**
     * 
     * 
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
    * 
    * 
    * @param record
    * @return
    */
    int insertSelective(T entity);

    /**
    * 
    * 
    * @param id
    * @return
    */
    T selectByPrimaryKey(Serializable id);

    /**
    * 
    * 
    * @param record
    * @return
    */
    int updateByPrimaryKeySelective(T entity);

    /**
    * 
    * 
    * @param record
    * @return
    */
    int updateByPrimaryKey(T t);
}
