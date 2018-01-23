package com.bt.om.entity;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 实体对象基类，提供通用方法
 * <p>
 * FIXME <code>Arrays</code>   deepCopy
 * 
 * @author tany
 * @version create on 2011-5-16 下午03:26:31
 */
public abstract class Entity implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = -3365154083759739647L;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    /**
     * 根据<code>HashMap</code>的存储结构，如果 <code>A.equals(B)</code>，则必有
     * <code>A.hashCode()==B.hashCode()</code>
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hashKey() == null) ? 0 : hashKey().hashCode());
        return result;
    }

    /**
     * 返回计算{@link #hashCode()}的关键字，强制重写，确保如果 <code>A.equals(B)</code>，则必有
     * <code>A.hashCode()==B.hashCode()</code>
     * 
     * @return 计算{@link #hashCode()}的关键字
     */
    protected abstract Object hashKey();

}
