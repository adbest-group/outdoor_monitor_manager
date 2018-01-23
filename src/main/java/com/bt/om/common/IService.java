package com.bt.om.common;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: IService.java, v 0.1 2016年12月2日 下午2:19:53 tanyong Exp $
 */
public interface IService<T, PK> {
    /**
     * 
     * 
     * @param id
     * @return
     */
    int deleteByPrimaryKey(PK id);

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
    T selectByPrimaryKey(PK id);

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
