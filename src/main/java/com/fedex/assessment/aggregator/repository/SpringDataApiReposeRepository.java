package com.fedex.assessment.aggregator.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fedex.assessment.aggregator.repository.entity.ApiResponse;
import com.fedex.assessment.aggregator.repository.entity.ApiResponseDataId;

@Repository
public interface SpringDataApiReposeRepository extends JpaRepository<ApiResponse, ApiResponseDataId> {

	@Query(nativeQuery = true, value = "SELECT * FROM API_RESPONSE WHERE uuid=?1 and req_param = ?2 and res_param is not null")
	public List<ApiResponse> findRespose(UUID uuid, String reqParam);

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM API_RESPONSE WHERE TIMESTAMPDIFF(MINUTE,CREATED_ON, CURRENT_TIMESTAMP())>10")
	public int deleteApiRespose();

	@Modifying
	@Query(nativeQuery = true, value = "UPDATE API_RESPONSE SET res_param=?1 WHERE  RES_PARAM IS NULL AND req_type=?2 AND req_param=?3 ")
	public int saveRespose(String resParam, String reqType, String reqParam);

	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO API_RESPONSE(uuid,req_type,req_param,created_on) VALUES(?1,?2,?3,?4) ")
	public int createRequest(UUID uuid, String reqType, String reqParam, Timestamp st);

}
