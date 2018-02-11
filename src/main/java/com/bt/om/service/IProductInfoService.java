package com.bt.om.service;

import java.util.List;
import java.util.Map;

import com.bt.om.entity.ProductInfo;

public interface IProductInfoService {
    public ProductInfo getByProductId(String productId);
    
    public List<ProductInfo> getByProductIds(Map<String,Object> productIdMap);
    
    public List<ProductInfo> getList(ProductInfo productInfo);
    
    public void updateCommission(ProductInfo productInfo);
}
