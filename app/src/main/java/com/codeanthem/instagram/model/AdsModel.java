package com.codeanthem.instagram.model;

import android.os.Parcelable;

import java.io.Serializable;

public class AdsModel implements Serializable {

    private int adId;
    private String publisherId;
    private double price;
    private String category;
    private String title;
    private String address;
    private int noOfBedroom;
    private int noOfBathroom;
    private int carpetArea;
    private String description;
    private float rating;
    private String coverImage;
    private double securityDeposit;
    private String availability;
    private int wifi;
    private int furnished;
    private String publishDate;
    private boolean favourite;

    public AdsModel(int adId, String publisherId, double price, String category, String title, String address, int noOfBedroom, int noOfBathroom, int carpetArea, String description, float rating, String coverImage, double securityDeposit, String availability, int wifi, int furnished, String publishDate, boolean favourite) {
        this.adId = adId;
        this.publisherId = publisherId;
        this.price = price;
        this.category = category;
        this.title = title;
        this.address = address;
        this.noOfBedroom = noOfBedroom;
        this.noOfBathroom = noOfBathroom;
        this.carpetArea = carpetArea;
        this.description = description;
        this.rating = rating;
        this.coverImage = coverImage;
        this.securityDeposit = securityDeposit;
        this.availability = availability;
        this.wifi = wifi;
        this.furnished = furnished;
        this.publishDate = publishDate;
        this.favourite = favourite;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNoOfBedroom() {
        return noOfBedroom;
    }

    public void setNoOfBedroom(int noOfBedroom) {
        this.noOfBedroom = noOfBedroom;
    }

    public int getNoOfBathroom() {
        return noOfBathroom;
    }

    public void setNoOfBathroom(int noOfBathroom) {
        this.noOfBathroom = noOfBathroom;
    }

    public int getCarpetArea() {
        return carpetArea;
    }

    public void setCarpetArea(int carpetArea) {
        this.carpetArea = carpetArea;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public int getFurnished() {
        return furnished;
    }

    public void setFurnished(int furnished) {
        this.furnished = furnished;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
       this.favourite = favourite;
    }
}
