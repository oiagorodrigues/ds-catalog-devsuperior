package dev.iagorodrigues.dscatalog.services;

import dev.iagorodrigues.dscatalog.dto.RoleDTO;
import dev.iagorodrigues.dscatalog.dto.UserDTO;
import dev.iagorodrigues.dscatalog.dto.UserInsertDTO;
import dev.iagorodrigues.dscatalog.dto.UserUpdateDTO;
import dev.iagorodrigues.dscatalog.entities.Role;
import dev.iagorodrigues.dscatalog.entities.User;
import dev.iagorodrigues.dscatalog.exceptions.DatabaseException;
import dev.iagorodrigues.dscatalog.exceptions.ResourceNotFoundException;
import dev.iagorodrigues.dscatalog.repositories.RoleRepository;
import dev.iagorodrigues.dscatalog.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> users = repository.findAll(pageable);
        return users.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> userOptional = repository.findById(id);
        return userOptional
                .map(UserDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("O usuário com id " + id + " não existe"));
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userInsertDto) {
        User user = new User();
        mapUserDtoToEntity(userInsertDto, user);
        user.setPassword(passwordEncoder.encode(userInsertDto.getPassword()));
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User user = repository.getOne(id);
            mapUserDtoToEntity(dto, user);
            user = repository.save(user);
            return new UserDTO(user);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("O usuário com id " + id + " não existe");
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("O usuário com id " + id + " não existe");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não é possível remover um usuário com roles associadas");
        }
    }

    private void mapUserDtoToEntity(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.getOne(roleDTO.getId());
            user.getRoles().add(role);
        }
    }
}
