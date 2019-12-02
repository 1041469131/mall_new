package com.zscat.mallplus.mbg.cms.vo;


import com.zscat.mallplus.mbg.cms.entity.CmsHelp;
import com.zscat.mallplus.mbg.cms.entity.CmsHelpCategory;

import java.util.List;

/**
 * 帮助分类
 */
public class CmsHelpCategoryVO extends CmsHelpCategory{

    private List<CmsHelp> cmsHelps;

    public List<CmsHelp> getCmsHelps() {
        return cmsHelps;
    }

    public void setCmsHelps(List<CmsHelp> cmsHelps) {
        this.cmsHelps = cmsHelps;
    }
}
