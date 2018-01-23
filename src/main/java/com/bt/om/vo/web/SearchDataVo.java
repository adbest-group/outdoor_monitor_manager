package com.bt.om.vo.web;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.ID;
import com.bt.om.util.StringUtil;
import com.bt.om.web.Pagination;
import com.google.common.collect.Maps;

/**
 * 
 * @author tany 2015年10月5日 下午10:38:47
 */
public class SearchDataVo extends ID {
    private static final long       serialVersionUID = -5871545293792038014L;

    private List<?>                 list;
    private long                    count;
    private int                     size;
    private int                     start;
    private String                  orderField;
    private String                  orderBy;
    private HashMap<String, Object> searchMap;
    private HashMap<String, String> queryMap;
    private RowBounds               bounds;

    public SearchDataVo(String orderField, String orderBy, int start, int size) {
        this.searchMap = Maps.newHashMap();
        this.queryMap = Maps.newHashMap();

        if (orderField != null) {
            queryMap.put("orderField", orderField);
        }

        if (orderBy != null) {
            queryMap.put("orderBy", orderBy);
        }

        this.orderBy = orderBy;
        this.orderField = orderField;

        this.size = size;
        this.start = start;
        if (this.start < 0) {
            this.start = 0;
        }

        queryMap.put("size", String.valueOf(this.size));
        queryMap.put("start", String.valueOf(this.start));

        if (StringUtil.isNotEmpty(orderField)) {
            searchMap.put("orderField", orderField);
        }
        if (StringUtil.isNotEmpty(orderBy)) {
            searchMap.put("orderBy", orderBy);
        }
    }

    /**
     * 加载搜索参数
     * 
     * @param key
     *            搜索Key
     * @param value
     *            URL传入的值
     * @param searchParam
     *            用于搜索的参数
     */
    public void putSearchParam(String key, String value, Object searchParam) {
        searchMap.put(key, searchParam);
        if (StringUtils.isNotBlank(value)) {
            queryMap.put(key, value);
        }
    }

    /**
     * 加载页面参数
     * 
     * @param key
     * @param value
     */
    public void putQueryParam(String key, String value) {
        queryMap.put(key, value);
    }

    /**
     * 得到翻页
     * 
     * @return
     */
    public Pagination getPage() {
        return new Pagination(this.getCount(), this.getStart(), this.getSize());
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        if (start >= count) {
            int total_page = (int) (Math.floor((count - 1) / size) + 1);
            start = (total_page - 1) * size;
            if (start <= 0) {
                start = 0;
            }
        }
        this.count = count;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
        searchMap.put("orderField", orderField);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        searchMap.put("orderBy", orderBy);
    }

    public HashMap<String, Object> getSearchMap() {
        return searchMap;
    }

    public HashMap<String, String> getQueryMap() {
        return queryMap;
    }

    public RowBounds getBounds() {
        if (null == bounds) {
            bounds = new RowBounds(getStart(), getSize());
        }
        return bounds;
    }

    public void setBounds(RowBounds bounds) {
        this.bounds = bounds;
    }

}
