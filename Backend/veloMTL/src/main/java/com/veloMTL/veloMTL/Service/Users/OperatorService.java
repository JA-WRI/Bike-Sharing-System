package com.veloMTL.veloMTL.Service.Users;

<<<<<<< HEAD
import com.veloMTL.veloMTL.DTO.Users.CreateOperatorDTO;
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.untils.Mappers.OperatorMapper;
import org.springframework.stereotype.Service;

=======
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Repository.OperatorRepository;
import com.veloMTL.veloMTL.untils.Mappers.OperatorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

>>>>>>> uroosa2
@Service
public class OperatorService {
    private final OperatorRepository operatorRepo;

    public OperatorService(OperatorRepository operatorRepo){
        this.operatorRepo = operatorRepo;
    }

<<<<<<< HEAD
    public OperatorDTO createOperator(CreateOperatorDTO operator){
        Operator createdOperator = new Operator(operator.getName(), operator.getEmail(), operator.getPassword());
        Operator savedOperator = operatorRepo.save(createdOperator);

        return OperatorMapper.entityToDto(savedOperator);
=======
    public OperatorDTO createOperator(OperatorDTO operatorDTO){
        Operator operator = OperatorMapper.dtoToEntity(operatorDTO);

        if (operator.getPermissions() == null || operator.getPermissions().isEmpty()) {
            operator.setPermissions(List.of("MANAGE_STATIONS", "VIEW_REPORTS"));
        }
        Operator savedOperator = operatorRepo.save(operator);

        return OperatorMapper.entityToDto(operator);
>>>>>>> uroosa2
    }

}
