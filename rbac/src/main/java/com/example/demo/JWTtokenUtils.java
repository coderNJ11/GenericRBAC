package com.example.demo;

import com.nimbusds.jwt.Jwt;
import com.nimbusds.jwt.JwtException;
import com.nimbusds.jwt.JwtDecoder;
import com.nimbusds.jwt.NimbusJwtDecoder;
import java.util.List;

public class JwtTokenUtils {

    public static Jwt parseToken(String token) throws JwtException {
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("http://keycloak-service.keycloak.svc.cluster.local/realms/app-realm/protocol/openid-connect/certs").build();
        return jwtDecoder.decode(token.replace("Bearer ", ""));
    }

    public static boolean hasAccess(Jwt token, String role) {
        List<String> roles = token.getClaim("realm_access").get("roles");
        return roles.contains(role);
    }
}