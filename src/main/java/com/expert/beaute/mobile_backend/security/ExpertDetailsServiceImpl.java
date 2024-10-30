package com.expert.beaute.mobile_backend.security;


import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("expertDetailsService")
@RequiredArgsConstructor
public class ExpertDetailsServiceImpl implements UserDetailsService {

    private final ExpertRepository repository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Expert not found"));
    }
}
