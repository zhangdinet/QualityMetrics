package com.env.qualitymetrics.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.WeightDao;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.entity.Indicator_weight;

public class WeightDaoImpl implements WeightDao {

	SessionFactory sessionFactory;
	private static final Logger log = LoggerFactory.getLogger(WeightDaoImpl.class); 
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public WeightDto getWeightList() {
		String hql="from Indicator_weight iw";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		List list=query.list();
		Iterator iterator=list.iterator();
		Map<String,Float> weightMap = new HashMap<String,Float>();
		while(iterator.hasNext()){
			Indicator_weight iw=(Indicator_weight)iterator.next();
			weightMap.put(iw.getIndicator_name(), iw.getWeight_rate());
		}
		WeightDto weightDto=new WeightDto();
		if(weightMap.containsKey(SysUtil.lmtOrIpd)){
			weightDto.setIpdOrLmt_rate(weightMap.get(SysUtil.lmtOrIpd));
		}
		if(weightMap.containsKey(SysUtil.sonar)){
			weightDto.setSonar_rate(weightMap.get(SysUtil.sonar));
		}
		if(weightMap.containsKey(SysUtil.test_pass)){
			weightDto.setTest_pass_rate(weightMap.get(SysUtil.test_pass));
		}
		if(weightMap.containsKey(SysUtil.tc_exec)){
			weightDto.setTc_exec_rate(weightMap.get(SysUtil.tc_exec));
		}
		if(weightMap.containsKey(SysUtil.bug_new)){
			weightDto.setBug_new_rate(weightMap.get(SysUtil.bug_new));
		}
		if(weightMap.containsKey(SysUtil.bug_reopen)){
			weightDto.setBug_reopen_rate(weightMap.get(SysUtil.bug_reopen));
		}
		if(weightMap.containsKey(SysUtil.bug_escape)){
			weightDto.setBug_escape_rate(weightMap.get(SysUtil.bug_escape));
		}
		if(weightMap.containsKey(SysUtil.rate_support)){
			weightDto.setRate_support_rate(weightMap.get(SysUtil.rate_support));
		}
		if(weightMap.containsKey(SysUtil.rate_ce)){
			weightDto.setRate_ce_rate(weightMap.get(SysUtil.rate_ce));
		}
		if(weightMap.containsKey(SysUtil.rate_patch)){
			weightDto.setRate_patch_rate(weightMap.get(SysUtil.rate_patch));
		}
		return weightDto;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Override
	public boolean updateWeights(WeightDto weightDto) {
		if(!updateWeightByName(SysUtil.lmtOrIpd,weightDto.getIpdOrLmt_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.sonar,weightDto.getSonar_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.test_pass,weightDto.getTest_pass_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.tc_exec,weightDto.getTc_exec_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.bug_new,weightDto.getBug_new_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.bug_reopen,weightDto.getBug_reopen_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.bug_escape,weightDto.getBug_escape_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.rate_support,weightDto.getRate_support_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.rate_ce,weightDto.getRate_ce_rate()))
			return false;
		
		if(!updateWeightByName(SysUtil.rate_patch,weightDto.getRate_patch_rate()))
			return false;
		return true;
	}
	@Override
	public boolean updateWeightByName(String indicator_name, float weight_rate) {
		try {
			String hql="update Indicator_weight iw set iw.weight_rate=? where iw.indicator_name=?";
			Query query=sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, weight_rate+"");
			query.setString(1, indicator_name);
			query.executeUpdate();
		} catch (HibernateException e) {
			log.error("update indicator weight "+indicator_name+" with "+indicator_name+" error "+e.getMessage());
			return false;
		}
		return true;
	}

}
