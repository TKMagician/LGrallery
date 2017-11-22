package grallary.lee.com.grallery.inter;

import java.util.List;

/**
 * @Author Lee
 * @Date 创建时间 2017/11/22 0022
 * @Description 回调handler
 * @Version
 */
public interface IHandlerCallBack {

    void onStart();

    void onSuccess(List<String> photoList, int requestId);

    void onCancel();

    void onFinish();

    void onError();

}
