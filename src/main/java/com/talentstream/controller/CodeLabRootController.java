package com.talentstream.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeLabRootController {

    @GetMapping("/codelab-status")
    public String healthCheck() {
        return "<html>" +
                "<body style='font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; background-color: #f4f7f6;'>" +
                "  <div style='text-align: center; padding: 50px; background: white; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.1);'>" +
                "    <h1 style='color: #e49723;'>🚀 CodeLab Module is Integrated</h1>" +
                "    <p style='color: #666; font-size: 1.1rem;'>TalentStream backend is running with CodeLab support.</p>" +
                "    <div style='margin-top: 20px; padding: 10px; background: #fff3e0; border-radius: 8px; color: #e65100; font-weight: bold;'>" +
                "      Status: OK" +
                "    </div>" +
                "    <p style='margin-top: 20px; color: #999; font-size: 0.8rem;'>© 2026 BitLabs TalentStream</p>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}
