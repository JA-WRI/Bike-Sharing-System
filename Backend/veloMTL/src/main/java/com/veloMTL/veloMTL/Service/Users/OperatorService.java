package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.DTO.Users.CreateOperatorDTO;
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.untils.Mappers.OperatorMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OperatorService implements UserDetailsService {
    private final OperatorRepository operatorRepo;

    public OperatorService(OperatorRepository operatorRepo){
        this.operatorRepo = operatorRepo;
    }

    public OperatorDTO createOperator(CreateOperatorDTO operator){
        Operator createdOperator = new Operator(operator.getName(), operator.getEmail(), operator.getPassword());
        Operator savedOperator = operatorRepo.save(createdOperator);

        return OperatorMapper.entityToDto(savedOperator);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Operator operator = operatorRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("operator not found with email: " + email));
        return new CustomOperatorDetails(operator); // Wrap Rider into UserDetails
    }
}
