package com.course.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.InputStream;

public class VodUtil {

    /**
     * Use AK to initialize the VOD client
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     * @throws ClientException
     */
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     *Obtain video upload address and credentials 
     * @param vodClient
     * @return
     * @throws ClientException
     */
    public static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient vodClient, String fileName) throws ClientException {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setFileName(fileName);
        request.setTitle(fileName);
        request.setCateId(1000115308L);
        request.setTemplateGroupId("78fffb8c0c2426efd5baaaafed76fe36");
        request.setSysReadTimeout(1000);
        request.setSysConnectTimeout(1000);
        return vodClient.getAcsResponse(request);
    }

    /**
     * Initialize the OSS client with the upload certificate and address (note that Base64 decoding and Json Decoding are required before passing in)
     * @param uploadAuth
     * @param uploadAddress
     * @return
     */
    public static OSSClient initOssClient(JSONObject uploadAuth, JSONObject uploadAddress) {
        String endpoint = uploadAddress.getString("Endpoint");
        String accessKeyId = uploadAuth.getString("AccessKeyId");
        String accessKeySecret = uploadAuth.getString("AccessKeySecret");
        String securityToken = uploadAuth.getString("SecurityToken");
        return new OSSClient(endpoint, accessKeyId, accessKeySecret, securityToken);
    }

    /**
     * @param ossClient
     * @param uploadAddress
     * @param inputStream
     */
    public static void uploadLocalFile(OSSClient ossClient, JSONObject uploadAddress, InputStream inputStream){
        String bucketName = uploadAddress.getString("Bucket");
        String objectName = uploadAddress.getString("FileName");
        // single file upload
        ossClient.putObject(bucketName, objectName, inputStream);

        ossClient.appendObject(request);*/
    }

    /**
     * @param ossClient
     * @param uploadAddress
     * @param localFile
     */
    public static void uploadLocalFile(OSSClient ossClient, JSONObject uploadAddress, String localFile){
        String bucketName = uploadAddress.getString("Bucket");
        String objectName = uploadAddress.getString("FileName");
        File file = new File(localFile);
        // single file upload
         ossClient.putObject(bucketName, objectName, file);

        ossClient.appendObject(request);*/
    }

    /**
     * @param vodClient
     * @return
     * @throws ClientException
     */
    public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient vodClient) throws ClientException {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setVideoId("VideoId");
        request.setSysReadTimeout(1000);
        request.setSysConnectTimeout(1000);
        return vodClient.getAcsResponse(request);
    }

    /**
     * @param client 
     * @return GetMezzanineInfoResponse 
     * @throws Exception
     */
    public static GetMezzanineInfoResponse getMezzanineInfo(DefaultAcsClient client, String videoId) throws Exception {
        GetMezzanineInfoRequest request = new GetMezzanineInfoRequest();
        request.setVideoId(videoId);
        //Expiration time of source film download URL
        request.setAuthTimeout(3600L);
        return client.getAcsResponse(request);
    }

    /**
     * @param client
     * @return
     * @throws Exception
     */
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client, String videoId) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }

    public static void main(String[] argv) {
        String accessKeyId = "LTAI4G5znb2KMC4QHSYmUWSL";
        String accessKeySecret = "ar63FppLoUHswCYd5qSTpwe5F9JjvB";
        String localFile = "D:\\imooc\\course\\workspace\\course\\admin\\public\\static\\image\\test.mp4";
        try {
            DefaultAcsClient vodClient = initVodClient(accessKeyId, accessKeySecret);
            String fileName = "test.mp4";
            CreateUploadVideoResponse createUploadVideoResponse = createUploadVideo(vodClient, fileName);
            String videoId = createUploadVideoResponse.getVideoId();
            JSONObject uploadAuth = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAuth()), JSONObject.class);
            JSONObject uploadAddress = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAddress()), JSONObject.class);
            OSSClient ossClient = initOssClient(uploadAuth, uploadAddress);
            uploadLocalFile(ossClient, uploadAddress, localFile);
            System.out.println("upload video success, VideoId : " + videoId); // 7d6b8c07ab48456e932187080f42e88f

            GetMezzanineInfoResponse response = new GetMezzanineInfoResponse();
            response = getMezzanineInfo(vodClient, videoId);
            System.out.println("get video info, response : " + JSON.toJSONString(response));
        } catch (Exception e) {
            System.out.println("upload video fail, ErrorMessage : " + e.getLocalizedMessage());
        }
    }
}
