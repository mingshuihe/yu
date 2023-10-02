package com.helo.ming.yu.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        TimeInterval timer = DateUtil.timer();
        Response response =  chain.proceed(request);
        return logForResponse(response, timer);
    }

    private Response logForResponse(Response response, TimeInterval timer) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            logger.info("=========repose==log==start======");
            logger.info(String.format("repose url:%s,code:%s,time is:%s,headers:%s", clone.request().url(), clone.code(), timer.intervalMs() + "ms", clone.protocol()));

            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null && isText(mediaType)) {
                    String content = body.string();
                    logger.info(String.format("message:%s,contentType:%s,content is:%s,", clone.message(), mediaType.toString(),content ));
                    body = ResponseBody.create(mediaType, content);
                    return response.newBuilder().body(body).build();
                }
            }
        } catch (Exception e) {
            logger.warn("print reponse error", e);
        }finally {
            logger.info("=========repose==log==end======");

        }
        return response;


    }

    private void logForRequest(Request request) {
        String url = request.url().toString();
        String method = request.method();
        Headers headers = request.headers();
        String headerStr = headers != null && headers.size() > 0 ? headers.toString() : "";
        logger.info("=========request==log==start======");
        logger.info(String.format("request url:%s,method:%s,headers:%s", url, method, headerStr));
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null && isText(mediaType)) {
                logger.info("requestBody mediaType:%s,bodyToString:%s", mediaType.toString(), bodyToString(request));
            }
        }
        logger.info("=========request==log==end======");
    }

    private String bodyToString(final Request request) {
        final Request copy = request.newBuilder().build();
        final Buffer buffer=new Buffer();
        try {
            copy.body().writeTo(buffer);
        } catch (IOException e) {
            return "something error,when show requestBody";
        }
        return buffer.readUtf8();
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")) {
                return true;
            }
        }
        return false;
    }
}


