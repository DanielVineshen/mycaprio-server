package org.everowl.mycaprio.shared.service;

import lombok.RequiredArgsConstructor;
import org.everowl.mycaprio.database.entity.CustomerEntity;
import org.everowl.mycaprio.database.entity.StaffEntity;
import org.everowl.mycaprio.database.repository.CustomerRepository;
import org.everowl.mycaprio.database.repository.StaffRepository;
import org.everowl.mycaprio.shared.enums.UserType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try finding customer
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            return new CustomUserDetails(customer.get(), UserType.CUSTOMER);
        }

        // Try finding staff
        Optional<StaffEntity> staff = staffRepository.findByUsername(username);
        if (staff.isPresent()) {
            return new CustomUserDetails(staff.get(), UserType.STAFF);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}