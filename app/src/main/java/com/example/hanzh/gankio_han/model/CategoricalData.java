package com.example.hanzh.gankio_han.model;

import java.util.List;

/**
 * Created by hanzh on 2015/10/6.
 * 分类数据
 */
public class CategoricalData extends BaseGank{

    private List<Gank> results;

    public List<Gank> getResults() {
        return results;
    }

    public void setResults(List<Gank> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "CategoricalData{" +
                "results=" + results +
                '}';
    }
}
