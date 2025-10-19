package com.veloMTL.veloMTL.Service.Auth;

import com.veloMTL.veloMTL.DTO.auth.AuthUserDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.OperatorRepository;
import com.veloMTL.veloMTL.Repository.RiderRepository;
import com.veloMTL.veloMTL.Security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final RiderRepository riderRepo;
    private final OperatorRepository operatorRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(RiderRepository riderRepo,
                       OperatorRepository operatorRepo,
                       PasswordEncoder encoder,
                       JwtService jwt) {
        this.riderRepo = riderRepo;
        this.operatorRepo = operatorRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    /** Regular login using email + password (checks Rider first, then Operator). */
    public String loginLocal(String email, String rawPassword) {
        // 1) Try Rider
        Optional<Rider> riderOpt = riderRepo.findByEmail(email);
        if (riderOpt.isPresent()) {
            Rider r = riderOpt.get();

            String stored = safeGetPassword(r);
            if (stored != null && encoder.matches(rawPassword, stored)) {
                // build claims without nulls
                java.util.Map<String, Object> claims = new java.util.HashMap<>();
                claims.put("id", r.getId());
                claims.put("role", r.getRole());
                if (r.getName() != null) claims.put("name", r.getName());

                return jwt.issue(email, claims);
            }
            throw new IllegalArgumentException("Invalid credentials");

        }

        // 2) Try Operator
        Optional<Operator> opOpt = operatorRepo.findByEmail(email);
        if (opOpt.isPresent()) {
            Operator o = opOpt.get();

            String stored = safeGetPassword(o);
            if (stored != null && encoder.matches(rawPassword, stored)) {
                java.util.Map<String, Object> claims = new java.util.HashMap<>();
                claims.put("id", o.getId());
                claims.put("role", o.getRole());
                if (o.getName() != null) claims.put("name", o.getName());

                return jwt.issue(email, claims);
            }
            throw new IllegalArgumentException("Invalid credentials");
        }

        throw new IllegalArgumentException("Account not found");
    }

    private String safeGetPassword(Object user) {
        try {
            // try getPassword()
            return (String) user.getClass().getMethod("getPassword").invoke(user);
        } catch (Exception ignored) {}

        try {
            // try getHashedPassword()
            return (String) user.getClass().getMethod("getHashedPassword").invoke(user);
        } catch (Exception ignored) {}

        try {
            // try getPasswordHash()
            return (String) user.getClass().getMethod("getPasswordHash").invoke(user);
        } catch (Exception ignored) {}

        return null;
    }

    public AuthUserDTO toAuthUser(String id, String name, String email, String role) {
        return new AuthUserDTO(id, name, email, role);
    }

    // TEMP DEBUG:
    public java.util.Map<String, Object> debugLoginCheck(String email, String rawPassword) {
        var out = new java.util.HashMap<String, Object>();
        out.put("email", email);

        var riderOpt = riderRepo.findByEmail(email);
        if (riderOpt.isPresent()) {
            var r = riderOpt.get();
            String stored = safeGetPassword(r);
            out.put("foundIn", "RIDER");
            out.put("storedNull", stored == null);
            out.put("storedPrefix", stored == null ? null : stored.substring(0, Math.min(4, stored.length())));
            out.put("bcryptPrefix", stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$")));
            out.put("matches", stored != null && encoder.matches(rawPassword, stored));
            return out;
        }

        var opOpt = operatorRepo.findByEmail(email);
        if (opOpt.isPresent()) {
            var o = opOpt.get();
            String stored = safeGetPassword(o);
            out.put("foundIn", "OPERATOR");
            out.put("storedNull", stored == null);
            out.put("storedPrefix", stored == null ? null : stored.substring(0, Math.min(4, stored.length())));
            out.put("bcryptPrefix", stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$")));
            out.put("matches", stored != null && encoder.matches(rawPassword, stored));
            return out;
        }

        out.put("foundIn", "NONE");
        return out;
    }

    public AuthUserDTO resolveUserByEmail(String email) {
        var riderOpt = riderRepo.findByEmail(email);
        if (riderOpt.isPresent()) {
            var r = riderOpt.get();
            return new AuthUserDTO(r.getId(), r.getName(), r.getEmail(), r.getRole());
        }
        var opOpt = operatorRepo.findByEmail(email);
        if (opOpt.isPresent()) {
            var o = opOpt.get();
            return new AuthUserDTO(o.getId(), o.getName(), o.getEmail(), o.getRole());
        }
        throw new IllegalArgumentException("Account not found");
    }



}
