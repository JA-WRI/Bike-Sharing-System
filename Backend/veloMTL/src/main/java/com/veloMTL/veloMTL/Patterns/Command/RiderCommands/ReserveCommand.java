package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import java.util.Date;

public class ReserveCommand implements Command<ResponseDTO<BikeDTO>>{
    private Date date;
    private String bikeId;
    private String username;

    public ResponseDTO<BikeDTO> execute() {
        return null;
    }

}
