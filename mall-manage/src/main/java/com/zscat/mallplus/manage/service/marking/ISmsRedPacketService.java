package com.zscat.mallplus.manage.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.marking.entity.SmsRedPacket;

/**
 * <p>
 * 红包 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface ISmsRedPacketService extends IService<SmsRedPacket> {
    int acceptRedPacket(Integer id);

    int createRedPacket(SmsRedPacket redPacket);
}
