package com.wolfcode.car.flowdefinition.controller;

import com.wolfcode.car.common.annotation.Log;
import com.wolfcode.car.common.core.controller.BaseController;
import com.wolfcode.car.common.core.domain.AjaxResult;
import com.wolfcode.car.common.core.page.TableDataInfo;
import com.wolfcode.car.common.enums.BusinessType;
import com.wolfcode.car.flowdefinition.domain.BusBpmnInfo;
import com.wolfcode.car.flowdefinition.service.IBusBpmnInfoService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 流程定义明细Controller
 * 
 * @author wolfcode
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/business/flow")
public class BusBpmnInfoController extends BaseController
{
    @Autowired
    private IBusBpmnInfoService busBpmnInfoService;

    /**
     * 查询流程定义明细列表
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusBpmnInfo busBpmnInfo)
    {
        startPage();
        List<BusBpmnInfo> list = busBpmnInfoService.selectBusBpmnInfoList(busBpmnInfo);
        return getDataTable(list);
    }

    /**
     * 获取流程定义明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busBpmnInfoService.selectBusBpmnInfoById(id));
    }

    /**
     * 删除流程定义明细
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:remove')")
    @Log(title = "流程定义明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(busBpmnInfoService.deleteBusBpmnInfoById(id));
    }

    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:readResource')")
    @GetMapping("/{type}/{id}")
    public void readResource(HttpServletResponse response,@PathVariable Long id,@PathVariable String type) throws IOException{
        InputStream inputStream = busBpmnInfoService.getFileInputStream(id, type);
//        FileOutputStream fout=new FileOutputStream("D:\\test.svg");
        IOUtils.copy(inputStream,response.getOutputStream());
    }

    /**
     * 流程部署
     * @param file
     * @param bpmnLabel
     * @param bpmnType
     * @param info
     * @return
     * @throws IOException
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:deploy')")
    public AjaxResult deploy(MultipartFile file,String bpmnLabel,Integer bpmnType,String info) throws IOException{
        busBpmnInfoService.deploy(file,bpmnLabel,bpmnType,info);
        return AjaxResult.success("部署成功");
    }
}
