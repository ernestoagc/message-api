package com.fplusf.message.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import com.fplusf.message.model.Message;
import com.fplusf.message.repository.MessageRepository;
import com.fplusf.message.service.RuleService;
import com.fplusf.message.util.Constant;

@Service
public class RuleServiceImpl implements RuleService {
	
	
	Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);
	
	@Autowired
	MessageRepository messageRepository;
	

	@Override
	public String reversal(String text) {
		
		logger.info("===>start reversal");
		char[] letters = new char[text.length()];
		
		int letterIndex = 0;
		
		for(int i= text.length() -1; i>=0;i--)
		{
			letters[letterIndex] = text.charAt(i);
			letterIndex++;
		}
		
		String reverse ="";
		
		for(int i=0; i<text.length();i++) {
			reverse=reverse+letters[i];
		}
		
		logger.info("===>end reversal");
		return reverse;
	}

	@Override
	public String replacement(String text) {
		// TODO Auto-generated method stub
		logger.info("===>start replacement");
		String resultTest ="";
		
		resultTest= text.replace("$", "e").replace("^", "y").replace("&", "u");
		logger.info("===>end replacement");
		return resultTest;
	}

	@Override
	public Message saveMessage(Message message) {
		// TODO Auto-generated method stub
		logger.info("===>start saveMessage");
		boolean setRuleReverse=false;
		boolean setRuleReplace=false;
		
		//Validate in subject to set ruleReverse
		if(!StringUtils.isEmpty(message.getSubject()) &&  message.getSubject().contains(Constant.WORD.SECURE)) {
			setRuleReverse=true;
		}
		
		//Validate in subject to set ruleReplace		
		if(message.getTo().contains(Constant.WORD.DOMAINCOM)) {
			setRuleReplace=true;
		}
		
		//Validate in body to set replace and reverse rule		
		if(!StringUtils.isEmpty( message.getBody())) {
			String[] words =  message.getBody().split(" ");
			
			for(int i=0; i<words.length; i++) {
				
				if(words[i].length()>10) {
					setRuleReverse=true;
					setRuleReplace=true;
				}
			}
		}
		
		String newBodyContent = message.getBody();
		
		if(setRuleReplace) {
			newBodyContent = replacement(newBodyContent);
		}
		
		if(setRuleReverse) {
			newBodyContent = reversal(newBodyContent);
		}
				
		logger.info("===>newBodyContent: "+newBodyContent);
		message.setBody(newBodyContent);
		Message messageResult = messageRepository.save(message);
				
		logger.info("===>end saveMessage");
		return messageResult;
	}
	
	
	public Message saveMessageFromFile(String path) {
		logger.info("===>start saveMessageFromFile");
		Message message = getMessageObject(path);
		Message messageResult = saveMessage(message);
		
		logger.info("===>end saveMessageFromFile");
		return messageResult;
	}
	
	private Message getMessageObject(String path) {
		logger.info("===>start getMessageObject");
		Message resultObject = new Message();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String textLine; 
				while( (textLine = br.readLine()) !=null ) {
					if(!StringUtils.isEmpty(textLine)) {
						
						 if( textLine.toLowerCase().contains(Constant.FIELD.TO)) {
							 resultObject.setTo(textLine.substring(3,textLine.length()));
						 }
						 
						 if( textLine.toLowerCase().contains(Constant.FIELD.FROM)) {
							 resultObject.setFrom(textLine.substring(5,textLine.length()));
						 }
						 
						 if( textLine.toLowerCase().contains(Constant.FIELD.SUBJECT)) {
							 resultObject.setSubject(textLine.substring(8,textLine.length()));
						 }
						 
						 if( textLine.toLowerCase().contains(Constant.FIELD.BODY)) {
							
							 
							 StringBuilder sb = new StringBuilder();
							 sb.append(textLine.substring(5,textLine.length()));
							 
							 while( (textLine = br.readLine()) !=null ) {
								 sb.append(textLine);
							 }
							 
							 resultObject.setBody(sb.toString());
						 }
					}	
				}			
				br.close();
				
				
				
		} catch (Exception e) {
			logger.error("===>error" + e.getMessage());
		}
		logger.info("===>end getMessageObject");
		return resultObject;
	}

	@Override
	public List<Message> listMessage() {
		return (List<Message>) messageRepository.findAll(Sort.by("id").descending());
	}

}
