package com.fplusf.message.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fplusf.message.model.Message;
import com.fplusf.message.service.RuleService;
import com.fplusf.message.service.impl.RuleServiceImpl;

@Import({
	RuleServiceImpl.class
})

@WebMvcTest(MessageController.class)
class MessageControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	
	@MockBean
	RuleService ruleService;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	void testValidateMandotaryField() throws Exception {
		
		ObjectMapper objectMapper = getObjectMapper();	
		
		Message messageDb=	objectMapper.readValue("{\r\n" + 
				"    \"id\": 10,\r\n" + 
				"    \"to\": \":user2@domain.net,user@domain.com\",\r\n" + 
				"    \"from\": \":another_origin@somewhere\",\r\n" + 
				"    \"subject\": \":Re: Re: Saying hi\",\r\n" + 
				"    \"body\": \":Please don't do that just yet!\"\r\n" + 
				"}",Message.class); 
		
		Mockito.when(ruleService.saveMessage(new Message())).thenReturn(messageDb);		
		String uri="/message/send";
		mockMvc.perform(post(uri) .contentType(APPLICATION_JSON_UTF8).
				content("{\"to\":\"user2@domain.net,user@domain.com\"}")).andExpect(status().isBadRequest());
		
	}
	
	@Test
	void testSaveSuccess() throws Exception {
		ObjectMapper objectMapper = getObjectMapper();	
		
		Message messageDb=	objectMapper.readValue("{\r\n" + 
				"    \"id\": 10,\r\n" + 
				"    \"to\": \":user2@domain.net,user@domain.com\",\r\n" + 
				"    \"from\": \":another_origin@somewhere\",\r\n" + 
				"    \"subject\": \":Re: Re: Saying hi\",\r\n" + 
				"    \"body\": \":Please don't do that just yet!\"\r\n" + 
				"}",Message.class); 
		
		Mockito.when(ruleService.saveMessage(new Message())).thenReturn(messageDb);		
		String uri="/message/send";
		mockMvc.perform(post(uri) .contentType(APPLICATION_JSON_UTF8).
				content("{\r\n" + 
						"    \"to\": \":user2@domain.net,user@domain.com\",\r\n" + 
						"    \"from\": \":another_origin@somewhere\",\r\n" + 
						"    \"subject\": \":Re: Re: Saying hi\",\r\n" + 
						"    \"body\": \":Please don't do that just yet!\"\r\n" + 
						"}")).andExpect(status().isOk());
	
	}
	
	protected ObjectMapper getObjectMapper()
	  {

		ObjectMapper objectMapper = new ObjectMapper();
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	    objectMapper.setDateFormat(df);
	    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    objectMapper.setSerializationInclusion(Include.NON_NULL);
	    return objectMapper;
	  }

}
