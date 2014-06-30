package com.env.qualitymetrics.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dto.SurveyMonkeyQuestionDto;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class SurveyMonkeyHandler {
	String accessToken;
	String apiKey;
	private static final Logger log = LoggerFactory.getLogger(SonarHandler.class);
	public float getSurveyMonkeyScore(String title){ 
		/*
		 * 根据accessToken去SurveyMonkey调用API获取返回内容进行解析即可ps:accessToken 请根据自己的开发key和surveyMonkey在API Console
		 *  参考：https://developer.surveymonkey.com/api_console 
		 * 1. 获取survey_name对应的survey_id, 调用get_survey_list
		 * 2. 获取survey_id对应的回答者，调用get_respondent_list
		 * 3. 根据回答者的id获取回答内容，进行解析。
		 * */
//		String survey_id = getSurveyIdByAnalyzeUrl(analyseUrl);
		String survey_id = getSurveyIdByTitle(title);
		if(survey_id == null){
			log.error("无法找到title为"+title+"对应的suvery，请联系相关人员填写名称！");
			return -1;
		}
		List<String> respondent_list = getRespondentList(survey_id);
		if(respondent_list.size() ==0){
			//没有人回答问卷，为0分====================
			log.info("暂时还没有人回答此问卷");
			return 0;
		}
		List<SurveyMonkeyQuestionDto> questionList = getSuveryDetailAnswers(survey_id);
		Map<String, SurveyMonkeyQuestionDto> questionsMap = new HashMap<String, SurveyMonkeyQuestionDto>();
		for(int i=0; i<questionList.size(); i++){
			questionsMap.put(questionList.get(i).getQuestion_id(), questionList.get(i));
		}
		List<Float> respondent_score_list  = getRespondentScoreList(survey_id, respondent_list , questionsMap);
		float totalScore = 0;
		for(int j=0; j<respondent_score_list.size(); j++){
			totalScore +=respondent_score_list.get(j);
		}
		
		float score = totalScore/respondent_score_list.size();
		score = (float)(Math.round(score*10))/10;
		System.out.print(score);
		return score;
	}
	
	/**
	 * 根据suvery_id获取Survey详情中的问题内容
	 * @param survey_id
	 * @return
	 */
	private List<SurveyMonkeyQuestionDto> getSuveryDetailAnswers(String survey_id) {
		List<SurveyMonkeyQuestionDto> questionList = new ArrayList<SurveyMonkeyQuestionDto>();
		HttpURLConnection connection;
		try {
			connection = getConnection("get_survey_details");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("survey_id", survey_id);
			String responseString = getResponse(writer,jsonParam,connection);
			JSONArray resultArray = JSONArray.fromObject("["+responseString+"]");
			JSONObject object = (JSONObject) resultArray.get(0);
			JSONArray pages =  object.getJSONArray("pages");
			//可能有若干页
			for(int i = 0; i < pages.size(); i++){
				JSONObject pageObejct = pages.getJSONObject(i);
				JSONArray questions = pageObejct.getJSONArray("questions");
				//每一页有若干个问题
				for(int j = 0; j < questions.size(); j++){
					JSONObject quesionObject = questions.getJSONObject(j);
					JSONArray answers = quesionObject.getJSONArray("answers");
					SurveyMonkeyQuestionDto questionDto = new SurveyMonkeyQuestionDto();
					questionDto.setQuestion_id(quesionObject.getString("question_id"));
					Map<String,String> answersMap = new HashMap<String,String>();
					for(int k = 0; k < answers.size(); k++){
						JSONObject answerObject = answers.getJSONObject(k);
						answersMap.put(answerObject.getString("answer_id"),answerObject.getString("text"));
					}
					questionDto.setAnswers(answersMap);
					questionList.add(questionDto);
				}
			}
		} catch (IOException e) {
			log.error("根据suvery_id获取Survey详情中的问题内容出错！异常信息： "+e.getMessage());
		}

		return questionList;
	}

	/***
	 * 获取respondent_list中每一个人的回答分数
	 * @param respondent_list
	 * @param questionsMap 
	 * @param questionList 
	 * @return
	 */
	private List<Float> getRespondentScoreList(String survey_id, List<String> respondent_list, Map<String, SurveyMonkeyQuestionDto> questionsMap) {
		List<Float> respondentScoreList = new ArrayList<Float>();
		//对每一个回答者的回答进行解析	
		try {
			HttpURLConnection connection = getConnection("get_responses");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			JSONObject jsonParam = new JSONObject();
			JSONArray respondentArray = new JSONArray();
			jsonParam.put("survey_id", survey_id);
			for(int i = 0;i < respondent_list.size(); i ++){
				respondentArray.add(respondent_list.get(i));
			}
			jsonParam.put("respondent_ids",respondentArray);
			String responseString = getResponse(writer,jsonParam,connection);
			JSONArray responseArray = JSONArray.fromObject(responseString);
			//对不同的回答者进行回答解析
			for(int i = 0; i < responseArray.size(); i++){
				JSONObject responseObject = responseArray.getJSONObject(i);
				JSONArray questionArray = responseObject.getJSONArray("questions");
				float score = 0;
				//有效计分题目数
				int validQC = 0;
				//统计回答者的回答
				Map<String,String> responseQuestions = new HashMap<String,String>();
				for(int j = 0; j < questionArray.size(); j++){
					JSONObject questionObject = questionArray.getJSONObject(j);
					String question_id = questionObject.getString("question_id");
					JSONArray answerArray = questionObject.getJSONArray("answers");
					//单选，答案只有一个。
					JSONObject answerObject = answerArray.getJSONObject(0);
					String answer_id = answerObject.getString("row");
					responseQuestions.put(question_id, answer_id);
				}
				//解析回答者的回答，如果存在有些有效问题没有回答的，记为平均分
				Iterator iterator = questionsMap.keySet().iterator();
				while(iterator.hasNext()){
					String survery_question_id = (String) iterator.next();
					if(responseQuestions.containsKey(survery_question_id)){
						int answerScore = getAnswerScore(survery_question_id,responseQuestions.get(survery_question_id),questionsMap);
						if(answerScore!= -1){
							score += answerScore;
							validQC ++;
						}
					}else{
						SurveyMonkeyQuestionDto questionDto = questionsMap.get(survery_question_id);
						Map<String,String> answerMap = questionDto.getAnswers();
						String testKey = "非常满意";
						if(answerMap.containsValue(testKey)){
							//此题为有效题目，但是用户未作答，记为0分。=====================================================
							score += 0;
							validQC ++;
						}else{
							//此题为无效题目，不计分，不计入统计
						}
					}
				}
				
				
				float avgScore = -1;
				if(validQC == 0){
					avgScore = -1;
					log.error("没有符合统计规范的题目，无法统计结果！");
				}else{
					avgScore = (score/validQC);
					avgScore = (float)(Math.round(avgScore*10))/10;
				}
				respondentScoreList.add(avgScore);
			}
		} catch (Exception e) {
			log.error("获取回答者的回答出错，异常信息："+ e.getMessage());
		}
		return respondentScoreList;
	}

	/**
	 * 根据question id和answer_id计算分数
	 * @param question_id
	 * @param answer_id
	 * @param questionsMap 
	 * @return
	 */
	private int getAnswerScore(String question_id, String answer_id, Map<String, SurveyMonkeyQuestionDto> questionsMap) {
		SurveyMonkeyQuestionDto questionDto = questionsMap.get(question_id);
		if(questionDto == null){
			log.error("无法找到question_id为"+question_id+" 对应的question！");
			return -1;
		}
		Map<String,String> answerMap = questionDto.getAnswers();
		String testKey = "非常满意";
		if(answerMap.containsValue(testKey)){
			//System.out.println("包含非常满意，此题计入统计！");
		}else{
			//System.out.println("不包含非常满意，此题不计入统计！");
			return -1;
		}
		String text = answerMap.get(answer_id);
		if(text == null){
			log.error("无法找到answer_id对应的text");
			return -1;
		}
		if(text.equals("非常满意")){
			return 5;
		}
		if(text.equals("满意")){
			return 4;
		}
		if(text.equals("一般")){
			return 3;
		}
		if(text.equals("不满意")){
			return 2;
		}
		if(text.equals("非常不满意")){
			return 1;
		}
		return 0;
	}

	/***
	 * 根据survey_id获取所有回复者的id
	 * @param survey_id
	 * @return
	 */
	private List<String> getRespondentList(String survey_id) {
		List<String> respondentList = new ArrayList<String>();
		try {
			HttpURLConnection connection = getConnection("get_respondent_list");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("survey_id", survey_id);
			String responseString = getResponse(writer,jsonParam,connection);
			JSONArray resultArray = JSONArray.fromObject("["+responseString+"]");
			JSONObject jsonObject = (JSONObject) resultArray.get(0);
//			System.out.println(jsonObject.getString("respondents"));
			
			JSONArray respondentArray =  JSONArray.fromObject(jsonObject.getString("respondents"));
			Iterator iterator = respondentArray.iterator();
			while(iterator.hasNext()){
				respondentList.add((String)((JSONObject)iterator.next()).get("respondent_id"));
			}
		} catch (IOException e) {
			log.error("获取survey_id对应的respondent list 出现异常，异常信息： "+e.getMessage());
		}
		return respondentList;
	}

	/***
	 * 根据analyse_url获取survey_id
	 * @param survey_name
	 * @return
	 */
	public String getSurveyIdByAnalyzeUrl(String analyseUrl) {
		String survey_id = null;
		if(analyseUrl.length()<72){
			return null;
		}
		analyseUrl = analyseUrl.toLowerCase().substring(56,72);
		analyseUrl = analyseUrl.replace('_', '%');
		try {
			HttpURLConnection connection = getConnection("get_survey_list");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			JSONObject jsonParam = new JSONObject();
			JSONArray fieldsArray = new JSONArray();
			fieldsArray.add("analysis_url");
			jsonParam.put("fields", fieldsArray);
			String responseString = getResponse(writer,jsonParam,connection);
			JSONArray resultArray = JSONArray.fromObject("["+responseString+"]");
			JSONObject jsonObject = (JSONObject) resultArray.get(0);
			JSONArray surveysArray = jsonObject.getJSONArray("surveys");
			for(int i=0; i < surveysArray.size(); i++){
				JSONObject surveyObject = surveysArray.getJSONObject(i);
				if(surveyObject.getString("analysis_url").toLowerCase().contains(analyseUrl)){
					survey_id = surveyObject.getString("survey_id");
					break;
				}
			}
			writer.close();
		} catch (Exception e) {
			log.error("获取survey_id出现异常，请检查suvey名称是否存在！异常信息： "+e.getMessage());
		} 
		return survey_id;
	}

	public String getSurveyIdByTitle(String title) {
		String survey_id = null;
		try {
			HttpURLConnection connection = getConnection("get_survey_list");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			JSONObject jsonParam = new JSONObject();
			JSONArray fieldsArray = new JSONArray();
			fieldsArray.add("title");
			jsonParam.put("fields", fieldsArray);
			String responseString = getResponse(writer,jsonParam,connection);
			JSONArray resultArray = JSONArray.fromObject("["+responseString+"]");
			JSONObject jsonObject = (JSONObject) resultArray.get(0);
			JSONArray surveysArray = jsonObject.getJSONArray("surveys");
			for(int i=0; i < surveysArray.size(); i++){
				JSONObject surveyObject = surveysArray.getJSONObject(i);
				if(surveyObject.getString("title").equals(title)){
					survey_id = surveyObject.getString("survey_id");
					break;
				}
			}
			writer.close();
		} catch (Exception e) {
			log.error("获取survey_id出现异常，请检查suvey名称是否存在！异常信息： "+e.getMessage());
		} 
		return survey_id;
	}
	
	//=====================zhangdi 140421============
	
	public List<String> getSurveyMonkeyTitles()
	{
		final List<String> lstTitle=new ArrayList<String>();
		try 
		{
			HttpURLConnection connection = getConnection("get_survey_list");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			JSONObject jsonParam = new JSONObject();
			JSONArray fieldsArray = new JSONArray();
			fieldsArray.add("title");
			jsonParam.put("fields", fieldsArray);
			String responseString = getResponse(writer,jsonParam,connection);
			JSONArray resultArray = JSONArray.fromObject("["+responseString+"]");
			JSONObject jsonObject = (JSONObject) resultArray.get(0);
			JSONArray surveysArray = jsonObject.getJSONArray("surveys");
			for(int i=0; i < surveysArray.size(); i++)
			{
				JSONObject surveyObject = surveysArray.getJSONObject(i);
				lstTitle.add(surveyObject.getString("title"));
				
			} 
			writer.close();
		} 
		catch (Exception e) {
			log.error("获取survey_id出现异常，请检查suvey名称是否存在！异常信息： "+e.getMessage());
		} 
		return lstTitle;
	}
	
	//===============================================
	
	
	private String getResponse(OutputStreamWriter writer, JSONObject jsonParam,
			HttpURLConnection connection) throws IOException {
		writer.write(jsonParam.toString());
		writer.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuilder result = new StringBuilder();
		while ((line = reader.readLine()) != null) {
		    	result.append(line);
		}
		reader.close();
		return result.toString().substring(result.toString().indexOf("data")+6, result.toString().length()-1);
	}


	private HttpURLConnection getConnection(String requestMethod) throws IOException {
		URL postUrl = new URL("https://api.surveymonkey.net/v2/surveys/"+requestMethod+"?api_key="+apiKey+""); 
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty ("Authorization", "bearer " + accessToken);
		connection.setRequestProperty("Content-Type","application/json");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		return connection;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}


//	public static void main(String args[]){
//		SurveyMonkeyHandler sm = new SurveyMonkeyHandler();
//		sm.getSurveyMonkeyScore("https://www.surveymonkey.com/MySurvey_Responses.aspx?sm=Em77SWc3_2B94lBCjVhg7NwgSAatHHXGtVsPMKMG_2B4Y_2BE_3D");
//	}
}	
