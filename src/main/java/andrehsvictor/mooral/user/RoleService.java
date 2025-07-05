package andrehsvictor.mooral.user;

import java.util.List;

import org.springframework.stereotype.Service;

import andrehsvictor.mooral.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    public List<Role> getAllByNameIn(List<String> names) {
        List<Role> roles = roleRepository.findAllByNameIn(names);
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("Roles", "names", names.toString());
        }
        return roles;
    }
}
