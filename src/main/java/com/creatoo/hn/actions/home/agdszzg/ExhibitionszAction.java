package com.creatoo.hn.actions.home.agdszzg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.creatoo.hn.model.WhArt;
import com.creatoo.hn.model.WhArtExhibition;
import com.creatoo.hn.model.WhCfgAdv;
import com.creatoo.hn.model.WhEntsource;
import com.creatoo.hn.model.WhTyp;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.agdszzg.SzzgService;
import com.creatoo.hn.services.home.art.ArtService;

/**
 * 数字展馆
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/agdszzg")
public class ExhibitionszAction {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * 公用服务类
	 */
	@Autowired
	private CommService commservice;
	
	@Autowired
	public ArtService artService;
	@Autowired
	public SzzgService szzgService;
	
	/**
	 * 查询数字馆全分类信息
	 */
	@RequestMapping("/index")
	public ModelAndView news(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView view = new ModelAndView( "home/agdszzg/index" );
		try {
			//查询艺术分类
			List<WhTyp> typList = commservice.findWhtyp("0");
			view.addObject("artList", typList);
			//查询广告
			String type = "2016122100000005";
			List<WhCfgAdv> listadv= this.szzgService.selectadv(type);
			if (listadv.size() > 0 ) {
				view.addObject("listadv", listadv.get(0));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return view;
	}
	
	/**
	 * 查询数字馆全分类信息
	 */
	@RequestMapping("/zglist")
	public ModelAndView zglist(HttpServletRequest req, HttpServletResponse resp, String id){
		ModelAndView view = new ModelAndView( "home/agdszzg/zglist" );
		try {
			//查询艺术分类
			List<WhTyp> typList = commservice.findWhtyp("0");
			view.addObject("artList", typList);
			
			//查询精品文化展标题
			WhArtExhibition exhibition = this.artService.srchExhibition(id);
			view.addObject("exh", exhibition);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return view;
	}
	/**
	 * 查询数字馆全信息详情
	 */
	@RequestMapping("/zginfo")
	public ModelAndView newsinfo(String id){
		ModelAndView view = new ModelAndView( "home/agdszzg/zginfo" );
		try {
			//查询个人艺术作品详情
			WhArt whart = this.artService.srchWhartInfo(id);
			view.addObject("art", whart);
			String arttyp="2016101400000036";
			String arttypid= whart.getArttypid();
			
			//标签
			view.addObject("tags", this.artService.srchTags(whart.getArttags()));
			
			//关键字
			view.addObject("keys", this.artService.srchKeys(whart.getArtkeys()));
			
			//前一个艺术作品
			view.addObject("preArt", this.artService.getPreArt(id, arttyp,arttypid));
			
			//后一个艺术作品
			view.addObject("nextArt", this.artService.getNextArt(id, arttyp,arttypid));
			
			//查询文化展信息
			WhArtExhibition exhibition = this.artService.srchExhibition(whart.getArttypid());
			view.addObject("exh", exhibition);
			
			//查询个人艺术作品图片列表
			List<WhEntsource> picList = this.artService.srchWhartSrc(whart.getArtid(), "2016101400000055", "2016101400000054");
			if(picList != null && picList.size() > 0){
				view.addObject("picList", picList);
			}
			
			//查询个人艺术作品视频列表
			List<WhEntsource> videoList = this.artService.srchWhartSrc(whart.getArtid(), "2016101400000056", "2016101400000054");
			if(videoList != null && videoList.size() > 0){
				view.addObject("videoList", videoList);
			}
			
			//查询个人艺术作品音频列表
			List<WhEntsource> audioList = this.artService.srchWhartSrc(whart.getArtid(), "2016101400000057", "2016101400000054");
			if(audioList != null && audioList.size() > 0){
				view.addObject("audioList", audioList);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return view;
	}	
}
