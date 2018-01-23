package com.bt.om.vo.common;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: Constant.java, v 0.1 2016年12月8日 上午11:03:14 tanyong Exp $
 */
public class Constant<K, V> implements Comparable<Constant<Integer, V>>{

    private K K;

    private V v;

    public Constant(K k, V v) {
        this.K = k;
        this.v = v;
    }

    public K getK() {
        return K;
    }

    public void setK(K k) {
        K = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
    
    @Override
    public int compareTo(Constant<Integer, V> o) {
        try {
            Integer key = (Integer) this.getK();
            return key.compareTo(o.getK());
        } catch (Exception e) {
        }
        return 0;
    }

}
