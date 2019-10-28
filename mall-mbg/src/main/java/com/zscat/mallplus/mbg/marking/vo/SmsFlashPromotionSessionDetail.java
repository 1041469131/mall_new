package com.zscat.mallplus.mbg.marking.vo;


import com.zscat.mallplus.mbg.marking.entity.SmsFlashPromotionSession;
import lombok.Getter;
import lombok.Setter;

/**
 * 包含商品数量的场次信息
 * https://github.com/shenzhuan/mallplus on 2018/11/19.
 */
public class SmsFlashPromotionSessionDetail extends SmsFlashPromotionSession {
    @Setter
    @Getter
    private Integer productCount;
}
