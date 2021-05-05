package com.fplusf.message.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fplusf.message.controller.BaseController;
import com.fplusf.message.dto.MessageResponseDto;
import com.fplusf.message.exception.ValidateException;
import com.fplusf.message.util.Constant;

@ControllerAdvice(basePackages={"com.fplusf.message.controller"})
public class BaseController {
	
	private static Logger LOGGER = LogManager.getLogger(BaseController.class);
	
	
	  @Autowired
	  private MessageSource messageSource;
	  String defaultMessage = "Error message.";
	
	  @ExceptionHandler({Exception.class})
	  @ResponseBody
	  public MessageResponseDto generalException(HttpServletRequest request, HttpServletResponse response, Exception ex)
	  {
		  response.setContentType("application/json;charset=UTF-8");
		    if ((ex instanceof ValidateException)) {
		      return resolverException(request, response, (ValidateException)ex);
		    }
		
		return resolverException(request, response, ex);
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
	
	 public MessageResponseDto resolverException(HttpServletRequest request, HttpServletResponse response, ValidateException ex)
	  {
		 MessageResponseDto resultBody =  new MessageResponseDto();
		  String ticket = request.getHeader("REQUEST_ID");
	    if ((ex.getErrors() != null) && (ex.getErrors().size() > 0)) {
	    	String codigos="";
	    	String mensajes="";
	      for (Map<String, Object> error : ex.getErrors()) {
	    	String codigoError=(String)error.get("codeError");
	    	if(Constant.ERROR_CODE.NO_CONTENT.equals(codigoError)){
	    		addError(response, codigoError, (String)error.get("message"), 204, new Object[0]);
	    	}else{
	    		codigos+=codigoError+"|";
	    		mensajes+=(String)error.get("message")+"|";    		
	    	}        
	      }
	      if(!codigos.equals("")){
	    	  addError(response, codigos.substring(0,codigos.length()-1), mensajes.substring(0,mensajes.length()-1), 409, new Object[0]);
	      }
	    } else if (Constant.ERROR_CODE.NO_CONTENT.equals(ex.getMessageCode())) {
	    	LOGGER.error("{}| A004 - BEGIN", new Object[] { ticket });
	  	  	LOGGER.error("{}|",ex.getMessage(), ex, new Object[] { ticket });
	  	  	LOGGER.error(ticket, ex);
	  	  	LOGGER.error("{}| A004 - END", new Object[] { ticket });
	      addError(response, ex.getMessageCode(),Constant.ERROR_MESSAGE.NO_CONTENT, 204, new Object[0]);
	      resultBody.setCode(Constant.ERROR_CODE.NO_CONTENT);
	      resultBody.setMessage(Constant.ERROR_MESSAGE.NO_CONTENT);
	      return resultBody;
	    } else if (Constant.ERROR_CODE.REQUIRED_FIELDS.equals(ex.getMessageCode()) || Constant.ERROR_CODE.BAD_REQUEST.equals(ex.getMessageCode())) {
	    	LOGGER.error("{}| - BEGIN", new Object[] { ticket });
	  	  	LOGGER.error("{}|",ex.getMessage(), ex, new Object[] { ticket });
	  	  	LOGGER.error(ticket, ex);
	  	  	LOGGER.error("{}| - END", new Object[] { ticket });
	      addError(response, ex.getMessageCode(),ex.getMessage(), 400, new Object[0]);
	      resultBody.setCode(ex.getMessageCode());
	      resultBody.setMessage(ex.getMessage());
	      return resultBody;
	    }   else {
	    	LOGGER.error("{}| else - BEGIN", new Object[] { ticket });
	  	  	LOGGER.error(ticket +"|"+ex.getMessageCode()+" "+ex.getMessage(), ex);
	  	  	LOGGER.error(ticket, ex);
	  	  	LOGGER.error("{}| else - END", new Object[] { ticket });
	      addError(response, ex.getMessageCode(), 409, ex.getArgs());
	      resultBody.setCode(ex.getMessageCode());
	      resultBody.setMessage(ex.getMessage());
	      return resultBody;
	    }
	    return null;
	  }
	 
	 public void addError(HttpServletResponse response, String code, int status, Object... parametro)
	  {
	    addError(response, code, null, status, parametro);
	  }
	 
	
	  public MessageResponseDto resolverException(HttpServletRequest request, HttpServletResponse response, Exception ex)
	  {
		String ticket = request.getHeader("REQUEST_ID");
		LOGGER.error("{}| ERROR NO CONTROLADO - BEGIN", new Object[] { ticket });
	    LOGGER.error(ticket, ex);
	    LOGGER.error("{}| ERROR NO CONTROLADO - END", new Object[] { ticket });
	    String codigo = null;
	    String mensaje = null;
		    codigo = "technicalError";
	    if ((ex instanceof NullPointerException)) {
	      mensaje = "Error nulo";
	    } else {
	      mensaje = ex.getMessage();
	    }
	    addError(response, codigo, mensaje, 500, new Object[0]);
	    
	    MessageResponseDto resultBody =  new MessageResponseDto();
	    resultBody.setCode(codigo);
	    resultBody.setMessage(mensaje);
	    return resultBody;
	  }
	  
	  public void addError(HttpServletResponse response, String code, String mensaje, int status, Object... parametro)
	  {
	    if (mensaje == null) {
	      mensaje = this.messageSource.getMessage(code, parametro, this.defaultMessage, Locale.getDefault());
	    }
	    response.addHeader("errorCode", code);
	    response.addHeader("errorMessage", mensaje);
	    response.setStatus(status);
	  }
	  
	  private Map<String,String> responseException(String message,String code){
			
			Map<String,String>  respuesta = new HashMap<String, String>();
			respuesta.put("errorMessage", message);
			respuesta.put("errorCode", code);
			return respuesta;
		}

}
