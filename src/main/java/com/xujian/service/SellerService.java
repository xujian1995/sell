package com.xujian.service;

import com.xujian.dataobject.SellerInfo;

/**卖家端*/
public interface SellerService {
/**通过openid查询卖家信息*/
SellerInfo findSellerInfoByOpenid(String openid);
}
