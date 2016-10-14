package com.ypwl.xiaotouzi.manager;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * <p>
 * function : 事件注册、反注册、发布 帮助类,用于解耦.
 * </p>
 * Created by lzj on 2016/1/21
 */
public final class EventHelper {
    private static final Bus BUS = new Bus(ThreadEnforcer.MAIN);

    private static Bus instance() {
        return BUS;
    }

    private EventHelper() {
    }

    public static void post(Object event) {
        instance().post(event);
    }

    public static void register(Object object) {
        instance().register(object);
    }

    public static void unregister(Object object) {
        instance().unregister(object);
    }
}