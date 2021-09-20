package com.cryallen.wanlearning.data.remote.monitor.listener;

import android.os.SystemClock;

import com.cryallen.wanlearning.data.remote.monitor.data.NetworkTraceBean;
import com.cryallen.wanlearning.data.remote.monitor.inter.IDataPoolHandleImpl;
import com.cryallen.wanlearning.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 监听统计网络请求耗时 EventListener子类
 * @author vsh9p8q
 * @DATE 2021/3/31
 ***/
public class NetworkListener extends EventListener {
    private static final String TAG = "OkNetworkMonitor";

    private static AtomicInteger mNextRequestId = new AtomicInteger(0);
    private String mRequestId ;

    public static Factory get(){
        return new Factory() {
            @NotNull
            @Override
            public EventListener create(@NotNull Call call) {
                return new NetworkListener();
            }
        };
    }

    public NetworkListener() {
        super();
    }

    @Override
    public void callStart(@NotNull Call call) {
        super.callStart(call);
        //mRequestId = mNextRequestId.getAndIncrement() + "";
        //getAndAdd，在多线程下使用cas保证原子性
        mRequestId = String.valueOf(mNextRequestId.getAndIncrement());
        LogUtils.d(TAG,  "-------callStart---requestId-----" + mRequestId);
        saveEvent(NetworkTraceBean.CALL_START);
        saveUrl(call.request().url().toString());
    }

    @Override
    public void dnsStart(@NotNull Call call, @NotNull String domainName) {
        super.dnsStart(call, domainName);
        LogUtils.d(TAG, "dnsStart");
        saveEvent(NetworkTraceBean.DNS_START);
    }

    @Override
    public void dnsEnd(@NotNull Call call, @NotNull String domainName, @NotNull List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        LogUtils.d(TAG, "dnsEnd");
        saveEvent(NetworkTraceBean.DNS_END);
    }

    @Override
    public void connectStart(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        LogUtils.d(TAG, "connectStart");
        saveEvent(NetworkTraceBean.CONNECT_START);
    }

    @Override
    public void secureConnectStart(@NotNull Call call) {
        super.secureConnectStart(call);
        LogUtils.d(TAG, "secureConnectStart");
        saveEvent(NetworkTraceBean.SECURE_CONNECT_START);
    }

    @Override
    public void secureConnectEnd(@NotNull Call call, @Nullable Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        LogUtils.d(TAG, "secureConnectEnd");
        saveEvent(NetworkTraceBean.SECURE_CONNECT_END);
    }

    @Override
    public void connectionAcquired(@NotNull Call call, @NotNull Connection connection) {
        super.connectionAcquired(call, connection);
        LogUtils.d(TAG, "connectionAcquired");
    }

    @Override
    public void connectionReleased(@NotNull Call call, @NotNull Connection connection) {
        super.connectionReleased(call, connection);
        LogUtils.d(TAG, "connectionReleased");
    }

    @Override
    public void requestHeadersStart(@NotNull Call call) {
        super.requestHeadersStart(call);
        LogUtils.d(TAG, "requestHeadersStart");
        saveEvent(NetworkTraceBean.REQUEST_HEADERS_START);
    }

    @Override
    public void requestHeadersEnd(@NotNull Call call, @NotNull Request request) {
        super.requestHeadersEnd(call, request);
        LogUtils.d(TAG, "requestHeadersEnd");
        saveEvent(NetworkTraceBean.REQUEST_HEADERS_END);
    }

    @Override
    public void requestBodyStart(@NotNull Call call) {
        super.requestBodyStart(call);
        LogUtils.d(TAG, "requestBodyStart");
        saveEvent(NetworkTraceBean.REQUEST_BODY_START);
    }

    @Override
    public void requestBodyEnd(@NotNull Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        LogUtils.d(TAG, "requestBodyEnd");
        saveEvent(NetworkTraceBean.REQUEST_BODY_END);
    }

    @Override
    public void responseHeadersStart(@NotNull Call call) {
        super.responseHeadersStart(call);
        LogUtils.d(TAG, "responseHeadersStart");
        saveEvent(NetworkTraceBean.RESPONSE_HEADERS_START);
    }

    @Override
    public void responseHeadersEnd(@NotNull Call call, @NotNull Response response) {
        super.responseHeadersEnd(call, response);
        LogUtils.d(TAG, "responseHeadersEnd");
        saveEvent(NetworkTraceBean.RESPONSE_HEADERS_END);
    }

    @Override
    public void responseBodyStart(@NotNull Call call) {
        super.responseBodyStart(call);
        LogUtils.d(TAG, "responseBodyStart");
        saveEvent(NetworkTraceBean.RESPONSE_BODY_START);
    }

    @Override
    public void responseBodyEnd(@NotNull Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        LogUtils.d(TAG, "responseBodyEnd");
        saveEvent(NetworkTraceBean.RESPONSE_BODY_END);
    }

    @Override
    public void connectEnd(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy, @Nullable Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        LogUtils.d(TAG, "connectEnd");
        saveEvent(NetworkTraceBean.CONNECT_END);
    }

    @Override
    public void connectFailed(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy, @Nullable Protocol protocol, @NotNull IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        LogUtils.d(TAG, "connectFailed");
    }

    @Override
    public void callEnd(@NotNull Call call) {
        super.callEnd(call);
        LogUtils.d(TAG, "callEnd");
        saveEvent(NetworkTraceBean.CALL_END);
        generateTraceData();
    }

    @Override
    public void callFailed(@NotNull Call call, @NotNull IOException ioe) {
        super.callFailed(call, ioe);
        LogUtils.d(TAG, "callFailed");
    }

    @Override
    public void proxySelectEnd(@NotNull Call call, @NotNull HttpUrl url, @NotNull List<Proxy> proxies) {
        super.proxySelectEnd(call, url, proxies);
        LogUtils.d(TAG, "proxySelectEnd");
    }

    @Override
    public void proxySelectStart(@NotNull Call call, @NotNull HttpUrl url) {
        super.proxySelectStart(call, url);
        LogUtils.d(TAG, "proxySelectStart");
    }

    @Override
    public void requestFailed(@NotNull Call call, @NotNull IOException ioe) {
        super.requestFailed(call, ioe);
        LogUtils.d(TAG, "requestFailed");
    }

    @Override
    public void responseFailed(@NotNull Call call, @NotNull IOException ioe) {
        super.responseFailed(call, ioe);
        LogUtils.d(TAG, "responseFailed");
    }

    private void generateTraceData(){
        NetworkTraceBean networkTraceBean = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(mRequestId);
        LogUtils.d(TAG, "generateTraceData requestId：" + networkTraceBean.getId() + "----url:" + networkTraceBean.getUrl());
        Map<String, Long> eventsTimeMap = networkTraceBean.getNetworkEventsMap();
        Map<String, Long> traceItemMap = networkTraceBean.getTraceItemList();
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_TOTAL,getEventCostTime(eventsTimeMap,NetworkTraceBean.CALL_START, NetworkTraceBean.CALL_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_DNS,getEventCostTime(eventsTimeMap,NetworkTraceBean.DNS_START, NetworkTraceBean.DNS_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_SECURE_CONNECT,getEventCostTime(eventsTimeMap,NetworkTraceBean.SECURE_CONNECT_START, NetworkTraceBean.SECURE_CONNECT_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_CONNECT,getEventCostTime(eventsTimeMap,NetworkTraceBean.CONNECT_START, NetworkTraceBean.CONNECT_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_REQUEST_HEADERS,getEventCostTime(eventsTimeMap,NetworkTraceBean.REQUEST_HEADERS_START, NetworkTraceBean.REQUEST_HEADERS_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_REQUEST_BODY,getEventCostTime(eventsTimeMap,NetworkTraceBean.REQUEST_BODY_START, NetworkTraceBean.REQUEST_BODY_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_RESPONSE_HEADERS,getEventCostTime(eventsTimeMap,NetworkTraceBean.RESPONSE_HEADERS_START, NetworkTraceBean.RESPONSE_HEADERS_END));
        traceItemMap.put(NetworkTraceBean.TRACE_NAME_RESPONSE_BODY,getEventCostTime(eventsTimeMap,NetworkTraceBean.RESPONSE_BODY_START, NetworkTraceBean.RESPONSE_BODY_END));
        showNetworkConsume(traceItemMap);
    }

    private void showNetworkConsume(Map<String, Long> traceItemMap) {
        //转换为list进行排序
        List<Map.Entry<String, Long>> traceList = new ArrayList<Map.Entry<String, Long>>(traceItemMap.entrySet());
        Collections.sort(traceList, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (int i = 0; i < traceList.size(); i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(traceList.get(i).getKey());
            sb.append(" 消耗时间");
            sb.append(" : ");
            sb.append(traceList.get(i).getValue());
            sb.append(" ms");
            LogUtils.d(TAG, sb.toString());
        }
    }

    private void saveEvent(String eventName){
        NetworkTraceBean networkTraceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(mRequestId);
        Map<String, Long> networkEventsMap = networkTraceModel.getNetworkEventsMap();
        networkEventsMap.put(eventName, SystemClock.elapsedRealtime());
    }

    private void saveUrl(String url){
        NetworkTraceBean networkTraceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(mRequestId);
        networkTraceModel.setUrl(url);
    }

    private long getEventCostTime(Map<String, Long> eventsTimeMap, String startName, String endName) {
        if (!eventsTimeMap.containsKey(startName) || !eventsTimeMap.containsKey(endName)) {
            return 0;
        }
        Long endTime = eventsTimeMap.get(endName);
        Long start = eventsTimeMap.get(startName);
        long result = endTime - start;
        return result;
    }
}
