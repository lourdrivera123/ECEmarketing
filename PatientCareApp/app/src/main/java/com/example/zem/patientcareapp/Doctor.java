package com.example.zem.patientcareapp;

import java.io.Serializable;

/**
 * Created by Dexter B. on 5/4/2015.
 */
public class Doctor implements Serializable {
    private String fname = "", mname = "", lname = "",
        address_house_no = "", address_street = "", address_barangay = "",
        address_city_municipality = "", address_province = "", address_region = "", address_zip = "",
        specialty = "", sub_specialty = "", cell_no = "", tel_no = "", photo = "", clinic_sched = "",
        email = "", country = "", affiliation = "", username = "", password = "";

    private int id = 0, clinic_id = 0, secretary_id = 0, prc_no = 0, doc_id = 0;

    public Doctor(){

    }


    /**    SETTERS ------------------------------------------------------------------ **/
        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setFullAddress(String house_no, String street, String barangay, String city_municipality,
                                   String province, String region, String country, String zip){
            this.setAddress_house_no(house_no);
            this.setAddress_street(street);
            this.setAddress_barangay(barangay);
            this.setAddress_city_municipality(city_municipality);
            this.setAddress_province(province);
            this.setAddress_zip(zip);
            this.setAddress_region(region);
            this.setCountry(country);
        }

        public void setFullname(String first_name, String middle_name, String last_name){
            this.setFname(first_name);
            this.setMname(middle_name);
            this.setLname(last_name);
        }

        public void setContactInfo(String email, String cell_no, String tel_no){
            this.setEmail(email);
            this.setCell_no(cell_no);
            this.setTel_no(tel_no);
        }

        public void setDoc_id(int doc_id){ this.doc_id = doc_id; }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public void setPrc_no(int prc_no) {
            this.prc_no = prc_no;
        }

        public void setAddress_house_no(String address_house_no) {
            this.address_house_no = address_house_no;
        }

        public void setAddress_street(String address_street) {
            this.address_street = address_street;
        }

        public void setAddress_barangay(String address_barangay) {
            this.address_barangay = address_barangay;
        }

        public void setAddress_city_municipality(String address_city_municipality) {
            this.address_city_municipality = address_city_municipality;
        }

        public void setAddress_province(String address_province) {
            this.address_province = address_province;
        }

        public void setAddress_region(String address_region) {
            this.address_region = address_region;
        }

        public void setAddress_zip(String address_zip) {
            this.address_zip = address_zip;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public void setSub_specialty(String sub_specialty) {
            this.sub_specialty = sub_specialty;
        }

        public void setCell_no(String cell_no) {
            this.cell_no = cell_no;
        }

        public void setTel_no(String tel_no) {
            this.tel_no = tel_no;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setClinic_sched(String clinic_sched) {
            this.clinic_sched = clinic_sched;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setCountry(String country){
            this.country = country;
        }

        public void setID(int id){
            this.id = id;
        }

        public void setAffiliation(String affiliation){
            this.affiliation = affiliation;
        }

        public void setClinic_id(int clinic_id){
            this.clinic_id = clinic_id;
        }

        public void setSecretary_id(int id){
            this.secretary_id = secretary_id;
        }


    /**   GETTERS ------------------------------------------------------------------- **/
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getFullname(boolean middlename_is_full){
            if(middlename_is_full == true) {
                return fname + " " + mname + " " + lname;
            }
            return fname+" "+mname.indexOf(0)+". "+lname;

        }

        public String getFullAddress(){
            return address_house_no+" "+address_street+" "+address_barangay+"\n"
                    +address_city_municipality+" "+address_province+" "+address_region+" "+country+", "+address_zip;
        }

        public int getDoc_id() { return doc_id; }

        public String getFname() {
            return fname;
        }

        public String getMname() {
            return mname;
        }

        public String getLname() {
            return lname;
        }

        public int getPrc_no() {
            return prc_no;
        }

        public String getAddress_house_no() {
            return address_house_no;
        }

        public String getAddress_street() {
            return address_street;
        }

        public String getAddress_barangay() {
            return address_barangay;
        }

        public String getAddress_city_municipality() {
            return address_city_municipality;
        }

        public String getAddress_province() {
            return address_province;
        }

        public String getAddress_region() {
            return address_region;
        }

        public String getAddress_zip() {
            return address_zip;
        }

        public String getSpecialty() {
            return specialty;
        }

        public String getSub_specialty() {
            return sub_specialty;
        }

        public String getCell_no() {
            return cell_no;
        }

        public String getTel_no() {
            return tel_no;
        }

        public String getPhoto() {
            return photo;
        }

        public String getClinic_sched() {
            return clinic_sched;
        }

        public String getEmail() {
            return email;
        }

        public String getCountry(){
            return country;
        }

        public int getID(){
            return id;
        }

        public int getClinic_id(){
            return clinic_id;
        }

        public String getAffiliation(){
            return affiliation;
        }

        public int getSecretary_id(){
            return secretary_id;
        }
}
