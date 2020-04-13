package com.teamcool.touristum.data.model;

import androidx.annotation.Nullable;

public class Filter implements Comparable<Filter>{

    String type,filter;

    public Filter(String type, String filter) {
        this.type = type;
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public int compareTo(Filter o) {
        return this.type.compareTo(o.type);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Filter filter = (Filter) obj;
        return this.getType().equals(filter.getType()) && this.getFilter().equals(filter.getFilter());
    }
}
