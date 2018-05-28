package com.ftn.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by Jasmina on 15/05/2018.
 */
public class JWTUtils {

    private static String key = "LAKSDJLAKSJalasdksjdlaskjdla";

    public static String makeJWT(int id) {
        String jwt = "";

        try {
            jwt = Jwts.builder().setSubject(id + "").signWith(SignatureAlgorithm.HS256, key).compact();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

        return jwt;
    }

    public static int unpackJWT(String token) {
        String tokenContent = "";
        int id;

        if(token != null && !token.isEmpty()) {
            try {
                tokenContent = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
                id = Integer.parseInt(tokenContent);
            } catch(Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            id = -1;
        }

        return id;
    }
}
