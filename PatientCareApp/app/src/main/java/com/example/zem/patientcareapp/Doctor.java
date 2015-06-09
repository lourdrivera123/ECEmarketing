package com.example.zem.patientcareapp;

import java.io.Serializable;

/**
 * Created by Dexter B. on 5/4/2015.
 */
public class Doctor implements Serializable {
    private String fname = "", mname = "", lname = "",
            cell_no = "", tel_no = "", photo = "",
            email = "", affiliation = "", specialty = "", sub_specialty = "",
            created_at = "", updated_at = "", deleted_at = "";

    private int id = 0, prc_no = 0, doc_id = 0, sub_specialty_id = 0;

    public Doctor() {

    }

    public void setFullname(String first_name, String middle_name, String last_name) {
        this.setFname(first_name);
        this.setMname(middle_name);
        this.setLname(last_name);
    }

    public void setContactInfo(String email, String cell_no, String tel_no) {
        this.setEmail(email);
        this.setCell_no(cell_no);
        this.setTel_no(tel_no);
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

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

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setSub_specialty(String sub_specialty) {
        this.sub_specialty = sub_specialty;
    }

    public void setSub_specialty_id(int sub_specialty_id) {
        this.sub_specialty_id = sub_specialty_id;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }


    /**
     * GETTERS ------------------------------------------------------------------- *
     */

    public String getFullname(boolean middlename_is_full) {
        if (middlename_is_full == true) {
            return fname + " " + mname + " " + lname;
        }
        return fname + " " + mname.indexOf(0) + ". " + lname;
    }

//    public String getFirstLineAddress() {
//        if(get) {
//
//        }
//    }

    public int getDoc_id() {
        return doc_id;
    }

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

    public String getSpecialty() {
        return specialty;
    }

    public String getSub_specialty() {
        return sub_specialty;
    }

    public int getSub_specialty_id() {
        return sub_specialty_id;
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

    public String getEmail() {
        return email;
    }

    public int getID() {
        return id;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }
}
