package com.sap.josh0207.sap2016;

/**
 * Created by Josh0207 on 16/11/2016.
 */

public class Campaign {
    private String description,campaignName,hero_image,expired,merchantId;

    public Campaign(){

    }

    public Campaign(String description, String campaignName, String hero_image, String expired, String merchnatId) {
        this.description = description;
        this.campaignName = campaignName;
        this.hero_image = hero_image;
        this.expired = expired;
        this.merchantId = merchnatId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHero_image() {
        return hero_image;
    }

    public void setHero_image(String hero_image) {
        this.hero_image = hero_image;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}

