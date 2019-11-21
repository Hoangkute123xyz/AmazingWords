package com.hoangpro.amazingwords.model;

public class Top {
    private int imgAvatar;
    private String name;
    private int coin;

    public Top(int imgAvatar, String name, int coin) {
        this.imgAvatar = imgAvatar;
        this.name = name;
        this.coin = coin;
    }

    public int getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(int imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
