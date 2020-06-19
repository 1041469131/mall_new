package com.zscat.mallplus.admin.po;

import java.math.BigDecimal;
import lombok.Data;

/**
 * @author xiang.li create date 2020/6/15 description
 */
@Data
public class OrderCommissionStatistics {
  BigDecimal avgPayAmount;
  BigDecimal totalPayAmount;
  BigDecimal profitAmount;
}
