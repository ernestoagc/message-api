package com.fplusf.message.service;

import java.util.List;

import com.fplusf.message.model.Message;

public interface RuleService {
	
	String reversal(String text);
	String replacement(String text);

	Message saveMessage(Message message);
	Message saveMessageFromFile(String path) ;
	
	List<Message> listMessage();
} 
