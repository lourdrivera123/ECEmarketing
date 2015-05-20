package com.example.zem.patientcareapp;

import java.io.Serializable;

/**
 * Created by User PC on 5/4/2015.
 */
public class Patient implements Serializable {


    private String fname = "", mname = "", lname = "", username = "", password = "",
            occupation = "", doctor_referred_by = "", doctor_referred_to = "", birthdate = "",
            sex = "", civil_status = "", height = "", weight = "", address_street = "",
            address_barangay = "", address_city_municipality = "", address_province = "", address_region = "",
            country = "", address_zip = "", cell_no = "", tel_no = "", email = "", photo = "", building = "";

    int id = 0, serverID = 0, unit_floor_room_no = 0, lot_no = 0, block_no = 0, phase_no = 0, address_house_no;

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public int getServerID() {
        return serverID;
    }

    public void setId(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public Patient() {

    }

    public int getUnit_floor_room_no() {
        return unit_floor_room_no;
    }

    public String getBuilding() {
        return building;
    }

    public int getLot_no() {
        return lot_no;
    }

    public int getBlock_no() {
        return block_no;
    }

    public int getPhase_no() {
        return phase_no;
    }

    public String getFullname(boolean middlename_is_full) {
        if (middlename_is_full == true) {
            return fname + " " + mname + " " + lname;
        }
        return fname + " " + mname.indexOf(0) + ". " + lname;
    }

    public void setFullname(String first_name, String middle_name, String last_name) {
        this.setFname(first_name);
        this.setMname(middle_name);
        this.setLname(last_name);
    }

    public void setFullAddress(int unit_floor_room_no, String building, int lot_no, int block_no,
                               int phase_no, int address_house_no, String street, String barangay, String city_municipality,
                               String province, String region, String country, String zip){
        this.setUnit_floor_room_no(unit_floor_room_no);
        this.setBuilding(building);
        this.setLot_no(lot_no);
        this.setBlock_no(block_no);
        this.setPhase_no(phase_no);
        this.setAddress_house_no(address_house_no);
        this.setAddress_street(street);
        this.setAddress_barangay(barangay);
        this.setAddress_city_municipality(city_municipality);
        this.setAddress_province(province);
        this.setAddress_zip(zip);
        this.setAddress_region(region);
        this.setCountry(country);
    }

    public String getFullAddress() {
        return address_house_no + " " + address_street + " " + address_barangay + "\n"
                + address_city_municipality + " " + address_province + " " + address_region + " " + country + ", " + address_zip;
    }

    public void setUnit_floor_room_no(int unit_floor_room_no) {
        this.unit_floor_room_no = unit_floor_room_no;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setLot_no(int lot_no) {
        this.lot_no = lot_no;
    }

    public void setBlock_no(int block_no) {
        this.block_no = block_no;
    }

    public void setPhase_no(int phase_no) {
        this.phase_no = phase_no;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDoctor_referred_by() {
        return doctor_referred_by;
    }

    public void setDoctor_referred_by(String doctor_referred_by) {
        this.doctor_referred_by = doctor_referred_by;
    }

    public String getDoctor_referred_to() {
        return doctor_referred_to;
    }

    public void setDoctor_referred_to(String doctor_referred_to) {
        this.doctor_referred_to = doctor_referred_to;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCivil_status() {
        return civil_status;
    }

    public void setCivil_status(String civil_status) {
        this.civil_status = civil_status;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getAddress_house_no() {
        return address_house_no;
    }

    public void setAddress_house_no(int address_house_no) {
        this.address_house_no = address_house_no;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getAddress_barangay() {
        return address_barangay;
    }

    public void setAddress_barangay(String address_barangay) {
        this.address_barangay = address_barangay;
    }

    public String getAddress_city_municipality() {
        return address_city_municipality;
    }

    public void setAddress_city_municipality(String address_city_municipality) {
        this.address_city_municipality = address_city_municipality;
    }

    public String getAddress_province() {
        return address_province;
    }

    public void setAddress_province(String address_province) {
        this.address_province = address_province;
    }

    public String getAddress_region() {
        return address_region;
    }

    public void setAddress_region(String address_region) {
        this.address_region = address_region;
    }

    public String getAddress_zip() {
        return address_zip;
    }

    public void setAddress_zip(String address_zip) {
        this.address_zip = address_zip;
    }

    public String getCell_no() {
        return cell_no;
    }

    public void setCell_no(String cell_no) {
        this.cell_no = cell_no;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
