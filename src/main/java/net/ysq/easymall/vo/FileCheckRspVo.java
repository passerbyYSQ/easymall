package net.ysq.easymall.vo;

/**
 * @author	passerbyYSQ
 * @date	2020-11-13 17:47:03
 */
public class FileCheckRspVo {
	
	private String uploadToken;
	
	private long uploadedBytes;

	public String getUploadToken() {
		return uploadToken;
	}

	public void setUploadToken(String uploadToken) {
		this.uploadToken = uploadToken;
	}

	public long getUploadedBytes() {
		return uploadedBytes;
	}

	public void setUploadedBytes(long uploadedBytes) {
		this.uploadedBytes = uploadedBytes;
	}

}
