package com.bt.om.entity;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: ID.java, v 0.1 2015年9月18日 下午2:25:41 hl-tanyong Exp $
 */
public abstract class ID extends Entity implements Comparable<ID> {
    private static final long serialVersionUID = 2632249062976595824L;

    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ID相同的2个对象，返回true
     */
    protected boolean isEquals(Object obj) {
        ID other = (ID) obj;
        if (id != other.id)
            return false;
        return true;
    }

    protected Object hashKey() {
        return id;
    }

    /**
     * 比较主键值大小
     */
    public int compareTo(final ID other) {
        return (id < other.id ? -1 : (id == other.id ? 0 : 1));
    }

}
