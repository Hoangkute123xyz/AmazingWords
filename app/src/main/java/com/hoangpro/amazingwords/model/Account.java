package com.hoangpro.amazingwords.model;

public class Account {
    public String idFacebook="",name="",email="";
    public int coin=0,lvSortWord=1, lvFindWord=1,timeCount=30;

    public Account(String idFacebook, String name, String email) {
        this.idFacebook = idFacebook;
        this.name = name;
        this.email = email;
    }

    public Account(String idFacebook, String name, String email, int coin, int lvSortWord, int lvFindword, int timeCount) {
        this.idFacebook = idFacebook;
        this.name = name;
        this.email = email;
        this.coin = coin;
        this.lvSortWord = lvSortWord;
        this.lvFindWord = lvFindword;
        this.timeCount = timeCount;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "idFacebook='" + idFacebook + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", coin=" + coin +
                ", lvSortWord=" + lvSortWord +
                ", lvFindWord=" + lvFindWord +
                ", timeCount=" + timeCount +
                '}';
    }
}
