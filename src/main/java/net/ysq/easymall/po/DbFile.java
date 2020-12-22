package net.ysq.easymall.po;

import java.time.LocalDateTime;
import javax.persistence.*;

@Table(name = "db_file")
public class DbFile {
    @Id
    private Integer id;

    /**
     * 新的随即名
     */
    @Column(name = "rand_name")
    private String randName;

    /**
     * 在服务器上的相对路径
     */
    private String path;

    /**
     * 整个文件的md5值，用于唯一标识一个文件
     */
    @Column(name = "file_md5")
    private String fileMd5;

    /**
     * 文件的内容类型
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * 整个文件的总字节数
     */
    @Column(name = "total_bytes")
    private Long totalBytes;

    /**
     * 已上传的字节数
     */
    @Column(name = "uploaded_bytes")
    private Long uploadedBytes;

    /**
     * 下载次数
     */
    @Column(name = "download_count")
    private Integer downloadCount;

    /**
     * 资源状态。0：禁用，1：正常
     */
    private Byte status;

    /**
     * 资源上一次被访问（修改、或下载）的时间
     */
    @Column(name = "last_visit_time")
    private LocalDateTime lastVisitTime;

    /**
     * 创建者的用户id
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取新的随即名
     *
     * @return rand_name - 新的随即名
     */
    public String getRandName() {
        return randName;
    }

    /**
     * 设置新的随即名
     *
     * @param randName 新的随即名
     */
    public void setRandName(String randName) {
        this.randName = randName;
    }

    /**
     * 获取在服务器上的相对路径
     *
     * @return path - 在服务器上的相对路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置在服务器上的相对路径
     *
     * @param path 在服务器上的相对路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取整个文件的md5值，用于唯一标识一个文件
     *
     * @return file_md5 - 整个文件的md5值，用于唯一标识一个文件
     */
    public String getFileMd5() {
        return fileMd5;
    }

    /**
     * 设置整个文件的md5值，用于唯一标识一个文件
     *
     * @param fileMd5 整个文件的md5值，用于唯一标识一个文件
     */
    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    /**
     * 获取文件的内容类型
     *
     * @return content_type - 文件的内容类型
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 设置文件的内容类型
     *
     * @param contentType 文件的内容类型
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 获取整个文件的总字节数
     *
     * @return total_bytes - 整个文件的总字节数
     */
    public Long getTotalBytes() {
        return totalBytes;
    }

    /**
     * 设置整个文件的总字节数
     *
     * @param totalBytes 整个文件的总字节数
     */
    public void setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
    }

    /**
     * 获取已上传的字节数
     *
     * @return uploaded_bytes - 已上传的字节数
     */
    public Long getUploadedBytes() {
        return uploadedBytes;
    }

    /**
     * 设置已上传的字节数
     *
     * @param uploadedBytes 已上传的字节数
     */
    public void setUploadedBytes(Long uploadedBytes) {
        this.uploadedBytes = uploadedBytes;
    }

    /**
     * 获取下载次数
     *
     * @return download_count - 下载次数
     */
    public Integer getDownloadCount() {
        return downloadCount;
    }

    /**
     * 设置下载次数
     *
     * @param downloadCount 下载次数
     */
    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    /**
     * 获取资源状态。0：禁用，1：正常
     *
     * @return status - 资源状态。0：禁用，1：正常
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置资源状态。0：禁用，1：正常
     *
     * @param status 资源状态。0：禁用，1：正常
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取资源上一次被访问（修改、或下载）的时间
     *
     * @return last_visit_time - 资源上一次被访问（修改、或下载）的时间
     */
    public LocalDateTime getLastVisitTime() {
        return lastVisitTime;
    }

    /**
     * 设置资源上一次被访问（修改、或下载）的时间
     *
     * @param lastVisitTime 资源上一次被访问（修改、或下载）的时间
     */
    public void setLastVisitTime(LocalDateTime lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    /**
     * 获取创建者的用户id
     *
     * @return creator_id - 创建者的用户id
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建者的用户id
     *
     * @param creatorId 创建者的用户id
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}