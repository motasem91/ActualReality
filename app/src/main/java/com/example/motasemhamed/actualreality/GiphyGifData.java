package com.example.motasemhamed.actualreality;

/**
 * Created by motasemhamed on 9/8/16.
 */
public class GiphyGifData {

    String fixedHeightDownsampledUrl, fixedHeightDownsampledWidth, fixedWidthUrl;

    public GiphyGifData(){}

    public GiphyGifData(String fixedHeightDownsampledUrl, String fixedHeightDownsampledWidth, String fixedWidthUrl){
        this.fixedHeightDownsampledUrl = fixedHeightDownsampledUrl;
        this.fixedHeightDownsampledWidth = fixedHeightDownsampledWidth;
        this.fixedWidthUrl = fixedWidthUrl;
    }

    public void setFixedHeightDownsampledUrl(String fixedHeightDownsampledUrl) {
        this.fixedHeightDownsampledUrl = fixedHeightDownsampledUrl;
    }

    public String getFixedHeightDownsampledUrl() {
        return fixedHeightDownsampledUrl;
    }

    public void setFixedHeightDownsampledWidth(String fixedHeightDownsampledWidth) {
        this.fixedHeightDownsampledWidth = fixedHeightDownsampledWidth;
    }

    public String getFixedHeightDownsampledWidth() {
        return fixedHeightDownsampledWidth;
    }

    public void setFixedWidthUrl(String fixedWidthUrl) {
        this.fixedWidthUrl = fixedWidthUrl;
    }

    public String getFixedWidthUrl() {
        return fixedWidthUrl;
    }
}
