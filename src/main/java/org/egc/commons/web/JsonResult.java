package org.egc.commons.web;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

/**
 * 用于组织数据为一个JSON对象
 *
 * @Author houzhiwei
 * @Date 2016/11/4 21:52.
 */
public class JsonResult implements Serializable {
    private static final long serialVersionUID = -6559362101721248596L;

    private Object data;
    private String msg;
    /**
     * redirect URL
     */
    private String url;

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    private HttpStatus status;

    public JsonResult()
    {
        super();
    }

    public JsonResult(String msg)
    {
        this.msg = msg;
    }

    public JsonResult(String msg, HttpStatus status)
    {
        this.msg = msg;
        this.status = status;
    }

    public JsonResult(Object data)
    {
        this.data = data;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public static FastJsonJsonView jsonView(Object data, String msg)
    {
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attrs = Maps.newHashMap();
        attrs.put("data", data);
        attrs.put("msg", msg);
        view.setAttributesMap(attrs);
        return view;
    }
}
