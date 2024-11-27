package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.model.Contact;
import com.example.SpringSecurity.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;

    @PostMapping("/contact")
    //@PreFilter("filterObject.contactName != 'Test'")
    @PostFilter("filterObject.contactName != 'Test'") // check method return object
    public List<Contact> getContactInquiryDetails(@RequestBody List<Contact> contacts){
        List<Contact> returnContacts = new ArrayList<>();
        if(!contacts.isEmpty()){
            Contact contact = contacts.get(0);
            contact.setContactId(getServiceReqNumber());
            contact.setCreateDt(new Date(System.currentTimeMillis()));
            Contact savedContact =  contactRepository.save(contact);
            returnContacts.add(savedContact);
        }

        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://console.melipayamak.com/api/send/simple/dea72f061d9d4afaaebfff133de91263");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String params = "{\"from\": \"50002710023346\", \"to\": \"09105297973\", \"text\": \"test sms\"}";
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                byte[] paramsAsByte = params.getBytes("utf-8");
                dos.write(paramsAsByte, 0, paramsAsByte.length);
            }

            int responseCode = conn.getResponseCode();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response Code: " + responseCode);
                System.out.println("Response: " + response.toString());
            }

        } catch (Exception e) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    errorResponse.append(errorLine.trim());
                }
                System.err.println("Error Response: " + errorResponse.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return returnContacts;
    }

    public String getServiceReqNumber(){
        Random random = new Random();
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return  "SR" + ranNum;
    }

}
