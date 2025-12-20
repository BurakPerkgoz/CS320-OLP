package olp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class AuthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> payload, HttpSession session) {
       
        String studentId = (String) payload.get("studentId");
        String major = (String) payload.get("major");
        String minor = (String) payload.get("minor");
       
       
        Double creditLimit = 0.0;
        if (payload.get("creditLimit") != null) {
            creditLimit = Double.valueOf(payload.get("creditLimit").toString());
        }

        
        session.setAttribute("user_id", studentId);
        session.setAttribute("major", major);
        session.setAttribute("minor", minor);
        session.setAttribute("credits", creditLimit);

      
        String sql = "INSERT INTO Students(student_id, major, minor, credit_limit, remaining_credits) VALUES(?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE major=?, minor=?, credit_limit=?";
        
      //veritabanına öğrenci kaydet
        jdbcTemplate.update(sql, studentId, major, minor, creditLimit, creditLimit, 
                                 major, minor, creditLimit);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Welcome " + studentId);
        response.put("redirect", "/student/home");
        
        return response;
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Collections.singletonMap("message", "Logged out successfully");
    }
    
    @GetMapping("/check-session")
    public Map<String, Object> checkSession(HttpSession session) {
        Map<String, Object> resp = new HashMap<>();
        if (session.getAttribute("user_id") != null) {
            resp.put("isLoggedIn", true);
            resp.put("user_id", session.getAttribute("user_id"));
        } else {
            resp.put("isLoggedIn", false);
        }
        return resp;
    }
}
