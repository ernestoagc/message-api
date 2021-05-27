package com.fplusf.message.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MESSAGE")
public class Message implements Serializable{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="IDENTIFICADOR")
	private Long id;	

	@Column(name="MESSAGE_TO")
	private String to;
	
	@Column(name="MESSAGE_FROM")
	private String from;
	
	@Column(name="MESSAGE_SUBJECT")
	private String subject;
	
	@Column(name="MESSAGE_BODY")
	private String body;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", to=" + to + ", from=" + from + ", subject=" + subject + ", body=" + body + "]";
	}
	
	
	
	
	
}
