package com.fplusf.message.controller;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fplusf.message.exception.ValidateException;
import com.fplusf.message.util.Constant;
import com.fplusf.message.controller.BaseController;
import com.fplusf.message.model.Message;
import com.fplusf.message.service.RuleService;
 
@CrossOrigin(origins="*")
@RestController
@RequestMapping(value="message")
public class MessageController extends BaseController{
	
	
	@Autowired
	RuleService ruleServive;
	

	Logger logger = LoggerFactory.getLogger(MessageController.class);

	@PostMapping("/file")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
		logger.info("===>Start file");
		if(!file.isEmpty()) {
			
			String fileName= UUID.randomUUID().toString()+"_"+ file.getOriginalFilename();				
			Path filePath = Paths.get("uploads").resolve(fileName).toAbsolutePath();
			
			try {
				Files.copy(file.getInputStream(), filePath);
				String pathString = filePath.toUri().getPath();

				logger.info("===>pathString: "+pathString);
				Message messageResult =	ruleServive.saveMessageFromFile(pathString);
				
				logger.info("===>End file");
				return ResponseEntity.status(HttpStatus.OK).body(messageResult);
				
			} catch (Exception e) {				 	
					throw new ValidateException(Constant.ERROR_CODE.BAD_REQUEST,Constant.ERROR_MESSAGE.BAD_REQUEST_FILE); 
			}
			
		}
		
		throw new ValidateException(Constant.ERROR_CODE.BAD_REQUEST,Constant.ERROR_MESSAGE.BAD_REQUEST); 
		
	}
	
	
	@PostMapping("/send")
	public ResponseEntity<?> saveForm(@RequestBody Message message){
		logger.info("===>Start send");
		logger.info("===>messageObject:" +message);
		
		
		if( StringUtils.isEmpty(message.getTo()))  {			
			throw new ValidateException(Constant.ERROR_CODE.REQUIRED_FIELDS,MessageFormat.format(Constant.ERROR_MESSAGE.REQUIRED_FIELDS,"to"));
		}
		

		if( StringUtils.isEmpty(message.getFrom()))  {			
			throw new ValidateException(Constant.ERROR_CODE.REQUIRED_FIELDS,MessageFormat.format(Constant.ERROR_MESSAGE.REQUIRED_FIELDS,"from"));
		}
		
		if( StringUtils.isEmpty(message.getSubject()))  {			
			throw new ValidateException(Constant.ERROR_CODE.REQUIRED_FIELDS,MessageFormat.format(Constant.ERROR_MESSAGE.REQUIRED_FIELDS,"subject"));
		}
		
		if( StringUtils.isEmpty(message.getBody()))  {			
			throw new ValidateException(Constant.ERROR_CODE.REQUIRED_FIELDS,MessageFormat.format(Constant.ERROR_MESSAGE.REQUIRED_FIELDS,"body"));
		}
		
		Message messageResult =	ruleServive.saveMessage(message);
		
		logger.info("===>End send");
		return ResponseEntity.status(HttpStatus.OK).body(messageResult);
		
	}
	
	
	@GetMapping("/findall")	
	@ResponseBody
	public ResponseEntity<?> findall(@RequestParam Map<String,String> parametros) throws FileNotFoundException  {

		logger.info("===>Start findall");  		
		List<Message> listMessage =ruleServive.listMessage();
		
		
		logger.info("===>End findall");  		
		return ResponseEntity.status(HttpStatus.OK).body(listMessage);
	}
	

}
