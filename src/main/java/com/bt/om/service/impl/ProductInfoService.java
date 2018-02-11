package com.bt.om.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.ProductInfo;
import com.bt.om.mapper.ProductInfoMapper;
import com.bt.om.service.IProductInfoService;

@Service
public class ProductInfoService implements IProductInfoService {
	@Autowired
	private ProductInfoMapper productInfoMapper;

	@Override
	public ProductInfo getByProductId(String productId) {
		return productInfoMapper.selectByProductId(productId);
	}

	@Override
	public List<ProductInfo> getByProductIds(Map<String, Object> productIdMap) {
		return productInfoMapper.selectByProductIds(productIdMap);
	}

	@Override
	public List<ProductInfo> getList(ProductInfo productInfo) {
		return productInfoMapper.selectList(productInfo);
	}
	
	@Override
	public void updateCommission(ProductInfo productInfo){
		productInfoMapper.updateByProductId(productInfo);
	}
}
