package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.BMSCore.PaymentMethodDTO;
import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Factory.RiderCommandFactory;
import com.veloMTL.veloMTL.Service.Users.RiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final RiderService riderService; // change to facade obj
    private final RiderCommandFactory commandFactory;


    public RiderController(RiderService riderService, RiderCommandFactory commandFactory) {
        this.riderService = riderService;
        this.commandFactory = commandFactory;
    }

    @PostMapping("/command")
    public ResponseEntity<ResponseDTO<?>> executeCommand(@RequestBody CommandDTO commandDTO){

        Command<?> action = commandFactory.createCommand(commandDTO);
        ResponseDTO<?> response = (ResponseDTO<?>) action.execute();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/addPaymentMethod")
    public String addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return riderService.addPaymentMethod(paymentMethodDTO);
    }


}
