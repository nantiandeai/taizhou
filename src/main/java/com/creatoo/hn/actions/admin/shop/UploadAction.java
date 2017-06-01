package com.creatoo.hn.actions.admin.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.creatoo.hn.model.WhCfgList;
import com.creatoo.hn.model.WhZxUpload;
import com.creatoo.hn.services.admin.shop.MuseumService;
import com.creatoo.hn.services.admin.shop.UploadService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.UploadUtil;

@RestController
@RequestMapping("/admin/shop")
public class UploadAction {
	
	//日志
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	public CommService commService;
	@Autowired
	private UploadService uploadService;
	
	@RequestMapping("/uploads")
	public ModelAndView index() {
		return new ModelAndView("/admin/shop/upload");
	}
	/**
	 * 查询
	 * 
	 */
	@RequestMapping("/seletup")
	public Object selecuplo(HttpServletRequest req, HttpServletResponse resp){
		//获取请求参数
		Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
		
		//分页查询
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
			rtnMap = this.uploadService.inquire(paramMap);
		} catch (Exception e) {
	        rtnMap.put("total", 0);
	        rtnMap.put("rows", new ArrayList<Map<String, Object>>(0));
		}
        return rtnMap;
	}
    /**
     * 添加
     */
	@RequestMapping("/adduploda")
	public Object add(WhZxUpload whup,HttpServletRequest req, HttpServletResponse resp, @RequestParam("uplink_up")MultipartFile uplink_up){
		String success = "0";
		String errmasg = "";
		try {
			//当前日期
			Date now = new Date();
			//保存图片
			String uploadPath = UploadUtil.getUploadPath(req);
			//列表图
			if(uplink_up != null && !uplink_up.isEmpty()){
				String imgPath_uplink = UploadUtil.getUploadFilePath(uplink_up.getOriginalFilename(), commService.getKey("art.picture"), "shop", "picture", now);
				uplink_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_uplink) );
				whup.setUplink(UploadUtil.getUploadFileUrl(uploadPath, imgPath_uplink));
			}
			whup.setUpid(this.commService.getKey("WhZxUpload"));
			whup.setUptime(now);
			this.uploadService.save(whup);
		} catch (Exception e) {
			success = "1";
			errmasg = e.getMessage();
		}
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", success);
		res.put("msg", errmasg);
		return res;
	}
	/**
	 * 修改
	 */
	@RequestMapping("/douploda")
	public Object doloda(WhZxUpload whup,HttpServletRequest req, HttpServletResponse resp, @RequestParam("uplink_up")MultipartFile uplink_up){
		String success = "0";
		String errmasg = "";
		try {
			//当前日期
			Date now = new Date();
			//保存图片
			String uploadPath = UploadUtil.getUploadPath(req);
			//列表图
			if(uplink_up != null && !uplink_up.isEmpty()){
				UploadUtil.delUploadFile(uploadPath, whup.getUplink());
				
				String imgPath_uplink = UploadUtil.getUploadFilePath(uplink_up.getOriginalFilename(), commService.getKey("art.picture"), "shop", "picture", now);
				uplink_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_uplink) );
				whup.setUplink(UploadUtil.getUploadFileUrl(uploadPath, imgPath_uplink));
			}
			whup.setUptime(now);
			this.uploadService.updata(whup);
		} catch (Exception e) {
			success = "1";
			errmasg = e.getMessage();
		}
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", success);
		res.put("msg", errmasg);
		return res;
	}
	/**
	 * 删除
	 */
	@RequestMapping("/deluploda")
	public Object delup(String upid,HttpServletRequest req,String uplink){
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		String success = "0";
		String errmsg = "";
        //删除图片
		try {
			String uploadPath = UploadUtil.getUploadPath(req);
			if(uplink!= null && !uplink.isEmpty()){
				UploadUtil.delUploadFile(uploadPath, uplink);
			}
			this.uploadService.delete(upid);
		} catch (Exception e) {
			errmsg = e.getMessage();
		}
		// 返回
		rtnMap.put("success", success);
		rtnMap.put("errmsg", errmsg);
		return rtnMap;
	}
	
	/**
	 * 改变状态
	 */
	@RequestMapping("/upstate")
	public String upCheck(WhZxUpload whup){
		try {
			this.uploadService.checkup(whup);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "error";
		}
	}
}
