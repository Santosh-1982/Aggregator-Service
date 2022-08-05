package com.fedex.assessment.aggregator.repository.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "RESPONSE_DATA")

public class ResponseData implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	@Column(name = "req_type")
	private String reqType;
	@Column(name = "req_param")
	private String reqParam;
	@Column(name = "created_on")
	private Timestamp createdOn;
	
	@Column(name = "res_param")
	private String resParam;

	public ResponseData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseData(String reqType, String reqParam, Timestamp createdOn, String resParam) {
		super();
		this.reqType = reqType;
		this.reqParam = reqParam;
		this.createdOn = createdOn;
		this.resParam = resParam;
	}

	@Override
	public String toString() {
		return "ResponseData [reqType=" + reqType + ", reqParam=" + reqParam + ", createdOn=" + createdOn
				+ ", resParam=" + resParam + "]";
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
