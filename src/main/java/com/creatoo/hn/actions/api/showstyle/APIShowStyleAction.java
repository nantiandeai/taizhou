package com.creatoo.hn.actions.api.showstyle;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.ext.bean.UploadFileBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgSpecilResource;
import com.creatoo.hn.model.WhgYwiLbt;
import com.creatoo.hn.services.admin.showstyle.WhgSpecialSourceService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiLbtService;
import com.creatoo.hn.services.api.showstyle.APIShowStyleService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.CommUploadService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 秀我风采接口
 * Created by luzhihuai on 2017/6/26.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/showStyle")
public class APIShowStyleAction {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 * 公用服务类
	 */
	@Autowired
	public CommService commservice;

	@Autowired
	private APIShowStyleService apiShowStyleService;

	@Autowired
	private WhgYunweiLbtService whgYunweiLbtService;

	@Autowired
	private WhgSpecialSourceService whgSpecialSourceService;

	/**
	 * 图片文件上传服务
	 */
	@Autowired
	private CommUploadService commUploadService;

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询秀我风采列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/showStyleList", method = RequestMethod.POST)
	public RetMobileEntity showStyleList(HttpServletRequest request, Integer index, Integer size,String type) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
//		String type = request.getParameter("type");//资源类型
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiShowStyleService.t_srchList4p(type, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取秀我风采列表失败");
				return retMobileEntity;
			}
			retMobileEntity.setCode(0);
			retMobileEntity.setData(pageInfo.getList());
			RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
			pager.setIndex(pageInfo.getPageNum());
			pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
			pager.setSize(pageInfo.getSize());
			pager.setCount(pageInfo.getPages());
			retMobileEntity.setPager(pager);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return retMobileEntity;
	}

	/**
	 * 查询秀我风采轮播图
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/showStylePic", method = RequestMethod.POST)
	public RetMobileEntity  showStylePic(){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			WhgYwiLbt whgYwiLbt = new WhgYwiLbt();
			whgYwiLbt.setType("13");
			whgYwiLbt.setState(1);
			List<WhgYwiLbt> stylelbt = this.whgYunweiLbtService.t_srchList(whgYwiLbt);
			param.put("stylelbt", stylelbt);
			res.setCode(0);
			res.setData(param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查询秀我风采详情
	 * @param id 文化遗产id
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/showStyleDetail", method = RequestMethod.POST)
	public RetMobileEntity  showStyleDetail(String id,Integer intype){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			if(id ==null || "".equals(id)){
				res.setCode(101);
				res.setMsg("Id不允许为空");//Id不允许为空
			}else{
				WhgSpecilResource resource = this.apiShowStyleService.t_srchOne(id);
				List<WhgCultHeritage> styletj = this.apiShowStyleService.selectStyleRecommend(null,id,intype);
				param.put("resource", resource);
				param.put("styletj", styletj);
				res.setCode(0);
				res.setData(param);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}


	/**
	 * 添加
	 *
	 * @return res
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@CrossOrigin
	public ResponseBean add(WhgSpecilResource resource, HttpServletRequest request) {
		ResponseBean res = new ResponseBean();
		try {
			String newId = commservice.getKey("whg_specil_resource");
			resource.setId(newId);
			this.whgSpecialSourceService.t_add(request, resource);
		} catch (Exception e) {
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg("保存失败");
			log.error(res.getErrormsg(), e);
		}
		return res;
	}

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询秀我风采列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/searchPersonStyle", method = RequestMethod.POST)
	public RetMobileEntity searchPersonStyle(HttpServletRequest request, Integer index, Integer size) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
		String userId = request.getParameter("userId");//
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiShowStyleService.searchPersonStyle(userId, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取我的上传列表失败");
				return retMobileEntity;
			}
			retMobileEntity.setCode(0);
			retMobileEntity.setData(pageInfo.getList());
			RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
			pager.setIndex(pageInfo.getPageNum());
			pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
			pager.setSize(pageInfo.getSize());
			pager.setCount(pageInfo.getPages());
			retMobileEntity.setPager(pager);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return retMobileEntity;
	}

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询市民风采列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/searchCityStyle", method = RequestMethod.POST)
	public RetMobileEntity cityStyle(HttpServletRequest request, Integer index, Integer size,String type, String areaId) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
//		String type = request.getParameter("type");//资源类型
//		String areaId = request.getParameter("areaId");//资源类型
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiShowStyleService.cityStyle(type,areaId, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取市民风采列表失败");
				return retMobileEntity;
			}
			retMobileEntity.setCode(0);
			retMobileEntity.setData(pageInfo.getList());
			RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
			pager.setIndex(pageInfo.getPageNum());
			pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
			pager.setSize(pageInfo.getSize());
			pager.setCount(pageInfo.getPages());
			retMobileEntity.setPager(pager);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return retMobileEntity;
	}


	/**
	 * 上传文件
	 * @param request 请求对象
	 * @return
	 */
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	@CrossOrigin
	public ResponseBean uploadImg(HttpServletRequest request){
		ResponseBean res = new ResponseBean();
		try{
			//文件类型
			String uploadFileType = request.getParameter("uploadFileType");//图片/视频/音频/文件
			String needCut = null;//是否裁剪图片，如果是上传的图片时有用
			String cutWidth = null;//宽
			String cutHeight = null;//高

			//获取上传文件对象
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			MultipartFile mulFile = multipartHttpServletRequest.getFile("whgUploadFile");

			//上传处理
			UploadFileBean uploadFileBean = commUploadService.uploadFile(mulFile, uploadFileType, "true".equals(needCut), cutWidth, cutHeight);
			res.setData(uploadFileBean);
		}catch (Exception e){
			log.error(e.getMessage(), e);
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg(e.getMessage());
		}
		return res;
	}

}
