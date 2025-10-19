package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Repository.OperatorRepository;
import com.veloMTL.veloMTL.untils.Mappers.OperatorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {
    private final OperatorRepository operatorRepo;

    public OperatorService(OperatorRepository operatorRepo){
        this.operatorRepo = operatorRepo;
    }

    public OperatorDTO createOperator(OperatorDTO operatorDTO){
        Operator operator = OperatorMapper.dtoToEntity(operatorDTO);

        if (operator.getPermissions() == null || operator.getPermissions().isEmpty()) {
            operator.setPermissions(List.of("MANAGE_STATIONS", "VIEW_REPORTS"));
        }
        Operator savedOperator = operatorRepo.save(operator);

        return OperatorMapper.entityToDto(operator);
    }

}
