package com.bt.om.common;

/**
 * <p>
 * 主键 Long 类型 IService 实现类（ 泛型：M 是 mapper 对象， T 是实体 ）
 * </p>
 * @author tanyong
 * @version $Id: SuperServiceImpl.java, v 0.1 2016年12月2日 下午3:07:05 tanyong Exp $
 */
public class SuperServiceImpl<M extends AutoMapper<T>, T> extends ServiceImpl<M, T, Integer> {

}
