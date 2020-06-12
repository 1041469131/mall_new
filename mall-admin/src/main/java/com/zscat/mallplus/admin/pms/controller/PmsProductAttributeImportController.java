package com.zscat.mallplus.admin.pms.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hqjl.common.excel.bean.CellBean;
import com.hqjl.common.excel.service.ImportTableService;
import com.zscat.mallplus.admin.po.ProductAttributeCategoryModel;
import com.zscat.mallplus.admin.po.ProductAttributeModel;
import com.zscat.mallplus.manage.service.pms.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.manage.service.pms.IPmsProductAttributeService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttribute;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 商品属性参数表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "PmsProductAttributeController", description = "商品属性参数表管理")
@RequestMapping("/pms/PmsProductAttributeImport")
public class PmsProductAttributeImportController {

  private static List<String> selectTypeNames = Arrays.asList(new String[]{"唯一", "单选", "多选"});
  private static List<String> inputTypeNames = Arrays.asList(new String[]{"手工录入", "列表"});
  private static List<String> filterTypeNames = Arrays.asList(new String[]{"普通", "颜色"});
  private static List<String> searchTypeNames = Arrays.asList(new String[]{"不需要进行检索", "关键字检索", "范围检索"});
  private static List<String> relatedStatusNames = Arrays.asList(new String[]{"是", "否"});
  private static List<String> handAddStatusNames = Arrays.asList(new String[]{"否", "是"});

  @Autowired
  private IPmsProductAttributeCategoryService pmsProductAttributeCategoryService;
  @Autowired
  private IPmsProductAttributeService pmsProductAttributeService;
  @SysLog(MODULE = "pms", REMARK = "导入商品分类")
  @ApiOperation("导入商品分类")
  @RequestMapping(value = "/import", method = RequestMethod.POST)
  @ResponseBody
  @IgnoreAuth
  public CommonResult importForExcel(@RequestParam(value = "file") MultipartFile file) throws Exception {
    Workbook workbook = WorkbookFactory.create(file.getInputStream());
    ImportTableService tableService = new ImportTableService(workbook.getSheet("类目-属性及属性值对应总表"));
    List<Map> dataMaps = tableService.read(getImportColumnNameTitle(tableService), Map.class);
    List<String> productAttributeCategoryNameList = new ArrayList<>();
    int columnCount = tableService.getTableBean().getColumnCount();
    int rowCount = tableService.getTableBean().getRowCount();
    List<String> attNames=new ArrayList<>();
    for(int i=0;i<rowCount;i++){
      String content = tableService.getTableBean().getCellBean(i, 0).getContent();
      if(!StringUtils.isEmpty(content)) {
        attNames.add(content.trim());
      }
    }
    if(attNames.stream().distinct().count()<attNames.size()){
      throw new Exception("上传的文件模板头部第1列有重复元素，请删除！");
    }

    for (int j = 7; j < columnCount; j++) {
      CellBean cellBean = tableService.getTableBean().getCellBean(tableService.getStartRow(), j);
      if (cellBean == null || cellBean.getContent() == null) {
        throw new Exception("上传的文件模板头部第" + (j + 1) + "列标题为空，请删除！");
      }
      String columnName = cellBean.getContent();
      productAttributeCategoryNameList.add(columnName.trim());
    }
    List<ProductAttributeModel> productAttributeModels=new ArrayList<>();
    for (int i = 0; i < dataMaps.size(); i++) {
      Map map = dataMaps.get(i);
      if(map==null||map.size()==0){
        continue;
      }
      ProductAttributeModel productAttributeModel = new ProductAttributeModel();
      String name = map.get("羊毛 腈纶 蚕丝 麻 马海毛 棉 羊绒 涤纶 锦纶 兔毛 莱卡 莫代尔 格子布 牛津纺 绸缎 法兰绒(磨毛) 牛仔布 蚕丝 羊毛布 亚麻布 色织布").toString().trim();
      productAttributeModel.setName(name);

      String filterType = (String) map.get("分类筛选样式").toString().trim();
      productAttributeModel.setFilterType(filterTypeNames.indexOf(filterType));

      String searchType = (String) map.get("检索").toString().trim();
      productAttributeModel.setSearchType(searchTypeNames.indexOf(searchType));

      String relatedStatus = (String) map.get("属性关联").toString().trim();
      productAttributeModel.setRelatedStatus(relatedStatusNames.indexOf(relatedStatus));

      String selectType = (String) map.get("属性可选").toString().trim();
      productAttributeModel.setSelectType(selectTypeNames.indexOf(selectType));

      String inputType = (String) map.get("录入方式").toString().trim();
      productAttributeModel.setInputType(inputTypeNames.indexOf(inputType));

      String handAddStatus = (String) map.get("是否支持手动新增").toString().trim();
      productAttributeModel.setHandAddStatus(handAddStatusNames.indexOf(handAddStatus));

      List<ProductAttributeCategoryModel> productAttributeCategoryModels = productAttributeCategoryNameList.stream().map(categoryName -> {
        ProductAttributeCategoryModel productAttributeCategoryModel = new ProductAttributeCategoryModel();
        productAttributeCategoryModel.setName(categoryName);
        String value =map.get(categoryName).toString().trim();
        if(value.contains("√")){
          value=null;
        }else{
         value = String.join(",", value.split("\\s+"));
        }
        productAttributeCategoryModel.setValue(value);
        return productAttributeCategoryModel;
      }).filter(p->p.getValue()==null||!p.getValue().contains("❌")).collect(Collectors.toList());
      productAttributeModel.setList(productAttributeCategoryModels);
      productAttributeModels.add(productAttributeModel);
    }

    //TODO 设置数据
    Map<String, Long> productCategoryCountMap = productAttributeModels.stream().map(ProductAttributeModel::getList).reduce(new ArrayList<>(), (x, y) -> {
      x.addAll(y);
      return x;
    }).stream().collect(Collectors.groupingBy(ProductAttributeCategoryModel::getName, Collectors.counting()));

    List<PmsProductAttributeCategory> attributeCategories = productAttributeCategoryNameList.stream().map(name -> {
      PmsProductAttributeCategory productAttributeCategory = pmsProductAttributeCategoryService
        .getOne(new QueryWrapper<PmsProductAttributeCategory>().lambda().eq(PmsProductAttributeCategory::getName, name));
      if(productAttributeCategory==null) {
        productAttributeCategory = new PmsProductAttributeCategory();
      }
      productAttributeCategory.setName(name);
      Long aLong = productCategoryCountMap.get(name);
      productAttributeCategory.setAttributeCount(aLong == null ? 0 : aLong.intValue());
      productAttributeCategory.setParamCount(0);
      return productAttributeCategory;
    }).collect(Collectors.toList());
    pmsProductAttributeCategoryService.saveOrUpdateBatch(attributeCategories);
    Map<String, PmsProductAttributeCategory> attributeCategoryNameMap = attributeCategories.stream()
      .collect(Collectors.toMap(PmsProductAttributeCategory::getName, Function.identity()));
    List<PmsProductAttribute> pmsProductAttributes=new ArrayList<>();
    productAttributeModels.forEach(p->
      p.getList().forEach(attr->{
        PmsProductAttributeCategory pmsProductAttributeCategory = attributeCategoryNameMap.get(attr.getName());
        PmsProductAttribute pmsProductAttribute = pmsProductAttributeService.getOne(new QueryWrapper<PmsProductAttribute>().lambda()
          .eq(PmsProductAttribute::getProductAttributeCategoryId, pmsProductAttributeCategory.getId())
          .eq(PmsProductAttribute::getName, p.getName()));
        if(pmsProductAttribute==null) {
           pmsProductAttribute = new PmsProductAttribute();
        }
        pmsProductAttribute.setType(p.getRelatedStatus());
        pmsProductAttribute.setRelatedStatus(p.getRelatedStatus());
        pmsProductAttribute.setFilterType(p.getFilterType());
        pmsProductAttribute.setHandAddStatus(p.getHandAddStatus());
        pmsProductAttribute.setInputType(p.getInputType());
        pmsProductAttribute.setName(p.getName());
        pmsProductAttribute.setSearchType(p.getSearchType());
        pmsProductAttribute.setSelectType(p.getSelectType());
        pmsProductAttribute.setInputList(attr.getValue());
        pmsProductAttribute.setProductAttributeCategoryId(pmsProductAttributeCategory.getId());
        pmsProductAttribute.setSort(0);
        pmsProductAttributes.add(pmsProductAttribute);
      })
    );
    pmsProductAttributeService.saveOrUpdateBatch(pmsProductAttributes);
    return new CommonResult().success();
  }

  public String[] getImportColumnNameTitle(ImportTableService tableService) throws Exception {
    int columnCount = tableService.getTableBean().getColumnCount();
    String[] columnTitle = new String[columnCount];
    for (int i = 0; i < columnCount; i++) {
      CellBean cellBean = tableService.getTableBean().getCellBean(tableService.getStartRow(), i);
      if (cellBean == null || cellBean.getContent() == null) {
        throw new Exception("上传的文件模板头部第" + (i + 1) + "列标题为空，请删除！");
      }
      String columnName = cellBean.getContent();
      columnTitle[i] = columnName;
    }
    return columnTitle;
  }


}
