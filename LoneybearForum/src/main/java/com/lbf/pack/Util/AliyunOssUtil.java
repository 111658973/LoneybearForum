package com.lbf.pack.Util;

import com.aliyun.oss.*;
import com.aliyun.oss.OSS;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AliyunOssUtil {
    public OSS CreatNewClient(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
//        String endpoint = "https://oss-cn-beijing.aliyuncs.com";

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI5tGsAdXduU9RXeQppcpx";
        String accessKeySecret = "IH54nc7olx11wUZWhCgvPSjhme6dZW";
        String bucketName = "loneybear";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
//        ossClient.shutdown();
        return ossClient;
    }

    public OSS getDefaultClient(){
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        String accessKeyId = "LTAI5tHkYCypKcZN4JJAoXhx";
        String accessKeySecret = "FytwXE1teRZUbCTC9kEaDQcec5oHtF";
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public void PutLocalFile(OSS ossClient,String filename,String Osspath,File file,String callbackUrl) throws IOException {
        // 创建PutObjectRequest对象。
        // 填写Bucket名称、Object完整路径和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        OSS defaultClient = getDefaultClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest("loneybear", Osspath+"/"+filename, file);
        defaultClient.putObject(putObjectRequest.withProgressListener(new PutObjectProgressListener()));
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        //回调
        Callback callback = new Callback();
        callback.setCallbackUrl(callbackUrl);
        //（可选）设置回调请求消息头中Host的值，即您的服务器配置Host的值。
        // callback.setCallbackHost("yourCallbackHost");
        // 设置发起回调时请求body的值。
        callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
        // 设置发起回调请求的Content-Type。
        callback.setCalbackBodyType(Callback.CalbackBodyType.JSON);
        // 设置发起回调请求的自定义参数，由Key和Value组成，Key必须以x:开始。
        callback.addCallbackVar("x:filename", filename);
        callback.addCallbackVar("x:size", String.valueOf(file.length()));
        callback.addCallbackVar("x:url", Osspath+"/"+filename);
        callback.addCallbackVar("x:time", new TimeUtil().getCuerrent_time());
        callback.addCallbackVar("x:operator", "");

        putObjectRequest.setCallback(callback);

        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);

        // 读取上传回调返回的消息内容。
        byte[] buffer = new byte[1024];
        putObjectResult.getResponse().getContent().read(buffer);
// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        putObjectResult.getResponse().getContent().close();


        ossClient.shutdown();
    }

    public Map<String,Object> PutLocalFile(OSS client,String bucketName,String filename, String Osspath, File file){
        // 创建PutObjectRequest对象。
        // 填写Bucket名称、Object完整路径和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        OSS defaultClient = getDefaultClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, Osspath+"/"+filename, file);
        defaultClient.putObject(putObjectRequest.withProgressListener(new PutObjectProgressListener()));
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);
        HashMap<String, Object> map = new HashMap<>();
        map.put("filename",filename);
        map.put("size",file.length());
        String url ="https://"+bucketName+"."+client.getBucketLocation(bucketName)+".aliyuncs.com"+"/"+Osspath+"/"+filename;
        map.put("url",url);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("time",df.format(new Date()));
        defaultClient.shutdown();

        return map;
    }

    public static class PutObjectProgressListener implements ProgressListener {
        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to upload......");
                    break;
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                        System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    } else {
                        System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }

        public boolean isSucceed() {
            return succeed;
        }


    }

    static class GetObjectProgressListener implements ProgressListener {
        private long bytesRead = 0;
        private long totalBytes = -1;
        private boolean succeed = false;
        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to download......");
                    break;
                case RESPONSE_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be downloaded to a local file");
                    break;
                case RESPONSE_BYTE_TRANSFER_EVENT:
                    this.bytesRead += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int)(this.bytesRead * 100.0 / this.totalBytes);
                        System.out.println(bytes + " bytes have been read at this time, download progress: " +
                                percent + "%(" + this.bytesRead + "/" + this.totalBytes + ")");
                    } else {
                        System.out.println(bytes + " bytes have been read at this time, download ratio: unknown" +
                                "(" + this.bytesRead + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to download, " + this.bytesRead + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to download, " + this.bytesRead + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }
        public boolean isSucceed() {
            return succeed;
        }
    }


    public void DownloadFile(String bucketName,String FilePullPath,File LocalFile){
        OSS client = getDefaultClient();
        // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
        // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
        client.getObject(new GetObjectRequest(bucketName, FilePullPath).withProgressListener(new GetObjectProgressListener()), LocalFile);

        // 关闭OSSClient。
        client.shutdown();
    }


}
