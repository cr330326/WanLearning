package com.cryallen.wanlearning.data.remote.monitor.inter;


import com.cryallen.wanlearning.data.remote.monitor.data.NetworkFeedBean;
import com.cryallen.wanlearning.data.remote.monitor.data.NetworkTraceBean;

import java.util.HashMap;

/***
 * 数据处理接口
 * @author vsh9p8q
 * @DATE 2021/3/31
 ***/
public interface IDataPoolHandle {
    void initDataPool();

    void clearDataPool();

    void addNetworkFeedData(String key, NetworkFeedBean networkFeedModel);

    void removeNetworkFeedData(String key);

    HashMap<String, NetworkFeedBean> getNetworkFeedMap();

    NetworkFeedBean getNetworkFeedModel(String requestId);

    NetworkTraceBean getNetworkTraceModel(String id);
}
