package com.bt.om.common;

/**
  * <p>
 * Mapper 继承该接口后，即可获得CRUD功能
 * </p>
 * <p>
 * java.lang.Integer 类型 ID 主键
 * @author tanyong
 * @version $Id: AutoMapper.java, v 0.1 2016年12月2日 下午2:09:16 tanyong Exp $
 */
public interface AutoMapper<T> extends BaseMapper<T, Integer> {

}
