package com.jhobor.ddc.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/16.
 * 银行卡
 */

public class BankCard implements Parcelable {
    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel in) {
            return new BankCard(in);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };
    private int id;
    private int userId;
    private String bankName;//银行名称
    private String branch;//支行名称
    private int chosen;//是否选择当前卡为提现银行卡
    private String cardNo;//卡号或尾号

    public BankCard() {
    }

    public BankCard(int id, int userId, String bankName, String branch, int chosen) {
        this.id = id;
        this.userId = userId;
        this.bankName = bankName;
        this.branch = branch;
        this.chosen = chosen;
    }

    public BankCard(int id, int userId, String bankName, String branch, int chosen, String cardNo) {
        this.id = id;
        this.userId = userId;
        this.bankName = bankName;
        this.branch = branch;
        this.chosen = chosen;
        this.cardNo = cardNo;
    }

    protected BankCard(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        bankName = in.readString();
        branch = in.readString();
        chosen = in.readInt();
        cardNo = in.readString();
    }

    public String getCardNo() {

        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getChosen() {
        return chosen;
    }

    public void setChosen(int chosen) {
        this.chosen = chosen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(bankName);
        dest.writeString(branch);
        dest.writeInt(chosen);
        dest.writeString(cardNo);
    }
}
