package com.route876.classes.objects;

import java.util.List;

/**
 * Created by Howard on 9/13/2015.
 */
public class Route {
    private int id;
    private String routeTo, routeFrom, routeVia, routeNum, routeTag;
    private String routePrice;
    private List<String> routeStops;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getRouteVia() {
        return routeVia;
    }

    public void setRouteVia(String routeVia) {
        this.routeVia = routeVia;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public void setRouteTag(String routeTag) {
        this.routeTag = routeTag;
    }

    public String getRoutePrice() {
        return routePrice;
    }

    public void setRoutePrice(String routePrice) {
        this.routePrice = routePrice;
    }

    public List<String> getRouteStops() {
        return routeStops;
    }

    public void setRouteStops(List<String> routeStops) {
        this.routeStops = routeStops;
    }
}
