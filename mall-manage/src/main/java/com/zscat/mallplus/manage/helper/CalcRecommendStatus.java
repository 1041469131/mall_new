package com.zscat.mallplus.manage.helper;

import com.zscat.mallplus.manage.utils.DateUtils;

import java.util.Date;

/**
 *  计算推荐状态的帮助类
 */
public class CalcRecommendStatus {

    public static String getRecommendStatus(Date recomendTime, String dressFreqCode){
        Date nextRecomendTime = null;
        if(recomendTime != null){
            if("everymonth".equals(dressFreqCode)){
                nextRecomendTime = DateUtils.adjustMonth(recomendTime,1 );
            }else if("everytwomonth".equals(dressFreqCode)){
                nextRecomendTime = DateUtils.adjustMonth(recomendTime,2 );
            }else if("everyquarter".equals(dressFreqCode)){
                nextRecomendTime = DateUtils.adjustMonth(recomendTime,3 );
            }
            if(nextRecomendTime != null){
                if(DateUtils.calculateDaysNew(nextRecomendTime,new Date()) > 3){
                    return "2";
                }else if(DateUtils.calculateDaysNew(nextRecomendTime,new Date()) < 3 && DateUtils.calculateDaysNew(nextRecomendTime,new Date()) > 0 ){
                    return "0";
                }else{
                    return "1";
                }
            }else{
                return "0";
            }

        }else{
            return "0";
        }
    }
}
