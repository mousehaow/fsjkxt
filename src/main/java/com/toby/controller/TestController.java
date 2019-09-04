package com.toby.controller;

import com.toby.model.User;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public ResponseEntity test() {
//        User admin = new User();
//        try {
//            String password = "admin";
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update(password.getBytes("UTF-8"));
//            String result = byte2Hex(md.digest());
//
//            admin.setPassword(result);
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        admin.setAuthority(0);
//        admin.setUserName("admin");
//        admin.setRegisterTime(new Date());
//        userService.saveNewUser(admin);
        return new ResponseEntity<>("hello",  HttpStatus.OK);
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

}
