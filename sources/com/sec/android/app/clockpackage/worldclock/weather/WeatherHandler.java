package com.sec.android.app.clockpackage.worldclock.weather;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.callback.WeatherHandlerListener;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.message.HeaderGroup;

public class WeatherHandler {
    private Context mContext;
    private WeatherUrlManager mUrlManager = new WeatherUrlManager();
    private Handler mWeatherHandler;
    private WeatherHandlerListener mWeatherHandlerListener;
    private Thread mWeatherThread;

    private static final class WorldclockWeatherHandler extends Handler {
        private final WeakReference<WeatherHandler> mParent;

        private WorldclockWeatherHandler(WeatherHandler parent) {
            this.mParent = new WeakReference(parent);
        }

        public void handleMessage(Message msg) {
            WeatherHandler refs = (WeatherHandler) this.mParent.get();
            if (refs != null) {
                refs.handleMessage(msg);
            }
        }
    }

    public WeatherHandler(Context context, WeatherHandlerListener handlerListener) {
        this.mContext = context;
        this.mWeatherHandlerListener = handlerListener;
        this.mWeatherHandler = new WorldclockWeatherHandler();
    }

    public void stopThreadAndMessage() {
        if (this.mWeatherThread != null && this.mWeatherThread.isAlive()) {
            this.mWeatherThread.interrupt();
            Log.secD("WeatherHandler", "Stop Thread");
        }
        if (this.mWeatherHandler != null) {
            this.mWeatherHandler.removeCallbacksAndMessages(null);
            Log.secD("WeatherHandler", "Stop Message");
        }
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, Object obj) {
        this.mWeatherHandler.sendMessageDelayed(Message.obtain(this.mWeatherHandler, what, arg1, arg2, obj), 7000);
    }

    public void sendMessage(int what, int arg1, int arg2, Object obj) {
        this.mWeatherHandler.sendMessage(Message.obtain(this.mWeatherHandler, what, arg1, arg2, obj));
    }

    private int getTempScale() {
        return this.mContext.getSharedPreferences("ClocksTabStatus", 0).getInt("WeatherUnit", WorldclockWeatherUtils.getDefaultTempScale(this.mContext));
    }

    private void handleMessage(Message msg) {
        HeaderGroup headerGroup = this.mUrlManager.makeHeader(WeatherUrlManager.getPackageName(false));
        final int position = msg.arg2;
        switch (msg.what) {
            case 100:
                this.mWeatherHandlerListener.onTimeOut(((Integer) msg.obj).intValue(), position);
                return;
            case 800:
                ArrayList<String> placeIdList = new ArrayList((ArrayList) msg.obj);
                int placeIdNum = placeIdList.size();
                StringBuilder bld = new StringBuilder();
                for (int i = 0; i < placeIdNum; i++) {
                    bld.append((String) placeIdList.get(i)).append(";");
                }
                String placeId = bld.toString();
                if (!placeId.isEmpty()) {
                    URL url = this.mUrlManager.makeUrlForGetDetailDataList(placeId, getTempScale() == 0 ? "m" : "e");
                    if (url != null) {
                        this.mWeatherThread = new AdvancedHttpClient(this.mContext, true).get(url, headerGroup, new HttpResponseHandler() {
                            public void onReceive(int responseCode, String responseStatus, String responseBody) {
                                super.onReceive(responseCode, responseStatus, responseBody);
                                if (responseCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                                    ArrayList<WorldclockCityWeatherInfo> detailInfo = new TwcWeatherJsonParser().parseWeatherData(responseBody);
                                    if (detailInfo != null) {
                                        WeatherHandler.this.mWeatherHandler.sendMessage(Message.obtain(WeatherHandler.this.mWeatherHandler, 900, 0, position, detailInfo));
                                    }
                                }
                            }
                        });
                        return;
                    }
                    return;
                }
                return;
            case 900:
                this.mWeatherHandlerListener.onSaveData(msg.obj, position);
                return;
            default:
                return;
        }
    }
}
