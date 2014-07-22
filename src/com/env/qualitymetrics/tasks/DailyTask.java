package com.env.qualitymetrics.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.SprintService;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.tool.RedmineBugNew;
import com.env.qualitymetrics.tool.RedmineBugNewForCategory;
import com.env.qualitymetrics.tool.RedmineBugOmission;
import com.env.qualitymetrics.tool.RedmineBugOmissionForCategory;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.tool.RedminePatch;
import com.env.qualitymetrics.tool.RedmineReopen;
import com.env.qualitymetrics.tool.RedmineReopenForCategory;
import com.env.qualitymetrics.tool.RedmineSupport;
import com.env.qualitymetrics.tool.RedmineSupportForCategory;
import com.env.qualitymetrics.tool.SonarHandler;
import com.env.qualitymetrics.tool.SurveyMonkeyHandler;
import com.env.qualitymetrics.tool.TestLinkForSuiteHandler;
import com.env.qualitymetrics.tool.TestLinkHandler;

public class DailyTask {
	@Resource(name="sprintService")
	SprintService sprintService;
	
	@Resource(name="projectService")
	ProjectService projectService;
	
	@Resource(name="weightService")
	WeightService weightService;
	
	@Resource(name="testlinkHandler")
	TestLinkHandler testlinkHandler;
	
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="redminePatch")
	RedminePatch redminePatch;
	
	@Resource(name="redmineBugOmission")
	RedmineBugOmission redmineBugOmission;
	
	@Resource(name="sonarHandler")
	SonarHandler sonarHandler;
	
	@Resource(name="surveyMonkeyHandler")
	SurveyMonkeyHandler surveyMonkeyHandler;
	
	@Resource(name="redmineBugNew")
	RedmineBugNew redmineBugNew;
	
	@Resource(name="redmineReopen")
	RedmineReopen redmineReopen;
	
	@Resource(name="redmineSupport")
	RedmineSupport redmineSupport;
	
	@Resource(name="testlinkForSuiteHandler")
	TestLinkForSuiteHandler testlinkForSuiteHandler;

	@Resource(name="redmineBugOmissionForCategory")
	RedmineBugOmissionForCategory redmineBugOmissionForCategory;
	
	@Resource(name="redmineBugNewForCategory")
	RedmineBugNewForCategory redmineBugNewForCategory;
	
	@Resource(name="redmineReopenForCategory")
	RedmineReopenForCategory redmineReopenForCategory;
	
	@Resource(name="redmineSupportForCategory")
	RedmineSupportForCategory redmineSupportForCategory;
	
	private static final Logger log = LoggerFactory.getLogger(DailyTask.class); 
	public void intervalCheck() {
		log.info("开始Daily task！");
		updateRatePatchs();
		//获取信息不完整的sprint
		List<SprintDto> sprintList = sprintService.getUncompleteSprints();
		Calendar cal = Calendar.getInstance();
		//对每个不完整的sprint，进行数据拉取
		if(sprintList.size() == 0){
			log.info("没有要更新的sprint!");
			return;
		}else{
			Iterator<SprintDto> iterator = sprintList.iterator();
			while(iterator.hasNext()){
				SprintDto sprintDto = iterator.next();
				int project_flag=projectService.getProjectFlagById(sprintDto.getProject_id());
				if(project_flag==SysUtil.project_flag)
				{
					checkProjectUpdate(sprintDto);  //zhangdi  todo
				}
				else
				{
					checkModuleUpdate(sprintDto);
				}
			}
		}
	}
	
	/***
	 * 更新所有项目的补丁发布率
	 */
	private void updateRatePatchs() {
		List<ProjectDto> projectList = projectService.getAllProjectsDetail();
		for(int i=0;i<projectList.size();i++){
			updateRatePatch(projectList.get(i));
		}
	}
	/***
	 * 更新每个项目的补丁发布率
	 * @param projectDto
	 */
	private void updateRatePatch(ProjectDto projectDto) {
		String project_name_rm = projectDto.getProject_name_rm();
		float rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		log.info("项目名为："+projectDto.getProject_name()+"的项目补丁发布率为"+rate_patch);
		if(rate_patch == -1)
			rate_patch = 0;
		if(projectService.updateProjectRatePatchById(projectDto.getProject_id(),rate_patch)){
			log.info("项目名为："+projectDto.getProject_name()+"的项目补丁发布率更新成功！");
		}
	}
	//获取权重值总和
	public float getTotalWeight(WeightDto weightDto){
		float total=weightDto.getIpdOrLmt_rate()+weightDto.getSonar_rate()+weightDto.getTest_pass_rate()+weightDto.getTc_exec_rate()
				+weightDto.getBug_new_rate()+weightDto.getBug_reopen_rate()+weightDto.getBug_escape_rate()
				+weightDto.getRate_patch_rate()+weightDto.getRate_support_rate()+weightDto.getRate_ce_rate();
		return total;
	}

	//按项目更新sprint
	public void checkProjectUpdate(SprintDto sprintDto) {
		log.info("开始更新sprint名为"+sprintDto.getSprint_name()+"的信息...");
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		//获取Project，sprint相关的映射信息
		String project_name = projectService.getProjectNameById(sprintDto.getProject_id()); 
		String sprint_testplan = sprintDto.getTestplan_testlink();
		String sprint_version_redmine = sprintDto.getVersion_redmine();
		String sprint_build_sonar = sprintDto.getBuild_sonar();
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),SysUtil.project_flag);
		String project_name_tl = projectDto.getProject_name_tl();
		String project_name_rm = projectDto.getProject_name_rm();
		String project_name_rm_spt = projectDto.getProject_name_rm_support();
		if(project_name_tl == null || project_name_rm==null || sprint_testplan == null || sprint_version_redmine==null || sprint_build_sonar == null || project_name==null){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的相关配置信息不完整，请相关人员进行完善！");
			return;
		}
		
		if(sprintDto.getSonar_score_origin() == -1f){
			log.info("sprint id为"+sprintDto.getSprint_id()+"的sonar数据不完整，开始拉取...");
			//===== zhangdi 140512 重构======
			try
			{
				updateSonarScore(sprintDto);
			}
			catch(Exception e)
			{
			}
		}
		
		
		//判断test_pass, tc_exec, bug_new, bug_reopen： sprint_enddate7天后要的数据
		if(sprintDto.getTc_exec_score_origin() == -1f ||sprintDto.getTc_exec_score_origin() == -1f
				||sprintDto.getBug_new_score_origin() == -1f ||sprintDto.getBug_reopen_score_origin() == -1f){
			log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的test_pass, tc_exec, bug_new, bug_reopen数据不完整，开始拉取...");

			Date enddDate = sprintDto.getSprint_enddate();
			if(enddDate == null){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+" end_date为空！");
				return;
			}
			//获取当前日期距离Sprint结束日期是否超过7天
			if((today.getTime()-enddDate.getTime())/(1000*60*60*24)>=SysUtil.afterTestCheckDay){
				float test_pass_origin = testlinkHandler.getPassRate(project_name_tl, sprint_testplan);
				float tc_exec_origin = testlinkHandler.getExcutionRate(project_name_tl, sprint_testplan);
				//拉取redmine
				if(redmineCommon.getProjectId(project_name_rm) == 0 ||
						redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
					log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
					return;
				}
				float bug_new_origin = redmineBugNew.getBugNewRate(project_name_rm, sprint_version_redmine, 
						SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
				float bug_reopen_origin = redmineReopen.getReopenRate(project_name_rm, sprint_version_redmine, 
 						SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
				
				if(test_pass_origin != -1){
					//拉取到了数据，更新数据库
					float test_pass = testlinkHandler.rateToScore(test_pass_origin);
					test_pass_origin = SysUtil.formatFloat(test_pass_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), test_pass_origin ,SysUtil.test_pass);
					sprintDto.setTest_pass_score_origin(test_pass_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), test_pass ,SysUtil.test_pass);
					sprintDto.setTest_pass_score(test_pass);
				}
				if(tc_exec_origin != -1){
					float tc_exec = testlinkHandler.rateToScore(tc_exec_origin);
					tc_exec_origin = SysUtil.formatFloat(tc_exec_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), tc_exec_origin ,SysUtil.tc_exec);
					sprintDto.setTc_exec_score_origin(tc_exec_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), tc_exec ,SysUtil.tc_exec);
					sprintDto.setTc_exec_score(tc_exec);
				}
				if(bug_new_origin != -1){
					float bug_new = this.redmineBugNew.rateToScore(bug_new_origin);
					bug_new_origin = SysUtil.formatFloat(bug_new_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), bug_new_origin ,SysUtil.bug_new);
					sprintDto.setBug_new_score_origin(bug_new_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), bug_new ,SysUtil.bug_new);
					sprintDto.setBug_new_score(bug_new);
				}
				if(bug_reopen_origin != -1){
					float bug_reopen = this.redmineReopen.rateToScore(bug_reopen_origin);
					bug_reopen_origin = SysUtil.formatFloat(bug_reopen_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), bug_reopen_origin ,SysUtil.bug_reopen);
					sprintDto.setBug_reopen_score_origin(bug_reopen_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), bug_reopen ,SysUtil.bug_reopen);
					sprintDto.setBug_reopen_score(bug_reopen);
				}
			}else{
				log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"距离sprint_enddate不足7天，暂不拉取数据");
				return;
			}
		}
		
		//判断bug_escape,  rate_support, rate_ce 这些sprint_enddate后31天拉取的数据
		if(sprintDto.getBug_escape_score_origin() == -1f || sprintDto.getRate_support_score_origin() == -1f || sprintDto.getRate_ce_score_origin()== -1f){
			log.info(sprintDto.getSprint_id()+"的bug_escape, rate_patch, rate_support, rate_ce数据不完整，开始拉取...");
			Date enddDate = sprintDto.getSprint_enddate();
			if(enddDate == null){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+" end_date为空！");
				return;
			}
			if((today.getTime()-enddDate.getTime())/(1000*60*60*24)>=SysUtil.afterReleaseCheckDay){
				//开始拉取数据并更新，拉取出错逻辑在接口中
				
				float rate_support_origin = redmineSupport.getSupportRate(project_name_rm, sprint_version_redmine,
						project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_enddate()));
				String titleArrays[] = sprintDto.getUrl_surveymonkey().split(SysUtil.splitFlag);
				float rate_ce_total = 0;
				int validCount = 0;
				for(int i=0;i<titleArrays.length;i++){
					float temp = surveyMonkeyHandler.getSurveyMonkeyScore(titleArrays[i].trim());
					if(temp != -1){
						rate_ce_total+=temp;
						validCount++;
					}else{
						continue;
					}
				}
				float rate_ce = -1;
				if(validCount == 0){
					rate_ce = -1;
				}else{
					rate_ce = rate_ce_total/validCount;
					rate_ce = (float)(Math.round(rate_ce*100))/100;
				}
				
				if(redmineCommon.getProjectId(project_name_rm) == 0 ||
						redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
					//在redmine中未查找到相应的projectName或者versionName
					log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
					return;
				}
				float bug_escape_origin = redmineBugOmission.getBugOmissionRate(project_name_rm,sprint_version_redmine,project_name_rm_spt,
						SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
				if(bug_escape_origin != -1){
					//拉取到了数据，更新数据库
					float bug_escape = this.redmineBugOmission.rateToScore(bug_escape_origin);
					bug_escape_origin = SysUtil.formatFloat(bug_escape_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),bug_escape_origin,SysUtil.bug_escape);
					sprintDto.setBug_escape_score_origin(bug_escape_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(),bug_escape,SysUtil.bug_escape);
					sprintDto.setBug_escape_score(bug_escape);
				}
				if(rate_support_origin != -1){
					float rate_support = this.redmineSupport.rateToScore(rate_support_origin);
					rate_support_origin = SysUtil.formatFloat(rate_support_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),rate_support_origin,SysUtil.rate_support);
					sprintDto.setRate_support_score_origin(rate_support_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(),rate_support,SysUtil.rate_support);
					sprintDto.setRate_support_score(rate_support);
				}
				if(rate_ce != -1){
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce);
					sprintDto.setRate_ce_score(rate_ce);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce);
					sprintDto.setRate_ce_score_origin(rate_ce);
				}
			}else{
				log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"距离sprint_enddate不足31天，暂不拉取数据");
				return;
			}
		}
		
		//所有的数据都拉取成功了，计算sprint_score，更新数据库
		if(sprintDto.getSonar_score_origin()==-1||sprintDto.getTc_exec_score_origin()==-1||sprintDto.getTest_pass_score_origin()==-1
			||sprintDto.getBug_new_score_origin()==-1 ||sprintDto.getBug_reopen_score_origin()==-1||sprintDto.getBug_escape_score_origin()==-1
			||sprintDto.getRate_support_score_origin()==-1||sprintDto.getRate_ce_score_origin()==-1){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"数据不完整，下次定时任务会继续拉取！");
				return;
		}
		if(sprintDto.getLmt_score() == -1 && sprintDto.getIpd_score() == -1){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"lmt或者ipd分数为空，请联系相关人员填写");
			return;
		}
		/*
		 * 判断补丁发布率是否存在，不存在不计算
		 */
		float rate_patch = -1;
		rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		log.info("Sprint名为"+sprintDto.getSprint_name()+"补丁发布率为："+rate_patch);
		if(rate_patch == -1){
			log.error("项目名为:"+project_name+"的项目补丁发布率不存在，请联系相关人员设置！");
			return;
		}
		
		float lmtOrIpdScore = sprintDto.getLmt_score() != -1 ? sprintDto.getLmt_score() : sprintDto.getIpd_score();
		
		log.info("所有"+sprintDto.getSprint_name()+"的相关记录均已生成，计算平均分...");
		WeightDto weightDto=weightService.getWeights();
		float total=getTotalWeight(weightDto);
		//按权重比例计算sprint总分
		float sprint_score = lmtOrIpdScore*weightDto.getIpdOrLmt_rate()/total
		+ sprintDto.getSonar_score()*weightDto.getSonar_rate()/total
		+ sprintDto.getTest_pass_score()*weightDto.getTest_pass_rate()/total 
		+ sprintDto.getTc_exec_score()*weightDto.getTc_exec_rate()/total 
		+ sprintDto.getBug_new_score()*weightDto.getBug_new_rate()/total 
		+ sprintDto.getBug_reopen_score()*weightDto.getBug_reopen_rate()/total 
		+ sprintDto.getBug_escape_score()*weightDto.getBug_escape_rate()/total
		+ redminePatch.getPatchScoreUntilNow(project_name_rm)*weightDto.getRate_patch_rate()/total//补丁发布率，根据项目来
		+ sprintDto.getRate_support_score()*weightDto.getRate_support_rate()/total
		+ sprintDto.getRate_ce_score()*weightDto.getRate_ce_rate()/total;
		
		//sprint_score = sprint_score/SysUtil.score_item_num;
		sprint_score = ((float)Math.round(sprint_score*100))/100;
		sprintService.updateSprintScore(sprintDto.getSprint_id(),sprint_score);
		sprintDto.setSprint_score(sprint_score);
		log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的数据拉取已经完成！,分数更新为："+sprint_score);
		/*
		 * 更新项目总平均分
		 */
		updateProjectAvgScore(sprintDto.getProject_id());
}
	//按模块更新sprint
	public void checkModuleUpdate(SprintDto sprintDto) {
		log.info("开始更新sprint名为"+sprintDto.getSprint_name()+"的模块信息...");
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		//获取Project，sprint相关的映射信息
		String project_name = projectService.getProjectNameById(sprintDto.getProject_id()); 
		String sprint_testplan = sprintDto.getTestplan_testlink();
		String sprint_version_redmine = sprintDto.getVersion_redmine();
		String sprint_build_sonar = sprintDto.getBuild_sonar();
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),SysUtil.module_flag);
		String project_name_tl = projectDto.getProject_name_tl();
		String project_name_rm = projectDto.getProject_name_rm();
		String project_name_rm_spt = projectDto.getProject_name_rm_support();
		String suite_name_tl=projectDto.getSuite_name_tl();
		String category_name_rm=projectDto.getCategory_name_rm();
		if(project_name_tl == null || project_name_rm==null || sprint_testplan == null || sprint_version_redmine==null || sprint_build_sonar == null || project_name==null){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的相关配置信息不完整，请相关人员进行完善！");
			return;
		}	
		try
		{
			updateSonarScore(sprintDto);
		}
		catch(Exception e)
		{
		}
		 
		//判断test_pass, tc_exec, bug_new, bug_reopen： sprint_enddate7天后要的数据
		if(sprintDto.getTc_exec_score_origin() == -1f ||sprintDto.getTc_exec_score_origin() == -1f
				||sprintDto.getBug_new_score_origin() == -1f ||sprintDto.getBug_reopen_score_origin() == -1f){
			log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的test_pass, tc_exec, bug_new, bug_reopen数据不完整，开始拉取...");

			Date enddDate = sprintDto.getSprint_enddate();
			if(enddDate == null){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+" end_date为空！");
				return;
			}
			//获取当前日期距离Sprint结束日期是否超过7天
			if((today.getTime()-enddDate.getTime())/(1000*60*60*24)>=SysUtil.afterTestCheckDay){
				float[] test_results=testlinkForSuiteHandler.getResults(project_name_tl, suite_name_tl, sprint_testplan);
				float test_pass_origin = test_results[0];//testlinkForSuiteHandler.getPassRate(project_name_tl, suite_name_tl, sprint_testplan);
				float tc_exec_origin = test_results[1];//testlinkForSuiteHandler.getExcutionRate(project_name_tl, suite_name_tl, sprint_testplan);
				//拉取redmine
				if(redmineCommon.getProjectId(project_name_rm) == 0 ||
						redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
					log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
					return;
				}
				float bug_new_origin = redmineBugNewForCategory.getBugNewRate(project_name_rm, sprint_version_redmine, category_name_rm,
						SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
				float bug_reopen_origin = redmineReopenForCategory.getReopenRate(project_name_rm, sprint_version_redmine, category_name_rm,
						SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
								
				if(test_pass_origin != -1){
					//拉取到了数据，更新数据库
					
					float test_pass = testlinkForSuiteHandler.rateToScore(test_pass_origin);
					test_pass_origin = SysUtil.formatFloat(test_pass_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), test_pass_origin ,SysUtil.test_pass);
					sprintDto.setTest_pass_score_origin(test_pass_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), test_pass ,SysUtil.test_pass);
					sprintDto.setTest_pass_score(test_pass);
				}
				if(tc_exec_origin != -1){
					float tc_exec = testlinkForSuiteHandler.rateToScore(tc_exec_origin);
					tc_exec_origin = SysUtil.formatFloat(tc_exec_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), tc_exec_origin ,SysUtil.tc_exec);
					sprintDto.setTc_exec_score_origin(tc_exec_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), tc_exec ,SysUtil.tc_exec);
					sprintDto.setTc_exec_score(tc_exec);
				}
				if(bug_new_origin != -1){
					float bug_new = this.redmineBugNewForCategory.rateToScore(bug_new_origin);
					bug_new_origin = SysUtil.formatFloat(bug_new_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), bug_new_origin ,SysUtil.bug_new);
					sprintDto.setBug_new_score_origin(bug_new_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), bug_new ,SysUtil.bug_new);
					sprintDto.setBug_new_score(bug_new);
				}
				if(bug_reopen_origin != -1){
					float bug_reopen = this.redmineReopenForCategory.rateToScore(bug_reopen_origin);
					bug_reopen_origin = SysUtil.formatFloat(bug_reopen_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), bug_reopen_origin ,SysUtil.bug_reopen);
					sprintDto.setBug_reopen_score_origin(bug_reopen_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(), bug_reopen ,SysUtil.bug_reopen);
					sprintDto.setBug_reopen_score(bug_reopen);
				}
			}else{
				log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"距离sprint_enddate不足7天，暂不拉取数据");
				return;
			}
		}
		
		//判断bug_escape,  rate_support, rate_ce 这些sprint_enddate后31天拉取的数据
		if(sprintDto.getBug_escape_score_origin() == -1f || sprintDto.getRate_support_score_origin() == -1f || sprintDto.getRate_ce_score_origin()== -1f){
			log.info(sprintDto.getSprint_id()+"的bug_escape, rate_patch, rate_support, rate_ce数据不完整，开始拉取...");
			Date enddDate = sprintDto.getSprint_enddate();
			if(enddDate == null){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+" end_date为空！");
				return;
			}
			if((today.getTime()-enddDate.getTime())/(1000*60*60*24)>=SysUtil.afterReleaseCheckDay){
				//开始拉取数据并更新，拉取出错逻辑在接口中
				
				float rate_support_origin = redmineSupportForCategory.getSupportRate(project_name_rm, sprint_version_redmine, category_name_rm,
						project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_enddate()));
				String titleArrays[] = sprintDto.getUrl_surveymonkey().split(SysUtil.splitFlag);
				float rate_ce_total = 0;
				int validCount = 0;
				for(int i=0;i<titleArrays.length;i++){
					float temp = surveyMonkeyHandler.getSurveyMonkeyScore(titleArrays[i].trim());
					if(temp != -1){
						rate_ce_total+=temp;
						validCount++;
					}else{
						continue;
					}
				}
				float rate_ce = -1;
				if(validCount == 0){
					rate_ce = -1;
				}else{
					rate_ce = rate_ce_total/validCount;
					rate_ce = (float)(Math.round(rate_ce*100))/100;
				}
				
				if(redmineCommon.getProjectId(project_name_rm) == 0 ||
						redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
					//在redmine中未查找到相应的projectName或者versionName
					log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
					return;
				}
				float bug_escape_origin = redmineBugOmissionForCategory.getBugOmissionRate(project_name_rm, sprint_version_redmine, category_name_rm, project_name_rm_spt,
						SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
				if(bug_escape_origin != -1){
					//拉取到了数据，更新数据库
					float bug_escape = this.redmineBugOmissionForCategory.rateToScore(bug_escape_origin);
					bug_escape_origin = SysUtil.formatFloat(bug_escape_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),bug_escape_origin,SysUtil.bug_escape);
					sprintDto.setBug_escape_score_origin(bug_escape_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(),bug_escape,SysUtil.bug_escape);
					sprintDto.setBug_escape_score(bug_escape);
				}
				if(rate_support_origin != -1){
					float rate_support = this.redmineSupportForCategory.rateToScore(rate_support_origin);
					rate_support_origin = SysUtil.formatFloat(rate_support_origin);
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),rate_support_origin,SysUtil.rate_support);
					sprintDto.setRate_support_score_origin(rate_support_origin);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(),rate_support,SysUtil.rate_support);
					sprintDto.setRate_support_score(rate_support);
				}
				if(rate_ce != -1){
					sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce);
					sprintDto.setRate_ce_score(rate_ce);
					sprintService.updateScore_item_score(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce);
					sprintDto.setRate_ce_score_origin(rate_ce);
				}
			}else{
				log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"距离sprint_enddate不足31天，暂不拉取数据");
				return;
			}
		}
		
		//所有的数据都拉取成功了，计算sprint_score，更新数据库
		if(sprintDto.getSonar_score_origin()==-1||sprintDto.getTc_exec_score_origin()==-1||sprintDto.getTest_pass_score_origin()==-1
			||sprintDto.getBug_new_score_origin()==-1 ||sprintDto.getBug_reopen_score_origin()==-1||sprintDto.getBug_escape_score_origin()==-1
			||sprintDto.getRate_support_score_origin()==-1||sprintDto.getRate_ce_score_origin()==-1){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"数据不完整，下次定时任务会继续拉取！");
				return;
		}
		if(sprintDto.getLmt_score() == -1 && sprintDto.getIpd_score() == -1){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"lmt或者ipd分数为空，请联系相关人员填写");
			return;
		}
		/*
		 * 判断补丁发布率是否存在，不存在不计算
		 */
		float rate_patch = -1;
		rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		log.info("Sprint名为"+sprintDto.getSprint_name()+"补丁发布率为："+rate_patch);
		if(rate_patch == -1){
			log.error("项目名为:"+project_name+"的项目补丁发布率不存在，请联系相关人员设置！");
			return;
		}
		
		float lmtOrIpdScore = sprintDto.getLmt_score() != -1 ? sprintDto.getLmt_score() : sprintDto.getIpd_score();
		
		log.info("所有"+sprintDto.getSprint_name()+"的相关记录均已生成，计算平均分...");
		WeightDto weightDto=weightService.getWeights();
		float total=getTotalWeight(weightDto);
		//按权重比例计算sprint总分
		float sprint_score = lmtOrIpdScore*weightDto.getIpdOrLmt_rate()/total
		+ sprintDto.getSonar_score()*weightDto.getSonar_rate()/total
		+ sprintDto.getTest_pass_score()*weightDto.getTest_pass_rate()/total 
		+ sprintDto.getTc_exec_score()*weightDto.getTc_exec_rate()/total 
		+ sprintDto.getBug_new_score()*weightDto.getBug_new_rate()/total 
		+ sprintDto.getBug_reopen_score()*weightDto.getBug_reopen_rate()/total 
		+ sprintDto.getBug_escape_score()*weightDto.getBug_escape_rate()/total
		+ redminePatch.getPatchScoreUntilNow(project_name_rm)*weightDto.getRate_patch_rate()/total//补丁发布率，根据项目来
		+ sprintDto.getRate_support_score()*weightDto.getRate_support_rate()/total
		+ sprintDto.getRate_ce_score()*weightDto.getRate_ce_rate()/total;
		
		//sprint_score = sprint_score/SysUtil.score_item_num;
		sprint_score = ((float)Math.round(sprint_score*100))/100;
		sprintService.updateSprintScore(sprintDto.getSprint_id(),sprint_score);
		sprintDto.setSprint_score(sprint_score);
		log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的数据拉取已经完成！,分数更新为："+sprint_score);
		/*
		 * 更新项目总平均分
		 */
		updateProjectAvgScore(sprintDto.getProject_id());
}
	
	/***
	 * 更新项目的总平均分
	 * @param project_id
	 */
	private void updateProjectAvgScore(int project_id) {
		// TODO Auto-generated method stub
		projectService.updateProjectAvgScoreById(project_id);
	}
	
	/***
	 * 手动强制更新
	 * 通过project_flag判断是更新项目还是模块
	 */
	//zhangdi todo 此处需和定时更新处重构==========
	
	public void checkUpdateManual(SprintDto sprintDto,int project_flag){
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		//获取Project，sprint相关的映射信息
		String project_name = projectService.getProjectNameById(sprintDto.getProject_id()); 
		String sprint_testplan = sprintDto.getTestplan_testlink();
		String sprint_version_redmine = sprintDto.getVersion_redmine();
		String sprint_build_sonar = sprintDto.getSprint_build();
		
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),project_flag);
		String project_name_tl = projectDto.getProject_name_tl();
		String project_name_rm = projectDto.getProject_name_rm();
		String project_name_rm_spt = projectDto.getProject_name_rm_support();
		
		if(project_name_tl == null || project_name_rm==null 
				|| sprint_testplan == null || sprint_version_redmine==null || sprint_build_sonar == null || project_name==null){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的相关配置信息不完整，请相关人员进行完善！");
			return;
		}
		
		log.info("开始拉取...sprint id为"+sprintDto.getSprint_id()+"的sonar数据.");
		Date builddate = sprintDto.getSprint_builddate();
		Date endDate = sprintDto.getSprint_enddate();
		
		//=======================  zhangdi 140512 =================================
		try
		{
			updateSonarScore(sprintDto);
		}
		catch(Exception e)
		{
			
		}
		
		log.info("开始拉取...project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的test_pass, tc_exec, bug_new, bug_reopen数据");
		Date enddDate = sprintDto.getSprint_enddate();
		if(enddDate == null){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+" end_date为空！");
			return;
		}
		float test_pass_origin=-1;
		float tc_exec_origin=-1;
		if(project_flag==SysUtil.project_flag){
			test_pass_origin = testlinkHandler.getPassRate(project_name_tl, sprint_testplan);
			tc_exec_origin = testlinkHandler.getExcutionRate(project_name_tl, sprint_testplan);
		}
		else{
			float[] test_results=testlinkForSuiteHandler.getResults(project_name_tl, projectDto.getSuite_name_tl(), sprint_testplan);
			test_pass_origin=test_results[0];//testlinkForSuiteHandler.getPassRate(project_name_tl, projectDto.getSuite_name_tl(), sprint_testplan);
			tc_exec_origin=test_results[1];//testlinkForSuiteHandler.getExcutionRate(project_name_tl, projectDto.getSuite_name_tl(), sprint_testplan);
		}
		//拉取redmine
		if(redmineCommon.getProjectId(project_name_rm) == 0 ||
				redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
			log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
			return;
		}
		float bug_new_origin=-1;
		float bug_reopen_origin=-1;
		if(project_flag==SysUtil.project_flag){
			bug_new_origin = redmineBugNew.getBugNewRate(project_name_rm, sprint_version_redmine, 
				SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		
			bug_reopen_origin = redmineReopen.getReopenRate(project_name_rm, sprint_version_redmine, 
					SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			bug_new_origin=redmineBugNewForCategory.getBugNewRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(), 
					SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
			bug_reopen_origin=redmineReopenForCategory.getReopenRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(), 
					SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}
		if(test_pass_origin != -1){
			//拉取到了数据，更新数据库
			float test_pass = testlinkHandler.rateToScore(test_pass_origin);
			test_pass_origin = SysUtil.formatFloat(test_pass_origin);
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), test_pass_origin ,SysUtil.test_pass);
			sprintDto.setTest_pass_score_origin(test_pass_origin);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(), test_pass ,SysUtil.test_pass);
			sprintDto.setTest_pass_score(test_pass);
		}
		if(tc_exec_origin != -1){
			float tc_exec = testlinkHandler.rateToScore(tc_exec_origin);
			tc_exec_origin = SysUtil.formatFloat(tc_exec_origin);
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), tc_exec_origin ,SysUtil.tc_exec);
			sprintDto.setTc_exec_score_origin(tc_exec_origin);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(), tc_exec ,SysUtil.tc_exec);
			sprintDto.setTc_exec_score(tc_exec);
		}
		if(bug_new_origin != -1){
			float bug_new = this.redmineBugNew.rateToScore(bug_new_origin);
			bug_new_origin = SysUtil.formatFloat(bug_new_origin);
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), bug_new_origin ,SysUtil.bug_new);
			sprintDto.setBug_new_score_origin(bug_new_origin);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(), bug_new ,SysUtil.bug_new);
			sprintDto.setBug_new_score(bug_new);
		}
		if(bug_reopen_origin != -1){
			float bug_reopen = this.redmineReopen.rateToScore(bug_reopen_origin);
			bug_reopen_origin = SysUtil.formatFloat(bug_reopen_origin);
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), bug_reopen_origin ,SysUtil.bug_reopen);
			sprintDto.setBug_reopen_score_origin(bug_reopen_origin);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(), bug_reopen ,SysUtil.bug_reopen);
			sprintDto.setBug_reopen_score(bug_reopen);
		}
		
		
		log.info("开始拉取...sprint id为"+sprintDto.getSprint_id()+"的bug_escape, rate_patch, rate_support, rate_ce数据");
		//开始拉取数据并更新
		float rate_support_origin=-1;
		if(project_flag==SysUtil.project_flag){
			rate_support_origin = redmineSupport.getSupportRate(project_name_rm, sprint_version_redmine,
				project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			rate_support_origin=redmineSupportForCategory.getSupportRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(), 
					project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_enddate()));
			
		}
		String titleArrays[] = sprintDto.getUrl_surveymonkey().split(SysUtil.splitFlag.trim());
		float rate_ce_total = 0;
		int validCount = 0;
		for(int i=0;i<titleArrays.length;i++){
			float temp = surveyMonkeyHandler.getSurveyMonkeyScore(titleArrays[i]);
			if(temp != -1){
				rate_ce_total+=temp;
				validCount++;
			}else{
				continue;
			}
		}
		float rate_ce = -1;
		if(validCount == 0){
			rate_ce = -1;
		}else{
			rate_ce = rate_ce_total/validCount;
			rate_ce = (float)(Math.round(rate_ce*10))/10;
		}
		
		if(redmineCommon.getProjectId(project_name_rm) == 0 ||
				redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
			//在redmine中未查找到相应的projectName或者versionName
			log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
			return;
		}
		float bug_escape_origin=-1;
		if(project_flag==SysUtil.project_flag){
			bug_escape_origin = redmineBugOmission.getBugOmissionRate(project_name_rm,sprint_version_redmine,project_name_rm_spt,
				SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			bug_escape_origin=redmineBugOmissionForCategory.getBugOmissionRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(),
				project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}
		if(bug_escape_origin != -1){
			//拉取到了数据，更新数据库
			float bug_escape = this.redmineBugOmission.rateToScore(bug_escape_origin);
			bug_escape_origin = SysUtil.formatFloat(bug_escape_origin);
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),bug_escape_origin,SysUtil.bug_escape);
			sprintDto.setBug_escape_score_origin(bug_escape_origin);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(),bug_escape,SysUtil.bug_escape);
			sprintDto.setBug_escape_score(bug_escape);
		}
		if(rate_support_origin != -1){
			float rate_support = this.redmineSupport.rateToScore(rate_support_origin);
			rate_support_origin = SysUtil.formatFloat(rate_support_origin);
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),rate_support_origin,SysUtil.rate_support);
			sprintDto.setRate_support_score_origin(rate_support_origin);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(),rate_support,SysUtil.rate_support);
			sprintDto.setRate_support_score(rate_support);
		}
		if(rate_ce != -1){
			sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce);
			sprintDto.setRate_ce_score(rate_ce);
			sprintService.updateScore_item_score(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce);
			sprintDto.setRate_ce_score_origin(rate_ce);
		}
	
		
		//所有的数据都拉取成功了，计算sprint_score，更新数据库
		if(sprintDto.getSonar_score_origin()==-1||sprintDto.getTc_exec_score_origin()==-1||sprintDto.getTest_pass_score_origin()==-1
			||sprintDto.getBug_new_score_origin()==-1 ||sprintDto.getBug_reopen_score_origin()==-1||sprintDto.getBug_escape_score_origin()==-1
			||sprintDto.getRate_support_score_origin()==-1||sprintDto.getRate_ce_score_origin()==-1){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"数据不完整，下次定时任务会继续拉取！");
				return;
			}
			if(sprintDto.getLmt_score() == -1 && sprintDto.getIpd_score() == -1){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"lmt或者ipd分数为空，请联系相关人员填写");
				return;
			}
			
			float lmtOrIpdScore = sprintDto.getLmt_score() != -1 ? sprintDto.getLmt_score() : sprintDto.getIpd_score();

			/*
			 * 补丁发布率手动更新时需要分情况
			 * 1. 更新时间在sprint结束日期之前：补丁发布率按照实时的分数进行统计
			 * 2. 更新时间在sprint结束日期之后：补丁发布率按照Sprint结束日期进行统计
			 * */
			float rate_patch = -1;
			//zhangdi  todo  此处待研究
			//if((today.getTime()-builddate.getTime())<0){
			if((today.getTime()-endDate.getTime())<0){
				//情况1
				rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
			}else{
				//情况2
				rate_patch = redminePatch.getPatchRateWithDate(project_name_rm, SysUtil.formatDate(enddDate));
			}
			
			if(rate_patch == -1){
				log.error("项目名为:"+project_name+"的项目补丁发布率不存在，请联系相关人员设置！");
				return;
			}
				
			if(projectService.updateProjectRatePatchById(sprintDto.getProject_id(),rate_patch)){
				log.info("项目名为："+project_name+"的项目补丁发布率更新成功！");
			}
			log.info("各项得分:ipd/lmt:"+lmtOrIpdScore+" sonar："+
					+ sprintDto.getSonar_score()+" test_pass"+
					+ sprintDto.getTest_pass_score() +" tc_exec"+
					+ sprintDto.getTc_exec_score() +" bug_new"+
					+ sprintDto.getBug_new_score() 
					+" bug_reopen"+
					+ sprintDto.getBug_reopen_score()+" bug_escape："+
					+ sprintDto.getBug_escape_score()
					+" rate_patch score："+redminePatch.rateToScore(rate_patch)+" rate_support："+sprintDto.getRate_support_score()+ " rate_Ce："+sprintDto.getRate_ce_score());
			WeightDto weightDto=weightService.getWeights();
			float total=getTotalWeight(weightDto);
			//按权重比例计算sprint总分
			float sprint_score = lmtOrIpdScore*weightDto.getIpdOrLmt_rate()/total 
			+ sprintDto.getSonar_score()*weightDto.getSonar_rate()/total
			+ sprintDto.getTest_pass_score()*weightDto.getTest_pass_rate()/total 
			+ sprintDto.getTc_exec_score()*weightDto.getTc_exec_rate()/total 
			+ sprintDto.getBug_new_score()*weightDto.getBug_new_rate()/total 
			+ sprintDto.getBug_reopen_score()*weightDto.getBug_reopen_rate()/total 
			+ sprintDto.getBug_escape_score()*weightDto.getBug_escape_rate()/total
			+ redminePatch.rateToScore(rate_patch)*weightDto.getRate_patch_rate()/total//补丁发布率，根据项目来
			+ sprintDto.getRate_support_score()*weightDto.getRate_support_rate()/total
			+ sprintDto.getRate_ce_score()*weightDto.getRate_ce_rate()/total;

			
			//sprint_score = sprint_score/SysUtil.score_item_num;
			sprint_score = ((float)Math.round(sprint_score*100))/100;
			sprintService.updateSprintScore(sprintDto.getSprint_id(),sprint_score);
			sprintDto.setSprint_score((float)(Math.round(sprint_score*100))/100);
			log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的数据拉取已经完成！分数更新为："+sprint_score);
			
			updateProjectAvgScore(sprintDto.getProject_id());
	}
	/***
	 * 手动强制更新往期sprint历史分数
	 * 通过project_flag判断是更新项目还是模块
	 */
	public void checkUpdateHistoryManual(SprintDto sprintDto,int project_flag,int rank_id){
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		//获取Project，sprint相关的映射信息
		String project_name = projectService.getProjectNameById(sprintDto.getProject_id());
		String sprint_testplan = sprintDto.getTestplan_testlink();
		String sprint_version_redmine = sprintDto.getVersion_redmine();
		String sprint_build_sonar = sprintDto.getBuild_sonar();
		
		String sprint_build=sprintDto.getSprint_build();
		
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),project_flag);
		String project_name_tl = projectDto.getProject_name_tl();
		String project_name_rm = projectDto.getProject_name_rm();
		String project_name_rm_spt = projectDto.getProject_name_rm_support();
		
		if(project_name_tl == null || project_name_rm==null 
				|| sprint_testplan == null || sprint_version_redmine==null || sprint_build_sonar == null || project_name==null){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的相关配置信息不完整，请相关人员进行完善！");
			return;
		}
		
		log.info("开始拉取...sprint id为"+sprintDto.getSprint_id()+"的sonar数据.");
		Date builddate = sprintDto.getSprint_builddate();
		Date endDate = sprintDto.getSprint_enddate();
		
		//=======================  zhangdi 140512 =================================
		try
		{
			updateSonarScore(sprintDto);
		}
		catch(Exception e)
		{
			
		}
		
		log.info("开始拉取...project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的test_pass, tc_exec, bug_new, bug_reopen数据");

		Date enddDate = sprintDto.getSprint_enddate();
		if(enddDate == null){
			log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+" end_date为空！");
			return;
		}
		float test_pass_origin=-1;
		float tc_exec_origin=-1;
		if(project_flag==SysUtil.project_flag){
			test_pass_origin = testlinkHandler.getPassRate(project_name_tl, sprint_testplan);
			tc_exec_origin = testlinkHandler.getExcutionRate(project_name_tl, sprint_testplan);
		}
		else{
			float[] test_results=testlinkForSuiteHandler.getResults(project_name_tl, projectDto.getSuite_name_tl(), sprint_testplan);
			test_pass_origin=test_results[0];//testlinkForSuiteHandler.getPassRate(project_name_tl, projectDto.getSuite_name_tl(), sprint_testplan);
			tc_exec_origin=test_results[1];//testlinkForSuiteHandler.getExcutionRate(project_name_tl, projectDto.getSuite_name_tl(), sprint_testplan);
		}
		//拉取redmine
		if(redmineCommon.getProjectId(project_name_rm) == 0 ||
				redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
			log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
			return;
		}
		float bug_new_origin=-1;
		float bug_reopen_origin=-1;
		if(project_flag==SysUtil.project_flag){
			bug_new_origin = redmineBugNew.getBugNewRate(project_name_rm, sprint_version_redmine, 
				SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		
			bug_reopen_origin = redmineReopen.getReopenRate(project_name_rm, sprint_version_redmine, 
					SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			bug_new_origin=redmineBugNewForCategory.getBugNewRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(), 
					SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
			bug_reopen_origin=redmineReopenForCategory.getReopenRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(), 
					SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}
		if(test_pass_origin != -1){
			//拉取到了数据，更新数据库
			float test_pass = testlinkHandler.rateToScore(test_pass_origin);
			test_pass_origin = SysUtil.formatFloat(test_pass_origin);
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(), test_pass_origin ,SysUtil.test_pass,rank_id);
			sprintDto.setTest_pass_score_origin(test_pass_origin);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(), test_pass ,SysUtil.test_pass,rank_id);
			sprintDto.setTest_pass_score(test_pass);
		}
		if(tc_exec_origin != -1){
			float tc_exec = testlinkHandler.rateToScore(tc_exec_origin);
			tc_exec_origin = SysUtil.formatFloat(tc_exec_origin);
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(), tc_exec_origin ,SysUtil.tc_exec,rank_id);
			sprintDto.setTc_exec_score_origin(tc_exec_origin);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(), tc_exec ,SysUtil.tc_exec,rank_id);
			sprintDto.setTc_exec_score(tc_exec);
		}
		if(bug_new_origin != -1){
			float bug_new = this.redmineBugNew.rateToScore(bug_new_origin);
			bug_new_origin = SysUtil.formatFloat(bug_new_origin);
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(), bug_new_origin ,SysUtil.bug_new,rank_id);
			sprintDto.setBug_new_score_origin(bug_new_origin);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(), bug_new ,SysUtil.bug_new,rank_id);
			sprintDto.setBug_new_score(bug_new);
		}
		if(bug_reopen_origin != -1){
			float bug_reopen = this.redmineReopen.rateToScore(bug_reopen_origin);
			bug_reopen_origin = SysUtil.formatFloat(bug_reopen_origin);
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(), bug_reopen_origin ,SysUtil.bug_reopen,rank_id);
			sprintDto.setBug_reopen_score_origin(bug_reopen_origin);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(), bug_reopen ,SysUtil.bug_reopen,rank_id);
			sprintDto.setBug_reopen_score(bug_reopen);
		}
		
		
		log.info("开始拉取...sprint id为"+sprintDto.getSprint_id()+"的bug_escape, rate_patch, rate_support, rate_ce数据");
		//开始拉取数据并更新
		float rate_support_origin=-1;
		if(project_flag==SysUtil.project_flag){
			rate_support_origin = redmineSupport.getSupportRate(project_name_rm, sprint_version_redmine,
				project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			rate_support_origin=redmineSupportForCategory.getSupportRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(), 
					project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_enddate()));
			
		}
		String titleArrays[] = sprintDto.getUrl_surveymonkey().split(SysUtil.splitFlag.trim());
		float rate_ce_total = 0;
		int validCount = 0;
		for(int i=0;i<titleArrays.length;i++){
			float temp = surveyMonkeyHandler.getSurveyMonkeyScore(titleArrays[i]);
			if(temp != -1){
				rate_ce_total+=temp;
				validCount++;
			}else{
				continue;
			}
		}
		float rate_ce = -1;
		if(validCount == 0){
			rate_ce = -1;
		}else{
			rate_ce = rate_ce_total/validCount;
			rate_ce = (float)(Math.round(rate_ce*10))/10;
		}
		
		if(redmineCommon.getProjectId(project_name_rm) == 0 ||
				redmineCommon.getVersionId(project_name_rm, sprint_version_redmine) == 0){
			//在redmine中未查找到相应的projectName或者versionName
			log.error("在Redmine中未找到ProjectName为："+project_name_rm+" VersionName为"+sprint_version_redmine+"的相关记录！");
			return;
		}
		float bug_escape_origin=-1;
		if(project_flag==SysUtil.project_flag){
			bug_escape_origin = redmineBugOmission.getBugOmissionRate(project_name_rm,sprint_version_redmine,project_name_rm_spt,
				SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			bug_escape_origin=redmineBugOmissionForCategory.getBugOmissionRate(project_name_rm, sprint_version_redmine, projectDto.getCategory_name_rm(),
				project_name_rm_spt, SysUtil.formatDate(sprintDto.getSprint_startdate()), SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}
		if(bug_escape_origin != -1){
			//拉取到了数据，更新数据库
			float bug_escape = this.redmineBugOmission.rateToScore(bug_escape_origin);
			bug_escape_origin = SysUtil.formatFloat(bug_escape_origin);
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(),bug_escape_origin,SysUtil.bug_escape,rank_id);
			sprintDto.setBug_escape_score_origin(bug_escape_origin);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(),bug_escape,SysUtil.bug_escape,rank_id);
			sprintDto.setBug_escape_score(bug_escape);
		}
		if(rate_support_origin != -1){
			float rate_support = this.redmineSupport.rateToScore(rate_support_origin);
			rate_support_origin = SysUtil.formatFloat(rate_support_origin);
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(),rate_support_origin,SysUtil.rate_support,rank_id);
			sprintDto.setRate_support_score_origin(rate_support_origin);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(),rate_support,SysUtil.rate_support,rank_id);
			sprintDto.setRate_support_score(rate_support);
		}
		if(rate_ce != -1){
			sprintService.updateHistoryScore_item_score_origin(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce,rank_id);
			sprintDto.setRate_ce_score(rate_ce);
			sprintService.updateHistoryScore_item_score(sprintDto.getSprint_id(),rate_ce,SysUtil.rate_ce,rank_id);
			sprintDto.setRate_ce_score_origin(rate_ce);
		}
		//所有的数据都拉取成功了，计算sprint_score，更新数据库
		if(sprintDto.getSonar_score_origin()==-1||sprintDto.getTc_exec_score_origin()==-1||sprintDto.getTest_pass_score_origin()==-1
			||sprintDto.getBug_new_score_origin()==-1 ||sprintDto.getBug_reopen_score_origin()==-1||sprintDto.getBug_escape_score_origin()==-1
			||sprintDto.getRate_support_score_origin()==-1||sprintDto.getRate_ce_score_origin()==-1){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"数据不完整，下次定时任务会继续拉取！");
				return;
			}
			if(sprintDto.getLmt_score() == -1 && sprintDto.getIpd_score() == -1){
				log.error("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"lmt或者ipd分数为空，请联系相关人员填写");
				return;
			}
			
			float lmtOrIpdScore = sprintDto.getLmt_score() != -1 ? sprintDto.getLmt_score() : sprintDto.getIpd_score();
			/*
			 * 补丁发布率手动更新时需要分情况
			 * 1. 更新时间在sprint结束日期之前：补丁发布率按照实时的分数进行统计
			 * 2. 更新时间在sprint结束日期之后：补丁发布率按照Sprint结束日期进行统计
			 * */
			float rate_patch = -1;
			//if((today.getTime()-builddate.getTime())<0){
			if((today.getTime()-endDate.getTime())<0){
				//情况1
				rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
			}else{
				//情况2
				rate_patch = redminePatch.getPatchRateWithDate(project_name_rm, SysUtil.formatDate(enddDate));
			}
			
			if(rate_patch == -1){
				log.error("项目名为:"+project_name+"的项目补丁发布率不存在，请联系相关人员设置！");
				return;
			}
				
			if(projectService.updateProjectRatePatchById(sprintDto.getProject_id(),rate_patch)){
				log.info("项目名为："+project_name+"的项目补丁发布率更新成功！");
			}
			log.info("各项得分:ipd/lmt:"+lmtOrIpdScore+" sonar："+
					+ sprintDto.getSonar_score()+" test_pass"+
					+ sprintDto.getTest_pass_score() +" tc_exec"+
					+ sprintDto.getTc_exec_score() +" bug_new"+
					+ sprintDto.getBug_new_score() 
					+" bug_reopen"+
					+ sprintDto.getBug_reopen_score()+" bug_escape："+
					+ sprintDto.getBug_escape_score()
					+" rate_patch score："+redminePatch.rateToScore(rate_patch)+" rate_support："+sprintDto.getRate_support_score()+ " rate_Ce："+sprintDto.getRate_ce_score());
			WeightDto weightDto=weightService.getWeights();
			float total=getTotalWeight(weightDto);
			//按权重比例计算sprint总分
			float sprint_score = lmtOrIpdScore*weightDto.getIpdOrLmt_rate()/total 
			+ sprintDto.getSonar_score()*weightDto.getSonar_rate()/total
			+ sprintDto.getTest_pass_score()*weightDto.getTest_pass_rate()/total 
			+ sprintDto.getTc_exec_score()*weightDto.getTc_exec_rate()/total 
			+ sprintDto.getBug_new_score()*weightDto.getBug_new_rate()/total 
			+ sprintDto.getBug_reopen_score()*weightDto.getBug_reopen_rate()/total 
			+ sprintDto.getBug_escape_score()*weightDto.getBug_escape_rate()/total
			+ redminePatch.rateToScore(rate_patch)*weightDto.getRate_patch_rate()/total//补丁发布率，根据项目来
			+ sprintDto.getRate_support_score()*weightDto.getRate_support_rate()/total
			+ sprintDto.getRate_ce_score()*weightDto.getRate_ce_rate()/total;
			
			//sprint_score = sprint_score/SysUtil.score_item_num;
			sprint_score = ((float)Math.round(sprint_score*100))/100;
			sprintService.updateSprintHistoryScore(sprintDto.getSprint_id(),sprint_score,rank_id);
			sprintDto.setSprint_score((float)(Math.round(sprint_score*100))/100);
			log.info("project_name为"+project_name+" SprintName为"+sprintDto.getSprint_name()+"的数据拉取已经完成！分数更新为："+sprint_score);
			
			projectService.updateProjectHistoryAvgScoreById(sprintDto.getProject_id(),rank_id);//updateProjectAvgScore(sprintDto.getProject_id());
	}	
	
	
	
	public void updateSonarScore(SprintDto sprintDto) throws ArrayIndexOutOfBoundsException
	{
		String sprintBuildSonar=sprintDto.getBuild_sonar();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date today = calendar.getTime();
		
		String[] arrBuild = sprintBuildSonar.split("<br>");
		int buildCount=arrBuild.length;
		String[] arrBuildName=new String[buildCount];
		String[] arrBuildDate =new String[buildCount];
		for(int i=0;i<buildCount;i++)
		{
			String[] nameDate=arrBuild[i].split("=");
			arrBuildName[i]=nameDate[0].trim();
			arrBuildDate[i]=nameDate[1].trim();
		}
		float totalSonarScore = 0.0f;
		float avgSonarScore = 0.0f;
		
		//=====
		List<Long> lstCodeLine=new ArrayList<Long>();
		List<Float> lstOriginScore=new ArrayList<Float>();
		//=====
		
		for(int i=0;i<buildCount;i++)
		{
			float eachScore = sonarHandler.getSonarScoreOrigin(arrBuildName[i],arrBuildDate[i]);
			Long  codeLine =sonarHandler.getCodeLine(arrBuildName[i],arrBuildDate[i]);
			if(eachScore == -1 || codeLine==-1)
			{
				return;
			}
			else
			{
				lstOriginScore.add(eachScore);
				lstCodeLine.add(codeLine);
			}
		}
		
		Long totalCodeLine=0L;
		for(int i=0;i<lstCodeLine.size();i++)
		{
			totalCodeLine+=lstCodeLine.get(i);
		}
		for(int i=0;i<lstOriginScore.size();i++)
		{
			avgSonarScore+=lstOriginScore.get(i)/(lstCodeLine.get(i)/totalCodeLine);
		}
		
		sprintService.updateScore_item_score_origin(sprintDto.getSprint_id(), avgSonarScore, SysUtil.sonar);
		sprintDto.setSonar_score_origin(avgSonarScore);
		float avgStandScore = sonarHandler.getSonarScore(avgSonarScore);
		sprintService.updateScore_item_score(sprintDto.getSprint_id(), avgStandScore, SysUtil.sonar);
		sprintDto.setSonar_score(avgStandScore);
	}
	
	/***
	 * 测试拉取数据方法
	 */
	public void test() {
		//surveyMonkeyHandler.getSurveyMonkeyScore("https://www.surveymonkey.com/MySurvey_Responses.aspx?sm=Em77SWc3_2B94lBCjVhg7NwgSAatHHXGtVsPMKMG_2B4Y_2BE_3D");
		//sonarHandler.getSonarScore("baoweb2 Maven Webapp");
		//System.out.println(testlinkHandler.getPassScore("SCADA2.0", "SCADA2.0.1_SIT"));
		//System.out.println(redminePatch.getPatchScore("SCADA v2", "v2.2"));
	}
}
