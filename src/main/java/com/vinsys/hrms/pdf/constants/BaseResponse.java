package com.vinsys.hrms.pdf.constants;
import org.apache.commons.lang3.ObjectUtils;

import com.vinsys.hrms.constants.EResponse;

public class BaseResponse<T> {
	
		private T responseBody;

		private int responseCode;

		private String responseStatus;

		private long totalRecords;

		public static <T> BaseResponse<T> failure(T body) {
			BaseResponse<T> failureResponse = new BaseResponse<>();
			failureResponse.setResponseStatus(EResponse.FAILED.getMessage());
			failureResponse.setResponseCode(EResponse.FAILED.getCode());
			failureResponse.setResponseBody(body);
			return failureResponse;
		}

		public static <T> BaseResponse<T> failure(T body, String message) {
			BaseResponse<T> failureResponse = new BaseResponse<>();
			failureResponse.setResponseStatus(ObjectUtils.isNotEmpty(message) ? message : EResponse.FAILED.getMessage());
			failureResponse.setResponseCode(EResponse.FAILED.getCode());
			failureResponse.setResponseBody(body);
			return failureResponse;
		}
		public static <T> BaseResponse<T> failure(T body, String message,int code) {
			BaseResponse<T> failureResponse = new BaseResponse<>();
			failureResponse.setResponseStatus(message);
			failureResponse.setResponseCode(code);
			failureResponse.setResponseBody(body);
			return failureResponse;
		}

		public static <T> BaseResponse<T> success(T body) {
			BaseResponse<T> successResponse = new BaseResponse<>();
			successResponse.setResponseStatus(EResponse.SUCCESS.getMessage());
			successResponse.setResponseCode(EResponse.SUCCESS.getCode());
			successResponse.setResponseBody(body);
			return successResponse;
		}

		public static <T> BaseResponse<T> success(T body, long totalRecords) {
			BaseResponse<T> successResponse = new BaseResponse<>();
			successResponse.setResponseStatus(EResponse.SUCCESS.getMessage());
			successResponse.setResponseCode(EResponse.SUCCESS.getCode());
			successResponse.setResponseBody(body);
			successResponse.setTotalRecords(totalRecords);
			return successResponse;
		}

		public T getResponseBody() {
			return responseBody;
		}

		public void setResponseBody(T responseBody) {
			this.responseBody = responseBody;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}

		public String getResponseStatus() {
			return responseStatus;
		}

		public void setResponseStatus(String responseStatus) {
			this.responseStatus = responseStatus;
		}

		public long getTotalRecords() {
			return totalRecords;
		}

		public void setTotalRecords(long totalRecords) {
			this.totalRecords = totalRecords;
		}

	}

