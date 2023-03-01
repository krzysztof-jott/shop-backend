package com.ex.shop.security;

import com.ex.shop.security.model.ShopUserDetails;
import com.ex.shop.security.model.User;
import com.ex.shop.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 40.0 musi implementować UserDetailsService:
@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional // 43.0
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 41.1 muszę z UserRepository pobrać odpowiednie informacje. Username jest tym id, czyli muszę zamienić Stringa na
        // Longa:
        User user = userRepository.findById(Long.parseLong(username)).orElseThrow();
        // 41.2 muszę jeszcze zwrócić UserDetails:
        ShopUserDetails shopUserDetails = new ShopUserDetails(
                // pobieram username, hasło i  z Usera:
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream() // lista ról, enumów
                        // muszę tu dostarczyć listę Granted Authority, więc robię mapowanie;
                        // muszę zrobić rzutowanie, żeby było interfejsem. Lambda w lambdzie:
                        .map(userRole -> (GrantedAuthority) userRole::name)
                        .toList());
        shopUserDetails.setId(user.getId());
        return shopUserDetails;
    }
}