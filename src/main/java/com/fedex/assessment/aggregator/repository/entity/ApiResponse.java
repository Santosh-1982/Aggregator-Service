package com.fedex.assessment.aggregator.repository.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "API_RESPONSE")
@IdClass(ApiResponseDataId.class)
public class ApiResponse implements Serializable {

	@Id
	private UUID uuid;
	@Id
	private String reqParam;
	
	@Column(name = "req_type")
	private String reqType;
	

	@Column(name = "created_on")
	private Timestamp createdOn;

	@Column(name = "res_param")
	private String resParam;

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(String reqType, String reqParam, Timestamp createdOn, String resParam) {
		super();
		this.reqType = reqType;
		this.reqParam = reqParam;
		this.createdOn = createdOn;
		this.resParam = resParam;
	}

	@Override
	public String toString() {
		return "ApiResponse [uuid=" + uuid + ", reqType=" + reqType + ", reqParam=" + reqParam + ", createdOn="
				+ createdOn + ", resParam=" + resParam + "]";
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqParam() {
		return reqParam;
	}

	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getResParam() {
		return resParam;
	}

	public void setResParam(String resParam) {
		this.resParam = resParam;
	}

}
