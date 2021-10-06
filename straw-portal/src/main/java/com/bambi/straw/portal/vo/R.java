package com.bambi.straw.portal.vo;

import com.bambi.straw.portal.service.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    /** 200 OK - [GET]：服务器成功返回用户请求的数据 */
    public static final int OK = 200;

    /** 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。 */
    public static final int CREATED = 201;

    /** 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务） */
    public static final int ACCEPTED = 202;

    /** 204 NO CONTENT - [DELETE]：用户删除数据成功。 */
    public static final int NO_CONTENT = 204;

    /** 400 INVALID REQUEST - [POST/PUT/PATCH]：
     用户发出的请求有错误，服务器没有进行新建或修改数据的操作。*/
    public static final int INVALID_REQUEST = 400;

    /** 401 Unauthorized - [*]：
     表示用户没有权限（令牌、用户名、密码错误）。 */
    public static final int UNAUTHORIZED = 401;

    /** 403 Forbidden - [*]
     表示用户得到授权（与401错误相对），但是访问是被禁止的。*/
    public static final int FORBIDDEN = 403;

    /** 404 NOT FOUND - [*]：
     用户发出的请求针对的是不存在的记录，服务器没有进行操作。 */
    public static final int NOT_FOUND = 404;

    /** 410 Gone -[GET]：
     用户请求的资源被永久删除，且不会再得到的。*/
    public static final int GONE = 410;

    /** 422 Unprocesable entity - [POST/PUT/PATCH]
     当创建一个对象时，发生一个验证错误。 */
    public static final int UNPROCESABLE_ENTITY = 422;

    /** 500 INTERNAL SERVER ERROR - [*]：
     服务器发生错误，用户将无法判断发出的请求是否成功。 */
    public static final int INTERNAL_SERVER_ERROR = 500;

    private int code;
    private String message;
    private T data;

    /**
     * 服务器成功返回用户请求的数据
     * @param message 消息
     */
    public static R ok(String message){
        return new R().setCode(OK).setMessage(message);
    }

    /**
     * 服务器成功返回用户请求的数据
     * @param data 数据
     */
    public static R ok(Object data){
        return new R().setMessage("OK").setCode(OK).setData(data);
    }

    /**
     * 用户新建或修改数据成功。
     */
    public static R created(String message){
        return new R().setCode(CREATED).setMessage(message);
    }
    /**
     * 用户新建或修改数据成功。 返回实体类对象
     */
    public static R created(Object obj){
        return new R().setCode(CREATED).setMessage("创建成功").setData(obj);
    }

    /**
     * 表示一个请求已经进入后台排队（异步任务）
     */
    public static R accepted(String message){
        return new R().setCode(ACCEPTED).setMessage(message);
    }

    /**
     * 用户删除数据成功
     */
    public static R noContent(String message){
        return new R().setCode(NO_CONTENT).setMessage(message);
    }

    /**
     * 用户发出的请求有错误，服务器没有进行新建或修改数据的操作。
     */
    public static R invalidRequest(String message){
        return new R().setCode(INVALID_REQUEST).setMessage(message);
    }

    /**
     * 表示用户没有权限（令牌、用户名、密码错误）
     */
    public static R unauthorized(String  message){
        return new R().setCode(UNAUTHORIZED).setMessage(message);
    }

    /**
     * 登录以后，但是没有足够权限
     */
    public static R forbidden(){
        return new R().setCode(FORBIDDEN).setMessage("权限不足！");
    }

    /**
     * 用户发出的请求针对的是不存在的记录，服务器没有进行操作。
     */
    public static R notFound(String message){
        return new R().setCode(NOT_FOUND).setMessage(message);
    }

    /**
     * 用户请求的资源被永久删除，且不会再得到的。
     */
    public static R gone(String message){
        return new R().setCode(GONE).setMessage(message);
    }

    /**
     * 当创建一个对象时，发生一个验证错误。
     */
    public static R unproecsableEntity(String message){
        return new R().setCode(UNPROCESABLE_ENTITY)
                .setMessage(message);
    }

    /**
     * 将异常消息复制到返回结果中
     */
    public static R failed(ServiceException e){
        return new R().setCode(e.getCode())
                .setMessage(e.getMessage());
    }

    /**
     * 服务器发生错误，用户将无法判断发出的请求是否成功。
     */
    public static R failed(Throwable e){
        return new R().setCode(INTERNAL_SERVER_ERROR)
                .setMessage(e.getMessage());
    }
}
