package prueba.com.example.demo.services;

import prueba.com.example.demo.entities.Authority;

public interface AuthorityService {
    public Authority insertAuthority(Authority authority);
    public Authority findByName(String name);
}
