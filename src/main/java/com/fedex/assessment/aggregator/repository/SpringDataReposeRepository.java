package com.fedex.assessment.aggregator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fedex.assessment.aggregator.repository.entity.ResponseData;

@Repository
public interface SpringDataReposeRepository extends JpaRepository<ResponseData, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM RESPONSE_DATA WHERE req_type = ?1 AND req_param=?2 order by created_on desc")
	public List<ResponseData> findRespose(String reqType, String reqParam);

	@Query(nativeQuery = true, value = "DELETE FROM RESPONSE_DATA WHERE Created_on<CURRENT_DATE")
	public List<ResponseData> deleteRespose(String reqType, String reqParam);

}
