package com.sap.josh0207.sap2016;

/**
 * Created by Josh0207 on 24/11/2016.
 */

public class Proposal {
    private String campaignID, merchantID, influencerID, content, price, photoURL;

    public Proposal(){

    }

    public Proposal(String photoURL, String campaignID, String merchantID, String influencerID, String content, String price) {
        this.photoURL = photoURL;
        this.campaignID = campaignID;
        this.merchantID = merchantID;
        this.influencerID = influencerID;
        this.content = content;
        this.price = price;
    }

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getInfluencerID() {
        return influencerID;
    }

    public void setInfluencerID(String influencerID) {
        this.influencerID = influencerID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoUrl) {
        this.photoURL = photoUrl;
    }
}

