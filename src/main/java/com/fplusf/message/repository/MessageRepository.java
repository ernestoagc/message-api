package com.fplusf.message.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.fplusf.message.model.Message;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{

}
