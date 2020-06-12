package com.zscat.mallplus.manage.service.ums;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberVo;
import com.zscat.mallplus.mbg.ums.vo.VUmsMemberVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IUmsMemberService extends IService<UmsMember> {

    Object loginByWeixin(HttpServletRequest req);

    /**
     * 根据用户名获取会员
     */
    UmsMember getByUsername(String username);

    /**
     * 根据会员编号获取会员
     */
    UmsMember getById(Long id);

    /**
     * 用户注册
     */
    @Transactional
    CommonResult register(String username, String password, String telephone, String authCode);

    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 修改密码
     */
    @Transactional
    CommonResult updatePassword(String telephone, String password, String authCode);

    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();

    /**
     * 根据会员id修改会员积分
     */
    void updateIntegration(Long id, Integer integration);


    public UmsMember queryByOpenId(String openId);


    Map<String, Object> login(String username, String password);

    String refreshToken(String token);

    Object register(UmsMember umsMember);

    /**
     * 随机获取一条用户信息
     * @return
     */
    UmsMember getRandomUmsMember();

    Object loginByAuthCode(String telephone, String authCode);

    Object modifyPhoneByAuthCode(UmsMember umsMember, String phone, String authCode);

    Map<String,Object> getRecommedInfos(Long recommendedId);

    /**
     * 用户注册
     * @param umsMember
     * @param matchUserId
     * @return
     */
    String register4MiniProgram(UmsMemberVo umsMember)throws Exception;

    Page<UmsMemberVo> pageUmsMembers(Page<UmsMemberVo> umsMemberPage, Map<String, Object> paramMap);

    Page<VUmsMemberVo> listVUmsMembers(VUmsMemberVo vUmsMemberVo);

    String getWebAccess(String CODE);
}
