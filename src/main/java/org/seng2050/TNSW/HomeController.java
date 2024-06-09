package org.seng2050.TNSW;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class HomeController {

    @Autowired
    private TransportService transportService;

    @Autowired
    private DataSource dataSource;

	//home screen modelview
    @GetMapping("/homepage")
    public ModelAndView homepage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        return new ModelAndView("homepage");
    }

    @PostMapping("/process_query")
    public ModelAndView processQuery(
            @RequestParam("departureTime") String departureTime,
            @RequestParam("departureStop") String departureStop,
            @RequestParam("arrivalStop") String arrivalStop) {

        // Convert departureTime to Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date departureDate;
        try {
            departureDate = dateFormat.parse(departureTime);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("error_page");
        }


        // Call Transport NSW API
        List<StopEvent> stopEvents;
        try {
            stopEvents = transportService.getStopEvents(departureStop, departureTime);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("error_page");
        }

        ModelAndView modelAndView = new ModelAndView("homepage");
        modelAndView.addObject("stopEvents", stopEvents);
        
        return modelAndView;
    }

    //Placeholder for actual API call method
    private String callTransportNSWApi(Date departureDate, String departureStop, String arrivalStop) {
         // Implement API call logic here
         return "API Response";
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam String param) {
        return new ModelAndView("login");
    }
    
    
  
}
